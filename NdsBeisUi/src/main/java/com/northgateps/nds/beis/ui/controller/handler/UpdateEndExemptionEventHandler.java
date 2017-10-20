package com.northgateps.nds.beis.ui.controller.handler;

import java.time.ZonedDateTime;
import java.util.List;

import com.northgateps.nds.beis.api.UpdateExemptionDetails;
import com.northgateps.nds.beis.api.updateexemptiondetails.UpdateExemptionDetailsNdsRequest;
import com.northgateps.nds.beis.api.updateexemptiondetails.UpdateExemptionDetailsNdsResponse;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.serviceclient.NdsResponseConsumer;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.AbstractNdsMvcModel;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.security.PlatformUser;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;
import com.northgateps.nds.platform.ui.view.ViewMessage;

/***
 * 
 * handler to end current exemption by updating end date
 *
 */
public class UpdateEndExemptionEventHandler extends AbstractViewEventHandler {

    private static final String prsExemptionUpdateServiceEndPoint = ConfigurationFactory.getConfiguration().getString(
            "esbEndpoint.updateExemptiontDetails");

    @Override
    public void onBeforeValidation(NdsFormModel allModel, ControllerState<?> controllerState) {

        final BeisAllModel model = (BeisAllModel) allModel;

        model.populateDefaults();
        // create and setup the request
        UpdateExemptionDetailsNdsRequest request = new UpdateExemptionDetailsNdsRequest();        

        String referenceId = model.getAction().getValueName();
        UpdateExemptionDetails updateExemptionDetails = new UpdateExemptionDetails(); 
        updateExemptionDetails.setReferenceId(referenceId);
        // passed current date as endDate
        updateExemptionDetails.setEndDate(ZonedDateTime.now());
        request.setUpdateExemptionDetails(updateExemptionDetails);

        // get logged in user's details
        final PlatformUser user = PlatformUser.getLoggedInUser();
        request.setUserName(user.getUsername());
        request.setTenant(user.getTenant());

        // make the call
        final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                UpdateExemptionDetailsNdsResponse.class);
        if (!controllerState.isSyncController()) {
            uiSvcClient.post(prsExemptionUpdateServiceEndPoint, request);
        } else {
            uiSvcClient.postSync(prsExemptionUpdateServiceEndPoint, request);
        }

        uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

            @Override
            public boolean consume(NdsResponse response) {
                if (!(response instanceof UpdateExemptionDetailsNdsResponse)) {
                    model.setPostSubmitMessage(null); // defer message construction back to controller
                    return false;
                }
                UpdateExemptionDetailsNdsResponse updateExemptionDetailsNdsResponse = ((UpdateExemptionDetailsNdsResponse) (response));

                if (updateExemptionDetailsNdsResponse.isSuccess()) {

                    return updateExemptionDetailsNdsResponse.isSuccess();

                } else if (updateExemptionDetailsNdsResponse.getNdsMessages() != null) {

                    List<ValidationViolation> violations = updateExemptionDetailsNdsResponse.getNdsMessages().getViolations();
                    if (violations != null) {
                        model.setPostSubmitMessage(ViewMessage.fromViolations(violations));
                    } else {
                        model.setPostSubmitMessage(new ViewMessage(updateExemptionDetailsNdsResponse.getNdsMessages()));
                    }
                    return false;

                } else {
                    model.setPostSubmitMessage(new ViewMessage("Error_No_more_information"));
                    return false;
                }
            }

        });
        controllerState.registerUiServiceClient(uiSvcClient);
    }
        
    @Override
    public void onAfterNavigationUpdate(NdsFormModel allModel, ControllerState<?> controllerState) {
        /**
         * We are appending referenceId to button'value, we can not decide next page flow based on button value in the sitemap 
         * So, this code is to move to next page.
         */
         controllerState.getPageFlowEngine().updateNavigationalState((AbstractNdsMvcModel) allModel,
                "personalised-end-exemption-confirmation");
    }
}
