package com.northgateps.nds.cas.pm.web.flow;

import java.util.Map;
import java.util.stream.Stream;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.configuration.model.support.pm.PasswordManagementProperties;
import org.apereo.cas.pm.PasswordManagementService;
import org.apereo.cas.util.CollectionUtils;
import org.apereo.cas.web.flow.CasWebflowConstants;
import org.apereo.cas.web.flow.actions.ConsumerExecutionAction;
import org.apereo.cas.web.flow.actions.StaticEventExecutionAction;
import org.apereo.cas.web.flow.configurer.AbstractCasWebflowConfigurer;
import org.apereo.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.webflow.action.SetAction;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.ActionState;
import org.springframework.webflow.engine.EndState;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.SubflowState;
import org.springframework.webflow.engine.TransitionSet;
import org.springframework.webflow.engine.TransitionableState;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.engine.builder.BinderConfiguration;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

import com.northgateps.nds.cas.pm.ChangePasswordDetails;

/**
* Configures the web flow for password change. Adapted from 
* org.apereo.cas.pm.web.flow.PasswordManagementWebflowConfigurer which has lots of private methods
* hence the copying. 
*
* This contains code that has knowledge of the login flow, and is thus vulnerable to changes in
* that login flow, as happened between CAS 5.2 and 5.3 and 6.2 and 6.4 and 6.6.
*/
//@formatter:off
public class NdsPasswordManagementWebflowConfigurer extends AbstractCasWebflowConfigurer {
  private static final Logger logger = LoggerFactory.getLogger(NdsPasswordManagementWebflowConfigurer.class);
	
  /**
   * Flow id for password reset.
   * TODO: 6.6 has this as just "password" but we have it in html etc as newPassword
   */
  public static final String FLOW_VAR_ID_PASSWORD = "newPassword";

  /**
   * Name of parameter that can be supplied to login url to force display of password change during login.
   */
  public static final String DO_CHANGE_PASSWORD_PARAMETER = "doChangePassword";

  public NdsPasswordManagementWebflowConfigurer(final FlowBuilderServices flowBuilderServices,
                                             final FlowDefinitionRegistry loginFlowDefinitionRegistry,
                                             final ConfigurableApplicationContext applicationContext,
                                             final CasConfigurationProperties casProperties) {
      super(flowBuilderServices, loginFlowDefinitionRegistry, applicationContext, casProperties);
      setOrder(casProperties.getAuthn().getPm().getWebflow().getOrder());
  }

  @Override
  protected void doInitialize() {
      Flow flow = getLoginFlow();
      if (flow != null) {
          createAccountStatusViewStates(flow);
      }
  }

