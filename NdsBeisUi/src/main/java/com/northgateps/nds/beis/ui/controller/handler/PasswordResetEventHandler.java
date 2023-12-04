package com.northgateps.nds.beis.ui.controller.handler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.northgateps.nds.platform.api.NdsMessages;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.passwordreset.PasswordResetNdsRequest;
import com.northgateps.nds.platform.api.passwordreset.PasswordResetNdsResponse;
import com.northgateps.nds.platform.api.serviceclient.NdsResponseConsumer;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.controller.ViolationFilter;
import com.northgateps.nds.platform.ui.model.AbstractNdsMvcModel;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.ViewMessage;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Submit a password reset request to the ESB.
 */
public class PasswordResetEventHandler extends ThrottledEventHandler {

    protected final NdsLogger logger = NdsLogger.getLogger(getClass());
    
    private String publicWebAddress = null;
    
    @Override
    public void onAfterResolvedView(NdsFormModel allModel, ControllerState<?> controllerState) {
        final AbstractNdsMvcModel model = (AbstractNdsMvcModel) allModel;
        
        if (model.getPasswordResetDetails() == null) {
            // The password reset details are not retained by the view state, so if the user does a BACK on the
            // confirmation page, the page gets redisplayed, but the password reset details object ref is null,
            // so there's nothing to do.
            return;
        }

        // see if the request should be stopped due to throttling rules
    	if (! doThrottling(model.getPasswordResetDetails().getUsername(), controllerState)) {
    		return;
    	}

        
        ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();
        final String serviceEndPoint = configurationManager.getString("esbEndpoint.passwordReset");
        final String emailConfigurationFile = configurationManager.getString("email.templatename");

        logger.trace("ESB endpoint from configuration is: " + serviceEndPoint);

        model.getPasswordResetDetails().setTenant(model.getTenant());
        PasswordResetNdsRequest request = new PasswordResetNdsRequest();
        request.setCommonContext(model.buildCommonContext(controllerState));
        
        request.setPasswordResetDetails(model.getPasswordResetDetails());
        request.setPublicWebAddress(getPublicWebAddress());
        
        if (emailConfigurationFile != null) {
            request.setEmailTemplateName(emailConfigurationFile);
        }

        final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(PasswordResetNdsResponse.class);
        if (!controllerState.isSyncController()) {
            uiSvcClient.post(serviceEndPoint, request);
        } else {
            uiSvcClient.postSync(serviceEndPoint, request);
        }

        uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

            @Override
            public boolean consume(NdsResponse response) {
                if (!(uiSvcClient.getResponse() instanceof PasswordResetNdsResponse)) {
                    model.setPostSubmitMessage(null); // defer message construction back to
                                                      // controller
                    return false;
                }

                PasswordResetNdsResponse regResponse = ((PasswordResetNdsResponse) (response));
                if (regResponse.isSuccess()) {
                    return true;
                } else if (regResponse.getNdsMessages() != null) {
                    List<ValidationViolation> violations = regResponse.getNdsMessages().getViolations();
                    if (violations != null) {
                        ViewMessage viewMessage = 
                        ViewMessage.fromViolations(violations, new ViolationFilter() {
                            /* 
                             * When an email address is un-routeable, the error would disclose that the username was valid, so
                             * again it's necessary to suppress the failure and pretend that everythng's OK.   
                             */
                            @Override
                            public boolean filter(ValidationViolation violation) {
                                return violation.getMessage().contains("Username or activation code is invalid.") || violation.getMessage().contains("Unable to send e-mail ");
                            }
                        });
                        if (viewMessage == null) {
                            // if the only violations have been filtered out, then pretend that the request
                            // worked to avoid giving away security information.
                            return true;
                        }
                        model.setPostSubmitMessage(viewMessage);
                    } else {
                        model.setPostSubmitMessage(new ViewMessage(regResponse.getNdsMessages()));
                    }
                    return false;
                } else {
                    model.setPostSubmitMessage(new ViewMessage("Error_No_more_information"));
                    return false;
                }
            }

        });

        /* When a user does not exist in LDAP, the ESB returns an error. For security reasons, its necessary to
         * suppress the fact that the named user doesn't exist from the unauthenticated user. So the "no user" error
         * is recognised and true is returned which will cause the response to be treated as if everything was fine. */
        uiSvcClient.setFailureConsumer(new NdsResponseConsumer() {

            @Override
            public boolean consume(NdsResponse response) {
                if (response.getNdsMessages() != null) {
                    NdsMessages error = response.getNdsMessages();
                    if (error.getExceptionCaught() != null) {
                        if (error.getExceptionCaught().contains("Unable to locate source DN")) {
                            if (controllerState.isPrototypeMode()) {
                                controllerState.getAttributes().put("prototypeMode_LdapInfo", error.getExceptionCaught()); 
                            }
                            logger.debug(error.getExceptionCaught());
                            return true;
                        }                        
                    }
                }
                return false;
            }
        });

        controllerState.registerUiServiceClient(uiSvcClient);
    }

    private String getPublicWebAddress() {
        if (publicWebAddress == null) {
            HttpServletRequest webRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            return ConfigurationFactory.getConfiguration().getString("app.publicBaseWebAddress")
                    + webRequest.getServletContext().getContextPath();
        } else {
            return publicWebAddress;
        }
    }

    public void setPublicWebAddress(String publicWebAddress) {
        this.publicWebAddress = publicWebAddress;
    }

}
