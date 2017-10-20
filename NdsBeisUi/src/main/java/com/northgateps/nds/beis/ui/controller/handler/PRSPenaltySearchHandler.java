package com.northgateps.nds.beis.ui.controller.handler;

import java.util.ArrayList;
import java.util.List;
import com.northgateps.nds.beis.api.GetPenaltySearchResponseDetail;
import com.northgateps.nds.beis.api.PRSPenaltySearchNdsRequest;
import com.northgateps.nds.beis.api.PRSPenaltySearchNdsResponse;
import com.northgateps.nds.beis.api.PenaltyData;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.serviceclient.NdsResponseConsumer;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;
import com.northgateps.nds.platform.ui.view.ViewMessage;

/***
 * 
 * Handle penalty search
 *
 */
public class PRSPenaltySearchHandler extends AbstractViewEventHandler {

    private static final String prsPenaltySearchServiceEndPoint = ConfigurationFactory.getConfiguration().getString(
            "esbEndpoint.prsPenaltySearchService");

    @Override
    public void onAfterNavigationUpdate(NdsFormModel allModel, final ControllerState<?> controllerState) {

        final BeisAllModel model = (BeisAllModel) allModel;
        final UiData uiData = (UiData) model.getUiData();

        final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                PRSPenaltySearchNdsResponse.class);

        PRSPenaltySearchNdsRequest prsPenaltySearchNdsRequest = new PRSPenaltySearchNdsRequest();
        
        prsPenaltySearchNdsRequest.setPenaltySearch(uiData.getPenaltySearch());

        if (prsPenaltySearchNdsRequest != null) {

            if (!controllerState.isSyncController()) {
                uiSvcClient.post(prsPenaltySearchServiceEndPoint, prsPenaltySearchNdsRequest);
            } else {
                uiSvcClient.postSync(prsPenaltySearchServiceEndPoint, prsPenaltySearchNdsRequest);
            }

            uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

                @Override
                public boolean consume(NdsResponse response) {
                    if (!(response instanceof PRSPenaltySearchNdsResponse)) {
                        model.setPostSubmitMessage(null); // defer message
                                                          // construction back
                                                          // to controller
                        return false;
                    }

                    PRSPenaltySearchNdsResponse prsPenaltySearchNdsresponse = (PRSPenaltySearchNdsResponse) (response);

                    if (prsPenaltySearchNdsresponse.isSuccess()) {
                        List<PenaltyData> penalties = new ArrayList<PenaltyData>();
                        GetPenaltySearchResponseDetail getPenaltySearchResponseDetail = new GetPenaltySearchResponseDetail();

                        if (prsPenaltySearchNdsresponse.getGetPenaltySearchResponseDetail() != null) {
                            for (PenaltyData penaltyData : prsPenaltySearchNdsresponse.getGetPenaltySearchResponseDetail().getPenalties()) {
                                penalties.add(penaltyData);
                            }
                        }
                        getPenaltySearchResponseDetail.setPenalties(penalties);
                        uiData.setGetPenaltySearchResult(getPenaltySearchResponseDetail);
                        return true;
                    } else if (prsPenaltySearchNdsresponse.getNdsMessages() != null) {
                        model.setPostSubmitMessage(new ViewMessage("Error_ESB_withCausedBy",
                                prsPenaltySearchNdsresponse.getNdsMessages()));
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

    public void onBeforeValidation(NdsFormModel allModel, final ControllerState<?> controllerState) {
        final BeisAllModel model = (BeisAllModel) allModel;
        final UiData uiData = (UiData) model.getUiData();
        GetPenaltySearchResponseDetail getPenaltySearchResponseDetail = new GetPenaltySearchResponseDetail();
        getPenaltySearchResponseDetail.setPenalties(null);
        uiData.setGetPenaltySearchResult(getPenaltySearchResponseDetail);
    }
}
