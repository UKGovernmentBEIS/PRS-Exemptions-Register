package com.northgateps.nds.beis.esb.beisregistration;

import org.apache.camel.TypeConverter;

import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsRequest;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsResponse;
import com.northgateps.nds.beis.backoffice.service.maintainpartydetails.MaintainPartyDetailsRequest;
import com.northgateps.nds.beis.backoffice.service.maintainpartydetails.MaintainPartyDetailsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsSoapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.beis.esb.beisregistration.BeisRegistrationUpdateAccountIdLdapAdapter;

/**
 * This adapts the Nds request to a foundation layer BEIS request for MaintainPartyDetails and handles the response.
 */
public class BeisRegistrationSoapAdapter extends NdsSoapAdapter <BeisRegistrationNdsRequest, BeisRegistrationNdsResponse, MaintainPartyDetailsRequest, MaintainPartyDetailsResponse>{

    private static final NdsLogger logger = NdsLogger.getLogger(BeisRegistrationSoapAdapter.class);
    
    /**
     * Adapt the Nds Request and produce a MaintainPartyDetails request to send to the BEIS foundation layer.
     */
    @Override
    protected MaintainPartyDetailsRequest doRequestProcess(BeisRegistrationNdsRequest request,
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
     * Receive a response from the BEIS foundation layer for MaintainPartyDetails and look for a party ref
     * so we can pass as an exchange property for further processing in the route. 
     */
    @Override
    protected BeisRegistrationNdsResponse doResponseProcess(MaintainPartyDetailsResponse externalResponse,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {

        Boolean success = false;
        
        if(externalResponse.isSuccess())
        {
            success = true;
            //Set party reference on exchange property for an LDAP adapter to deal with
            ndsExchange.setAnExchangeProperty(BeisRegistrationUpdateAccountIdLdapAdapter.EXCHANGE_PROPERTY_ACCOUNT_ID, externalResponse.getPartyRef());            
        }
        
        BeisRegistrationNdsResponse ndsResponse = new BeisRegistrationNdsResponse();
        ndsResponse.setSuccess(success);
        return ndsResponse;
        
    }
    
    @Override
    protected String getRequestClassName() {
        return BeisRegistrationNdsRequest.class.getName();
    }

    @Override
    protected String getResponseClassName() {
        return MaintainPartyDetailsResponse.class.getName();
    }

}
