package com.northgateps.nds.cas.config;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.pm.config.PasswordManagementWebflowConfiguration;
import org.apereo.cas.web.flow.CasWebflowConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.execution.Action;

import com.northgateps.nds.cas.configuration.NdsConfigurationProperties;
import com.northgateps.nds.cas.pm.web.flow.NdsPasswordChangeAction;
import com.northgateps.nds.cas.pm.web.flow.NdsPasswordManagementWebflowConfigurer;


/**
 * Provides Spring beans in addition to and overriding those that CAS provides
 * (ie our own versions).
 * This saves us extending or overriding CAS configuration and property classes which
 * causes a maintenance penalty on upgrades.
 * 
 * This configuration is for Password Management WebFlow
 * 
 * @see spring.factories which loads the configuration
 */
@Configuration("ndsPasswordManagementWebflowConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class NdsPasswordManagementWebflowConfiguration extends PasswordManagementWebflowConfiguration{   
	private static final Logger logger = LoggerFactory.getLogger(NdsCasWebflowContextConfiguration.class);
	
    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    private CasConfigurationProperties casProperties;
    
    @Autowired
    private ObjectProvider<FlowBuilderServices> flowBuilderServices;

    @Autowired
    @Qualifier("loginFlowRegistry")
    private ObjectProvider<FlowDefinitionRegistry> loginFlowDefinitionRegistry;

    // NDS use our version of the properties instead
    @Autowired
    private NdsConfigurationProperties ndsProperties;

    @RefreshScope
    @Bean
    public Action passwordChangeAction() {
    	logger.debug("Creating our passwordChangeAction bean");
        return new NdsPasswordChangeAction(casProperties.getAuthn().getPm(), ndsProperties);
    }

    @RefreshScope
    @Bean
    @DependsOn("defaultWebflowConfigurer")
    public CasWebflowConfigurer passwordManagementWebflowConfigurer() {
    	logger.info("Creating our version of passwordManagementWebflowConfigurer bean");
        return new NdsPasswordManagementWebflowConfigurer(flowBuilderServices.getObject(),
                loginFlowDefinitionRegistry.getObject(), applicationContext, casProperties);
    }    
}
