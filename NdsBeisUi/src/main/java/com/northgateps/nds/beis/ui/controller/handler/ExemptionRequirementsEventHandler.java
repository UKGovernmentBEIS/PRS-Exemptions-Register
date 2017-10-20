package com.northgateps.nds.beis.ui.controller.handler;

import java.util.List;

import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.ExemptionTypeText;
import com.northgateps.nds.beis.ui.model.RefData;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;

/**
 * Sets model values to display dynamic pages
 */
public class ExemptionRequirementsEventHandler extends AbstractViewEventHandler {

    @Override
    public void onAfterResolvedView(NdsFormModel allModel, final ControllerState<?> controllerState) {
        final BeisAllModel model = (BeisAllModel) allModel;
        final UiData uiData = (UiData) model.getUiData();
        com.northgateps.nds.beis.ui.model.RefData refData = (RefData) uiData.getRefData();
        List<ExemptionTypeText> exemptionTypeTextList = refData.getExemptionTypeText();
              
        for (ExemptionTypeText exemptionTypeText : exemptionTypeTextList) {
            if (exemptionTypeText.getService() != null
                    && exemptionTypeText.getService().equals(model.getExemptionDetails().getPropertyType().toString())
                    && exemptionTypeText.getCode() != null
                    && exemptionTypeText.getCode().equals(model.getExemptionDetails().getExemptionType())) {
               
                uiData.setDuration(Integer.parseInt(exemptionTypeText.getDuration()));
                uiData.setDurationUnit(exemptionTypeText.getDurationUnit());
                uiData.setSelectedExemptionTypeText(exemptionTypeText);
                        
            }
        }
    }

}
