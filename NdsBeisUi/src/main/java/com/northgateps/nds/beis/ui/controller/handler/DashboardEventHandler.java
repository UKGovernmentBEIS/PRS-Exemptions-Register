package com.northgateps.nds.beis.ui.controller.handler;

import java.util.HashMap;
import java.util.Map;

import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.BeisUpdateUserDetails;
import com.northgateps.nds.beis.api.BeisUserDetails;
import com.northgateps.nds.beis.api.UpdateBeisRegistrationDetails;
import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsRequest;
import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsResponse;
import com.northgateps.nds.beis.api.getpartiallyregistereddetails.GetPartiallyRegisteredDetailsNdsRequest;
import com.northgateps.nds.beis.api.getpartiallyregistereddetails.GetPartiallyRegisteredDetailsNdsResponse;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.RegistrationStatusType;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.api.NdsMessages;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.serviceclient.NdsResponseConsumer;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.AbstractNdsMvcModel;
import com.northgateps.nds.platform.ui.model.AddressField;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.model.NdsUiData;
import com.northgateps.nds.platform.ui.security.PlatformUser;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;

/**
 * Requests the ESB to submit view personalised-dashboard request, ie it gets the exemptions
 * for the party ref from the back office if the user is fully registered.
 *        
 * At the time of coding a partially registered user is one that has not got a UID
 * in LDAP against the user for FOUNDATION_LAYER_PARTY_SERVICE. This indicates the
 * registration service did not successfully communicate the registration to FL.
 * 
 * When this is detected the LDAP details are returned and the user is sent to the 
 * select landlord type page to go through account registration.
 * 
 * RegistrationStatusType is used throughout to avoid hitting the esb more times than
 * required.
 * 
 */
public class DashboardEventHandler extends AbstractViewEventHandler {

    protected final NdsLogger logger = NdsLogger.getLogger(getClass());

