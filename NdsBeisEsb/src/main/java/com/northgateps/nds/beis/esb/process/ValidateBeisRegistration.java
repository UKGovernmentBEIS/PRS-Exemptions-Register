package com.northgateps.nds.beis.esb.process;

import java.util.ArrayList;
import java.util.List;

import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.BeisRegistrationEsbModel;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsRequest;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsResponse;
import com.northgateps.nds.platform.application.api.validation.ValidationContext;
import com.northgateps.nds.platform.application.api.validation.ValidationDomain;
import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.esb.camel.ValidateCamelRouteBean;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.logger.NdsLogger;

public class ValidateBeisRegistration extends ValidateCamelRouteBean {

    private static final NdsLogger logger = NdsLogger.getLogger(ValidateRetrieveRegisteredUserDetails.class);

    /**
     * Validates a submitted registration.
     * 
     * @param request
     * @return request - after validation
     * @throws NdsBusinessException 
     * @throws NdsApplicationException 
     */
    public BeisRegistrationNdsRequest validate(final BeisRegistrationNdsRequest request)
            throws NdsBusinessException, NdsApplicationException {

        logger.info("Validating registration");

        List<ValidationViolation> violations = new ArrayList<ValidationViolation>();
        if (request.getRegistrationDetails() == null) {
            throw new NdsApplicationException("Registration details expected but found null");
        } 

        request.getRegistrationDetails().validate("beisRegistrationDetails", violations,
                new ValidationContext<BeisRegistrationEsbModel>(new BeisRegistrationEsbModel() {

                    @Override
                    public BeisRegistrationDetails getBeisRegistrationDetails() {
                        return request.getRegistrationDetails();
                    }
                }, ValidationDomain.COMPLETE, scriptsLoader, getModelAnnotationsProvider(), null));
        
        if (violations.size() > 0) {
            throw new NdsBusinessException(BeisRegistrationNdsResponse.class, violations);
        }
       return request;
    }

}
