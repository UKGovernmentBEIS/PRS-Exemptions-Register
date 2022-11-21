package com.northgateps.nds.cas.authentication;

import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.authentication.AuthenticationException;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.AuthenticationPasswordPolicyHandlingStrategy;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.LdapAuthenticationHandler;
import org.apereo.cas.authentication.MessageDescriptor;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.credential.UsernamePasswordCredential;
import org.apereo.cas.authentication.principal.Principal;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.ldaptive.LdapEntry;
import org.ldaptive.LdapException;
import org.ldaptive.auth.AuthenticationResponse;
import org.ldaptive.auth.AuthenticationResultCode;
import org.ldaptive.auth.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.northgateps.nds.ldaptive.auth.TenantAuthenticationRequest;
import com.northgateps.nds.ldaptive.auth.TenantAuthenticator;
import lombok.SneakyThrows;

/**
 * Custom CAS LDAP authentication handler to finds users by tenant.
 * Initialized by LdapAuthenticationConfiguration.
 */
public class LdapTenantAuthenticationHandler extends LdapAuthenticationHandler {
    private static final Logger logger = LoggerFactory.getLogger(LdapTenantAuthenticationHandler.class);
    private static final String LDAP_ATTRIBUTE_ENTRY_DN = LdapTenantAuthenticationHandler.class.getSimpleName().concat(".dn");

    /** Name of attribute to be used/set for tenant. */
    private String tenantAttribute = null;

    /** 
     * Need a separate reference since the superclass doesn't provide access to it, sadly.
     */
    private TenantAuthenticator tenantAuthenticator = null;

    /**
     * Creates a new authentication handler that delegates to the given authenticator.
     */
    public LdapTenantAuthenticationHandler(final String name, final ServicesManager servicesManager, final PrincipalFactory principalFactory,
                                          final Integer order, final Authenticator authenticator, final String tenantAttribute, 
                                          final AuthenticationPasswordPolicyHandlingStrategy<?, ?> strategy) {
        super(name, servicesManager, principalFactory, order, authenticator, strategy);
        this.tenantAuthenticator = (TenantAuthenticator) authenticator;
        setTenantAttribute(tenantAttribute);
    }
    
    /**
     * Sets the name of the tenant attribute.
     */
    public void setTenantAttribute(final String tenantAttribute) {
        this.tenantAttribute = tenantAttribute;
    }

    /** 
     * Override method in AbstractUsernamePasswordAuthenticationHandler to return the same Credential object passed in as the parameter.
     * CAS changed that to create a new UsernamePasswordCredential rather than modifying and passing along the one sent to it.
     * That limits how that class can be used as well as fixing what looked like the original bug.
     * @see org.apereo.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler
     * 
     * Because we override it we lose the principalNameTransformer and passwordEncoder - neither are currently used.
     */
    @Override
    @SneakyThrows
    protected AuthenticationHandlerExecutionResult doAuthentication(final Credential credential) {
        final UsernamePasswordCredential userPass = (UsernamePasswordCredential) credential;

        try {
            checkUsername(userPass);
        } catch (AccountNotFoundException e) {
            e.printStackTrace();
        }
        try {
            checkPassword(userPass);
        } catch (FailedLoginException e) {
            e.printStackTrace();
        }
        
        logger.debug("FYI Transforming username and encoding password would happen here but has been lost in the override.");

        logger.debug("Attempting authentication internally for transformed credential [{}]", userPass);

        /* 
         * Do not fix this compiler error! See @SneakyThrows on this method and run CAS from the command line (see README_CAS.md).
         * If you wrap this exception then you'll be stopping processing of CredentialExpiredExceptions later on
         * ie you'd disable the password policy system which shows the casExpiredPassView.html and similar pages.
         */
        return authenticateUsernamePasswordInternal(userPass, userPass.getPassword());
    }

    /**
    * Checks the username, used for throwing exception for the doAuthentication.
    * This was meant to be overriding the AbstractUsernamePasswordAuthenticationHandler.transfromUsername method,
    * but as we are only checking the username and not transforming it, then the method name would not have made sense.
    */
    protected void checkUsername(final UsernamePasswordCredential userPass) throws AccountNotFoundException {
        if (StringUtils.isBlank(userPass.getUsername())) {
            throw new AccountNotFoundException("Username is null.");
        }
    }

