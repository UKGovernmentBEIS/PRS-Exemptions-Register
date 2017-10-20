package com.northgateps.nds.beis.ui.controller;

import java.util.Map;

import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.platform.api.model.NdsDataModel;
import com.northgateps.nds.platform.application.api.utils.UiUtilities;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.controller.NdsViewNamePopulator;

public class ViewNamePopulator implements NdsViewNamePopulator {

    @Override
    public String populate(String viewName, Map<String, Object> attributes, ControllerState<?> controllerState,
            NdsDataModel formModel) {
        BeisAllModel model = (BeisAllModel) formModel;

        return UiUtilities.substitute(viewName, "tenant", model.getTenant());
    }

}
