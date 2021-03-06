package com.northgateps.nds.beis.esb.registerexemption;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.northgateps.nds.beis.api.PropertyType;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsRequest;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsResponse;
import com.northgateps.nds.beis.api.statistics.BeisEvent;
import com.northgateps.nds.platform.esb.adapter.NdsDirectoryComponent;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.adapter.persistence.AbstractPersistenceAdapter;
import com.northgateps.nds.platform.esb.directory.DirectoryAccessConnection;
import com.northgateps.nds.platform.esb.directory.DirectoryConnectionConfig;
import com.northgateps.nds.platform.esb.directory.ServiceAccessDetails;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryException;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Uses the username and tenant information to lookup the user's unique account id and user details such as name,address,email address
 */
public class RegisterPrsExemptionLookupIdComponent extends NdsDirectoryComponent<RegisterPrsExemptionNdsRequest, RegisterPrsExemptionNdsResponse> {
    
    private static final NdsLogger logger = NdsLogger.getLogger(RegisterPrsExemptionLookupIdComponent.class);
    final ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();

    public static String EMAIL_ADDRESS = "email";
    public static String AGENT_NAME = "agentName";
    public static String AGENT_ADDRESS = "agentAddress";
    public static String PROPERTY_TYPE = "propertyType";

    @Override
    protected void doProcess(RegisterPrsExemptionNdsRequest request, DirectoryAccessConnection directoryConnection,
            NdsSoapRequestAdapterExchangeProxy ndsExchange, DirectoryConnectionConfig directoryConnectionConfigImpl)
                    throws NdsApplicationException, NdsBusinessException, DirectoryException {
        
        ndsExchange.setAnExchangeProperty(AbstractPersistenceAdapter.EVENT_REFERENCE_ID,
                request.getRegisterPrsExemptionDetails().getExemptionDetails().getReferenceId());
        
        logger.info(" Started setting account id in request and user details in exchange ");
        ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();

        String serviceName = configurationManager.getString("service.foundationLayerPartyService.name");
        String userDn = getUserDn(configurationManager, request.getUserName(), request.getTenant());        
       
        ndsExchange.setAnExchangeProperty(AbstractPersistenceAdapter.EVENT_USER_DN, userDn);
                
        ndsExchange.setAnExchangeProperty(AbstractPersistenceAdapter.EVENT_TYPE, request.getPwsText());
        
        PropertyType propertyType = request.getRegisterPrsExemptionDetails().getExemptionDetails().getPropertyType();        
        if(propertyType == PropertyType.PRSD){
            ndsExchange.setAnExchangeProperty(PROPERTY_TYPE, BeisEvent.DOMESTIC.toString());
        }else{
            ndsExchange.setAnExchangeProperty(PROPERTY_TYPE, BeisEvent.NON_DOMESTIC.toString());
        }    
        
        ServiceAccessDetails serviceAccessDetails = getUserManager().serviceLookup(directoryConnection, userDn, serviceName,
                true, true, false);

        // Check the user id as we have a temporary issue where the uid is
        // set automatically. This however has hypens which will not be in a
        // partyref
        if (serviceAccessDetails.getUserId() == null || serviceAccessDetails.getUserId().isEmpty()
                || serviceAccessDetails.getUserId().contains("-")) {
            throw new NdsBusinessException(
                    "Error occurred. The user is only partially registered and does not have a valid account id");
        }

        // get the account id, set it in the request and set the request in the exchange
        request.setAccountId(serviceAccessDetails.getUserId());
        Map<String, String> userAttributesMap = getDirectoryManager().retrieveEntryAttributes(directoryConnection, userDn,
                "*");

        getUserdetails(userAttributesMap, ndsExchange);

        ndsExchange.setRequestMessageBody(request);
        logger.info("Sucessfully set account id in request and user details in exchange");
    }

    @Override
    protected RegisterPrsExemptionNdsResponse doResponseProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsApplicationException {
        logger.info("Creating RegisterPrsExemptionNdsResponse");
        RegisterPrsExemptionNdsResponse registerPrsExemptionNdsResponse = new RegisterPrsExemptionNdsResponse();
        registerPrsExemptionNdsResponse.setSuccess(true);
        logger.info("Successfully created RegisterPrsExemptionNdsResponse");
        return registerPrsExemptionNdsResponse;
    }

    @Override
    protected String getRequestClassName() {
        return RegisterPrsExemptionNdsRequest.class.getName();
    }
    
    //Get userDetails from ldap and set in exchange     
    public void getUserdetails(Map<String, String> userAttributesMap, NdsSoapRequestAdapterExchangeProxy ndsExchange) {

        if (StringUtils.isNotEmpty(userAttributesMap.get("mail"))) {
            ndsExchange.setAnExchangeProperty(EMAIL_ADDRESS, userAttributesMap.get("mail"));
        }
        if (StringUtils.isNotEmpty(             
            userAttributesMap.get(configurationManager.getString("attribute.organisationName")))) {
            ndsExchange.setAnExchangeProperty(AGENT_NAME,
                    userAttributesMap.get(configurationManager.getString("attribute.organisationName")));
        }
        ndsExchange.setAnExchangeProperty(AGENT_ADDRESS, getAddress(userAttributesMap));
    }

  
    //Get address details from ldap 
    public String getAddress(Map<String, String> userAttributesMap) {
        StringBuilder address = new StringBuilder();

        if (StringUtils.isNotEmpty(userAttributesMap.get(configurationManager.getString("attribute.street")))) {
            address.append(userAttributesMap.get(configurationManager.getString("attribute.street")));
        }
        if (StringUtils.isNotEmpty(userAttributesMap.get(configurationManager.getString("attribute.street2")))) {
            address.append(", ");
            address.append(userAttributesMap.get(configurationManager.getString("attribute.street2")));
        }
        if (StringUtils.isNotEmpty(userAttributesMap.get(configurationManager.getString("attribute.town")))) {
            address.append(", ");
            address.append(userAttributesMap.get(configurationManager.getString("attribute.town")));
        }
        if (StringUtils.isNotEmpty(userAttributesMap.get(configurationManager.getString("attribute.county")))) {
            address.append(", ");
            address.append(userAttributesMap.get(configurationManager.getString("attribute.county")));
        }
        if (StringUtils.isNotEmpty(userAttributesMap.get(configurationManager.getString("attribute.postalCode")))) {
            address.append(", ");
            address.append(userAttributesMap.get(configurationManager.getString("attribute.postalCode")));
        }
        return address.toString();
    }

}