  private void createAccountStatusViewStates(final Flow flow) {
      enablePasswordManagementForFlow(flow);

      createViewState(flow, CasWebflowConstants.STATE_ID_AUTHENTICATION_BLOCKED, "login-error/casAuthenticationBlockedView");
      createViewState(flow, CasWebflowConstants.STATE_ID_INVALID_WORKSTATION, "login-error/casBadWorkstationView");
      createViewState(flow, CasWebflowConstants.STATE_ID_INVALID_AUTHENTICATION_HOURS, "login-error/casBadHoursView");
      createViewState(flow, CasWebflowConstants.STATE_ID_PASSWORD_UPDATE_SUCCESS, "password-reset/casPasswordUpdateSuccessView");

        ViewState accountLockedState = createViewState(flow, CasWebflowConstants.STATE_ID_ACCOUNT_LOCKED, "login-error/casAccountLockedView");
        ViewState accountDisabledState = createViewState(flow, CasWebflowConstants.STATE_ID_ACCOUNT_DISABLED, "login-error/casAccountDisabledView");

        PasswordManagementProperties pm = casProperties.getAuthn().getPm();
        if (pm.getCore().isEnabled()) {
           configurePasswordResetFlow(flow, CasWebflowConstants.STATE_ID_EXPIRED_PASSWORD, "login-error/casExpiredPassView");
          configurePasswordResetFlow(flow, CasWebflowConstants.STATE_ID_MUST_CHANGE_PASSWORD, "login-error/casMustChangePassView");
          configurePasswordMustChangeForAuthnWarnings(flow);
          configurePasswordExpirationWarning(flow);
          createPasswordResetFlow();

          ActionState startState = (ActionState) flow.getStartState();
            prependActionsToActionStateExecutionList(flow, startState.getId(), CasWebflowConstants.ACTION_ID_PASSWORD_RESET_VALIDATE_TOKEN);
          createTransitionForState(startState, CasWebflowConstants.TRANSITION_ID_INVALID_PASSWORD_RESET_TOKEN,
              CasWebflowConstants.STATE_ID_PASSWORD_RESET_ERROR_VIEW);
          createViewState(flow, CasWebflowConstants.STATE_ID_PASSWORD_RESET_ERROR_VIEW,
              "password-reset/casResetPasswordErrorView");

            SetAction enableUnlockAction = createSetAction("viewScope.enableAccountUnlock", "true");
            Stream.of(accountLockedState, accountDisabledState).forEach(state -> {
                state.getRenderActionList().add(enableUnlockAction);
                state.getEntryActionList().add(createEvaluateAction(CasWebflowConstants.ACTION_ID_ACCOUNT_UNLOCK_PREPARE));
            });

            EndState unlockedView = createEndState(flow, CasWebflowConstants.STATE_ID_ACCOUNT_UNLOCKED, "login-error/casAccountUnlockedView");
            createTransitionForState(accountLockedState, CasWebflowConstants.TRANSITION_ID_SUBMIT, "unlockAccountStatus");
            ActionState unlockAction = createActionState(flow, "unlockAccountStatus", CasWebflowConstants.ACTION_ID_UNLOCK_ACCOUNT_STATUS);
            createTransitionForState(unlockAction, CasWebflowConstants.TRANSITION_ID_SUCCESS, unlockedView.getId());
            createTransitionForState(unlockAction, CasWebflowConstants.TRANSITION_ID_ERROR, accountLockedState.getId());

            createTransitionForState(accountDisabledState, CasWebflowConstants.TRANSITION_ID_SUBMIT, "enableAccountStatus");
            ActionState enableAction = createActionState(flow, "enableAccountStatus", CasWebflowConstants.ACTION_ID_UNLOCK_ACCOUNT_STATUS);
            createTransitionForState(enableAction, CasWebflowConstants.TRANSITION_ID_SUCCESS, unlockedView.getId());
            createTransitionForState(enableAction, CasWebflowConstants.TRANSITION_ID_ERROR, accountDisabledState.getId());

      } else {
          ViewState expiredState = createViewState(flow, CasWebflowConstants.STATE_ID_EXPIRED_PASSWORD, "login-error/casExpiredPassView");
          expiredState.getEntryActionList().add(createEvaluateAction(CasWebflowConstants.ACTION_ID_INIT_PASSWORD_CHANGE));
          ViewState mustChangeState = createViewState(flow, CasWebflowConstants.STATE_ID_MUST_CHANGE_PASSWORD, "login-error/casMustChangePassView");
          mustChangeState.getEntryActionList().add(createEvaluateAction(CasWebflowConstants.ACTION_ID_INIT_PASSWORD_CHANGE));
      }
  }

  private void configurePasswordExpirationWarning(final Flow flow) {
      TransitionableState warningState = getTransitionableState(flow, CasWebflowConstants.STATE_ID_SHOW_AUTHN_WARNING_MSGS);
        warningState.getEntryActionList().add(createEvaluateAction(CasWebflowConstants.ACTION_ID_PASSWORD_EXPIRATION_HANDLE_WARNINGS));
  }

  private void configurePasswordMustChangeForAuthnWarnings(final Flow flow) {
      TransitionableState warningState = getTransitionableState(flow, CasWebflowConstants.STATE_ID_SHOW_AUTHN_WARNING_MSGS);
      warningState.getEntryActionList().add(createEvaluateAction("flowScope.pswdChangePostLogin=true"));
      createTransitionForState(warningState, "changePassword", CasWebflowConstants.STATE_ID_MUST_CHANGE_PASSWORD);
  }

