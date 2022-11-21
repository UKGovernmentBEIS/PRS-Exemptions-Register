package com.northgateps.nds.beis.esb.process;

import org.apache.camel.Exchange;

import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsRequest;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsResponse;
import com.northgateps.nds.platform.esb.camel.ValidateCamelRouteBean;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.logger.NdsLogger;

/*
 * Validation class to ensure the dashboard details request is valid
 */
public class ValidateGetPRSAccountExemptionsNdsRequest extends ValidateCamelRouteBean {

    private static final NdsLogger logger = NdsLogger.getLogger(ValidateRetrieveRegisteredUserDetails.class);

    /**
     * Validates a dashboard request
     * 
     * @param request consists of data to be validated
     * @return request - after validation
     * @throws NdsApplicationException if either the username or the tenant doesn't exist
     */
    public GetPRSAccountExemptionsNdsRequest validate(final GetPRSAccountExemptionsNdsRequest request)
            throws NdsApplicationException {

        logger.info("Validating dashboard request");

        if (request.getUsername() == null) {
            throw new NdsApplicationException(
                    "Error in validating the GetPRSAccountExemptionsNdsRequest : USERNAME IS NULL ");
        }
        if (request.getTenant() == null) {
            throw new NdsApplicationException(
                    "Error in validating the GetPRSAccountExemptionsNdsRequest : TENANT IS NULL");
        }

        return request;
    }

    /**
     * Validates if user is present in backoffice or not
     * @param exchange contains the body to be checked for the validation process
     * @throws NdsBusinessException if the retrieved nds message from the response exists, and that message
     *   error number is either "BEI-187" or "BEI-188"
     */
    public void validateUserDataInBackoffice(Exchange exchange) throws NdsBusinessException {

        RetrieveRegisteredDetailsNdsResponse retrieveRegisteredDetailsNdsResponse = (RetrieveRegisteredDetailsNdsResponse) exchange.getIn().getBody();

        if (retrieveRegisteredDetailsNdsResponse.getNdsMessages() != null) {
            if (retrieveRegisteredDetailsNdsResponse.getNdsMessages().getErrorNumber().equals("BEI-187")
                    || retrieveRegisteredDetailsNdsResponse.getNdsMessages().getErrorNumber().equals("BEI-188")) {
                throw new NdsBusinessException(retrieveRegisteredDetailsNdsResponse.getNdsMessages().getErrorNumber());
            }
        }
    }
}
