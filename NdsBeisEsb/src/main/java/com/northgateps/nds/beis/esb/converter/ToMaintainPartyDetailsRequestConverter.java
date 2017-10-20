package com.northgateps.nds.beis.esb.converter;

import java.math.BigInteger;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.BeisAccountDetails;
import com.northgateps.nds.beis.api.BeisAddressDetail;
import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.BeisUpdateUserDetails;
import com.northgateps.nds.beis.api.BeisUserDetails;
import com.northgateps.nds.beis.api.UpdateBeisRegistrationDetails;
import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsRequest;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsRequest;
import com.northgateps.nds.beis.backoffice.service.common.AddressType;
import com.northgateps.nds.beis.backoffice.service.maintainpartydetails.MaintainPartyDetailsRequest;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * converter for BEIS foundation layer maintain party details which can convert from different request types.
 */
@Converter
@DoNotWeaveLoggingSystem
public final class ToMaintainPartyDetailsRequestConverter {

    private static final NdsLogger logger = NdsLogger.getLogger(ToMaintainPartyDetailsRequestConverter.class);

    /**
     * 
     * @param ndsAccountDetails - object containing account details to be converted
     * @param ndsContactAddressDetail - object containing address details to be converted
     * @param ndsUserDetails - object containing user details to be converted
     * @return the provided details converted to a MaintainPartyDetailsRequest object ready to send to the external
     *         service.
     */
    private static MaintainPartyDetailsRequest convertUserAccountAndAddressToAMaintainPartyDetailsRequest(
            BeisAccountDetails ndsAccountDetails, BeisAddressDetail ndsContactAddressDetail, String userName,
            String userType, String email) {

        MaintainPartyDetailsRequest backOfficeMaintainPartyDetailsRequest = new MaintainPartyDetailsRequest();
        // Test for account id to determine update or create look for exchange property
        if (ndsAccountDetails == null || ndsAccountDetails.getAccountId() == null
                || ndsAccountDetails.getAccountId().isEmpty()) {
            // Set for create
            logger.info("This is a create MaintainPartyDetails request");
            MaintainPartyDetailsRequest.Create createNode = new MaintainPartyDetailsRequest.Create();
            createNode.setUserName(userName);
            backOfficeMaintainPartyDetailsRequest.setCreate(createNode);
        } else {
            // Set for update
            logger.info("This is an update MaintainPartyDetails request");
            MaintainPartyDetailsRequest.Update updateNode = new MaintainPartyDetailsRequest.Update();
            updateNode.setPartyRef( new BigInteger(ndsAccountDetails.getAccountId()));
            backOfficeMaintainPartyDetailsRequest.setUpdate(updateNode);
        }
        
        if (ndsAccountDetails != null) {

            backOfficeMaintainPartyDetailsRequest.setLanguageCode(ndsAccountDetails.getLanguageCode());
            //set agent name
            if(userType !=null && userType.equals("Agent") && ndsAccountDetails.getAgentNameDetails() !=null){
                logger.info("This is an agent request");
                backOfficeMaintainPartyDetailsRequest.setOrganisationName(
                        ndsAccountDetails.getAgentNameDetails().getAgentName());
                }
     

            // Set the organisation name or person name
            if (ndsAccountDetails.getOrganisationNameDetail() != null) {
                logger.info("This is an organisation request");
                backOfficeMaintainPartyDetailsRequest.setOrganisationName(
                        ndsAccountDetails.getOrganisationNameDetail().getOrgName());
                
            }else if (ndsAccountDetails.getPersonNameDetail() != null) {
                // This need to be check since with update contact address details we are not setting person or organisation
                // object
                logger.info("This is a person request");
                backOfficeMaintainPartyDetailsRequest.setFirstName(
                        ndsAccountDetails.getPersonNameDetail().getFirstname());
                backOfficeMaintainPartyDetailsRequest.setLastName(ndsAccountDetails.getPersonNameDetail().getSurname());
            }

            backOfficeMaintainPartyDetailsRequest.setPhoneNumber(ndsAccountDetails.getTelNumber());

        }

        // Set the address details
        if (ndsContactAddressDetail != null) {

            AddressType addressNode = new AddressType();

            // We will have one or two lines of address
            addressNode.setLine1(ndsContactAddressDetail.getLine().get(0));

            if (ndsContactAddressDetail.getLine().size() > 1) {
                addressNode.setLine2(ndsContactAddressDetail.getLine().get(1));
            }

            addressNode.setTown(ndsContactAddressDetail.getTown());
            addressNode.setCounty(ndsContactAddressDetail.getCounty());
            addressNode.setCountry(ndsContactAddressDetail.getCountry());
            addressNode.setPostcode(ndsContactAddressDetail.getPostcode());
            backOfficeMaintainPartyDetailsRequest.setAddress(addressNode);
            backOfficeMaintainPartyDetailsRequest.setAddressMoniker(ndsContactAddressDetail.getQasMoniker());

        }
        
        backOfficeMaintainPartyDetailsRequest.setUserType(userType);
      
        // Email is mandatory on back office
        backOfficeMaintainPartyDetailsRequest.setEmailAddress(email);

        return backOfficeMaintainPartyDetailsRequest;
    }