  private void createPasswordResetFlow() {
      Flow flow = getLoginFlow();
      if (flow != null) {
          boolean autoLogin = casProperties.getAuthn().getPm().getCore().isAutoLogin();

          ViewState state = getState(flow, CasWebflowConstants.STATE_ID_VIEW_LOGIN_FORM, ViewState.class);
          createTransitionForState(state, CasWebflowConstants.TRANSITION_ID_RESET_PASSWORD,
              CasWebflowConstants.STATE_ID_SEND_RESET_PASSWORD_ACCT_INFO);

          ViewState viewState = createViewState(flow, CasWebflowConstants.STATE_ID_SEND_RESET_PASSWORD_ACCT_INFO,
              "password-reset/casResetPasswordSendInstructionsView");
          createTransitionForState(viewState, "findAccount", CasWebflowConstants.STATE_ID_SEND_PASSWORD_RESET_INSTRUCTIONS);

          ActionState sendInst = createActionState(flow, CasWebflowConstants.STATE_ID_SEND_PASSWORD_RESET_INSTRUCTIONS,
                CasWebflowConstants.ACTION_ID_PASSWORD_RESET_SEND_INSTRUCTIONS);
          createTransitionForState(sendInst, CasWebflowConstants.TRANSITION_ID_SUCCESS,
              CasWebflowConstants.STATE_ID_SENT_RESET_PASSWORD_ACCT_INFO);
          createTransitionForState(sendInst, CasWebflowConstants.TRANSITION_ID_ERROR, viewState.getId());
          createViewState(flow, CasWebflowConstants.STATE_ID_SENT_RESET_PASSWORD_ACCT_INFO,
              "password-reset/casResetPasswordSentInstructionsView");

          registerPasswordResetFlowDefinition();

          ActionState initializeLoginFormState = getState(flow, CasWebflowConstants.STATE_ID_INIT_LOGIN_FORM, ActionState.class);
          String originalTargetState = initializeLoginFormState.getTransition(CasWebflowConstants.STATE_ID_SUCCESS).getTargetStateId();
          SubflowState pswdResetSubFlowState = createSubflowState(flow, CasWebflowConstants.STATE_ID_PASSWORD_RESET_SUBFLOW, FLOW_ID_PASSWORD_RESET);

          TransitionableState createTgt = getTransitionableState(flow, CasWebflowConstants.STATE_ID_CREATE_TICKET_GRANTING_TICKET);
          createTgt.getEntryActionList().add(
              createEvaluateAction(String.join(DO_CHANGE_PASSWORD_PARAMETER, "flowScope.", " = requestParameters.", " != null")));

            createDecisionState(flow, CasWebflowConstants.DECISION_STATE_CHECK_FOR_PASSWORD_RESET_TOKEN_ACTION,
                "requestParameters."
                + PasswordManagementService.PARAMETER_PASSWORD_RESET_TOKEN
              + " != null", CasWebflowConstants.STATE_ID_PASSWORD_RESET_SUBFLOW, originalTargetState);
          createTransitionForState(initializeLoginFormState,
              CasWebflowConstants.STATE_ID_SUCCESS,
              CasWebflowConstants.DECISION_STATE_CHECK_FOR_PASSWORD_RESET_TOKEN_ACTION, true);

          ActionState redirect = createActionState(flow, CasWebflowConstants.STATE_ID_REDIRECT_TO_LOGIN, StaticEventExecutionAction.SUCCESS);
          createStateDefaultTransition(redirect, flow.getStartState().getId());

          createTransitionForState(
              pswdResetSubFlowState,
              CasWebflowConstants.STATE_ID_PASSWORD_RESET_FLOW_COMPLETE,
              autoLogin ? CasWebflowConstants.STATE_ID_REAL_SUBMIT : CasWebflowConstants.STATE_ID_REDIRECT_TO_LOGIN);

          createDecisionState(flow,
              CasWebflowConstants.STATE_ID_CHECK_DO_CHANGE_PASSWORD,
              "flowScope." + DO_CHANGE_PASSWORD_PARAMETER + " == true",
              CasWebflowConstants.STATE_ID_MUST_CHANGE_PASSWORD,
              createTgt.getTransition(CasWebflowConstants.TRANSITION_ID_SUCCESS).getTargetStateId())
              .getEntryActionList().add(createEvaluateAction("flowScope.pswdChangePostLogin=true"));

          createTransitionForState(createTgt,
              CasWebflowConstants.TRANSITION_ID_SUCCESS, CasWebflowConstants.STATE_ID_CHECK_DO_CHANGE_PASSWORD, true);

          createDecisionState(flow,
              CasWebflowConstants.STATE_ID_POST_LOGIN_PASSWORD_CHANGE_CHECK,
              "flowScope.pswdChangePostLogin == true",
              getTransitionableState(flow, CasWebflowConstants.STATE_ID_SHOW_AUTHN_WARNING_MSGS)
                  .getTransition(CasWebflowConstants.TRANSITION_ID_PROCEED).getTargetStateId(),
              autoLogin ? CasWebflowConstants.STATE_ID_REAL_SUBMIT : CasWebflowConstants.STATE_ID_REDIRECT_TO_LOGIN);

          createTransitionForState(
              getTransitionableState(flow, CasWebflowConstants.STATE_ID_PASSWORD_UPDATE_SUCCESS),
              CasWebflowConstants.TRANSITION_ID_PROCEED,
              CasWebflowConstants.STATE_ID_POST_LOGIN_PASSWORD_CHANGE_CHECK);
      }
  }

