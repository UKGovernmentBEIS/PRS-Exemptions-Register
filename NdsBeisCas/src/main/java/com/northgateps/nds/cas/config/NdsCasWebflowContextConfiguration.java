package com.northgateps.nds.cas.config;

import org.apereo.cas.web.flow.CasWebflowConstants;
import org.apereo.cas.CentralAuthenticationService;
import org.apereo.cas.authentication.principal.ServiceFactory;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.logout.LogoutExecutionPlan;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.web.cookie.CasCookieBuilder;
import org.apereo.cas.web.flow.CasWebflowConfigurer;
import org.apereo.cas.web.flow.CasWebflowExecutionPlan;
import org.apereo.cas.web.flow.CasWebflowExecutionPlanConfigurer;
import org.apereo.cas.web.flow.config.CasWebflowContextConfiguration;
import org.apereo.cas.web.flow.logout.LogoutAction;
import org.apereo.cas.web.support.ArgumentExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.execution.Action;
import com.northgateps.nds.cas.configuration.NdsConfigurationProperties;
import com.northgateps.nds.cas.web.flow.LogoutAndRedirectExternallyAction;
import com.northgateps.nds.cas.web.flow.NdsDefaultWebflowConfigurer;
import com.northgateps.nds.cas.web.flow.NdsPhaseAction;

/**
 * Provides Spring beans in addition to and overriding those that CAS provides
 * (ie our own versions).
 * This saves us extending or overriding CAS configuration and property classes which
 * causes a maintenance penalty on upgrades.
 * 
 * This configuration is for Spring WebFlow and Action customisations.
 * 
 * @see spring.factories which loads the configuration
 */
@Configuration("ndsCasConfiguration")
@EnableConfigurationProperties(NdsConfigurationProperties.class)
public class NdsCasWebflowContextConfiguration extends CasWebflowContextConfiguration
    implements CasWebflowExecutionPlanConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(NdsCasWebflowContextConfiguration.class);
    
    @Autowired
    private CasConfigurationProperties casProperties;
    @Autowired
    private NdsConfigurationProperties ndsProperties;
    @SuppressWarnings("rawtypes")
    @Autowired
    @Qualifier("webApplicationServiceFactory")
    private ServiceFactory webApplicationServiceFactory;
    @Autowired
    @Qualifier("centralAuthenticationService")
    private ObjectProvider<CentralAuthenticationService> centralAuthenticationService;
    @Autowired
    @Qualifier("ticketGrantingTicketCookieGenerator")
    private ObjectProvider<CasCookieBuilder> ticketGrantingTicketCookieGenerator;
    @Autowired
    @Qualifier("argumentExtractor")
    private ObjectProvider<ArgumentExtractor> argumentExtractor;
    @Autowired
    @Qualifier("servicesManager")
    private ObjectProvider<ServicesManager> servicesManager;
    @Autowired
    @Qualifier("logoutExecutionPlan")
    private ObjectProvider<LogoutExecutionPlan> logoutExecutionPlan;
    @Autowired
    @Qualifier("defaultWebflowConfigurer")
    private CasWebflowConfigurer defaultWebflowConfigurer;
    
    /**
     * Manages the default login form webflow.
     * 
     * @see org.apereo.cas.config.CasWebflowcontextConfiguration
     */
    @Bean
    @Order(0)
    @RefreshScope
    @Lazy(false)
    public CasWebflowConfigurer defaultWebflowConfigurer(final ConfigurableApplicationContext applicationContext,
            final CasConfigurationProperties casProperties,
            @Qualifier(CasWebflowConstants.BEAN_NAME_LOGIN_FLOW_DEFINITION_REGISTRY) final FlowDefinitionRegistry loginFlowRegistry,
            @Qualifier(CasWebflowConstants.BEAN_NAME_LOGOUT_FLOW_DEFINITION_REGISTRY) final FlowDefinitionRegistry logoutFlowRegistry,
            @Qualifier(CasWebflowConstants.BEAN_NAME_FLOW_BUILDER_SERVICES) final FlowBuilderServices flowBuilderServices) {
        logger.debug("Creating defaultWebflowConfigurer, ndsDefaultWebflowConfigurer");

        // instead of DefaultLoginWebflowConfigurer
        final NdsDefaultWebflowConfigurer c = new NdsDefaultWebflowConfigurer(flowBuilderServices, loginFlowRegistry, applicationContext, casProperties);
        c.setLogoutFlowDefinitionRegistry(logoutFlowRegistry);
        c.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return c;
    }

    /** Used on UI (casLoginView.html) page to display application phase (Live /Test) to user */
    @Bean
    public NdsPhaseAction extractPhaseAction() {
        logger.debug("Creating NdsPhaseAction");
        String phase = ndsProperties.getApplicationPhase();
        NdsPhaseAction action = new NdsPhaseAction(phase == null ? null : phase.trim().toUpperCase(), ndsProperties.getFeedbackEmail() == null ? null : ndsProperties.getFeedbackEmail().trim());
        logger.info("  Phase is " + action.getPhase());
        return action;
    }

    /**
     * Redefines the logoutAction bean to allow CAS to redirect to any URL rather than just the service URL.
     * 
     * @see org.apereo.cas.web.config.CasSupportActionsConfiguration
     */
    @RefreshScope
    @Bean
    public Action logoutAction() {
        final LogoutAction a = new LogoutAndRedirectExternallyAction(centralAuthenticationService.getObject(), 
        		ticketGrantingTicketCookieGenerator.getObject(), argumentExtractor.getObject(),
                servicesManager.getObject(), logoutExecutionPlan.getObject(), 
        		casProperties);
        return a;                
    }

    @Override
    public void configureWebflowExecutionPlan(CasWebflowExecutionPlan plan) {
      plan.registerWebflowConfigurer(defaultWebflowConfigurer);
    }
}
