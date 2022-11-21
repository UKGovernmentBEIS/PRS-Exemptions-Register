package com.northgateps.nds.cas.config;

import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.AuthenticationPasswordPolicyHandlingStrategy;
import org.apereo.cas.authentication.CoreAuthenticationUtils;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.PrincipalNameTransformerUtils;
import org.apereo.cas.authentication.principal.PrincipalResolver;
import org.apereo.cas.authentication.support.DefaultLdapAccountStateHandler;
import org.apereo.cas.authentication.support.OptionalWarningLdapAccountStateHandler;
import org.apereo.cas.authentication.support.RejectResultCodeLdapPasswordPolicyHandlingStrategy;
import org.apereo.cas.authentication.support.password.DefaultPasswordPolicyHandlingStrategy;
import org.apereo.cas.authentication.support.password.GroovyPasswordPolicyHandlingStrategy;
import org.apereo.cas.authentication.support.password.PasswordEncoderUtils;
import org.apereo.cas.authentication.support.password.PasswordPolicyContext;
import org.apereo.cas.configuration.model.support.ldap.AbstractLdapAuthenticationProperties;
import org.apereo.cas.configuration.model.support.ldap.AbstractLdapAuthenticationProperties.AuthenticationTypes;
import org.apereo.cas.configuration.model.support.ldap.LdapAuthenticationProperties;
import org.apereo.cas.configuration.model.support.ldap.LdapPasswordPolicyProperties;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.util.CollectionUtils;
import org.apereo.cas.util.LdapUtils;
import org.ldaptive.ConnectionFactory;
import org.ldaptive.auth.AuthenticationResponse;
import org.ldaptive.auth.AuthenticationResponseHandler;
import org.ldaptive.auth.Authenticator;
import org.ldaptive.auth.SimpleBindAuthenticationHandler;
import org.ldaptive.auth.ext.ActiveDirectoryAuthenticationResponseHandler;
import org.ldaptive.auth.ext.EDirectoryAuthenticationResponseHandler;
import org.ldaptive.auth.ext.FreeIPAAuthenticationResponseHandler;
import org.ldaptive.auth.ext.PasswordExpirationAuthenticationResponseHandler;
import org.ldaptive.auth.ext.PasswordPolicyAuthenticationResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.SetFactoryBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import com.google.common.collect.Multimap;
import com.northgateps.nds.cas.authentication.LdapTenantAuthenticationHandler;
import com.northgateps.nds.cas.configuration.NdsConfigurationProperties;
import com.northgateps.nds.cas.configuration.NdsLdapAuthenticationProperties;
import com.northgateps.nds.ldaptive.auth.TenantAuthenticator;
import com.northgateps.nds.ldaptive.auth.TenantDnResolver;
import lombok.SneakyThrows;

/**
 * Configures the custom Ldap authentication handler.
 */
