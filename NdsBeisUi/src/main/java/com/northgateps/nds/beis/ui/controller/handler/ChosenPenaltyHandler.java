package com.northgateps.nds.beis.ui.controller.handler;

import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.controller.NdsAction;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;

/***
 * Set the selected penalty reference number in the model and select next.
 */
public class ChosenPenaltyHandler extends AbstractViewEventHandler {
    
    protected final NdsLogger logger = NdsLogger.getLogger(getClass());

    @Override
    public void onBeforeValidation(NdsFormModel allModel, ControllerState<?> controllerState) {
        final BeisAllModel model = (BeisAllModel) allModel;
        final UiData uiData = (UiData) model.getUiData();
        final String penaltyRefNo = model.getAction().getValueName();
        
        if (!penaltyRefNo.isEmpty()) {
            uiData.setSelectedPenaltyRefNo(penaltyRefNo);
            model.setAction(NdsAction.NEXT);
        } else {
            logger.error("No penalty reference number found");
        }
        
    }

    
}
