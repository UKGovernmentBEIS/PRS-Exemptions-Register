package com.northgateps.nds.beis.ui.controller.handler;

import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.RegistrationStatusType;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;

/**
 * To clear previous organisation , person details or agent details when account type change
 */
public class SelectAccountTypeHandler extends AbstractViewEventHandler {

    @Override
    public void onBeforeValidation(NdsFormModel allModel, ControllerState<?> controllerState) {
        final BeisAllModel model = (BeisAllModel) allModel;
        final UiData uiData = (UiData) model.getUiData();
        if (model.getBeisRegistrationDetails().getUserDetails().getUserType() == UserType.AGENT) {
            if (model.getBeisRegistrationDetails().getAccountDetails() != null) {
                model.getBeisRegistrationDetails().getAccountDetails().setAccountType(null);
                model.getBeisRegistrationDetails().getAccountDetails().setPersonNameDetail(null);
                // because back-office use same field for organisation name and agent name which we need to retrieve
                // OrganisationNameDetail get null in AccountDetailEventHandler for FOUND_PARTIALLY_REGISTERED condition
                // after assigning the value
                if (uiData.getRegistrationStatus() != RegistrationStatusType.FOUND_PARTIALLY_REGISTERED) {
                    model.getBeisRegistrationDetails().getAccountDetails().setOrganisationNameDetail(null);
                }
            }
        } else {
            if (model.getBeisRegistrationDetails() != null
                    && model.getBeisRegistrationDetails().getAccountDetails() != null) {
                model.getBeisRegistrationDetails().getAccountDetails().setAgentNameDetails(null);
            }
        }

    }
}
