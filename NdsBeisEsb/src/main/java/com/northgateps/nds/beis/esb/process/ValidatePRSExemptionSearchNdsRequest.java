package com.northgateps.nds.beis.esb.process;

import java.util.ArrayList;
import java.util.List;
import com.northgateps.nds.beis.api.ExemptionSearch;
import com.northgateps.nds.beis.api.ExemptionSearchEsbModel;
import com.northgateps.nds.beis.api.PRSExemptionSearchNdsRequest;
import com.northgateps.nds.beis.api.PRSExemptionSearchNdsResponse;
import com.northgateps.nds.platform.application.api.validation.ValidationContext;
import com.northgateps.nds.platform.application.api.validation.ValidationDomain;
import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.esb.camel.ValidateCamelRouteBean;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;

import com.northgateps.nds.platform.logger.NdsLogger;

public class ValidatePRSExemptionSearchNdsRequest extends ValidateCamelRouteBean {

    private static final NdsLogger logger = NdsLogger.getLogger(ValidatePRSExemptionSearchNdsRequest.class);

    /**
     * Validates a PRSExemptionSearch request
     * 
     * @param request
     * @return request - after validation
     * @throws NdsApplicationException
     * @throws NdsBusinessException
     */

    public PRSExemptionSearchNdsRequest validate(final PRSExemptionSearchNdsRequest request)
            throws NdsApplicationException, NdsBusinessException {

        List<ValidationViolation> violations = new ArrayList<ValidationViolation>();

        logger.info("Validating PRSExemmptionSearch request");

       if (request.getExemptionSearch() == null) {
            throw new NdsApplicationException("Exemption search details expected but found null");
        } 

       request.getExemptionSearch().validate("exemptionSearch", violations,
               new ValidationContext<ExemptionSearchEsbModel>(new ExemptionSearchEsbModel() {

                   @Override
                   public ExemptionSearch getExemptionSearch() {
                       return request.getExemptionSearch();
                   }
               }, ValidationDomain.COMPLETE, scriptsLoader, getModelAnnotationsProvider(), null));
        
        if (violations.size() > 0) {
            throw new NdsBusinessException(PRSExemptionSearchNdsResponse.class, violations);
        }
       return request;

        
    }
}
