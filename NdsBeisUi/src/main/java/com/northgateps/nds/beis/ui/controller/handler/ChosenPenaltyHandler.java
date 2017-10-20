package com.northgateps.nds.beis.ui.controller.handler;

import java.math.BigInteger;
import java.util.List;

import com.northgateps.nds.beis.api.PenaltyData;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.controller.NdsAction;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;

public class ChosenPenaltyHandler extends AbstractViewEventHandler {

    @Override
    public void onBeforeValidation(NdsFormModel allModel, ControllerState<?> controllerState) {
        final BeisAllModel model = (BeisAllModel) allModel;
        final UiData uiData = (UiData) model.getUiData();
        final String penaltyRefNo = model.getAction().getValueName();
        PenaltyData penaltyData = getPenaltyDetails(penaltyRefNo, uiData.getGetPenaltySearchResult().getPenalties());
        uiData.setSelectedPenaltyData(penaltyData);
        model.setAction(NdsAction.NEXT);
    }

    private PenaltyData getPenaltyDetails(String penaltyRefNo, List<PenaltyData> penalties) {

        PenaltyData penaltyData = null;
        penaltyData = penalties.stream().filter(
                x -> x.getPenaltyRefNo().equals(new BigInteger(penaltyRefNo))).findFirst().get();
        return penaltyData;

    }
}
