package com.northgateps.nds.beis.ui.controller.handler;

import com.northgateps.nds.beis.api.ExemptionData;
import com.northgateps.nds.beis.api.ExemptionSearch;
import com.northgateps.nds.beis.api.PRSExemptionSearchNdsRequest;
import com.northgateps.nds.beis.api.PRSExemptionSearchNdsResponse;
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
 * Sends a call to the backoffice passing the exemption reference number
 * and stores the response in selectedExemptionData
 */
public class GetSelectedExemptionDetailsEventHandler extends AbstractViewEventHandler {

    private static final String prsExemptionSearchServiceEndPoint = ConfigurationFactory.getConfiguration().getString(
            "esbEndpoint.prsExemptionSearchService");

    protected static final NdsLogger logger = NdsLogger.getLogger(ChosenExemptionHandler.class);

    @Override
    public void onAfterResolvedView(NdsFormModel allModel, final ControllerState<?> controllerState) {
        final BeisAllModel model = (BeisAllModel) allModel;
        final UiData uiData = (UiData) model.getUiData();

        final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                PRSExemptionSearchNdsResponse.class);

        PRSExemptionSearchNdsRequest prsExemptionSearchNdsRequest = new PRSExemptionSearchNdsRequest();

        ExemptionSearch exemptionSearch = new ExemptionSearch();
        exemptionSearch.setExemptionRefNo(uiData.getSelectedExemptionRefNo());
        prsExemptionSearchNdsRequest.setExemptionSearch(exemptionSearch);

        if (!controllerState.isSyncController()) {
            uiSvcClient.post(prsExemptionSearchServiceEndPoint, prsExemptionSearchNdsRequest);
        } else {
            uiSvcClient.postSync(prsExemptionSearchServiceEndPoint, prsExemptionSearchNdsRequest);
        }

        uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

            @Override
            public boolean consume(NdsResponse response) {
                if (!(response instanceof PRSExemptionSearchNdsResponse)) {
                    model.setPostSubmitMessage(null);
                    return false;
                }

                PRSExemptionSearchNdsResponse prsExemptionSearchNdsResponse = ((PRSExemptionSearchNdsResponse) (response));
                if (prsExemptionSearchNdsResponse.isSuccess()) {

                    ExemptionData selectedExemptionData = new ExemptionData();
                    if (prsExemptionSearchNdsResponse.getGetExemptionSearchResponseDetail() != null
                            && prsExemptionSearchNdsResponse.getGetExemptionSearchResponseDetail().getExemptions() != null) {
                        
                        if (prsExemptionSearchNdsResponse.getGetExemptionSearchResponseDetail().getExemptions().size() == 1) {
                            selectedExemptionData = prsExemptionSearchNdsResponse.getGetExemptionSearchResponseDetail().getExemptions().get(0);
                            uiData.setSelectedExemptionData(selectedExemptionData);
                            
                        } else if (prsExemptionSearchNdsResponse.getGetExemptionSearchResponseDetail().getExemptions().size() == 0) {
                            logger.error("No details found for selected exemption");
                            
                        } else {
                            logger.error("Number of exemptions found is more than one");
                        }

                    }

                    return true;
                    
                } else if (prsExemptionSearchNdsResponse.getNdsMessages() != null) {
                    model.setPostSubmitMessage(new ViewMessage(prsExemptionSearchNdsResponse.getNdsMessages()));
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
