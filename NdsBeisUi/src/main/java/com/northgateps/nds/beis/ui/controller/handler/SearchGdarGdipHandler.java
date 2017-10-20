package com.northgateps.nds.beis.ui.controller.handler;

import com.northgateps.nds.beis.api.viewdocument.ViewPdfNdsRequest;
import com.northgateps.nds.beis.api.viewdocument.ViewPdfNdsResponse;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.GdipGdarSearch.SearchResultStatus;
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

/**
 * 
 * View event handler for the gdar gdip search page
 *
 */
public class SearchGdarGdipHandler extends AbstractViewEventHandler {

    protected final NdsLogger logger = NdsLogger.getLogger(getClass());

    @Override
    public void onAfterNavigationUpdate(NdsFormModel allModel, ControllerState<?> controllerState) {

        final NdsLogger logger = NdsLogger.getLogger(SearchGdarGdipHandler.class);

        final String serviceEndPoint = ConfigurationFactory.getConfiguration().getString("esbEndpoint.viewGdarGdip");
        final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                ViewPdfNdsResponse.class);

        logger.trace("ESB endpoint from configuration is: " + serviceEndPoint);

        BeisAllModel model = (BeisAllModel) allModel;
        UiData uiData = (UiData) model.getUiData();

        // assume no document available
        uiData.getGdarGdipSearch().resetFileContent();

        ViewPdfNdsRequest request = new ViewPdfNdsRequest();
        request.setReferenceNumber(uiData.getGdarGdipSearch().getSearchTerm());
        request.setIncludeDocument(uiData.getGdarGdipSearch().isIncludeDocument());

        if (!controllerState.isSyncController()) {
            uiSvcClient.post(serviceEndPoint, request);
        } else {
            uiSvcClient.postSync(serviceEndPoint, request);
        }

        uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

            @Override
            public boolean consume(NdsResponse response) {

                ViewPdfNdsResponse searchResponse = (ViewPdfNdsResponse) response;

                if (searchResponse != null) {

                    if (searchResponse.isSuccess()) {
                        if (searchResponse.getDocument() != null) {
                            // set the document details
                            uiData.getGdarGdipSearch().setFileName(searchResponse.getDocument().getDocumentName());
                            uiData.getGdarGdipSearch().setStatus(SearchResultStatus.FILE_AVAILABLE);
                        }

                        else if (searchResponse.getSuperseded()) {
                            uiData.getGdarGdipSearch().setStatus(SearchResultStatus.SUPERSEDED);

                        } else {
                            uiData.getGdarGdipSearch().setStatus(SearchResultStatus.NO_RESULTS);
                        }

                        return true;
                    } else if (searchResponse.getNdsMessages() != null) {
                        model.setPostSubmitMessage(
                                new ViewMessage("Error_ESB_withCausedBy", searchResponse.getNdsMessages()));
                    } else {
                        model.setPostSubmitMessage(new ViewMessage("Error_No_more_information"));
                    }
                }

                return false;
            }
        });

        controllerState.registerUiServiceClient(uiSvcClient);
    }

    public static FileDetails performSearch(String searchTerm, final ControllerState<?> controllerState) {

        final NdsLogger logger = NdsLogger.getLogger(SearchGdarGdipHandler.class);

        final String serviceEndPoint = ConfigurationFactory.getConfiguration().getString("esbEndpoint.viewGdarGdip");
        final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                ViewPdfNdsResponse.class);

        logger.trace("ESB endpoint from configuration is: " + serviceEndPoint);
        ViewPdfNdsRequest request = new ViewPdfNdsRequest();
        request.setReferenceNumber(searchTerm);
        request.setIncludeDocument(true);

        // sync call to get the data
        uiSvcClient.postSync(serviceEndPoint, request);
        ViewPdfNdsResponse searchResponse = (ViewPdfNdsResponse) uiSvcClient.getResponse();

        FileDetails fileDetails = null;

        if (searchResponse != null && searchResponse.isSuccess()) {

            fileDetails = new FileDetails();
            fileDetails.setSource(searchResponse.getDocument().getBinaryData());
            fileDetails.setContentType("application/" + searchResponse.getDocument().getDocumentFileType());
            fileDetails.setFileName(searchResponse.getDocument().getDocumentName());
            fileDetails.setFileSize(searchResponse.getDocument().getBinaryData().length);

        }

        return fileDetails;
    }
}