  private void registerPasswordResetFlowDefinition() {
      Flow pswdFlow = buildFlow(FLOW_ID_PASSWORD_RESET);

      pswdFlow.getStartActionList().add(createEvaluateAction(CasWebflowConstants.ACTION_ID_INITIAL_FLOW_SETUP));

      ActionState initReset = createActionState(pswdFlow, CasWebflowConstants.STATE_ID_INIT_PASSWORD_RESET,
            CasWebflowConstants.ACTION_ID_PASSWORD_RESET_INIT);
        
      createStateDefaultTransition(initReset, CasWebflowConstants.STATE_ID_MUST_CHANGE_PASSWORD);

      ActionState verifyQuestions = createActionState(pswdFlow, CasWebflowConstants.STATE_ID_VERIFY_SECURITY_QUESTIONS,
            CasWebflowConstants.ACTION_ID_PASSWORD_RESET_VERIFY_SECURITY_QUESTIONS);
                  createTransitionForState(verifyQuestions, CasWebflowConstants.TRANSITION_ID_SUCCESS, CasWebflowConstants.STATE_ID_INIT_PASSWORD_RESET);
      createTransitionForState(verifyQuestions, CasWebflowConstants.TRANSITION_ID_ERROR, CasWebflowConstants.STATE_ID_PASSWORD_RESET_ERROR_VIEW);

      ActionState verifyRequest = createActionState(pswdFlow, CasWebflowConstants.STATE_ID_VERIFY_PASSWORD_RESET_REQUEST,
            CasWebflowConstants.ACTION_ID_PASSWORD_RESET_VERIFY_REQUEST);
                  createTransitionForState(verifyRequest, CasWebflowConstants.TRANSITION_ID_SUCCESS, CasWebflowConstants.STATE_ID_SECURITY_QUESTIONS_VIEW);
      createTransitionForState(verifyRequest, CasWebflowConstants.TRANSITION_ID_ERROR, CasWebflowConstants.STATE_ID_PASSWORD_RESET_ERROR_VIEW);
      createTransitionForState(verifyRequest, "questionsDisabled", CasWebflowConstants.STATE_ID_INIT_PASSWORD_RESET);

      ViewState questionsView = createViewState(pswdFlow, CasWebflowConstants.STATE_ID_SECURITY_QUESTIONS_VIEW,
          "password-reset/casResetPasswordVerifyQuestionsView");
      createTransitionForState(questionsView, CasWebflowConstants.TRANSITION_ID_SUBMIT,
          CasWebflowConstants.STATE_ID_VERIFY_SECURITY_QUESTIONS,
          Map.of("bind", Boolean.FALSE, "validate", Boolean.FALSE));

      enablePasswordManagementForFlow(pswdFlow);

      createViewState(pswdFlow, CasWebflowConstants.STATE_ID_PASSWORD_RESET_ERROR_VIEW,
          "password-reset/casResetPasswordErrorView");
      createViewState(pswdFlow, CasWebflowConstants.STATE_ID_PASSWORD_UPDATE_SUCCESS,
          "password-reset/casPasswordUpdateSuccessView");
      configurePasswordResetFlow(pswdFlow, CasWebflowConstants.STATE_ID_MUST_CHANGE_PASSWORD,
          "login-error/casMustChangePassView");
      pswdFlow.setStartState(verifyRequest);
      mainFlowDefinitionRegistry.registerFlowDefinition(pswdFlow);

      createEndState(pswdFlow, CasWebflowConstants.STATE_ID_PASSWORD_RESET_FLOW_COMPLETE);
      createTransitionForState(
          getTransitionableState(pswdFlow, CasWebflowConstants.STATE_ID_PASSWORD_UPDATE_SUCCESS),
          CasWebflowConstants.TRANSITION_ID_PROCEED,
          CasWebflowConstants.STATE_ID_PASSWORD_RESET_FLOW_COMPLETE);
  }

