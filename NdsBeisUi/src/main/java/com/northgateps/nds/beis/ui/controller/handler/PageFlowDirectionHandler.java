package com.northgateps.nds.beis.ui.controller.handler;

import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.AbstractNdsMvcModel;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;

/**
 * Handler to navigate to named page
 *
 */
public class PageFlowDirectionHandler extends AbstractViewEventHandler{
    protected final NdsLogger logger = NdsLogger.getLogger(getClass());
    
    private String pageName; 

    @Override
    public void onAfterNavigationUpdate(NdsFormModel allModel, ControllerState<?> controllerState) {
        controllerState.getPageFlowEngine().updateNavigationalState((AbstractNdsMvcModel) allModel, pageName);
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
