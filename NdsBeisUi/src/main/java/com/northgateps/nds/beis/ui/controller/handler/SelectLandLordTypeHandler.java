package com.northgateps.nds.beis.ui.controller.handler;

import com.northgateps.nds.beis.api.AccountType;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;

/**
 * 
 * To   clear previous organisation or person details when landlordtype change 
 *
 */
public class SelectLandLordTypeHandler extends AbstractViewEventHandler {

    @Override
    public void onBeforeValidation(NdsFormModel allModel, ControllerState<?> controllerState) {
        final BeisAllModel model = (BeisAllModel) allModel;
        if (model.getBeisRegistrationDetails().getAccountDetails().getAccountType() == AccountType.PERSON)
        {
            model.getBeisRegistrationDetails().getAccountDetails().setOrganisationNameDetail(null);
        }
        else
        {
            model.getBeisRegistrationDetails().getAccountDetails().setPersonNameDetail(null);
        }
        
    }

}
