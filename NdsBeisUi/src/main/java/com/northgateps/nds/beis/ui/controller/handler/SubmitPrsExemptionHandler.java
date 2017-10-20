package com.northgateps.nds.beis.ui.controller.handler;

import java.util.List;
import java.util.Map;
import com.northgateps.nds.platform.api.serviceclient.NdsResponseConsumer;
import com.northgateps.nds.beis.api.ExemptionDetail;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionDetails;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsRequest;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsResponse;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.security.PlatformUser;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;
import com.northgateps.nds.platform.ui.view.ViewMessage;

/***
 * 
 * To Sumbit exemption details
 *
 */
public class SubmitPrsExemptionHandler extends AbstractViewEventHandler {

    private static final String serviceEndPoint = ConfigurationFactory.getConfiguration().getString(
            "esbEndpoint.RegisterPrsExemption");

    /**
     * submits request to esb to register the exemption
     */
    @Override
    public void onAfterResolvedView(NdsFormModel allModel, final ControllerState<?> controllerState) {
        final BeisAllModel model = (BeisAllModel) allModel;
        final UiData uiData = (UiData) model.getUiData();
        final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                RegisterPrsExemptionNdsResponse.class);

        RegisterPrsExemptionNdsRequest registerPrsExemptionNdsRequest = new RegisterPrsExemptionNdsRequest();

        // get logged in user's details
        final PlatformUser user = PlatformUser.getLoggedInUser();
        String userName = user.getUsername();
        registerPrsExemptionNdsRequest.setUserName(userName);
        registerPrsExemptionNdsRequest.setTenant(user.getTenant());
        RegisterPrsExemptionDetails registerPrsExemptionDetails = new RegisterPrsExemptionDetails();
        ExemptionDetail exemptionDetails = model.getExemptionDetails();
        registerPrsExemptionDetails.setExemptionDetails(exemptionDetails);
        registerPrsExemptionNdsRequest.setPwsText(uiData.getSelectedExemptionTypeText().getPwsDescription());
        registerPrsExemptionNdsRequest.setMaxPenaltyValue(uiData.getPenaltyValue());
        registerPrsExemptionNdsRequest.setRegisterPrsExemptionDetails(registerPrsExemptionDetails);

        if (!controllerState.isSyncController()) {
            uiSvcClient.post(serviceEndPoint, registerPrsExemptionNdsRequest);
        } else {
            uiSvcClient.postSync(serviceEndPoint, registerPrsExemptionNdsRequest);
        }

        uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

            @Override
            public boolean consume(NdsResponse response) {
                if (!(response instanceof RegisterPrsExemptionNdsResponse)) {
                    model.setPostSubmitMessage(null); // defer message
                                                      // construction back
                                                      // to controller
                    return false;
                }

                RegisterPrsExemptionNdsResponse registerPrsExemptionNdsResponse = (RegisterPrsExemptionNdsResponse) (response);

                if (registerPrsExemptionNdsResponse.isSuccess()) {
                    return true;
                } else if (registerPrsExemptionNdsResponse.getNdsMessages() != null) {

                    List<ValidationViolation> violations = registerPrsExemptionNdsResponse.getNdsMessages().getViolations();
                    if (violations != null) {
                        for (ValidationViolation validationViolation : violations) {
                            if (validationViolation.getMessage().contains(
                                    "Unable to send e-mail for exemption registration") && violations.size() == 1) {
                                final Map<String, Object> attributes = controllerState.getAttributes();
                                attributes.put("fault", controllerState.getController().sysErr(
                                        ViewMessage.fromViolations(violations), controllerState));
                                return true;
                            }
                        }
                    }
                    model.setPostSubmitMessage(new ViewMessage("Error_ESB_withCausedBy",
                            registerPrsExemptionNdsResponse.getNdsMessages()));

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
    public void onBeforeValidation(NdsFormModel allModel, ControllerState<?> controllerState) {
        BeisAllModel model = (BeisAllModel) allModel;

        model.setExemptionDetails(new ExemptionDetail());
        ((UiData) model.getUiData()).reset(controllerState);
    }

}