package com.northgateps.nds.beis.ui.controller.handler;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;

import com.northgateps.nds.beis.api.BeisUpdateUserDetails;
import com.northgateps.nds.beis.api.UpdateBeisRegistrationDetails;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsRequest;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsResponse;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.controller.NdsFieldMappingResponseConsumer;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.security.PlatformUser;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;
import com.northgateps.nds.platform.ui.view.ViewMessage;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Requests the ESB to submit update email address request.
 */
public class BeisUpdateEmailEventHandler extends AbstractViewEventHandler {

    protected final NdsLogger logger = NdsLogger.getLogger(getClass());

    private static final String saveRegisteredAccountDetailsServiceEndPoint = 
    		ConfigurationFactory.getConfiguration().getString("esbEndpoint.saveRegisteredAccountDetails");
    
    static ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();
    public static final String EMAIL_CONFIGURATION_FILE=configurationManager.getString("email.templatename");


    @Override
    public void onAfterNavigationUpdate(NdsFormModel model, ControllerState<?> controllerState) {
        final PlatformUser user = PlatformUser.getLoggedInUser();

        final BeisAllModel allModel = (BeisAllModel) model;

        if (user != null) {

            //populate the update registered account details
            UpdateBeisRegistrationDetails registeredAccountDetails = new UpdateBeisRegistrationDetails();
            //registeredAccountDetails.setContactAddress(allModel.getModifiedContactAddress());
            
            //create and setup the request
            SaveRegisteredAccountDetailsNdsRequest saveRegisteredAccountDetailsNdsRequest = new SaveRegisteredAccountDetailsNdsRequest();
            saveRegisteredAccountDetailsNdsRequest.setPartiallyRegistered(false);
            saveRegisteredAccountDetailsNdsRequest.setTenant(user.getTenant());
            saveRegisteredAccountDetailsNdsRequest.setRegisteredAccountDetails(registeredAccountDetails);
            
            if (EMAIL_CONFIGURATION_FILE != null) {
                saveRegisteredAccountDetailsNdsRequest.setEmailTemplateName(EMAIL_CONFIGURATION_FILE);
            }
            
            String signInUrl= null;
            
            try {
                signInUrl = ConfigurationFactory.getConfiguration().getString("signin.url");
            } catch(NoSuchElementException e) {
                logger.info("SignUrl in property is not used in the Application.");
            }
            
            if (!StringUtils.isEmpty(signInUrl)) {
                saveRegisteredAccountDetailsNdsRequest.setPublicWebAddress(signInUrl);
            }

            //Create the update user details and set user name and email
            BeisUpdateUserDetails updateUserDetails = new BeisUpdateUserDetails();
            updateUserDetails.setEmail(allModel.getUpdateEmailDetails().getEmail());
            updateUserDetails.setUsername(allModel.getBeisRegistrationDetails().getUserDetails().getUsername());
            updateUserDetails.setPassword(allModel.getUpdateEmailDetails().getPassword());
            updateUserDetails.setUserType(allModel.getBeisRegistrationDetails().getUserDetails().getUserType());
            updateUserDetails.setUpdatingEmail(true);

            //Now set on the request
            saveRegisteredAccountDetailsNdsRequest.getRegisteredAccountDetails().setUpdateUserDetails(updateUserDetails);

            //make the call ensuring it is called before the next view is resolved
            final UiServiceClient uiSvcClient = 
            		controllerState.getController().getUiSvcClientFactory().getInstance(SaveRegisteredAccountDetailsNdsResponse.class);
            uiSvcClient.setTargetSlice("before-prepare-view");

            if (!controllerState.isSyncController()) {
                uiSvcClient.post(saveRegisteredAccountDetailsServiceEndPoint, saveRegisteredAccountDetailsNdsRequest);
            } else {
                uiSvcClient.postSync(saveRegisteredAccountDetailsServiceEndPoint, saveRegisteredAccountDetailsNdsRequest);
            }

            uiSvcClient.setResponseConsumer(new NdsFieldMappingResponseConsumer() {
    			
    			@Override
    			public String mapFieldPath(String requestPath) {
    			
    				if (requestPath.compareTo("updateEmailDetails.password") == 0){        			
    					return "updateEmailDetails.password";
    				}
    				
    				if (requestPath.compareTo("updateEmailDetails.email") == 0){        			
    					return "updateEmailDetails.email";
    				}
    				
    				return null;
    			}
    			
                @Override
                public boolean consume(NdsResponse response) {
                	
	                if (!(response instanceof SaveRegisteredAccountDetailsNdsResponse)) {
	                    allModel.setPostSubmitMessage(null); // defer message construction back to controller
	                    return false;
	                }
	                
	                SaveRegisteredAccountDetailsNdsResponse saveRegisteredAccountDetailsNdsResponse = ((SaveRegisteredAccountDetailsNdsResponse) (response));
	
	                if (saveRegisteredAccountDetailsNdsResponse.isSuccess()) {
	
	                    return true;
	
	                } else if (saveRegisteredAccountDetailsNdsResponse.getNdsMessages() != null) {
		                    
	                    return false;
	
	                } else {
	
	                    allModel.setPostSubmitMessage(new ViewMessage("Error_No_more_information"));
	                    return false;
	                }
                }
            });
            
            controllerState.registerUiServiceClient(uiSvcClient);
        }
    }
}
