package com.northgateps.nds.beis.ui.controller.handler;

import com.northgateps.nds.beis.api.PRSPenaltySearchNdsRequest;
import com.northgateps.nds.beis.api.PRSPenaltySearchNdsResponse;
import com.northgateps.nds.beis.api.PenaltyData;
import com.northgateps.nds.beis.api.PenaltySearch;
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
 * Sends a call to the backoffice passing the penalty reference number
 * and stores the response in selectedPenaltyData
 */
public class GetSelectedPenaltyDetailsEventHandler extends AbstractViewEventHandler {

    private static final String prsPenaltySearchServiceEndPoint = ConfigurationFactory.getConfiguration().getString(
            "esbEndpoint.prsPenaltySearchService");

    protected static final NdsLogger logger = NdsLogger.getLogger(ChosenPenaltyHandler.class);

    @Override
    public void onAfterResolvedView(NdsFormModel allModel, final ControllerState<?> controllerState) {
        final BeisAllModel model = (BeisAllModel) allModel;
        final UiData uiData = (UiData) model.getUiData();

        final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                PRSPenaltySearchNdsResponse.class);

        PRSPenaltySearchNdsRequest prsPenaltySearchNdsRequest = new PRSPenaltySearchNdsRequest();

        PenaltySearch penaltySearch = new PenaltySearch();
        penaltySearch.setPenaltyRefNo(uiData.getSelectedPenaltyRefNo());
        prsPenaltySearchNdsRequest.setPenaltySearch(penaltySearch);

        if (!controllerState.isSyncController()) {
            uiSvcClient.post(prsPenaltySearchServiceEndPoint, prsPenaltySearchNdsRequest);
        } else {
            uiSvcClient.postSync(prsPenaltySearchServiceEndPoint, prsPenaltySearchNdsRequest);
        }

        uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

            @Override
            public boolean consume(NdsResponse response) {
                if (!(response instanceof PRSPenaltySearchNdsResponse)) {
                    model.setPostSubmitMessage(null);
                    return false;
                }

                PRSPenaltySearchNdsResponse prsPenaltySearchNdsResponse = ((PRSPenaltySearchNdsResponse) (response));
                if (prsPenaltySearchNdsResponse.isSuccess()) {

                    PenaltyData selectedPenaltyData = new PenaltyData();
                    if (prsPenaltySearchNdsResponse.getGetPenaltySearchResponseDetail() != null
                            && prsPenaltySearchNdsResponse.getGetPenaltySearchResponseDetail().getPenalties() != null) {
                        
                        if (prsPenaltySearchNdsResponse.getGetPenaltySearchResponseDetail().getPenalties().size() == 1) {
                            selectedPenaltyData = prsPenaltySearchNdsResponse.getGetPenaltySearchResponseDetail().getPenalties().get(0);
                            uiData.setSelectedPenaltyData(selectedPenaltyData);
                            
                        } else if (prsPenaltySearchNdsResponse.getGetPenaltySearchResponseDetail().getPenalties().size() == 0) {
                            logger.error("No details found for selected penalty");
                            
                        } else {
                            logger.error("Number of penalties found is more than one");
                        }

                    }

                    return true;
                    
                } else if (prsPenaltySearchNdsResponse.getNdsMessages() != null) {
                    model.setPostSubmitMessage(new ViewMessage(prsPenaltySearchNdsResponse.getNdsMessages()));
                    return false;
                    
                } else {
                    model.setPostSubmitMessage(new ViewMessage("Error_No_more_information"));
                    return false;
                }
            }
        });
        controllerState.registerUiServiceClient(uiSvcClient);
     }
}