  private void enablePasswordManagementForFlow(final Flow flow) {           
	  ConsumerExecutionAction action = new ConsumerExecutionAction(context -> {
            WebUtils.putAccountProfileManagementEnabled(context, applicationContext.containsBean(CasWebflowConstants.BEAN_NAME_ACCOUNT_PROFILE_FLOW_DEFINITION_REGISTRY));
            WebUtils.putPasswordManagementEnabled(context, casProperties.getAuthn().getPm().getCore().isEnabled());
        });
      flow.getStartActionList().add(action);  
  }

  /*
   * Looks like this is the method which this class is overlayed for, as it uses our ChangePasswordDetails class.
   */
  private void configurePasswordResetFlow(final Flow flow, final String id, final String viewId) {
      createFlowVariable(flow, FLOW_VAR_ID_PASSWORD, ChangePasswordDetails.class);

      BinderConfiguration binder = createStateBinderConfiguration(CollectionUtils.wrapList(FLOW_VAR_ID_PASSWORD, "confirmPassword", "oldPassword"));  // since 6.4 it's confirm"ed" but that doesn't affect our screen so leave it as is
      
      ViewState viewState = createViewState(flow, id, viewId, binder);
      createStateModelBinding(viewState, FLOW_VAR_ID_PASSWORD, ChangePasswordDetails.class);

      viewState.getEntryActionList().add(createEvaluateAction(CasWebflowConstants.ACTION_ID_INIT_PASSWORD_CHANGE));
      createTransitionForState(viewState, CasWebflowConstants.TRANSITION_ID_SUBMIT,
          CasWebflowConstants.STATE_ID_PASSWORD_CHANGE_ACTION, Map.of("bind", Boolean.TRUE, "validate", Boolean.TRUE));
      createStateDefaultTransition(viewState, id);

      ActionState pswChangeAction = createActionState(flow, CasWebflowConstants.STATE_ID_PASSWORD_CHANGE_ACTION,
          createEvaluateAction(CasWebflowConstants.ACTION_ID_PASSWORD_CHANGE));
      TransitionSet transitionSet = pswChangeAction.getTransitionSet();
        transitionSet.add(
            createTransition(CasWebflowConstants.TRANSITION_ID_PASSWORD_UPDATE_SUCCESS, CasWebflowConstants.STATE_ID_PASSWORD_UPDATE_SUCCESS));
      transitionSet.add(createTransition(CasWebflowConstants.TRANSITION_ID_ERROR, id));
  }
}