    /**
     * @param registrationRequest - This is the Nds request that needs transforming for BEIS foundation layer
     *            to understand.
     * @return - the provided details converted to a MaintainPartyDetailsRequest object ready to send to the external
     *         service.
     */
    @Converter
    public static MaintainPartyDetailsRequest converting(BeisRegistrationNdsRequest registrationRequest) {

        logger.info("Starting Conversion For Registration Request");

        BeisUserDetails ndsUserDetails = null;
        BeisAccountDetails ndsAccountDetails = null;
        BeisAddressDetail ndsContactAddressDetail = null;
        String userType = null;
        String email = null;
        String userName = null;

        // Get local references to the objects in the request that we need to use.
        BeisRegistrationDetails ndsRegistrationDetails = registrationRequest.getRegistrationDetails();
        ndsUserDetails = ndsRegistrationDetails.getUserDetails();    
        if (ndsUserDetails != null) {
            //back-office require user type in camel case
           if(ndsUserDetails.getUserType()==UserType.LANDLORD){
                userType="Landlord";
            }
            else{
                userType="Agent";
            }
            email = ndsUserDetails.getEmail();
            userName = ndsUserDetails.getUsername();
        }

        ndsAccountDetails = ndsRegistrationDetails.getAccountDetails();
        ndsContactAddressDetail = ndsRegistrationDetails.getContactAddress();

        MaintainPartyDetailsRequest backOfficeMaintainPartyDetailsRequest = convertUserAccountAndAddressToAMaintainPartyDetailsRequest(
                ndsAccountDetails, ndsContactAddressDetail, userName, userType, email);

        logger.info("Finished Conversion For Registration Request");

        return backOfficeMaintainPartyDetailsRequest;

    }

    /**
     * 
     * @param ndsSaveRegisteredRequest this represents an update to a registered user
     * @return the provided details converted to a MaintainPartyDetailsRequest object ready to send to the external
     *         service.
     */
    @Converter
    public static MaintainPartyDetailsRequest converting(
            SaveRegisteredAccountDetailsNdsRequest ndsSaveRegisteredRequest) {

        logger.info("Starting Conversion For Save Registered Details Request");

        BeisAccountDetails ndsAccountDetails = null;
        BeisAddressDetail ndsContactAddressDetail = null;
        BeisUpdateUserDetails ndsUpdateUserDetails = null;
        String userType = null;
        String email = null;
        String userName = null;

        // Get local references to the objects in the request that we need to use.
        UpdateBeisRegistrationDetails ndsRegistrationDetails = ndsSaveRegisteredRequest.getRegisteredAccountDetails();

        ndsAccountDetails = ndsRegistrationDetails.getAccountDetails();
        ndsContactAddressDetail = ndsRegistrationDetails.getContactAddress();

        ndsUpdateUserDetails = ndsRegistrationDetails.getUpdateUserDetails();

        if (ndsUpdateUserDetails != null) {
            //back-office require user type in camel case
            if(ndsUpdateUserDetails.getUserType()==UserType.LANDLORD){
                 userType="Landlord";
             }
             else{
                 userType="Agent";
             }      
            email = ndsUpdateUserDetails.getEmail();
            userName = ndsUpdateUserDetails.getUsername();
        }

        // Convert what we can. Currently the UpdateBeisRegistrationDetails does not have a ndsUserDetails property.
        MaintainPartyDetailsRequest backOfficeMaintainPartyDetailsRequest = convertUserAccountAndAddressToAMaintainPartyDetailsRequest(
                ndsAccountDetails, ndsContactAddressDetail, userName, userType, email);

        logger.info("Finished Conversion For Save Registered Details Request");

        return backOfficeMaintainPartyDetailsRequest;
    }

}
