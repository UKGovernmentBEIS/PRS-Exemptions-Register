package com.northgateps.nds.beis.ui.controller.handler;

import java.util.ArrayList;

import com.northgateps.nds.beis.api.RegisteredExemptionDetail;
import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsRequest;
import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsResponse;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.RegistrationStatusType;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.serviceclient.NdsResponseConsumer;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.AbstractNdsMvcModel;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.security.PlatformUser;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;

/***
 * 
 * handler to set selected ExemptionAddress and referenceId
 *
 */
public class EndExemptionEventHandler extends AbstractViewEventHandler {

    protected final NdsLogger logger = NdsLogger.getLogger(getClass());

    @Override
    public void onBeforeValidation(NdsFormModel allModel, final ControllerState<?> controllerState) {
      
        String referenceId;
        final BeisAllModel model = (BeisAllModel) allModel;

        final UiData uiData = (UiData) model.getUiData();
        referenceId = model.getAction().getValueName();

        if (uiData.getRegistrationStatus() != null
                && uiData.getRegistrationStatus() == RegistrationStatusType.USE_REGISTERED) {

            final String serviceEndPoint = ConfigurationFactory.getConfiguration().getString(
                    "esbEndpoint.viewDashboard");
            final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                    GetPRSAccountExemptionsNdsResponse.class);

            logger.trace("ESB endpoint from configuration is: " + serviceEndPoint);
            GetPRSAccountExemptionsNdsRequest request = new GetPRSAccountExemptionsNdsRequest();

            // get logged in user's details
            final PlatformUser user = PlatformUser.getLoggedInUser();
           
            request.setUsername(user.getUsername());
            request.setTenant(user.getTenant());
            request.setExemptionRefNo(referenceId);

            if (!controllerState.isSyncController()) {
                uiSvcClient.post(serviceEndPoint, request);
            } else {
                uiSvcClient.postSync(serviceEndPoint, request);
            }

            uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

                @Override
                public boolean consume(NdsResponse response) {

                    // set the details in the model
                    GetPRSAccountExemptionsNdsResponse dashboardResponse = (GetPRSAccountExemptionsNdsResponse) uiSvcClient.getResponse();

                   
                    if (dashboardResponse != null && dashboardResponse.isSuccess()) {
                        model.setDashboardDetails(dashboardResponse.getDashboardDetails());
                        ArrayList<RegisteredExemptionDetail> currentExemptionList = dashboardResponse.getDashboardDetails().getCurrentExemptions();
                        if(currentExemptionList.size() == 1){
                            uiData.setRegisteredExemptionDetail(currentExemptionList.get(0));
                            return true;
                        }                       
                    }

                    return false;
                }
            });
            controllerState.registerUiServiceClient(uiSvcClient);
        } 
    }

  
    @Override
    public void onAfterNavigationUpdate(NdsFormModel allModel, ControllerState<?> controllerState) {
        /**
         * We are appending referenceId to button'value, we can not decide next page flow based on button value in the sitemap 
         * So, this code is to move to next page.
         */
    	 controllerState.getPageFlowEngine().updateNavigationalState((AbstractNdsMvcModel) allModel,
                "personalised-end-exemption");
    }

}