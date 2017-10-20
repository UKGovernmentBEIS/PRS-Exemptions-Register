package com.northgateps.nds.beis.esb.converter;

import java.math.BigInteger;

import org.apache.camel.Converter;

import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsRequest;
import com.northgateps.nds.beis.backoffice.service.getpartydetails.GetPartyDetailsRequest;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;

/**
 * Converts between a NDS object and an external back office object 
 */
@Converter
@DoNotWeaveLoggingSystem
public final class ToGetPartyDetailsRequestConverter {

    private static final NdsLogger logger = NdsLogger.getLogger(ToGetPartyDetailsRequestConverter.class);
      
    /**
     * Convert the passed in retrieveRegisteredDetailsNdsRequest to the passed out GetPartyDetailsRequest
     * 
     * @param class com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsRequest
     * @return
     *     class  com.northgateps.nds.beis.backoffice.service.getpartydetails.GetPartyDetailsRequest
     * @throws NdsApplicationException 
     */
    @Converter
    public static GetPartyDetailsRequest converting(RetrieveRegisteredDetailsNdsRequest retrieveRegisteredDetailsNdsRequest) 
            throws NdsApplicationException, NdsBusinessException {
        
        logger.info("Starting Conversion For RetrievingRegistrationDetails Request");
        
        GetPartyDetailsRequest getPartyDetailsRequest = new GetPartyDetailsRequest();
       
        if(retrieveRegisteredDetailsNdsRequest.getAccountId() ==null){
            throw new NdsBusinessException(
                    "Error occured. The user is only partially registered and does not have a valid account id");
        }
        getPartyDetailsRequest.setPartyRef( new BigInteger(retrieveRegisteredDetailsNdsRequest.getAccountId()));
        
        logger.info("Finished Conversion For RetrievingRegistrationDetails Request");
        
        return getPartyDetailsRequest;        
    }    
}
