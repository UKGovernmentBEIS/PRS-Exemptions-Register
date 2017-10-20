package com.northgateps.nds.beis.ui.controller.handler;

import org.codehaus.plexus.util.StringUtils;

import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.generatereference.GenerateReferenceNdsRequest;
import com.northgateps.nds.platform.api.generatereference.GenerateReferenceNdsResponse;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;
import com.northgateps.nds.platform.ui.view.ViewMessage;
import com.northgateps.nds.platform.api.serviceclient.NdsResponseConsumer;

/**
 * 
 * Handler to generate and assigned Reference number
 *
 */
public class ReferenceNumberGenerationEventHandler extends AbstractViewEventHandler {

    /** Generate Reference Id service endpoint. */
    private static final String generateReferenceServiceEndPoint = ConfigurationFactory.getConfiguration().getString(
            "esbEndpoint.generateReferenceService");

    @Override
    public void onBeforeValidation(NdsFormModel allModel, final ControllerState<?> controllerState) {

        final BeisAllModel model = (BeisAllModel) allModel;
        // To reset the value if user change the exemption type --Start
        final UiData uiData = (UiData) model.getUiData();
        if (StringUtils.isNotEmpty(uiData.getSelectedExemptionType())
                && !uiData.getSelectedExemptionType().equals(model.getExemptionDetails().getExemptionType())) {
            model.getExemptionDetails().reset();
            ((UiData) model.getUiData()).reset(controllerState);
        }
        uiData.setSelectedExemptionType(model.getExemptionDetails().getExemptionType());
        // End
        model.populateDefaults();

        GenerateReferenceNdsRequest generateReferenceNdsRequest = new GenerateReferenceNdsRequest();

        String exemptionReference = model.getExemptionDetails().getReferenceId();
        if (exemptionReference == null) {

            final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                    GenerateReferenceNdsResponse.class);
            if (!controllerState.isSyncController()) {
                uiSvcClient.post(generateReferenceServiceEndPoint, generateReferenceNdsRequest);
            } else {
                uiSvcClient.postSync(generateReferenceServiceEndPoint, generateReferenceNdsRequest);
            }

            uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

                @Override
                public boolean consume(NdsResponse response) {
                    if (!(response instanceof GenerateReferenceNdsResponse)) {
                        model.setPostSubmitMessage(null); // defer message
                                                          // construction back
                                                          // to controller
                        return false;
                    }
                    GenerateReferenceNdsResponse generateReferenceNdsResponse = ((GenerateReferenceNdsResponse) (response));

                    if (generateReferenceNdsResponse.isSuccess()) {
                        model.getExemptionDetails().setReferenceId(generateReferenceNdsResponse.getReferenceId());
                        return generateReferenceNdsResponse.isSuccess();
                    } else if (generateReferenceNdsResponse.getNdsMessages() != null) {

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

}
