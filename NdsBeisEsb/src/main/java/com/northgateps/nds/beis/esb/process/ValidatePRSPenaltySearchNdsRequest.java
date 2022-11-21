package com.northgateps.nds.beis.esb.process;

import java.util.ArrayList;
import java.util.List;
import com.northgateps.nds.beis.api.PRSPenaltySearchNdsRequest;
import com.northgateps.nds.beis.api.PRSPenaltySearchNdsResponse;
import com.northgateps.nds.beis.api.PenaltySearch;
import com.northgateps.nds.beis.api.PenaltySearchEsbModel;
import com.northgateps.nds.platform.application.api.validation.ValidationContext;
import com.northgateps.nds.platform.application.api.validation.ValidationDomain;
import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.esb.camel.ValidateCamelRouteBean;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.logger.NdsLogger;

public class ValidatePRSPenaltySearchNdsRequest extends ValidateCamelRouteBean {

    private static final NdsLogger logger = NdsLogger.getLogger(ValidatePRSPenaltySearchNdsRequest.class);

    /**
     * Validates a PRSPenaltySearch request
     * 
     * @param request consists of data to be validated
     * @return request - after validation
     * @throws NdsApplicationException if penalty search is missing from the request
     * @throws NdsBusinessException if there's a violation found
     */

    public PRSPenaltySearchNdsRequest validate(final PRSPenaltySearchNdsRequest request)
            throws NdsApplicationException, NdsBusinessException {

        List<ValidationViolation> violations = new ArrayList<ValidationViolation>();

        logger.info("Validating PRSPenaltySearch request");

        if (request.getPenaltySearch() == null) {
            throw new NdsApplicationException("Penalty search details expected but found null");
        }

        request.getPenaltySearch().validate("penaltySearch", violations,
                new ValidationContext<PenaltySearchEsbModel>(new PenaltySearchEsbModel() {

                    @Override
                    public PenaltySearch getPenaltySearch() {
                        return request.getPenaltySearch();
                    }
                }, ValidationDomain.COMPLETE, scriptsLoader, getModelAnnotationsProvider(), null));

        if (violations.size() > 0) {
            throw new NdsBusinessException(PRSPenaltySearchNdsResponse.class, violations);
        }
        return request;
    }
}