    /**
    * Checks the password, used for throwing exception for the doAuthentication.
    * This was meant to be overriding the AbstractUsernamePasswordAuthenticationHandler.transfromPassword method,
    * but as we are only checking the password and not transforming it, then the method name would not have made sense.
    */
    protected void checkPassword(final UsernamePasswordCredential userPass) throws FailedLoginException {
        if (StringUtils.isBlank(userPass.getPassword())) {
            throw new FailedLoginException("Password is null.");
        }
    }
    
    /** Sets up authenticating username, password and tenant. */
    @Override
    protected AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(final UsernamePasswordCredential upc, String notUsed)
            throws GeneralSecurityException, PreventedException {
        
        final AuthenticationResponse response;

        logger.debug("Attempting LDAP tenant authentication for {}", upc);
        final String tenant = ((UsernamePasswordTenantCredential) upc).getTenant();

        if (tenant == null) {
          throw new AuthenticationException("Missing tenant data for " + upc.getUsername());
        }

        try {

            /*
             * Create request to authenticated based on tenant, userId, password.
             * Note that we skip the superclasses use of authenticatedEntryAttributes, partly because it doesn't
             * provide access to that and partly because the attributes aren't returned by this bind anyway!
             */
            final TenantAuthenticationRequest request = 
                    new TenantAuthenticationRequest(upc.getUsername(), tenant, 
                            new org.ldaptive.Credential(upc.getPassword()), 
                                this.principalAttributeMap.keySet().toArray(new String[3]));

            response = tenantAuthenticator.authenticate(request);
        } catch (final LdapException e) {
            logger.debug(e.getMessage(), e);
            throw new PreventedException("Unexpected LDAP error " + e.getMessage());
        }
        
        logger.debug("LDAP response: {}", response);
        
        if (!passwordPolicyHandlingStrategy.supports(response)) {
            logger.warn("Authentication has failed because LDAP password policy handling strategy [{}] cannot handle [{}].",
                response, passwordPolicyHandlingStrategy.getClass().getSimpleName());
            throw new FailedLoginException("Invalid credentials");
        }
        
        logger.debug("Attempting to examine and handle LDAP password policy via [{}]",
                passwordPolicyHandlingStrategy.getClass().getSimpleName());
                   
        // this will throw the CredentialExpiredException if credentials are expired
        final List<MessageDescriptor> messageList = passwordPolicyHandlingStrategy.handle(response, getPasswordPolicyConfiguration());
        
        if (response.isSuccess()) {
            logger.debug("LDAP response returned a result. Creating the final LDAP principal");
            final Principal principal = createPrincipal(upc.getUsername(), tenant, response.getLdapEntry());
            return createHandlerResult(upc, principal, messageList);
        }
                
        if (AuthenticationResultCode.DN_RESOLUTION_FAILURE == response.getAuthenticationResultCode()) {
            logger.warn("DN resolution failed. [{}]", response.getDiagnosticMessage());
            throw new AccountNotFoundException(upc.getUsername() + " not found.");
        }

        throw new FailedLoginException("Invalid credentials");
    }

    /**
     * Creates a CAS principal with attributes if the LDAP entry contains principal attributes.
     *
     * Since the principal object requires that getId() be unique, our Id will be the users LDAP DN.
     *
     * @throws LoginException On security policy errors related to principal creation.
     */
    protected Principal createPrincipal(final String username, final String tenant,
            final LdapEntry ldapEntry) throws PreventedException {
        logger.debug("Creating LDAP principal for {} based on {}", username, ldapEntry.getDn());
        final String id;
        
        // check we have config for the tenant attribute
        if (tenantAttribute == null || "".equals(tenantAttribute)) {
            throw new PreventedException("Missing TenantAttribute configuration");
        } else {
                        
            // set the id to be username @ tenant so that it's unique in our LDAP schema
            id = ldapEntry.getDn();
            logger.debug("Setting principal id attribute {}", id);
        }

        // set the tenant attribute and the entry dn
        final Map<String, List<Object>> attributeMap = collectAttributesForLdapEntry(ldapEntry, id);
        attributeMap.put(tenantAttribute, Arrays.asList(tenant));
        attributeMap.put(LDAP_ATTRIBUTE_ENTRY_DN, Arrays.asList(ldapEntry.getDn()));
        
        logger.debug("Created LDAP principal for id {} and {} attributes", id, attributeMap.size());
        return this.principalFactory.createPrincipal(id, attributeMap);
    }
}
