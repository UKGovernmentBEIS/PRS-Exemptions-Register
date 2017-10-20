package com.northgateps.nds.beis.ui.controller.handler;

import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.AbstractNdsMvcModel;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;
/**
 * Handler to navigate to home-page
 */
public class ViewIncidentDetailHandler extends AbstractViewEventHandler{
    @Override
    public void onAfterNavigationUpdate(NdsFormModel allModel, ControllerState<?> controllerState) {
        controllerState.getPageFlowEngine().updateNavigationalState((AbstractNdsMvcModel) allModel, "personalised-home-page");
    }

}
