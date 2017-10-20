package com.northgateps.nds.beis.ui.controller.handler;

import java.util.ArrayList;

import java.util.List;
import com.northgateps.nds.beis.api.ExemptionData;
import com.northgateps.nds.beis.api.ExemptionSearch;
import com.northgateps.nds.beis.api.GetExemptionSearchResponseDetail;
import com.northgateps.nds.beis.api.PRSExemptionSearchNdsRequest;
import com.northgateps.nds.beis.api.PRSExemptionSearchNdsResponse;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.api.FileDetails;
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
 * Handle exemption search
 *
 */
public class PRSExemptionSearchHandler extends AbstractViewEventHandler {

    private static final String prsExemptionSearchServiceEndPoint = ConfigurationFactory.getConfiguration().getString(
            "esbEndpoint.prsExemptionSearchService");

    @Override
    public void onAfterNavigationUpdate(NdsFormModel allModel, final ControllerState<?> controllerState) {
        final BeisAllModel model = (BeisAllModel) allModel;
        final UiData uiData = (UiData) model.getUiData();
        final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                PRSExemptionSearchNdsResponse.class);

        PRSExemptionSearchNdsRequest prsExemptionSearchNdsRequest = new PRSExemptionSearchNdsRequest();

        prsExemptionSearchNdsRequest.setExemptionSearch(uiData.getExemptionSearch());

       if (!controllerState.isSyncController()) {
            uiSvcClient.post(prsExemptionSearchServiceEndPoint, prsExemptionSearchNdsRequest);
        } else {
            uiSvcClient.postSync(prsExemptionSearchServiceEndPoint, prsExemptionSearchNdsRequest);
        }

        uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

            @Override
            public boolean consume(NdsResponse response) {
                if (!(response instanceof PRSExemptionSearchNdsResponse)) {
                    model.setPostSubmitMessage(null); // defer message
                    // construction back
                    // to controller
                    return false;
                }
                PRSExemptionSearchNdsResponse prsExemptionSearchNdsResponse = ((PRSExemptionSearchNdsResponse) (response));

                if (prsExemptionSearchNdsResponse.isSuccess()) {

                    List<ExemptionData> exemptions = new ArrayList<ExemptionData>();
                    GetExemptionSearchResponseDetail getExemptionSearchResponseDetail = new GetExemptionSearchResponseDetail();
                    if (prsExemptionSearchNdsResponse.getGetExemptionSearchResponseDetail() != null) {

                        for (ExemptionData exm : prsExemptionSearchNdsResponse.getGetExemptionSearchResponseDetail().getExemptions()) {
                            exemptions.add(exm);
                        }
                    }
                    getExemptionSearchResponseDetail.setExemptions(exemptions);
                    uiData.setGetExemptionSearchResult(getExemptionSearchResponseDetail);
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

    public void onBeforeValidation(NdsFormModel allModel, final ControllerState<?> controllerState) {
        final BeisAllModel model = (BeisAllModel) allModel;
        final UiData uiData = (UiData) model.getUiData();
        GetExemptionSearchResponseDetail getExemptionSearchResponseDetail = new GetExemptionSearchResponseDetail();
        getExemptionSearchResponseDetail.setExemptions(null);
        uiData.setGetExemptionSearchResult(getExemptionSearchResponseDetail);
    }

    public static FileDetails performSearch(String exemptionRefNo, final ControllerState<?> controllerState) {

        final NdsLogger logger = NdsLogger.getLogger(ChosenExemptionHandler.class);
        final String serviceEndPoint = ConfigurationFactory.getConfiguration().getString(
                "esbEndpoint.prsExemptionSearchService");
        final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                PRSExemptionSearchNdsResponse.class);

        logger.trace("ESB endpoint from configuration is: " + serviceEndPoint);

        PRSExemptionSearchNdsRequest request = new PRSExemptionSearchNdsRequest();
        ExemptionSearch exemptionSearch = new ExemptionSearch();
        exemptionSearch.setExemptionRefNo(exemptionRefNo);
        exemptionSearch.setEpcRequired(true);
        request.setExemptionSearch(exemptionSearch);
        
        // sync call to get the data
        uiSvcClient.postSync(serviceEndPoint, request);
        PRSExemptionSearchNdsResponse searchResponse = (PRSExemptionSearchNdsResponse) uiSvcClient.getResponse();

        FileDetails fileDetails = null;

        if (searchResponse.getGetExemptionSearchResponseDetail().getExemptions().get(0).isEpcExists() == true) {

            fileDetails = new FileDetails();
            fileDetails.setSource(
                    searchResponse.getGetExemptionSearchResponseDetail().getExemptions().get(0).getEpcObject());
            fileDetails.setContentType(
                    "application/"+searchResponse.getGetExemptionSearchResponseDetail().getExemptions().get(0).getMimeType()); 
            fileDetails.setFileName(
                    "EPC document."+searchResponse.getGetExemptionSearchResponseDetail().getExemptions().get(0).getMimeType());
        }

        return fileDetails;
    }

}
