package com.northgateps.nds.cas.web.flow;

import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;

import org.apereo.cas.authentication.PrincipalException;
import org.apereo.cas.authentication.adaptive.UnauthorizedAuthenticationException;
import org.apereo.cas.authentication.exceptions.AccountDisabledException;
import org.apereo.cas.authentication.exceptions.AccountPasswordMustChangeException;
import org.apereo.cas.authentication.exceptions.InvalidLoginLocationException;
import org.apereo.cas.authentication.exceptions.InvalidLoginTimeException;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.UnauthorizedServiceForPrincipalException;
import org.apereo.cas.ticket.UnsatisfiedAuthenticationPolicyException;
import org.apereo.cas.web.flow.CasWebflowConstants;
import org.apereo.cas.web.flow.configurer.DefaultLoginWebflowConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.ActionState;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.engine.builder.BinderConfiguration;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

import com.northgateps.nds.cas.authentication.UsernamePasswordTenantCredential;

/**
 * Extends the CAS DefaultLoginWebflowConfigurer to set up the login webflow
 * using our UsernamePasswordTenant credential instead of the standard
 * RememberMeUsernamePasswordCredential.
 */
public class NdsDefaultWebflowConfigurer extends DefaultLoginWebflowConfigurer {

	/**
	 * Instantiates a new Default webflow configurer.
	 *
	 * @param flowBuilderServices    the flow builder services
	 * @param flowDefinitionRegistry the flow definition registry
	 */
	public NdsDefaultWebflowConfigurer(final FlowBuilderServices flowBuilderServices,
			final FlowDefinitionRegistry flowDefinitionRegistry, ConfigurableApplicationContext applicationContext,
			CasConfigurationProperties casProperties) {
		super(flowBuilderServices, flowDefinitionRegistry, applicationContext, casProperties);
	}

	/**
	 * Uses a UsernamePasswordTenantCredential no matter what. "Remember me"
	 * configuration is untested.
	 */
	protected void createRememberMeAuthnWebflowConfig(final Flow flow) {
		if (casProperties.getTicket().getTgt().getRememberMe().isEnabled()) {
			createFlowVariable(flow, CasWebflowConstants.VAR_ID_CREDENTIAL, UsernamePasswordTenantCredential.class);
			final ViewState state = getState(flow, CasWebflowConstants.STATE_ID_VIEW_LOGIN_FORM, ViewState.class);
			final BinderConfiguration cfg = getViewStateBinderConfiguration(state);
			cfg.addBinding(new BinderConfiguration.Binding("rememberMe", null, false));
		} else {
			createFlowVariable(flow, CasWebflowConstants.VAR_ID_CREDENTIAL, UsernamePasswordTenantCredential.class);
		}
	}
}