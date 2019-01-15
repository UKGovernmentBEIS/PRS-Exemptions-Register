package com.northgateps.nds.beis.esb.beisregistration;

import java.util.HashMap;

import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.BeisUserDetails;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsRequest;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsDirectoryComponent;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.directory.DirectoryAccessConnection;
import com.northgateps.nds.platform.esb.directory.DirectoryConnectionConfig;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryException;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryUnwillingToPerformOperationException;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.esb.exception.NdsDirectoryException;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * This adaptor relies on a previous setting of account Id in the exchange properties. This will set the uid for
 * the foundation layer party service for the user in LDAP.
 */
public class BeisRegistrationUpdateAccountIdLdapComponent extends NdsDirectoryComponent<BeisRegistrationNdsRequest, BeisRegistrationNdsResponse> {
    
    //This is to be reused where required
    public static final String EXCHANGE_PROPERTY_ACCOUNT_ID = "accountId";  
    
    private static final NdsLogger logger = NdsLogger.getLogger(BeisRegistrationUpdateAccountIdLdapComponent.class);
    
    /**
     * Read the exchange property and if exists, set the uid on the foundation layer party service for the user. 
     * @throws NdsBusinessException 
     */
    @Override
    protected void doProcess(BeisRegistrationNdsRequest request, DirectoryAccessConnection directoryConnection,
            NdsSoapRequestAdapterExchangeProxy ndsExchange, DirectoryConnectionConfig directoryConnectionConfigImpl)
                    throws NdsApplicationException, NdsBusinessException, DirectoryException {
      //Read property for account id
        if( ndsExchange.getAnExchangeProperty(EXCHANGE_PROPERTY_ACCOUNT_ID) != null){
            
            String accountId = ndsExchange.getAnExchangeProperty(EXCHANGE_PROPERTY_ACCOUNT_ID).toString();   
            
            BeisRegistrationDetails registrationDetails = request.getRegistrationDetails();
            BeisUserDetails userDetails = registrationDetails.getUserDetails();
            
            ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();

            //Get the service name
            String serviceName = configurationManager.getString("service.foundationLayerPartyService.name");

            String servicednForTheUser = getNdsServiceByUserDn(configurationManager, userDetails.getUsername(), registrationDetails.getTenant(), serviceName);
            
            logger.info("About to set the accountId to " + accountId + "For Dn" + servicednForTheUser);
            
            //Create hash map to update the uid on the service for the user.
            String ldapAttributeServiceUid = configurationManager.getString("attribute.userid");           

            // Any attributes to be changed on the new user after copying the template user
            HashMap<String, String> changes = new HashMap<String, String>();
            changes.put(ldapAttributeServiceUid, accountId);   
            
            try{                
                getUserManager().setAttributesOnEntry(directoryConnection, servicednForTheUser, changes);
            } catch (DirectoryUnwillingToPerformOperationException e) {
                throw new NdsBusinessException(e.getMessage());
            } catch (DirectoryException e) {
                throw new NdsDirectoryException(e.getMessage(), e);
            }
        }
        else{
            // account Id should not be null - fail fast so it can be investigated
            throw new NdsApplicationException("Null accountId provided");
        }                
        
    }
    
    /**
     * Return success on the response as the LDAP request must have worked.
     */
    @Override
    protected BeisRegistrationNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {

        //If we get this far then we have succeeded
        BeisRegistrationNdsResponse ndsResponse = new BeisRegistrationNdsResponse();
        ndsResponse.setSuccess(true);
        return ndsResponse;
    
    }

    @Override
    protected String getRequestClassName() {
        return BeisRegistrationNdsRequest.class.getName();
    }
}