    /**
     * The dashboard is the first page. 
     * 
     * When a page is navigated to only the onAfterResolvedView event should fire
     * When moving away from a page onBeforeValidation, onAfterValidation and onAfterNavigationUpdate.
     * 
     * This is the first page in the application and the usual rules do not apply.
     * When moving from CAS login to here all events fire (onBeforeValidation, onAfterValidation, onAfterNavigationUpdate, onAfterResolvedView)
     * When moving back to here from pages such as account detail (onAfterResolvedView).
     * 
     * This event fires:
     * 1. When moving from the security logon page to the dashboard
     * 2. When navigating away from the dashboard page.
     * 
     * The code in this event handler should only run for the first scenario.
     * The viewDashboard service will return the details required for the dashboard page
     * or an indicator that the user is only partially registered.
     *  
     * At the time of coding a partially registered user is one that has not got a UID
     * in LDAP against the user for FOUNDATION_LAYER_PARTY_SERVICE. This indicates the
     * registration service did not successfully communicate the registration to FL.
     * 
     * This handler forces the esb call to complete before the onAfterValidation
     * event using setTargetSlice.
     */
    @Override
    public void onBeforeValidation(NdsFormModel model, ControllerState<?> controllerState) {
        
        logger.trace("onBeforeValidation Entry");

        String userName;
        final BeisAllModel beisModel = (BeisAllModel) model;        
        final UiData uiData = (UiData)beisModel.getUiData();

        if (uiData != null && uiData.getRegistrationStatus() == null) {
            
            final String serviceEndPoint = ConfigurationFactory.getConfiguration().getString(
                    "esbEndpoint.viewDashboard");
            final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                    GetPRSAccountExemptionsNdsResponse.class);

            logger.trace("ESB endpoint from configuration is: " + serviceEndPoint);
            GetPRSAccountExemptionsNdsRequest request = new GetPRSAccountExemptionsNdsRequest();

            // Ensure we have registered details so we can set the partially registered property
            if (beisModel.getBeisRegistrationDetails() == null) {
                beisModel.setBeisRegistrationDetails(new BeisRegistrationDetails());
            }

            // get logged in user's details
            final PlatformUser user = PlatformUser.getLoggedInUser();
            userName = user.getUsername();
            request.setUsername(userName);
            request.setTenant(user.getTenant());

            // Force this request to the REST service to complete before validation.
            uiSvcClient.setTargetSlice("before-validation");

            if (!controllerState.isSyncController()) {
                uiSvcClient.post(serviceEndPoint, request);
            } else {
                uiSvcClient.postSync(serviceEndPoint, request);
            }

            uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

                @Override
                public boolean consume(NdsResponse response) {

                    // set the details in the model
                    GetPRSAccountExemptionsNdsResponse dashboardResponse = (GetPRSAccountExemptionsNdsResponse) uiSvcClient.getResponse();

                    if (dashboardResponse != null && dashboardResponse.isSuccess()) {
                        beisModel.setDashboardDetails(dashboardResponse.getDashboardDetails());
                        // Set an indicator on the model.
                        uiData.setRegistrationStatus(RegistrationStatusType.FOUND_REGISTERED);
                        uiData.setUserType(dashboardResponse.getUserType());
                        return true;
                    }
                    
                    // Check for partially registered exception
                    if (dashboardResponse.getNdsMessages() != null) {

                        NdsMessages error = dashboardResponse.getNdsMessages();

                        if(error.getViolations() != null){
                            if(error.getViolations().get(0) != null){
                                if (error.getViolations().get(0).getMessage() != null) {                                    
                                    if (error.getViolations().get(0).getMessage().contains(
                                            "The user is only partially registered and does not have a valid account id")) {        
                                        // Set an indicator on the model.
                                        uiData.setRegistrationStatus(RegistrationStatusType.FOUND_PARTIALLY_REGISTERED);
                                        return true;        
                                    }
                                }
                            }
                        }
                    }                    
                    return false;
                }
            });
            controllerState.registerUiServiceClient(uiSvcClient);
        }        
        logger.trace("onBeforeValidation Exit");
    }

    /**
     * If the viewDashboard esb call in the onBeforeValidation event indicates the user 
     * is only partially registered then retrieve the partially registered user details.
     * This handler forces the esb call to complete before the onAfterNavigationUpdate
     * event using setTargetSlice.
     */
    @Override
    public void onAfterValidation(NdsFormModel model, ControllerState<?> controllerState) {
        logger.trace("onAfterValidation Entry");

        // cast the generic model to the beis model
        BeisAllModel beisModel = (BeisAllModel) model;
        final UiData uiData = (UiData)beisModel.getUiData();

        if (uiData.getRegistrationStatus() != null 
                && uiData.getRegistrationStatus() == RegistrationStatusType.FOUND_PARTIALLY_REGISTERED){

            BeisRegistrationDetails registrationDetails = beisModel.getBeisRegistrationDetails();

            final String serviceEndPoint = ConfigurationFactory.getConfiguration().getString(
                    "esbEndpoint.getPartiallyRegisteredDetails");

            final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                    GetPartiallyRegisteredDetailsNdsResponse.class);

            logger.trace("ESB endpoint from configuration is: " + serviceEndPoint);

            // Build a request to get the partially registered details
            GetPartiallyRegisteredDetailsNdsRequest request = new GetPartiallyRegisteredDetailsNdsRequest();

            // get logged in user's details
            final PlatformUser user = PlatformUser.getLoggedInUser();
            String userName = user.getUsername();
            request.setUsername(userName);
            request.setTenant(user.getTenant());

            // Force this request to the REST service to complete before navigation update.
            uiSvcClient.setTargetSlice("before-navigation-update");

            if (!controllerState.isSyncController()) {
                uiSvcClient.post(serviceEndPoint, request);
            } else {
                uiSvcClient.postSync(serviceEndPoint, request);
            }

            uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

                @Override
                public boolean consume(NdsResponse response) {

                    // set the details in the model
                    GetPartiallyRegisteredDetailsNdsResponse partiallyRegisteredResponse = (GetPartiallyRegisteredDetailsNdsResponse) uiSvcClient.getResponse();

                    if (partiallyRegisteredResponse != null && partiallyRegisteredResponse.isSuccess()
                            && partiallyRegisteredResponse.getPartiallyRegisteredDetails() != null) {

                        fillModelWithRegisteredData(partiallyRegisteredResponse, beisModel);
                        return true;

                    } else {

                        return false;

                    }
                }

                private void fillModelWithRegisteredData(
                        GetPartiallyRegisteredDetailsNdsResponse partiallyRegisteredResponse, BeisAllModel beisModel) {

                    // Copy the model items
                    UpdateBeisRegistrationDetails registeredDetails = partiallyRegisteredResponse.getPartiallyRegisteredDetails();

                    registrationDetails.setAccountDetails(registeredDetails.getAccountDetails());

                    BeisUpdateUserDetails updateUserDetails = registeredDetails.getUpdateUserDetails();

                    if (updateUserDetails != null) {

                        BeisUserDetails userDetails = new BeisUserDetails();
                        userDetails.setEmail(updateUserDetails.getEmail());
                        userDetails.setConfirmEmail(updateUserDetails.getEmail());
                        userDetails.setUsername(updateUserDetails.getUsername());
                        if(updateUserDetails.getUserType() !=null){                           
                             userDetails.setUserType(updateUserDetails.getUserType());                           
                        }
                        registrationDetails.setUserDetails(userDetails);
                    }

                    beisModel.setBeisRegistrationDetails(registrationDetails);

                    if (registeredDetails.getContactAddress().getPostcode() != null
                            && registeredDetails.getContactAddress().getPostcode() != "") {

                        final NdsUiData uiData = (NdsUiData) beisModel.getUiData();

                        // Set the postcode in the UI data
                        Map<String, AddressField> addressFields = new HashMap<String, AddressField>();
                        AddressField addressField = new AddressField();
                        addressField.setPostcodeCriterion(registeredDetails.getContactAddress().getPostcode());
                        String key = "beisRegistrationDetails.contactAddress";
                        addressFields.put(key, addressField);
                        uiData.setAddressFields(addressFields);

                    }
                }

            });

            controllerState.registerUiServiceClient(uiSvcClient);

        }

        logger.trace("onAfterValidation Exit");

    }

    /**
     * If we have detected a partial registration then go to the start of registration
     */
    @Override
    public void onAfterNavigationUpdate(NdsFormModel model, ControllerState<?> controllerState) {

        logger.trace("onAfterNavigationUpdate Entry");

        final BeisAllModel beisModel = (BeisAllModel) model;
        final UiData uiData = (UiData)beisModel.getUiData();

        //Only need to do this if we are partially registered.
        if (uiData.getRegistrationStatus() != null 
                && uiData.getRegistrationStatus() == RegistrationStatusType.FOUND_PARTIALLY_REGISTERED) {

            logger.trace("onAfterNavigationUpdate partial registration detected");
            controllerState.getPageFlowEngine().updateNavigationalState((AbstractNdsMvcModel) model, "select-landlord-or-agent");
            
            /*
             * Since we're changing the page to show on a GET request (redirect from the login system), 
             * the refresh page preservation Javascript code may activate to replace the viewstate.
             * We don't want that, as it makes the flow controller show the site's first page again, rather than
             * select-landlord-or-agent.  Setting this to true skips the check altogether.
             */
            controllerState.setSkipRefreshPagePreservation(true);
        }

        logger.trace("onAfterNavigationUpdate Exit");
    }

    /**
     * If the user logged in is fully registered then get the details.
     */
    @Override
    public void onAfterResolvedView(NdsFormModel allModel, ControllerState<?> controllerState) {
        String userName;
        final BeisAllModel model = (BeisAllModel) allModel;

        final UiData uiData = (UiData)model.getUiData();

        if (uiData.getRegistrationStatus() != null 
                && uiData.getRegistrationStatus() == RegistrationStatusType.USE_REGISTERED){

            final String serviceEndPoint = ConfigurationFactory.getConfiguration().getString(
                    "esbEndpoint.viewDashboard");
            final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(
                    GetPRSAccountExemptionsNdsResponse.class);

            logger.trace("ESB endpoint from configuration is: " + serviceEndPoint);
            GetPRSAccountExemptionsNdsRequest request = new GetPRSAccountExemptionsNdsRequest();

            // get logged in user's details
            final PlatformUser user = PlatformUser.getLoggedInUser();
            userName = user.getUsername();
            request.setUsername(userName);
            request.setTenant(user.getTenant());

            if (!controllerState.isSyncController()) {
                uiSvcClient.post(serviceEndPoint, request);
            } else {
                uiSvcClient.postSync(serviceEndPoint, request);
            }

            uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

                @Override
                public boolean consume(NdsResponse response) {

                    // set the details in the model
                    GetPRSAccountExemptionsNdsResponse dashboardResponse = (GetPRSAccountExemptionsNdsResponse) uiSvcClient.getResponse();

                    if (dashboardResponse != null && dashboardResponse.isSuccess()) {
                        model.setDashboardDetails(dashboardResponse.getDashboardDetails());
                        return true;
                    }

                    return false;
                }
            });
            controllerState.registerUiServiceClient(uiSvcClient);
        }else if(uiData.getRegistrationStatus() != null ){
            uiData.setRegistrationStatus(RegistrationStatusType.USE_REGISTERED);
        }
    }
}
