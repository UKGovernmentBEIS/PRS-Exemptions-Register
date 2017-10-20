package com.northgateps.nds.beis.esb.registration;

import java.util.HashMap;

import com.northgateps.nds.beis.api.AccountType;
import com.northgateps.nds.beis.api.UpdateBeisRegistrationDetails;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsRequest;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsDirectoryAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.directory.DirectoryConnection;
import com.northgateps.nds.platform.esb.directory.DirectoryConnectionConfig;
import com.northgateps.nds.platform.esb.directory.UserServices;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryException;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryMissingAttributeValueException;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryUnwillingToPerformOperationException;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.esb.exception.NdsDirectoryException;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * 
 * @author DavidL
 *         Adapter class that processes the saving of registered account details between native (REST) objects and LDAP
 *         objects
 *         This would be nice if it could be derived from SaveRegisteredUserDetailsAdapter and that to be made more
 *         generic. Raised Jira task BEIS-187
 *
 */
public class SaveRegisteredAccountDetailsAdapter
        extends NdsDirectoryAdapter<SaveRegisteredAccountDetailsNdsRequest, SaveRegisteredAccountDetailsNdsResponse> {

    @SuppressWarnings("unused")
    private static final NdsLogger logger = NdsLogger.getLogger(SaveRegisteredAccountDetailsAdapter.class);

    public static final String EXCHANGE_PROPERTY_PARTIALLY_REGISTERED = "partiallyRegistered";

    final ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();

    @Override
    protected void doRequestProcess(SaveRegisteredAccountDetailsNdsRequest request,
            DirectoryConnection directoryConnection, NdsSoapRequestAdapterExchangeProxy ndsExchange,
            DirectoryConnectionConfig directoryConnectionConfig) throws NdsApplicationException, NdsBusinessException {

        // get the LDAP properties we will be setting
        String ldapAttributeForename = configurationManager.getString("attribute.forename");
        String ldapAttributeSurname = configurationManager.getString("attribute.surname");
        String ldapAttributeHomePhone = configurationManager.getString("attribute.homePhone");
        String ldapAttributeStreetLineOne = configurationManager.getString("attribute.street");
        String ldapAttributeStreetLineTwo = configurationManager.getString("attribute.street2");
        String ldapAttributeTown = configurationManager.getString("attribute.town");
        String ldapAttributeCounty = configurationManager.getString("attribute.county");
        String ldapAttributePostcode = configurationManager.getString("attribute.postalCode");
        String ldapAttributeCountry = configurationManager.getString("attribute.country");
        String ldapAttributeUprn = configurationManager.getString("attribute.uprn");
        String ldapAttributeOrgname = configurationManager.getString("attribute.organisationName");
        String ldapAttributeEmail = configurationManager.getString("attribute.email");

        UpdateBeisRegistrationDetails registeredAccountDetails = request.getRegisteredAccountDetails();

        try {
            HashMap<String, String> changes = new HashMap<>();

            // Any attributes to be changed on the user
            if (registeredAccountDetails.getAccountDetails() != null) {
                
                if(registeredAccountDetails.getAccountDetails().getAgentNameDetails() !=null)
                {
                    changes.put(ldapAttributeOrgname,
                            registeredAccountDetails.getAccountDetails().getAgentNameDetails().getAgentName());
                }

                else {
                    
                    if (registeredAccountDetails.getAccountDetails().getAccountType().equals(
                            AccountType.ORGANISATION)) {
                        changes.put(ldapAttributeOrgname,
                                registeredAccountDetails.getAccountDetails().getOrganisationNameDetail().getOrgName());
                    } else {
                        changes.put(ldapAttributeForename,
                                registeredAccountDetails.getAccountDetails().getPersonNameDetail().getFirstname());
                        changes.put(ldapAttributeSurname,
                                registeredAccountDetails.getAccountDetails().getPersonNameDetail().getSurname());
                    }
                }

                changes.put(ldapAttributeHomePhone, registeredAccountDetails.getAccountDetails().getTelNumber());
            }

            // if there is an address then update that
            if (registeredAccountDetails.getContactAddress() != null) {

                changes.put(ldapAttributeStreetLineOne, registeredAccountDetails.getContactAddress().getLine().get(0));

                if (registeredAccountDetails.getContactAddress().getLine().size() > 1) {
                    changes.put(ldapAttributeStreetLineTwo,
                            registeredAccountDetails.getContactAddress().getLine().get(1));
                }
                changes.put(ldapAttributeTown, registeredAccountDetails.getContactAddress().getTown());
                changes.put(ldapAttributeCounty, registeredAccountDetails.getContactAddress().getCounty());
                changes.put(ldapAttributePostcode, registeredAccountDetails.getContactAddress().getPostcode());
                changes.put(ldapAttributeCountry, registeredAccountDetails.getContactAddress().getCountry());
                changes.put(ldapAttributeUprn, registeredAccountDetails.getContactAddress().getUprn());

            }

            // if there is an email address then update it
            if (registeredAccountDetails.getUpdateUserDetails().getUpdatingEmail()) {

                changes.put(ldapAttributeEmail, registeredAccountDetails.getUpdateUserDetails().getEmail());

            }

            String userDn = getUserDn(configurationManager,
                    request.getRegisteredAccountDetails().getUpdateUserDetails().getUsername(), request.getTenant());

            UserServices.setAttributesOnEntry(directoryConnection, userDn, changes);
            
            //set an exchange property to indicate if this is a partial registration
            ndsExchange.setAnExchangeProperty(EXCHANGE_PROPERTY_PARTIALLY_REGISTERED, request.getPartiallyRegistered());

        } catch (DirectoryUnwillingToPerformOperationException e) {
            throw new NdsBusinessException(e.getMessage());
        } catch (DirectoryMissingAttributeValueException e) {
            throw new NdsDirectoryException("Unable to find attribute");
        } catch (DirectoryException e) {
            throw new NdsDirectoryException(e.getMessage(), e);
        }

    }

    /**
     * Create an NDS response that indicates successfully saving the registered account details
     * 
     * @param ndsExchange
     * @throws NdsApplicationException
     */
    @Override
    protected SaveRegisteredAccountDetailsNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {
        SaveRegisteredAccountDetailsNdsResponse response = new SaveRegisteredAccountDetailsNdsResponse();

        response.setSuccess(true);

        return response;
    }

    @Override
    protected String getRequestClassName() {
        return SaveRegisteredAccountDetailsNdsRequest.class.getName();
    }
}
