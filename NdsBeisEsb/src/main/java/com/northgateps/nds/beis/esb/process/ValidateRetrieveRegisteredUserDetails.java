package com.northgateps.nds.beis.esb.process;

import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsRequest;
import com.northgateps.nds.platform.esb.camel.ValidateCamelRouteBean;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * To validate request submitted to retrieve registered user details object
 */
public class ValidateRetrieveRegisteredUserDetails extends ValidateCamelRouteBean {

    private static final NdsLogger logger = NdsLogger.getLogger(ValidateRetrieveRegisteredUserDetails.class);

    /**
     * Validates submitted retrieve registered user details.
     * 
     * @param request
     * @return request - after validation
     * @throws NdsBusinessException
     * @throws NdsApplicationException 
     */
    public RetrieveRegisteredDetailsNdsRequest validate(final RetrieveRegisteredDetailsNdsRequest request)
            throws NdsBusinessException, NdsApplicationException {

        logger.info("Validating retrieve registered user details");
        if (request.getUsername() == null) {
            throw new NdsApplicationException("Expected the username but it was null");
        }
        if (request.getTenant() == null) {
            throw new NdsApplicationException("Expected the tenant but it was null");
        }

        return request;
    }

}