@Configuration("ndsLdapAuthenticationConfiguration")
@EnableConfigurationProperties(NdsConfigurationProperties.class)
public class NdsLdapAuthenticationConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(NdsLdapAuthenticationConfiguration.class);
  
    @Autowired
    private ApplicationContext applicationContext;
    
    // NDS use our version of the properties instead
    @Autowired
    private NdsConfigurationProperties ndsProperties;

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;
    
    @Autowired
    @Qualifier("ldapPrincipalFactory")
    PrincipalFactory ldapPrincipalFactory;
      
    @Autowired
    @Qualifier("defaultPrincipalResolver")
    private ObjectProvider<PrincipalResolver> defaultPrincipalResolver;

    /** 
     * Waits for connection to first specified LDAP system to be available.
     * Just repatedly calls the first part of the normal configuration that connects to Ldap.
     * On failure, waits 5 seconds then tries again.
     */
    @Bean
    String ldapAvailabilityChecker() {
        NdsLdapAuthenticationProperties ldapProperties = ndsProperties.getAuthn().getLdap().get(0);
        
        boolean connected = false;
        
        do {
            LOGGER.info("Testing access to Ldap");
            
            try {
                createTenantLdaptiveAuthenticator(ldapProperties);
                connected = true;
            } catch(Exception e) {
                LOGGER.warn("Ldap access not available yet, will wait and try again");
                LOGGER.debug(e.getMessage(), e);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {}          
            }
        } while(connected == false);
        
        LOGGER.info("Ldap connection possible");
        return "Ldap connection possible";
    }
    
    @SuppressWarnings("unchecked")
    @Bean
    @RefreshScope
    @DependsOn({"mongoAvailabilityChecker", "ldapAvailabilityChecker"})  // don't connect to LDAP if connection to Mongo or LDAP not available yet
    @SneakyThrows
    public Collection<AuthenticationHandler> ndsLdapAuthenticationHandlers(
        @Qualifier("ldapAuthenticationHandlerSetFactoryBean") final SetFactoryBean ldapAuthenticationHandlerSetFactoryBean) {
        LOGGER.info("Starting NDS Ldap configuration");
        final Collection<AuthenticationHandler> handlers = new HashSet<AuthenticationHandler>();
        
        // NDS use our version of the properties instead
        ndsProperties.getAuthn().getLdap()
                .stream()
                .forEach(ldapProperties -> {
                
                    final Multimap<String, Object> multiMapAttributes = 
                            CoreAuthenticationUtils.transformPrincipalAttributesListIntoMultiMap(ldapProperties.getPrincipalAttributeList());
                    LOGGER.debug("Created and mapped principal attributes [{}] for [{}]...", multiMapAttributes, ldapProperties.getLdapUrl());
                    LOGGER.debug("Creating LDAP authenticator for [{}] and baseDn [{}]", ldapProperties.getLdapUrl(), ldapProperties.getBaseDn());
                    
                    // NDS setup custom authenticator
                    // replaces final Authenticator authenticator = LdapUtils.newLdaptiveAuthenticator(l);
                    final Authenticator authenticator = createTenantLdaptiveAuthenticator(ldapProperties);
                    
                    LOGGER.debug("Ldap authenticator configured with return attributes [{}] for [{}] and baseDn [{}]",
                            multiMapAttributes.keySet(), ldapProperties.getLdapUrl(), ldapProperties.getBaseDn());
            
                    LOGGER.debug("Creating LDAP password policy handling strategy for [{}]", ldapProperties.getLdapUrl());
                    final AuthenticationPasswordPolicyHandlingStrategy strategy = createLdapPasswordPolicyHandlingStrategy(ldapProperties);
                
                    LOGGER.debug("Creating LDAP authentication handler for [{}]", ldapProperties.getLdapUrl());
            
                    // NDS
                    final LdapTenantAuthenticationHandler handler = 
                            new LdapTenantAuthenticationHandler(ldapProperties.getName(), servicesManager, ldapPrincipalFactory, 
                                    ldapProperties.getOrder(), authenticator, ldapProperties.getTenantAttributeName(), strategy);
            
                    handler.setCollectDnAttribute(ldapProperties.isCollectDnAttribute());
            
                    final List<String> additionalAttributes = ldapProperties.getAdditionalAttributes();
                    if (StringUtils.isNotBlank(ldapProperties.getPrincipalAttributeId())) {
                        additionalAttributes.add(ldapProperties.getPrincipalAttributeId());
                    }
                    if (StringUtils.isNotBlank(ldapProperties.getPrincipalDnAttributeName())) {
                        handler.setPrincipalDnAttributeName(ldapProperties.getPrincipalDnAttributeName());
                    }
                    handler.setAllowMultiplePrincipalAttributeValues(ldapProperties.isAllowMultiplePrincipalAttributeValues());
                    handler.setAllowMissingPrincipalAttributeValue(ldapProperties.isAllowMissingPrincipalAttributeValue());
                    handler.setPasswordEncoder(PasswordEncoderUtils.newPasswordEncoder(ldapProperties.getPasswordEncoder(), applicationContext));
                    handler.setPrincipalNameTransformer(PrincipalNameTransformerUtils.newPrincipalNameTransformer(ldapProperties.getPrincipalTransformation()));
            
                    if (StringUtils.isNotBlank(ldapProperties.getCredentialCriteria())) {
                        LOGGER.debug("Ldap authentication for [{}] is filtering credentials by [{}]",
                                ldapProperties.getLdapUrl(), ldapProperties.getCredentialCriteria());
                        handler.setCredentialSelectionPredicate(CoreAuthenticationUtils.newCredentialSelectionPredicate(ldapProperties.getCredentialCriteria()));
                    }
            
                    if (StringUtils.isBlank(ldapProperties.getPrincipalAttributeId())) {
                        LOGGER.debug("No principal id attribute is found for LDAP authentication via [{}]", ldapProperties.getLdapUrl());
                    } else {
                        handler.setPrincipalIdAttribute(ldapProperties.getPrincipalAttributeId());
                        LOGGER.debug("Using principal id attribute [{}] for LDAP authentication via [{}]", ldapProperties.getPrincipalAttributeId(),
                                    ldapProperties.getLdapUrl());
                    }
            
                    if (ldapProperties.getPasswordPolicy().isEnabled()) {
                    	LOGGER.info("Password policy is enabled for [{}]. Constructing password policy configuration", ldapProperties.getLdapUrl());
                        LdapPasswordPolicyProperties ldapPpProperties = new LdapPasswordPolicyProperties();
                        final PasswordPolicyContext cfg = LdapUtils.createLdapPasswordPolicyConfiguration(ldapPpProperties, authenticator, multiMapAttributes);
                        handler.setPasswordPolicyConfiguration(cfg);
                    }
            
                    final Map<String, Object> attributes = CollectionUtils.wrap(multiMapAttributes);
                    handler.setPrincipalAttributeMap(attributes);
            
                    LOGGER.debug("Initializing LDAP authentication handler for [{}]", ldapProperties.getLdapUrl());
                    handler.initialize();
                    handlers.add(handler);
                });
        try {
          ((Set) ldapAuthenticationHandlerSetFactoryBean.getObject()).addAll(handlers);
        } catch (Exception e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
        return handlers;
    }

    /** 
     * Creates a TenantAuthenticator using a TenantDnResolver.
     * Actually creates a CAS DIRECT bind type authenticator, and takes what it needs from that to create our version.
     */
    private static Authenticator createTenantLdaptiveAuthenticator(final AbstractLdapAuthenticationProperties ldapProperties) {
        LOGGER.debug("Creating tenant direct bind directory (LDAP) authenticator for {}", ldapProperties.getLdapUrl());

        if (StringUtils.isBlank(ldapProperties.getDnFormat())) {
            throw new IllegalArgumentException("Dn format cannot be empty/blank for direct bind authentication");
        }

        // get a CAS version
        ldapProperties.setType(AuthenticationTypes.DIRECT);
        Authenticator ldaptiveFromCasAuthenticator = LdapUtils.newLdaptiveAuthenticator(ldapProperties);
        
        // create our versions and insert into the CAS one
        final TenantDnResolver resolver = new TenantDnResolver(ldapProperties.getDnFormat());
        
        // specifying SimpleBindAuthenticationHandler so we will get errors if CAS change it (ie we will want to check it's still ok)
        final SimpleBindAuthenticationHandler handler = (SimpleBindAuthenticationHandler) ldaptiveFromCasAuthenticator.getAuthenticationHandler();
        final ConnectionFactory factory = handler.getConnectionFactory();
        
        final Authenticator ndsAuthenticator = new TenantAuthenticator(resolver, handler);

        if (ldapProperties.isEnhanceWithEntryResolver()) {
            ndsAuthenticator.setEntryResolver(LdapUtils.newLdaptiveSearchEntryResolver(ldapProperties, factory));
        }
        
        return ndsAuthenticator;
    }

    /** 
     * Copy of LdapAuthenticationConfiguration.createLdapPasswordPolicyHandlingStrategy since it's private.
     * On CAS upgrade, check this code is still identical to that method. 
     */
    private AuthenticationPasswordPolicyHandlingStrategy<AuthenticationResponse, PasswordPolicyContext>
        createLdapPasswordPolicyHandlingStrategy(final LdapAuthenticationProperties l) {
        if (l.getPasswordPolicy().getStrategy() == LdapPasswordPolicyProperties.PasswordPolicyHandlingOptions.REJECT_RESULT_CODE) {
            LOGGER.debug("Created LDAP password policy handling strategy based on blacklisted authentication result codes");
            return new RejectResultCodeLdapPasswordPolicyHandlingStrategy();
        }
    
        final Resource location = l.getPasswordPolicy().getGroovy().getLocation();
        if (l.getPasswordPolicy().getStrategy() == LdapPasswordPolicyProperties.PasswordPolicyHandlingOptions.GROOVY && location != null) {
            LOGGER.debug("Created LDAP password policy handling strategy based on Groovy script [{}]", location);
            return new GroovyPasswordPolicyHandlingStrategy(location, applicationContext);
        }
    
        LOGGER.debug("Created default LDAP password policy handling strategy");
        return new DefaultPasswordPolicyHandlingStrategy();
    }

    /** Registers our custom authentication handler with CAS. */
    @Bean
    @Autowired
    @RefreshScope
    public AuthenticationEventExecutionPlanConfigurer ldapAuthenticationEventExecutionPlanConfigurer(
        @Qualifier("ldapAuthenticationHandlerSetFactoryBean") final SetFactoryBean ldapAuthenticationHandlerSetFactoryBean) {
      return plan -> ndsLdapAuthenticationHandlers(ldapAuthenticationHandlerSetFactoryBean)
          .forEach(handler -> {
            LOGGER.info("Registering LDAP authentication for [{}]", handler.getName());
            plan.registerAuthenticationHandlerWithPrincipalResolver(handler,
                defaultPrincipalResolver.getObject());
          });
    }
}
