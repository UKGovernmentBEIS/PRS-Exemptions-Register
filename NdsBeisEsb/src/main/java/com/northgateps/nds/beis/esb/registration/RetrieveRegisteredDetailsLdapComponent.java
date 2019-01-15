package com.northgateps.nds.beis.esb.registration;

import java.util.ArrayList;
import java.util.Map;

import com.northgateps.nds.beis.api.BeisAccountDetails;
import com.northgateps.nds.beis.api.BeisAddressDetail;
import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.BeisUserDetails;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsRequest;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsDirectoryComponent;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.directory.DirectoryAccessConnection;
import com.northgateps.nds.platform.esb.directory.DirectoryConnectionConfig;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryException;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryMissingAttributeValueException;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.esb.exception.NdsDirectoryException;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Adapter class that processes the retrieving of registered user details between native (REST) objects and LDAP objects
 */
public class RetrieveRegisteredDetailsLdapComponent
        extends NdsDirectoryComponent<RetrieveRegisteredDetailsNdsRequest, RetrieveRegisteredDetailsNdsResponse> {

    @SuppressWarnings("unused")
    private static final NdsLogger logger = NdsLogger.getLogger(RetrieveRegisteredDetailsLdapComponent.class);

    final ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();

    /**
     * Retrieve registered user details from Ldap
     * 
     * @throws NdsBusinessException
     */
    @Override
    protected void doProcess(RetrieveRegisteredDetailsNdsRequest request, DirectoryAccessConnection directoryConnection,
            NdsSoapRequestAdapterExchangeProxy ndsExchange, DirectoryConnectionConfig directoryConnectionConfigImpl)
                    throws NdsApplicationException, NdsBusinessException, DirectoryException {

        String ldapAttributeForename = configurationManager.getString("attribute.forename");
        String ldapAttributeSurname = configurationManager.getString("attribute.surname");
        String ldapAttributeHomePhone = configurationManager.getString("attribute.homePhone");
        String ldapAttributeEmail = configurationManager.getString("attribute.email");
        String ldapAttributeStreetLineOne = configurationManager.getString("attribute.street");
        String ldapAttributeStreetLineTwo = configurationManager.getString("attribute.street2");
        String ldapAttributeTown = configurationManager.getString("attribute.town");
        String ldapAttributeCounty = configurationManager.getString("attribute.county");
        String ldapAttributePostcode = configurationManager.getString("attribute.postalCode");
        String ldapAttributeUprn = configurationManager.getString("attribute.uprn");
        String ldapAttributeOrgname = configurationManager.getString("attribute.organisationName");
        String ldapAttributeCountry = configurationManager.getString("attribute.country");
        String ldapAttributeUid = configurationManager.getString("attribute.userid");

        BeisRegistrationDetails beisRegistrationDetails = new BeisRegistrationDetails();
        beisRegistrationDetails.setAccountDetails(new BeisAccountDetails());
        beisRegistrationDetails.setUserDetails(new BeisUserDetails());

        try {
            String userDn = getUserDn(configurationManager, request.getUsername(), request.getTenant());

            Map<String, String> attributes = getUserManager().userAttributes(directoryConnection, userDn);
            beisRegistrationDetails.getAccountDetails().getOrganisationNameDetail().setOrgName(
                    attributes.get(ldapAttributeOrgname));
            beisRegistrationDetails.getAccountDetails().getPersonNameDetail().setFirstname(
                    attributes.get(ldapAttributeForename));
            beisRegistrationDetails.getAccountDetails().getPersonNameDetail().setSurname(
                    attributes.get(ldapAttributeSurname));
            beisRegistrationDetails.getAccountDetails().setTelNumber(attributes.get(ldapAttributeHomePhone));

            beisRegistrationDetails.getUserDetails().setEmail(attributes.get(ldapAttributeEmail));
            beisRegistrationDetails.getUserDetails().setUsername(attributes.get(ldapAttributeUid));
            BeisAddressDetail addressDetails = new BeisAddressDetail();
            addressDetails.setLine(new ArrayList<String>());
            addressDetails.getLine().add(attributes.get(ldapAttributeStreetLineOne));
            addressDetails.getLine().add(attributes.get(ldapAttributeStreetLineTwo));
            addressDetails.setTown(attributes.get(ldapAttributeTown));
            addressDetails.setCounty(attributes.get(ldapAttributeCounty));
            addressDetails.setPostcode(attributes.get(ldapAttributePostcode));
            addressDetails.setCountry(attributes.get(ldapAttributeCountry));
            addressDetails.setUprn(attributes.get(ldapAttributeUprn));
            beisRegistrationDetails.setContactAddress(addressDetails);

            RetrieveRegisteredDetailsNdsResponse response = new RetrieveRegisteredDetailsNdsResponse();

            response.setBeisRegistrationDetails(beisRegistrationDetails);

            ndsExchange.setResponseMessageBody(response);

        } catch (DirectoryMissingAttributeValueException e) {
            throw new NdsDirectoryException("Unable to find attribute");
        } catch (DirectoryException e) {
            throw new NdsDirectoryException(e.getMessage(), e);
        }
    }

    /**
     * Create an NDS response that indicates successfully retrieving the registered user details
     * 
     * @param exchange
     * @throws NdsApplicationException
     */

    @Override
    protected RetrieveRegisteredDetailsNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {

        RetrieveRegisteredDetailsNdsResponse response = null;

        try {

            response = (RetrieveRegisteredDetailsNdsResponse) ndsExchange.getRequestMessageBody(
                    RetrieveRegisteredDetailsNdsResponse.class);
            response.setSuccess(true);

        } catch (ClassNotFoundException e) {
            throw new NdsApplicationException(e.getMessage(), e);
        }

        return response;
    }

    /**
     * Called from the base-class to de-serialize the incoming request.
     * 
     * See this: http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
     */
    @Override
    protected String getRequestClassName() {
        return RetrieveRegisteredDetailsNdsRequest.class.getName();
    }
    
}
