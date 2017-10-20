package com.northgateps.nds.beis.esb.process;
import com.northgateps.nds.beis.api.getpartiallyregistereddetails.GetPartiallyRegisteredDetailsNdsRequest;

import com.northgateps.nds.platform.esb.camel.ValidateCamelRouteBean;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * To validate request submitted to get partially registered details
 */
public class ValidateGetPartiallyRegisteredDetailsNdsRequest extends ValidateCamelRouteBean { 

    private static final NdsLogger logger = NdsLogger.getLogger(ValidateGetPartiallyRegisteredDetailsNdsRequest.class);
       
    public GetPartiallyRegisteredDetailsNdsRequest validate(final GetPartiallyRegisteredDetailsNdsRequest request) throws NdsBusinessException, NdsApplicationException {
   
        logger.info("Validating get partially registered details request");        
        if (request.getUsername() == null) {
            throw new NdsApplicationException("Expected the username but it was null");
        }
        if (request.getTenant() == null) {
           throw new NdsApplicationException("Expected the tenant but it was null");
        }

        logger.info("Validated get partially registered details request");                
        
        return request;

    }
}
