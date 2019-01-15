package com.northgateps.nds.beis.ui.controller.handler;

import com.northgateps.nds.beis.api.AgentNameDetails;
import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.RegistrationStatusType;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;
/** 
 * Depending on user type we need to display details on account details page
 * As user type is not storing in back-office,we need to check here and
 * set agent name as for partially registered agent
 * because back-office use same field for organisation name and agent name * 
 *
 */

public class AccountDetailEventHandler extends AbstractViewEventHandler {

    public void onAfterResolvedView(NdsFormModel allModel, ControllerState<?> controllerState) {
        final BeisAllModel model = (BeisAllModel) allModel;

        final UiData uiData = (UiData) model.getUiData();
        if(uiData.getRegistrationStatus() == RegistrationStatusType.FOUND_PARTIALLY_REGISTERED){
           if(model.getBeisRegistrationDetails().getUserDetails().getUserType() == UserType.AGENT){
               if(model.getBeisRegistrationDetails().getAccountDetails().getOrganisationNameDetail() !=null  &&  model.getBeisRegistrationDetails().getAccountDetails().getOrganisationNameDetail().getOrgName() !=null){
                   AgentNameDetails agentNameDetails = new AgentNameDetails();
                   agentNameDetails.setAgentName(model.getBeisRegistrationDetails().getAccountDetails().getOrganisationNameDetail().getOrgName());
                   model.getBeisRegistrationDetails().getAccountDetails().setAgentNameDetails(agentNameDetails);
                   model.getBeisRegistrationDetails().getAccountDetails().setOrganisationNameDetail(null);
                   model.getBeisRegistrationDetails().getAccountDetails().setAccountType(null);
               }
                   
           }
            
        }
        
    }
    
}
