package com.northgateps.nds.beis.esb.beisregistration;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import com.northgateps.nds.beis.api.AccountType;
import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsRequest;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsResponse;
import com.northgateps.nds.platform.api.NdsEvent;
import com.northgateps.nds.platform.application.api.utils.PlatformTokenFactory;
import com.northgateps.nds.platform.esb.adapter.NdsDirectoryUsernameProcessingComponent;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.adapter.persistence.AbstractPersistenceAdapter;
import com.northgateps.nds.platform.esb.directory.DirectoryAccessConnection;
import com.northgateps.nds.platform.esb.directory.DirectoryConnectionConfig;
import com.northgateps.nds.platform.esb.directory.DirectoryManager;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryException;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryPasswordConstraintViolationException;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryUnwillingToPerformOperationException;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.esb.exception.NdsDirectoryException;
import com.northgateps.nds.platform.esb.persistence.PersistenceManager;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Adapter that services a user registration NdsRequest by interaction with LDAP and MongoDb.
 */
public class BeisRegistrationLdapComponent
        extends NdsDirectoryUsernameProcessingComponent<BeisRegistrationNdsRequest, BeisRegistrationNdsResponse> {

    @SuppressWarnings("unused")
    private static final NdsLogger logger = NdsLogger.getLogger(BeisRegistrationLdapComponent.class);
 
    private PersistenceManager persistenceManager = null; 

    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }
    
    /**
     * Create a new LDAP user from a template copy and update it with the request data.
     */
    @Override
    protected void doProcess(BeisRegistrationNdsRequest request, DirectoryAccessConnection directoryConnection,
            NdsSoapRequestAdapterExchangeProxy ndsExchange, DirectoryConnectionConfig directoryConnectionConfig)
                    throws NdsApplicationException, NdsBusinessException {

        BeisRegistrationDetails registrationDetails = request.getRegistrationDetails(); 

        String activationCode = request.getRegistrationDetails().getActivationCode();
        if (activationCode == null) {
            ZonedDateTime expiry = ZonedDateTime.now(ZoneOffset.UTC).plusHours(
                    Integer.parseInt(configurationManager.getString("app.registrationExpiryHours")));
            activationCode = PlatformTokenFactory.generateActivationToken(
                    request.getRegistrationDetails().getUserDetails().getUsername(), expiry);
        }
        request.getRegistrationDetails().setActivationCode(activationCode);

        String ldapAttributeUserId = configurationManager.getString("attribute.userid");
        String ldapAttributePassword = configurationManager.getString("attribute.password");
        String ldapAttributeEmail = configurationManager.getString("attribute.email");
        String ldapAttributeActivationCode = configurationManager.getString("attribute.activationCode");       
        String ldapAttributeFirstname = configurationManager.getString("attribute.forename");     
        String ldapAttributeSurname = configurationManager.getString("attribute.surname");
        String ldapAttributeStreetLineOne = configurationManager.getString("attribute.street");
        String ldapAttributeStreetLineTwo = configurationManager.getString("attribute.street2");
        String ldapAttributeTown = configurationManager.getString("attribute.town");
        String ldapAttributeCounty = configurationManager.getString("attribute.county");
        String ldapAttributeCountry = configurationManager.getString("attribute.country");
        String ldapAttributePostcode = configurationManager.getString("attribute.postalCode");
        String ldapAttributeOrganisation = configurationManager.getString("attribute.organisationName");
        String ldapAttributeHomePhone = configurationManager.getString("attribute.homePhone");
        

        // Any attributes to be changed on the new user after copying the template user
        HashMap<String, String> changes = new HashMap<String, String>();
        changes.put(ldapAttributeUserId, registrationDetails.getUserDetails().getUsername());
        changes.put(ldapAttributePassword, registrationDetails.getUserDetails().getPassword());
        changes.put(ldapAttributeEmail, registrationDetails.getUserDetails().getEmail());
        changes.put(ldapAttributeHomePhone, registrationDetails.getAccountDetails().getTelNumber());

        changes.put(ldapAttributeActivationCode, registrationDetails.getActivationCode());
        if(registrationDetails.getUserDetails().getUserType().equals(UserType.LANDLORD)){
            if (registrationDetails.getAccountDetails().getAccountType().equals(AccountType.PERSON)) {
                changes.put(ldapAttributeFirstname,
                        registrationDetails.getAccountDetails().getPersonNameDetail().getFirstname());
                changes.put(ldapAttributeSurname,
                        registrationDetails.getAccountDetails().getPersonNameDetail().getSurname());
            } else {
                changes.put(ldapAttributeOrganisation,
                        registrationDetails.getAccountDetails().getOrganisationNameDetail().getOrgName());
            }
          }else{
              changes.put(ldapAttributeOrganisation,
                      registrationDetails.getAccountDetails().getAgentNameDetails().getAgentName());
          }

        if (registrationDetails.getContactAddress() != null) {

            changes.put(ldapAttributeStreetLineOne, registrationDetails.getContactAddress().getLine().get(0));

            if (registrationDetails.getContactAddress().getLine().size() > 1) {
                changes.put(ldapAttributeStreetLineTwo, registrationDetails.getContactAddress().getLine().get(1));
            }

            changes.put(ldapAttributeTown, registrationDetails.getContactAddress().getTown());
            changes.put(ldapAttributeCounty, registrationDetails.getContactAddress().getCounty());
            changes.put(ldapAttributePostcode, registrationDetails.getContactAddress().getPostcode());
            changes.put(ldapAttributeCountry, registrationDetails.getContactAddress().getCountry());

        }


        // get standard template and work out where in LDAP the user should be created
        String ldapTemplateUserDn = getTemplateUserDn(configurationManager, registrationDetails.getTenant(), null);
        String newUserDn = getUserDn(configurationManager, registrationDetails.getUserDetails().getUsername(),
                registrationDetails.getTenant());
        ndsExchange.setAnExchangeProperty(AbstractPersistenceAdapter.EVENT_USER_DN, newUserDn);

        try {
            getUserManager().createNewUser(directoryConnection, newUserDn, ldapTemplateUserDn, changes);
            // if any errors (eg. email) occur later in the route, then this data can be used to remove the entry from
            // directory
            ndsExchange.setAnExchangeProperty("userNameEntryCreated", registrationDetails.getUserDetails().getUsername());
            ndsExchange.setAnExchangeProperty("userTenantEntryCreated", registrationDetails.getTenant());
        } catch (DirectoryUnwillingToPerformOperationException | DirectoryPasswordConstraintViolationException e) {
            throw new NdsBusinessException(e.getMessage());
        } catch (DirectoryException e) {
            throw new NdsDirectoryException(e.getMessage(), e);
        }
    }

    @Override
    protected void doRequestUsernameProcessing(BeisRegistrationNdsRequest request, DirectoryAccessConnection directoryConnection)
            throws NdsDirectoryException, NdsBusinessException {

        // check username doesn't already exist
        BeisRegistrationDetails registrationDetails = request.getRegistrationDetails();

        try {

            String userDn = getUserDn(configurationManager, registrationDetails.getUserDetails().getUsername(),
                    registrationDetails.getTenant());

            Map<String, String> attributes = getUserManager().userAttributes(directoryConnection, userDn);

           if (attributes != null) {          
               throw new NdsBusinessException("Username already exists, please try a different one.");
           }
        } catch (DirectoryException | NdsBusinessException e) {
            if (e.getMessage().contains(DirectoryManager.INVALID_ENTRY_DN)) {
                // do nothing - we expect it not to exist
            } else {
                throw e;
            }
        }

    }

    /**
     * Create an NDS response that indicates successfully created a new LDAP user
     * 
     * @param ndsExchange
     * @throws NdsApplicationException
     */
    @Override
    protected BeisRegistrationNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {

        BeisRegistrationNdsResponse response = new BeisRegistrationNdsResponse();
        response.setSuccess(true);
        new AbstractPersistenceAdapter() {
            @Override
            public PersistenceManager getPersistenceManager() {
                return (persistenceManager != null) ? persistenceManager : super.getPersistenceManager();
            }
        }.logEvent(ndsExchange, NdsEvent.REGISTRATION);

        return response;
    }

    /**
     * Called from the base-class to de-serialize the incoming request.
     * 
     * See this:
     * http://stackoverflow.com/questions/3403909/get-generic-type-of-class
     * -at-runtime
     */
    @Override
    protected String getRequestClassName() {
        return BeisRegistrationNdsRequest.class.getName();
    }
}
