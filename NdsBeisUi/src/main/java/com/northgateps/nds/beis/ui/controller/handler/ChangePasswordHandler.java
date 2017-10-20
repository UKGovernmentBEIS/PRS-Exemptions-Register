package com.northgateps.nds.beis.ui.controller.handler;

import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.AbstractNdsMvcModel;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;

public final class ChangePasswordHandler extends AbstractViewEventHandler{
    @Override
    public void onAfterNavigationUpdate(NdsFormModel allModel, ControllerState<?> controllerState) {
       controllerState.getPageFlowEngine().updateNavigationalState((AbstractNdsMvcModel) allModel, "personalised-change-password");
    }
}
