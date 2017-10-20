package com.northgateps.nds.beis.ui.controller.handler;

import java.util.List;

import com.northgateps.nds.beis.api.ExemptionData;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.controller.NdsAction;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;

/***
 * 
 * Handle result after specific exemption selected.
 *
 */
public class ChosenExemptionHandler extends AbstractViewEventHandler {

    @Override
    public void onBeforeValidation(NdsFormModel allModel, ControllerState<?> controllerState) {
        final BeisAllModel model = (BeisAllModel) allModel;
        final UiData uiData = (UiData) model.getUiData();
        final String exemptionRefNo = model.getAction().getValueName();
        ExemptionData exemptionData = getExemptionDeatils(exemptionRefNo,
                uiData.getGetExemptionSearchResult().getExemptions());
        uiData.setSelectedExemptionData(exemptionData);
        model.setAction(NdsAction.NEXT);
    }

    private ExemptionData getExemptionDeatils(String exemptionRefNo, List<ExemptionData> exemptions) {

        ExemptionData exemptionData = null;
        exemptionData = exemptions.stream().filter(x -> x.getExemptionRefNo().equals(exemptionRefNo)).findFirst().get();
        return exemptionData;

    }
}
