package com.northgateps.nds.beis.esb.process;

import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsRequest;
import com.northgateps.nds.platform.esb.camel.ValidateCamelRouteBean;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/*
 * Validation class to ensure the dashboard details request is valid
 */
public class ValidateGetPRSAccountExemptionsNdsRequest extends ValidateCamelRouteBean {

	private static final NdsLogger logger = NdsLogger.getLogger(ValidateRetrieveRegisteredUserDetails.class);

	/**
	 * Validates a dashboard request
	 * 
	 * @param request
	 * @return request - after validation	 
	 * @throws NdsApplicationException 
	 */
	public GetPRSAccountExemptionsNdsRequest validate(final GetPRSAccountExemptionsNdsRequest request) throws NdsApplicationException {

		logger.info("Validating dashboard request");

		if (request.getUsername() == null) {
		    throw new NdsApplicationException("Error in validating the GetPRSAccountExemptionsNdsRequest : USERNAME IS NULL ");
		}
		if (request.getTenant() == null) {
		    throw new NdsApplicationException("Error in validating the GetPRSAccountExemptionsNdsRequest : TENANT IS NULL");
		}
		
		return request;
	}

}
