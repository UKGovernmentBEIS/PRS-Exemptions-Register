package com.northgateps.nds.beis.ui.controller.handler;

import java.util.List;

import com.northgateps.nds.beis.api.ExemptionTypeLov;
import com.northgateps.nds.beis.api.PenaltyTypeDetails;
import com.northgateps.nds.beis.api.getprspenaltyrefdata.GetPrsPenaltyRefDataNdsRequest;
import com.northgateps.nds.beis.api.getprspenaltyrefdata.GetPrsPenaltyRefDataNdsResponse;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.serviceclient.NdsResponseConsumer;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;

import com.northgateps.nds.platform.ui.model.NdsFormModel;

import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;
import com.northgateps.nds.platform.ui.view.ViewMessage;

/***
 * 
 * To set selected ExemptionType list of value text and get penalty value from getPenaltyRefData service depending on propertyType
 *
 */
public class RegisterPrsExemptionHandler extends AbstractViewEventHandler {
    
    private static final String serviceEndPoint = ConfigurationFactory.getConfiguration().getString("esbEndpoint.getPrsPenaltyRefData");
    private static final NdsLogger logger = NdsLogger.getLogger(RegisterPrsExemptionHandler.class);

    /**
     * sets value of selectedExemptionTypeLovText on onAfterResolvedView event
     */
    @Override
    public void onAfterResolvedView(NdsFormModel allModel, final ControllerState<?> controllerState) {

        final BeisAllModel model = (BeisAllModel) allModel;
        final UiData uiData = (UiData) model.getUiData();  
        
        //To set selected ExemptionType list of value text
        if (uiData.getSelectedExemptionTypeText().getFrvRequired().equals("true")) {
            List<ExemptionTypeLov> exemptionTypeLovList = uiData.getSelectedExemptionTypeText().getExemptionTypeLovList();
            if (exemptionTypeLovList != null) {
                final String code = model.getExemptionDetails().getExemptionReason();
                ExemptionTypeLov exemptionTypeLovObj = exemptionTypeLovList.stream().filter(
                        x -> x.getCode().equals(code)).findFirst().get();

                String selectedExemptionTypeLovText = exemptionTypeLovObj != null ? exemptionTypeLovObj.getText() : "";
                uiData.setSelectedExemptionTypeLovText(selectedExemptionTypeLovText);

            }
        }
        
        
        // create and post the request to get penalty value from service
        GetPrsPenaltyRefDataNdsRequest submitRequest = new GetPrsPenaltyRefDataNdsRequest();
        final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                GetPrsPenaltyRefDataNdsResponse.class);

        // do not automatically retry on connection errors
        uiSvcClient.setRetriesRemaining(0);
        
        if (!controllerState.isSyncController()) {
            uiSvcClient.post(serviceEndPoint, submitRequest);
        } else {
            uiSvcClient.postSync(serviceEndPoint, submitRequest);
        }
        
        uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

            @Override
            public boolean consume(NdsResponse response) {
                if (!(response instanceof GetPrsPenaltyRefDataNdsResponse)) {
                    return false;
                }

                GetPrsPenaltyRefDataNdsResponse fetchResponse = (GetPrsPenaltyRefDataNdsResponse) response;
                if (fetchResponse.getNdsMessages() != null) {
                    model.setPostSubmitMessage(
                            new ViewMessage("Error_ESB_withCausedBy", fetchResponse.getNdsMessages()));                   
                    return false;
                } else {
                    try {
                        // convert the returned list to the UI list
                        List<PenaltyTypeDetails> penaltyTypeTextList = fetchResponse.getPenaltyTypeTextList();
                       
                        String propertyType = model.getExemptionDetails().getPropertyType().toString();
                      
                        for (PenaltyTypeDetails penaltyTypeDetails : penaltyTypeTextList) {
                           
                            if (penaltyTypeDetails.getService() != null
                                    && penaltyTypeDetails.getService().equals(propertyType)
                                    && penaltyTypeDetails.getCode() != null
                                    && penaltyTypeDetails.getCode().equals("EXEMPT")) {
                                uiData.setPenaltyValue(penaltyTypeDetails.getMaxValue());
                            }
                        }
                        
                    } catch (Exception e) {
                        logger.error("Could not set reference data set", e);
                    }
                }
                return true;
            }
        });
        controllerState.registerUiServiceClient(uiSvcClient);        
    }
    
}
