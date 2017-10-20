package com.northgateps.nds.beis.ui.controller.handler;

import java.util.List;

import com.northgateps.nds.beis.api.BeisAccountDetails;
import com.northgateps.nds.beis.api.BeisUpdateUserDetails;
import com.northgateps.nds.beis.api.UpdateBeisRegistrationDetails;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsRequest;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsResponse;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.serviceclient.NdsResponseConsumer;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;

import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.security.PlatformUser;

import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;
import com.northgateps.nds.platform.ui.view.ViewMessage;

/**
 * Update account details in LDAP
 *
 */
public final class UpdateAccountDetailsEventHandler extends AbstractViewEventHandler{ 

	private static final String saveRegisteredAccountDetailsServiceEndPoint = ConfigurationFactory.getConfiguration().getString(
            "esbEndpoint.saveRegisteredAccountDetails");
	
	@Override
    public void onAfterNavigationUpdate(NdsFormModel allModel, ControllerState<?> controllerState) {
        
		final PlatformUser user = PlatformUser.getLoggedInUser();

        final BeisAllModel model = (BeisAllModel) allModel;
        

        if (user != null) {
            
            model.populateDefaults();

        	//populate the update registered account details
        	UpdateBeisRegistrationDetails registeredAccountDetails = new UpdateBeisRegistrationDetails();
        	registeredAccountDetails.setAccountDetails(model.getBeisRegistrationDetails().getAccountDetails());
        	        	        	
        	//create and setup the request
        	SaveRegisteredAccountDetailsNdsRequest saveRegisteredAccountDetailsNdsRequest = new SaveRegisteredAccountDetailsNdsRequest();
        	
        	saveRegisteredAccountDetailsNdsRequest.setPartiallyRegistered(false);

        	saveRegisteredAccountDetailsNdsRequest.setTenant(user.getTenant());
        	saveRegisteredAccountDetailsNdsRequest.setRegisteredAccountDetails(registeredAccountDetails);

            //BEIS-63 set the account and user details as information is required
            //Create the update user details and set user name and email
            BeisUpdateUserDetails updateUserDetails = new BeisUpdateUserDetails();
            updateUserDetails.setEmail(model.getBeisRegistrationDetails().getUserDetails().getEmail());            
            updateUserDetails.setUsername(model.getBeisRegistrationDetails().getUserDetails().getUsername());

            updateUserDetails.setUserType(model.getBeisRegistrationDetails().getUserDetails().getUserType());          

                        
            //Now set on the request
            saveRegisteredAccountDetailsNdsRequest.getRegisteredAccountDetails().setUpdateUserDetails(updateUserDetails);
        	
        	
            //BEIS-63 set the language code so it can be sent to external systems from the esb 
            if(saveRegisteredAccountDetailsNdsRequest.getRegisteredAccountDetails().getAccountDetails() == null){
                saveRegisteredAccountDetailsNdsRequest.getRegisteredAccountDetails().setAccountDetails(new BeisAccountDetails());
            }

            saveRegisteredAccountDetailsNdsRequest.getRegisteredAccountDetails().getAccountDetails().setLanguageCode(model.getLanguage());
        	
        	//make the call ensuring it is done before the next view is resolved
            final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
            		SaveRegisteredAccountDetailsNdsResponse.class);
            
            uiSvcClient.setTargetSlice("before-prepare-view");
            
            if (!controllerState.isSyncController()) {
                uiSvcClient.post(saveRegisteredAccountDetailsServiceEndPoint, saveRegisteredAccountDetailsNdsRequest);
            } else {
                uiSvcClient.postSync(saveRegisteredAccountDetailsServiceEndPoint, saveRegisteredAccountDetailsNdsRequest);
            }

            uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

                @Override
                public boolean consume(NdsResponse response) {               	
                	                	                	
                    if (!(response instanceof SaveRegisteredAccountDetailsNdsResponse)) {
                    	model.setPostSubmitMessage(null); // defer message construction back to controller
                        return false;
                    }
                    SaveRegisteredAccountDetailsNdsResponse saveRegisteredAccountDetailsNdsResponse = ((SaveRegisteredAccountDetailsNdsResponse) (response));

                    if (saveRegisteredAccountDetailsNdsResponse.isSuccess()) {

                        return saveRegisteredAccountDetailsNdsResponse.isSuccess();

                    } else if (saveRegisteredAccountDetailsNdsResponse.getNdsMessages() != null) {

                        List<ValidationViolation> violations = saveRegisteredAccountDetailsNdsResponse.getNdsMessages().getViolations();
                        if (violations != null) {
                        	model.setPostSubmitMessage(ViewMessage.fromViolations(violations));
                        } else {
                        	model.setPostSubmitMessage(
                                    new ViewMessage(saveRegisteredAccountDetailsNdsResponse.getNdsMessages()));
                        }
                        return false;

                    } else {

                    	model.setPostSubmitMessage(new ViewMessage("Error_No_more_information"));
                        return false;
                    }
                }

            });
            controllerState.registerUiServiceClient(uiSvcClient);
        }
    }
}