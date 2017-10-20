package com.northgateps.nds.beis.ui.controller.handler;

import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsRequest;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsResponse;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;

import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.controller.NdsFieldMappingResponseConsumer;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;
import com.northgateps.nds.platform.ui.view.ViewMessage;

/**
 * Create registered user details to LDAP
 *
 */
public class CreateBeisRegisteredDetailsEventHandler extends AbstractViewEventHandler {

    private static final String createBeisRegisteredUserDetailsServiceEndPoint = ConfigurationFactory.getConfiguration().getString(
            "esbEndpoint.beisregistration");

    @Override
    public void onAfterResolvedView(NdsFormModel allModel, final ControllerState<?> controllerState) {

        final BeisAllModel model = (BeisAllModel) allModel;

        BeisRegistrationNdsRequest beisRegistrationNdsRequest = new BeisRegistrationNdsRequest();

        model.getBeisRegistrationDetails().setTenant(model.getTenant());

        beisRegistrationNdsRequest.setRegistrationDetails(model.getBeisRegistrationDetails());
        
        //BEIS-63 set the language code so it can be sent to external systems from the esb
        beisRegistrationNdsRequest.getRegistrationDetails().getAccountDetails().setLanguageCode(model.getLanguage());

        final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                BeisRegistrationNdsResponse.class);
        if (!controllerState.isSyncController()) {
            uiSvcClient.post(createBeisRegisteredUserDetailsServiceEndPoint, beisRegistrationNdsRequest);
        } else {
            uiSvcClient.postSync(createBeisRegisteredUserDetailsServiceEndPoint, beisRegistrationNdsRequest);
        }

        uiSvcClient.setResponseConsumer(new NdsFieldMappingResponseConsumer() {
        	
        	@Override 
        	public String mapFieldPath(String requestPath){
        		
        		//map our known request fields to model fields
        		if(requestPath.compareTo("registrationDetails.userDetails.username") == 0){        			
        			return "beisRegistrationDetails.userDetails.username";
        		}
        		else if(requestPath.compareTo("registrationDetails.userDetails.password") == 0){        			
        			return "beisRegistrationDetails.userDetails.password";
        		}        		
        		
        		return null;
        	}
        	
            @Override
            public boolean consume(NdsResponse response) {
                if (!(response instanceof BeisRegistrationNdsResponse)) {
                    model.setPostSubmitMessage(null); // defer message construction back to controller
                    return false;
                }
                BeisRegistrationNdsResponse beisRegistrationNdsResponse = ((BeisRegistrationNdsResponse) (response));
                
                if (beisRegistrationNdsResponse.isSuccess()) {

                    return beisRegistrationNdsResponse.isSuccess();

                } else if (beisRegistrationNdsResponse.getNdsMessages() != null) {
                    
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
