package com.northgateps.nds.beis.ui.controller.handler;

import java.util.List;

import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.BeisUpdateUserDetails;
import com.northgateps.nds.beis.api.UpdateBeisRegistrationDetails;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsRequest;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsResponse;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.RegistrationStatusType;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.serviceclient.NdsResponseConsumer;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.AbstractNdsMvcModel;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.security.PlatformUser;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;
import com.northgateps.nds.platform.ui.view.ViewMessage;

/**
 * Requests the ESB to submit a maintain party request, if the user was only
 * partially registered which currently means the FOUNDATION_LAYER_PARTY_REF uid
 * has not been set to the party ref from the FL back office.
 */
public class AccountAddressHandler extends AbstractViewEventHandler {

    protected final NdsLogger logger = NdsLogger.getLogger(getClass());
    
    private static final String saveRegisteredAccountDetailsServiceEndPoint = ConfigurationFactory.getConfiguration().getString(
            "esbEndpoint.saveRegisteredAccountDetails");

    @Override
    public void onAfterValidation(NdsFormModel model, ControllerState<?> controllerState) {
        logger.trace("onAfterValidation Entry");

        //If the page has validated and does not have errors
        if(!controllerState.getBindingResult().hasErrors()){
            
            final PlatformUser user = PlatformUser.getLoggedInUser();
        
            //If we have a logged on user then this is a partial registration
            if (user != null) {
    
                // cast the generic model to the beis model
                final BeisAllModel beisModel = (BeisAllModel) model;                
                
                final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                        SaveRegisteredAccountDetailsNdsResponse.class);
    
                // Get a request object
                SaveRegisteredAccountDetailsNdsRequest request = createSaveRegisteredAccountDetailsNdsRequest(
                        beisModel.getBeisRegistrationDetails(), model.getLanguage(), user.getTenant());
    
                // Force this request to the REST service to complete before navigation update.
                uiSvcClient.setTargetSlice("before-navigation-update");
    
                if (!controllerState.isSyncController()) {
                    uiSvcClient.post(saveRegisteredAccountDetailsServiceEndPoint, request);
                } else {
                    uiSvcClient.postSync(saveRegisteredAccountDetailsServiceEndPoint, request);
                }
    
                uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {
    
                    @Override
                    public boolean consume(NdsResponse response) {
                        if (!(response instanceof SaveRegisteredAccountDetailsNdsResponse)) {
                            beisModel.setPostSubmitMessage(null); // defer message construction back to controller
                            return false;
                        }
                        SaveRegisteredAccountDetailsNdsResponse saveRegisteredAccountDetailsNdsResponse = ((SaveRegisteredAccountDetailsNdsResponse) (response));
    
                        if (saveRegisteredAccountDetailsNdsResponse.isSuccess()) {
    
                            return saveRegisteredAccountDetailsNdsResponse.isSuccess();
    
                        } else if (saveRegisteredAccountDetailsNdsResponse.getNdsMessages() != null) {
    
                            List<ValidationViolation> violations = saveRegisteredAccountDetailsNdsResponse.getNdsMessages().getViolations();
                            if (violations != null) {
                                beisModel.setPostSubmitMessage(ViewMessage.fromViolations(violations));
                            } else {
                                beisModel.setPostSubmitMessage(
                                        new ViewMessage(saveRegisteredAccountDetailsNdsResponse.getNdsMessages()));
                            }
                            return false;
    
                        } else {
    
                            beisModel.setPostSubmitMessage(new ViewMessage("Error_No_more_information"));
                            return false;
                        }
                    }
    
                });
    
                controllerState.registerUiServiceClient(uiSvcClient);    
            }
        }

        logger.trace("onAfterValidation Exit");
    }

    /**
     * If partially registered (we have a logged on user) then move back to the personalised dashboard
     */
    @Override
    public void onAfterNavigationUpdate(NdsFormModel model, ControllerState<?> controllerState) {

        logger.trace("onAfterNavigationUpdate Entry");
                
        final PlatformUser user = PlatformUser.getLoggedInUser();
        
        //If we have a logged on user then this is a partial registration
        if (user != null) {
            
            // cast the generic model to the beis model
            final BeisAllModel beisModel = (BeisAllModel) model;  
            final UiData uiData = (UiData)beisModel.getUiData();

            //Clear the registration status for the dash board to re-establish
            uiData.setRegistrationStatus(RegistrationStatusType.USE_REGISTERED);
            
            controllerState.getPageFlowEngine().updateNavigationalState((AbstractNdsMvcModel) model,
                    "personalised-dashboard");
            
        }
        
        logger.trace("onAfterNavigationUpdate Exit");
    }

    /**
     * Helper function to create the SaveRegisteredAccountDetailsNdsRequest object based on
     * BeisRegistrationDetails
     * 
     * @param registrationDetails - Details as entered the user
     * @param languageCode - So messages can be returned different languages
     * @return
     */
    private SaveRegisteredAccountDetailsNdsRequest createSaveRegisteredAccountDetailsNdsRequest(
            BeisRegistrationDetails registrationDetails, String languageCode,
            String tenant) {

        SaveRegisteredAccountDetailsNdsRequest request = new SaveRegisteredAccountDetailsNdsRequest();
        UpdateBeisRegistrationDetails updateRegistrationDetails = new UpdateBeisRegistrationDetails();
        BeisUpdateUserDetails updateUserDetails = new BeisUpdateUserDetails();

        updateRegistrationDetails.setAccountDetails(registrationDetails.getAccountDetails());
        updateRegistrationDetails.setContactAddress(registrationDetails.getContactAddress());

        if (registrationDetails.getUserDetails() != null) {
            // Copy userDetails to update user details
            updateUserDetails.setEmail(registrationDetails.getUserDetails().getEmail());
            updateUserDetails.setUsername(registrationDetails.getUserDetails().getUsername());
            request.setUsername(updateUserDetails.getUsername());           
            updateUserDetails.setUserType(registrationDetails.getUserDetails().getUserType());
        }

        if (updateRegistrationDetails.getAccountDetails() != null) {
            updateRegistrationDetails.getAccountDetails().setLanguageCode(languageCode);
        }

        updateRegistrationDetails.setUpdateUserDetails(updateUserDetails);

        // Copy the details from registration to update registration
        request.setTenant(tenant);
        request.setRegisteredAccountDetails(updateRegistrationDetails);
        request.setPartiallyRegistered(true);

        return request;

    }
}
