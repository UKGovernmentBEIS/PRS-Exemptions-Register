package com.northgateps.nds.beis.esb.registration;

import org.apache.camel.TypeConverter;

import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsRequest;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsResponse;
import com.northgateps.nds.beis.backoffice.service.getpartydetails.GetPartyDetailsRequest;
import com.northgateps.nds.beis.backoffice.service.getpartydetails.GetPartyDetailsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsSoapAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * This adapts the Nds request to a foundation layer BEIS request for GetPartyDetails and handles the response.
 */
public class RetrieveRegisteredDetailsSoapAdapter extends
        NdsSoapAdapter<RetrieveRegisteredDetailsNdsRequest, RetrieveRegisteredDetailsNdsResponse, GetPartyDetailsRequest, GetPartyDetailsResponse> {

    private static final NdsLogger logger = NdsLogger.getLogger(RetrieveRegisteredDetailsSoapAdapter.class);
    
    public static String USER_TYPE = "userType";

    /**
     * Adapt the Nds Request and produce a GetPartyDetails request to send to the BEIS foundation layer.
     */
    @Override
    protected GetPartyDetailsRequest doRequestProcess(RetrieveRegisteredDetailsNdsRequest request,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {
        GetPartyDetailsRequest backOfficeRequest = null;

        logger.info("Started SOAP request");

        try {
            ndsExchange.setOperationName("GetPartyDetailsWSDL");
            ndsExchange.setAnExchangeProperty("USERNAME", request.getUsername());

            // Create the type converter
            TypeConverter converter = ndsExchange.getTypeConverter();
            backOfficeRequest = converter.convertTo(GetPartyDetailsRequest.class, request);
        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during request conversion process:  " + e.getMessage(), e);
        }

        logger.info("Successfully created the SOAP request");

        return backOfficeRequest;
    }

    /**
     * Receive a response from back office
     */
    @Override
    protected RetrieveRegisteredDetailsNdsResponse doResponseProcess(GetPartyDetailsResponse externalResponse,
            NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsApplicationException {

        RetrieveRegisteredDetailsNdsResponse retrieveRegisteredDetailsNdsResponse = new RetrieveRegisteredDetailsNdsResponse();

        logger.info("Started SOAP response");

        try {
            TypeConverter converter = ndsExchange.getTypeConverter();
            retrieveRegisteredDetailsNdsResponse = converter.convertTo(RetrieveRegisteredDetailsNdsResponse.class,
                    externalResponse);
            String username = retrieveUsernameFromDn((String) ndsExchange.getAnExchangeProperty("USERNAME"));
            retrieveRegisteredDetailsNdsResponse.getBeisRegistrationDetails().getUserDetails().setUsername(username);
            if(retrieveRegisteredDetailsNdsResponse.getBeisRegistrationDetails().getUserDetails().getUserType() != null){
                ndsExchange.setAnExchangeProperty(USER_TYPE,retrieveRegisteredDetailsNdsResponse.getBeisRegistrationDetails().getUserDetails().getUserType());
            }                    
            
        } catch (Exception e) {
            throw new NdsApplicationException("Error occured during response conversion process:  " + e.getMessage(),
                    e);
        }

        logger.info("Successfully created the NDS response");

        return retrieveRegisteredDetailsNdsResponse;
    }

    private String retrieveUsernameFromDn(String username) {
        if (username.indexOf('=', 0) <= 0) {
            return username;
        }
        return username.substring(username.indexOf('=', 0) + 1, username.indexOf(',', 0));
    }

    @Override
    protected String getRequestClassName() {
        return RetrieveRegisteredDetailsNdsRequest.class.getName();
    }

    @Override
    protected String getResponseClassName() {
        return GetPartyDetailsResponse.class.getName();
    }
}
