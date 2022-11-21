package com.northgateps.nds.beis.esb.process;

import com.northgateps.nds.beis.api.updateexemptiondetails.UpdateExemptionDetailsNdsRequest;
import com.northgateps.nds.platform.esb.camel.ValidateCamelRouteBean;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * To validate request submitted to Update Exemption Details
 */
public class ValidatePrsExemptionUpdateRequest extends ValidateCamelRouteBean {

    private static final NdsLogger logger = NdsLogger.getLogger(ValidatePrsExemptionUpdateRequest.class);

    /**
     * Validates Update Exemption Details
     * 
     * @param request consists of data to be validated
     * @return request - after validation     
     * @throws NdsApplicationException if any of the following data are missing: username, tenant, exemption details id, and end date
     */
    public UpdateExemptionDetailsNdsRequest validate(final UpdateExemptionDetailsNdsRequest request)
            throws NdsApplicationException {

        logger.info("Validating Update Exemption Details request");
        
        if (request.getUserName() == null) {
            throw new NdsApplicationException("Error in validating the UpdateExemptionDetailsNdsRequest : USERNAME IS NULL ");
        }
        if (request.getTenant() == null) {
            throw new NdsApplicationException("Error in validating the UpdateExemptionDetailsNdsRequest : TENANT IS NULL");
        }
        if (request.getUpdateExemptionDetails().getReferenceId() == null) {
            throw new NdsApplicationException("Error in validating the UpdateExemptionDetailsNdsRequest : REFERENCEID IS NULL");
        }
        if (request.getUpdateExemptionDetails().getEndDate() == null) {
            throw new NdsApplicationException("Error in validating the UpdateExemptionDetailsNdsRequest : ENDDATE IS NULL");
        }
       
        return request;
    }
}
