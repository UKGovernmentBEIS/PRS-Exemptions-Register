package com.northgateps.nds.beis.ui.controller.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.northgateps.nds.beis.api.AccountType;
import com.northgateps.nds.beis.api.AgentNameDetails;
import com.northgateps.nds.beis.api.BeisAccountDetails;
import com.northgateps.nds.beis.api.BeisAddressDetail;
import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.BeisUserDetails;
import com.northgateps.nds.beis.api.OrganisationNameDetail;
import com.northgateps.nds.beis.api.PersonNameDetail;
import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsRequest;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsResponse;
import com.northgateps.nds.beis.ui.model.BeisAllModel;
import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.model.utilities.ModelField;
import com.northgateps.nds.platform.api.serviceclient.NdsResponseConsumer;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.application.api.utils.UiUtilities;
import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.AddressField;
import com.northgateps.nds.platform.ui.model.NdsFormModel;
import com.northgateps.nds.platform.ui.security.PlatformUser;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;
import com.northgateps.nds.platform.ui.view.ViewMessage;

/**
 * Retrieve user account details for display on account page
 *
 */
public final class MyAccountDetailsHandler extends AbstractViewEventHandler {

    private static final String retrieveRegisteredUserDetailsServiceEndPoint = ConfigurationFactory.getConfiguration().getString(
            "esbEndpoint.retrieveRegisteredDetails");
    
