package com.northgateps.nds.beis.esb.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.AgentNameDetails;
import com.northgateps.nds.beis.api.BeisAccountDetails;
import com.northgateps.nds.beis.api.BeisAddressDetail;
import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.BeisUserDetails;
import com.northgateps.nds.beis.api.OrganisationNameDetail;
import com.northgateps.nds.beis.api.PersonNameDetail;
import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsResponse;
import com.northgateps.nds.beis.backoffice.service.common.AddressType;
import com.northgateps.nds.beis.backoffice.service.core.MessageStructure;
import com.northgateps.nds.beis.backoffice.service.core.MessagesStructure;
import com.northgateps.nds.beis.backoffice.service.getpartydetails.GetPartyDetailsResponse;
import com.northgateps.nds.platform.api.NdsMessages;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Converter class to convert an external back office object to a standard NDS object.
 * From GetPartyDetailsResponse to RetrieveRegisteredDetailsNdsResponse.
 */
@Converter
@DoNotWeaveLoggingSystem
public final class ToGetPartyDetailsResponseConverter {

    private static final NdsLogger logger = NdsLogger.getLogger(ToGetPartyDetailsResponseConverter.class);

    /**
     * Convert the passed in GetPartyDetailsResponse to the passed out RetrieveRegisteredDetailsNdsResponse
     * 
     * @param getPartyDetailsResponse response to be converted to RetrieveRegisteredDetailsNdsResponse
     * @return class com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsResponse
     */
    @Converter
    @SuppressWarnings("unused")
    public static RetrieveRegisteredDetailsNdsResponse converting(GetPartyDetailsResponse getPartyDetailsResponse) {

        logger.info("Starting Conversion For GetPartyDetailsResponse");

        /* Class being converted to */
        RetrieveRegisteredDetailsNdsResponse retrieveRegisteredDetailsNdsResponse = new RetrieveRegisteredDetailsNdsResponse();

        BeisRegistrationDetails beisRegistrationDetails = new BeisRegistrationDetails();

        BeisAccountDetails accountDetails = new BeisAccountDetails();
        BeisUserDetails beisUserDetails = new BeisUserDetails();
        BeisAddressDetail contactAddress = new BeisAddressDetail();
        AddressType address = null;
        List<String> line = new ArrayList<String>();

        if (getPartyDetailsResponse != null) {
            if (getPartyDetailsResponse.isSuccess()) {
                retrieveRegisteredDetailsNdsResponse.setSuccess(true);

                if (getPartyDetailsResponse.getUserType() != null) {
                    if (getPartyDetailsResponse.getUserType().equals("Agent")) {
                        beisUserDetails.setUserType(UserType.AGENT);
                        accountDetails.setAgentNameDetails(new AgentNameDetails());
                        accountDetails.getAgentNameDetails().setAgentName(
                                getPartyDetailsResponse.getOrganisationName());
                    } else {
                        beisUserDetails.setUserType(UserType.LANDLORD);
                        if (getPartyDetailsResponse.getFirstName() != null
                                && getPartyDetailsResponse.getLastName() != null) {
                            accountDetails.setPersonNameDetail(new PersonNameDetail());
                            accountDetails.getPersonNameDetail().setFirstname(getPartyDetailsResponse.getFirstName());
                            accountDetails.getPersonNameDetail().setSurname(getPartyDetailsResponse.getLastName());
                        } else {
                            accountDetails.setOrganisationNameDetail(new OrganisationNameDetail());
                            accountDetails.getOrganisationNameDetail().setOrgName(
                                    getPartyDetailsResponse.getOrganisationName());
                        }
                    }

                }

                accountDetails.setTelNumber(getPartyDetailsResponse.getPhoneNumber());
                beisRegistrationDetails.setAccountDetails(accountDetails);
                beisUserDetails.setEmail(getPartyDetailsResponse.getEmailAddress());
                beisRegistrationDetails.setUserDetails(beisUserDetails);

                if (getPartyDetailsResponse.getAddress() != null) {
                    line.add(getPartyDetailsResponse.getAddress().getLine1());
                    line.add(getPartyDetailsResponse.getAddress().getLine2());
                    contactAddress.setLine(line);
                    contactAddress.setTown(getPartyDetailsResponse.getAddress().getTown());
                    contactAddress.setCounty(getPartyDetailsResponse.getAddress().getCounty());
                    contactAddress.setPostcode(getPartyDetailsResponse.getAddress().getPostcode());
                    contactAddress.setCountry(getPartyDetailsResponse.getAddress().getCountry());
                    beisRegistrationDetails.setContactAddress(contactAddress);
                }

                retrieveRegisteredDetailsNdsResponse.setBeisRegistrationDetails(beisRegistrationDetails);
            } else {
                Boolean success = null;
                MessagesStructure messagesStructure = getPartyDetailsResponse.getMessages();
                if (messagesStructure != null) {
                    List<MessageStructure> messages = messagesStructure.getMessage();
                    if (messages != null) {

                        for (MessageStructure messageStructure : messages) {

                            // genuine error
                            NdsMessages ndsMessages = new NdsMessages();
                            ndsMessages.setExceptionCaught(
                                    (messageStructure.getText() != null) ? messageStructure.getText() : "");
                            ndsMessages.setErrorNumber(
                                    (messageStructure.getCode() != null) ? messageStructure.getCode() : "");
                            ndsMessages.setErrorType((messageStructure.getSeverity() != null)
                                    ? messageStructure.getSeverity().toString() : "");
                            retrieveRegisteredDetailsNdsResponse.setNdsMessages(ndsMessages);

                            success = false;

                        }
                    }
                }
                retrieveRegisteredDetailsNdsResponse.setSuccess(success);
            }
        }
        logger.info("Finished Conversion For GetPartyDetailsResponse");
        return retrieveRegisteredDetailsNdsResponse;
    }
}
