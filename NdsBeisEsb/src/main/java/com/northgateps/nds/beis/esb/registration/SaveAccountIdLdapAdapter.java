package com.northgateps.nds.beis.esb.registration;

import java.util.HashMap;

import com.northgateps.nds.beis.api.BeisPartyRequest;
import com.northgateps.nds.beis.esb.beisregistration.BeisRegistrationUpdateAccountIdLdapAdapter;
import com.northgateps.nds.platform.api.NdsRequest;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsDirectoryAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.directory.DirectoryConnection;
import com.northgateps.nds.platform.esb.directory.DirectoryConnectionConfig;
import com.northgateps.nds.platform.esb.directory.UserServices;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryException;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryUnwillingToPerformOperationException;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.esb.exception.NdsDirectoryException;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Abstract class to write the accountid exchange property to Ldap
 * This class specifies the request must implement BeisPartyRequest and 
 * NdsRequest classes. This is so that the username and tenant can be retrieved.
 */
public abstract class SaveAccountIdLdapAdapter<TRequest extends BeisPartyRequest & NdsRequest, TResponse extends NdsResponse> extends NdsDirectoryAdapter<TRequest, TResponse>{
    //This is to be reused where required
    public static final String EXCHANGE_PROPERTY_ACCOUNT_ID = "accountId";  
    
    private static final NdsLogger logger = NdsLogger.getLogger(BeisRegistrationUpdateAccountIdLdapAdapter.class);
    
    @Override
    protected void doRequestProcess(TRequest request, DirectoryConnection directoryConnection,
            NdsSoapRequestAdapterExchangeProxy ndsExchange, DirectoryConnectionConfig directoryConnectionConfigImpl)
            throws NdsApplicationException, NdsBusinessException {
        
        //Read property for account id
        if( ndsExchange.getAnExchangeProperty(EXCHANGE_PROPERTY_ACCOUNT_ID) != null){
            
            String accountId = ndsExchange.getAnExchangeProperty(EXCHANGE_PROPERTY_ACCOUNT_ID).toString();   
                        
            ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();

            //Get the service name
            String serviceName = configurationManager.getString("service.foundationLayerPartyService.name");

            String servicednForTheUser = getNdsServiceByUserDn(configurationManager, request.getUsername(), request.getTenant(), serviceName);
            
            logger.info("About to set the accountId to " + accountId + "For Dn" + servicednForTheUser);
            
            //Create hash map to update the uid on the service for the user.
            String ldapAttributeServiceUid = configurationManager.getString("attribute.userid");           

            // Any attributes to be changed on the new user after copying the template user
            HashMap<String, String> changes = new HashMap<String, String>();
            changes.put(ldapAttributeServiceUid, accountId);   
            
            try{                
                UserServices.setAttributesOnEntry(directoryConnection, servicednForTheUser, changes);
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
 }