    @Override
    public void onAfterResolvedView(NdsFormModel allModel, final ControllerState<?> controllerState) {
        
        final PlatformUser user = PlatformUser.getLoggedInUser();

        final BeisAllModel model = (BeisAllModel) allModel;
       
        if (user != null) {

            if (model.getRegisteredUserDetails() == null) {
                model.populateDefaults();

                RetrieveRegisteredDetailsNdsRequest retrieveRegisteredUserDetailsNdsRequest = new RetrieveRegisteredDetailsNdsRequest();
                retrieveRegisteredUserDetailsNdsRequest.setUsername(user.getUsername());
                retrieveRegisteredUserDetailsNdsRequest.setTenant(user.getTenant());

                final UiServiceClient uiSvcClient = controllerState.getController().getUiSvcClientFactory().getInstance(RetrieveRegisteredDetailsNdsResponse.class);
                
                if (!controllerState.isSyncController()) {
                    uiSvcClient.post(retrieveRegisteredUserDetailsServiceEndPoint, retrieveRegisteredUserDetailsNdsRequest);
                } else {
                    uiSvcClient.postSync(retrieveRegisteredUserDetailsServiceEndPoint, retrieveRegisteredUserDetailsNdsRequest);
                }

                uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

                    @Override
                    public boolean consume(NdsResponse response) {

                        if (!(response instanceof RetrieveRegisteredDetailsNdsResponse)) {
                            model.setPostSubmitMessage(null); // defer message
                                                              // construction
                                                              // back to
                                                              // controller
                            return false;
                        }
                        
                        RetrieveRegisteredDetailsNdsResponse retrieveRegisteredUserDetailsNdsResponse = ((RetrieveRegisteredDetailsNdsResponse) (response));

                        if (retrieveRegisteredUserDetailsNdsResponse.isSuccess()) {
                            fillModelWithData(retrieveRegisteredUserDetailsNdsResponse, model);
                            return retrieveRegisteredUserDetailsNdsResponse.isSuccess();
                        } else if (retrieveRegisteredUserDetailsNdsResponse.getNdsMessages() != null) {
                            List<ValidationViolation> violations = retrieveRegisteredUserDetailsNdsResponse.getNdsMessages().getViolations();

                            if (violations != null) {
                                model.setPostSubmitMessage(ViewMessage.fromViolations(violations));
                            } else {
                                model.setPostSubmitMessage(new ViewMessage(retrieveRegisteredUserDetailsNdsResponse.getNdsMessages()));
                            }
                            
                            return false;
                        } else {
                            model.setPostSubmitMessage(new ViewMessage("Error_No_more_information"));
                            return false;
                        }
                    }

                    private void fillModelWithData(RetrieveRegisteredDetailsNdsResponse retrieveRegisteredUserDetailsNdsResponse, BeisAllModel model) {

                        BeisRegistrationDetails registeredUserDetails = new BeisRegistrationDetails();
                        registeredUserDetails.setAccountDetails(new BeisAccountDetails());
                        registeredUserDetails.setUserDetails(new BeisUserDetails());
                        
                        if (retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getUserDetails().getUserType().toString().equals("AGENT")){
                            registeredUserDetails.getAccountDetails().setAgentNameDetails(new AgentNameDetails());
                            registeredUserDetails.getAccountDetails().getAgentNameDetails().setAgentName(
                            retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getAccountDetails().getAgentNameDetails().getAgentName());
                            registeredUserDetails.getUserDetails().setUserType(UserType.AGENT);                            
                        }
                    
                        // This null test need to fill person detail or organisation detail
                        else if (retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getAccountDetails().getOrganisationNameDetail() != null) {
                            registeredUserDetails.getUserDetails().setUserType(UserType.LANDLORD);
                            registeredUserDetails.getAccountDetails().setOrganisationNameDetail(new OrganisationNameDetail());
                            registeredUserDetails.getAccountDetails().getOrganisationNameDetail().setOrgName(
                            retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getAccountDetails().getOrganisationNameDetail().getOrgName());
                            registeredUserDetails.getAccountDetails().setAccountType(AccountType.ORGANISATION);
                        } else {
                            registeredUserDetails.getUserDetails().setUserType(UserType.LANDLORD);
                            registeredUserDetails.getAccountDetails().setPersonNameDetail(new PersonNameDetail());

                            registeredUserDetails.getAccountDetails().getPersonNameDetail().setFirstname(
                                    retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getAccountDetails().getPersonNameDetail().getFirstname());

                            registeredUserDetails.getAccountDetails().getPersonNameDetail().setSurname(
                                    retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getAccountDetails().getPersonNameDetail().getSurname());
                            registeredUserDetails.getAccountDetails().setAccountType(AccountType.PERSON);

                        }
                        registeredUserDetails.getAccountDetails().setTelNumber(
                                retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getAccountDetails().getTelNumber());
                        registeredUserDetails.getUserDetails().setEmail(
                                retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getUserDetails().getEmail());
                        registeredUserDetails.getUserDetails().setUsername(
                                retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getUserDetails().getUsername());

                        registeredUserDetails.getUserDetails().setUserType(retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getUserDetails().getUserType());


                        BeisAddressDetail addressDetails = new BeisAddressDetail();
                        addressDetails.setLine(new ArrayList<String>());

                        // Check if the array is empty
                        if (!retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getContactAddress().getLine().isEmpty()) {

                            addressDetails.getLine().add(
                                    retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getContactAddress().getLine().get(
                                            0));

                            // Check there is a second line in the array before accessing
                            if (retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getContactAddress().getLine().size() > 1) {
                                addressDetails.getLine().add(
                                        retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getContactAddress().getLine().get(
                                                1));
                            }
                        }

                        addressDetails.setTown(
                                retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getContactAddress().getTown());
                        addressDetails.setCounty(
                                retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getContactAddress().getCounty());
                        addressDetails.setPostcode(
                                retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getContactAddress().getPostcode());
                        addressDetails.setUprn(
                                retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getContactAddress().getUprn());
                        addressDetails.setCountry(
                                retrieveRegisteredUserDetailsNdsResponse.getBeisRegistrationDetails().getContactAddress().getCountry());
                        registeredUserDetails.setContactAddress(addressDetails);

                        model.setBeisRegistrationDetails(registeredUserDetails);

                        if (StringUtils.isNotBlank(
                                model.getBeisRegistrationDetails().getContactAddress().getLine().get(0))) {

                            final UiData uiData = (UiData) model.getUiData();

                            Map<String, AddressField> addressFields = new HashMap<String, AddressField>();
                            AddressField addressField = new AddressField();
                            String key = "registeredUserDetails.addressDetails";
                            addressFields.put(key, addressField);
                            uiData.setAddressFields(addressFields);

                            // Expand the address to show the address fields
                            String addressPath = UiUtilities.pathElEncode("registeredUserDetails.addressDetails");
                            String showManualAddressDetailsPath = "uiData.addressFields['" + addressPath
                                    + "'].showManualAddressDetails";
                            ModelField showManualAddressDetails = new ModelField().setPath(
                                    showManualAddressDetailsPath);

                            showManualAddressDetails.setValue(model, "true");
                        }

                    }
                });
                controllerState.registerUiServiceClient(uiSvcClient);
            }
        }

    }

}
