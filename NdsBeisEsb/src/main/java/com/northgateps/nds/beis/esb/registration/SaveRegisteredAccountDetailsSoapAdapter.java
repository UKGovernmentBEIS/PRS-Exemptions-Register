package com.northgateps.nds.beis.esb.registration;

import org.apache.camel.TypeConverter;

import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsRequest;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsResponse;
import com.northgateps.nds.beis.backoffice.service.maintainpartydetails.MaintainPartyDetailsRequest;
import com.northgateps.nds.beis.backoffice.service.maintainpartydetails.MaintainPartyDetailsResponse;
import com.northgateps.nds.beis.esb.beisregistration.BeisRegistrationUpdateAccountIdLdapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.beis.esb.registration.SaveRegisteredAccountDetailsAdapter;

/**
 * This adapts the Nds request to a foundation layer BEIS request for MaintainPartyDetails and handles the response.
 */
public class SaveRegisteredAccountDetailsSoapAdapter extends NdsSoapAdapter <SaveRegisteredAccountDetailsNdsRequest, SaveRegisteredAccountDetailsNdsResponse, MaintainPartyDetailsRequest, MaintainPartyDetailsResponse>{

    private static final NdsLogger logger = NdsLogger.getLogger(SaveRegisteredAccountDetailsSoapAdapter.class);
    
    /**
     * Adapt the Nds Request and produce a MaintainPartyDetails request to send to the BEIS foundation layer.
     */
    @Override
    protected MaintainPartyDetailsRequest doRequestProcess(SaveRegisteredAccountDetailsNdsRequest request,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {

        MaintainPartyDetailsRequest backOfficeRequest = null;
        
        try{
            ndsExchange.setOperationName("MaintainPartyDetailsWSDL");
            
            //Create the type converter
            TypeConverter converter = ndsExchange.getTypeConverter();
            backOfficeRequest = converter.convertTo(MaintainPartyDetailsRequest.class, request);                
        }
        catch (Exception e) {
            throw new NdsApplicationException("Error occured during request conversion process:  " + e.getMessage(), e);
        }

        logger.info("Successfully created the SOAP request");
                
        return backOfficeRequest;
    }

    /**
     * Receive a response from the BEIS foundation layer for MaintainPartyDetails and translate to a response
     */
    @Override
    protected SaveRegisteredAccountDetailsNdsResponse doResponseProcess(MaintainPartyDetailsResponse externalResponse,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {
                             
        SaveRegisteredAccountDetailsNdsResponse ndsResponse = new SaveRegisteredAccountDetailsNdsResponse();
        
        try {            
            TypeConverter converter = ndsExchange.getTypeConverter();
            ndsResponse = converter.convertTo(SaveRegisteredAccountDetailsNdsResponse.class, externalResponse);            
        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during response conversion process:  " + e.getMessage(),
                    e);
        }
        logger.info("Successfully created the NDS response");
                       
        //If we successfully converted the response then set the account ID in LDAP
        if(ndsResponse.isSuccess())
        {            
            //Set party reference on exchange property for an LDAP adapter to deal with if it was partially registered
            if(ndsExchange.getAnExchangeProperty(SaveRegisteredAccountDetailsAdapter.EXCHANGE_PROPERTY_PARTIALLY_REGISTERED) != null){
                
                Boolean partiallyRegistered = (Boolean) ndsExchange.getAnExchangeProperty(SaveRegisteredAccountDetailsAdapter.EXCHANGE_PROPERTY_PARTIALLY_REGISTERED);
                
                if(partiallyRegistered){
                    ndsExchange.setAnExchangeProperty(BeisRegistrationUpdateAccountIdLdapAdapter.EXCHANGE_PROPERTY_ACCOUNT_ID, externalResponse.getPartyRef());
                }
            }
        }
        
        return ndsResponse;
        
    }
    
    @Override
    protected String getRequestClassName() {
        return SaveRegisteredAccountDetailsNdsRequest.class.getName();
    }

    @Override
    protected String getResponseClassName() {
        return MaintainPartyDetailsResponse.class.getName();
    }

}
