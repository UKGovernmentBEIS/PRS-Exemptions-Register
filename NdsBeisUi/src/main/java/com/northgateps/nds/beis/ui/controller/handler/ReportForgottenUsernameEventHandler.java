package com.northgateps.nds.beis.ui.controller.handler;

import java.util.List;

import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.platform.api.ForgottenUsernameDetails;
import com.northgateps.nds.platform.api.forgottenusername.ReportForgottenUsernameNdsRequest;
import com.northgateps.nds.platform.api.forgottenusername.ReportForgottenUsernameNdsResponse;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.ViewMessage;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Event handler for the "NEXT" button of report-forgotten-username.jsp
 */
public class ReportForgottenUsernameEventHandler extends ThrottledEventHandler {

    protected final NdsLogger logger = NdsLogger.getLogger(getClass());
    private static String serviceEndpoint = ConfigurationFactory.getConfiguration().getString( "esbEndpoint.reportForgottenUsername");
    
    static ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();
    public static final String EMAIL_CONFIGURATION_FILE = configurationManager.getString("email.templatename");

    @Override
    public void onAfterResolvedView(NdsFormModel allModel, ControllerState<?> controllerState) {
        final BeisAllModel model = (BeisAllModel) allModel;
        ForgottenUsernameDetails forgottenUsernameDetailsModel = model.getForgottenUsernameDetails();
        
        if (forgottenUsernameDetailsModel == null) {
        	return;
        }
        
        String emailAddress = forgottenUsernameDetailsModel.getEmailAddress();
        
        if (emailAddress != null) {
        	
        	// see if the request should be stopped due to throttling rules
        	if (! doThrottling(emailAddress, controllerState)) {
        		return;
        	}
        	
            model.populateDefaults();

            ReportForgottenUsernameNdsRequest request = new ReportForgottenUsernameNdsRequest();
            request.setCommonContext(model.buildCommonContext(controllerState));
            ForgottenUsernameDetails requestForgottenUsernameDetails = new ForgottenUsernameDetails();
            request.setForgottenUsername(requestForgottenUsernameDetails);
            requestForgottenUsernameDetails.setEmailAddress(emailAddress);
            requestForgottenUsernameDetails.setTenant(model.getTenant());
            requestForgottenUsernameDetails.setForce(forgottenUsernameDetailsModel.getForce());
            if (EMAIL_CONFIGURATION_FILE != null) {
                request.setEmailTemplateName(EMAIL_CONFIGURATION_FILE);
            }

            UiServiceClient svcClient = controllerState.getController().getUiSvcClientFactory().getInstance(ReportForgottenUsernameNdsResponse.class);

            if (!controllerState.isSyncController()) {
                svcClient.post(serviceEndpoint, request);
            } else {
                svcClient.postSync(serviceEndpoint, request);
            }

            svcClient.setResponseConsumer(ndsResponse -> {
                if (!(ndsResponse instanceof ReportForgottenUsernameNdsResponse)) {
                    model.setPostSubmitMessage(null); // defer message construction back to controller
                    return false;
                }
                ReportForgottenUsernameNdsResponse response = (ReportForgottenUsernameNdsResponse) ndsResponse;

                if (response.isSuccess()) {
                    return true;
                } else if (response.getNdsMessages() != null) {
                    List<ValidationViolation> violations = response.getNdsMessages().getViolations();
                    if (violations != null) {
                        model.setPostSubmitMessage(ViewMessage.fromViolations(violations));
                    } else {
                        model.setPostSubmitMessage(new ViewMessage(response.getNdsMessages()));
                    }
                } else {
                    model.setPostSubmitMessage(new ViewMessage("Error_No_more_information"));
                }

                return false;
            });
        }
    }
}
