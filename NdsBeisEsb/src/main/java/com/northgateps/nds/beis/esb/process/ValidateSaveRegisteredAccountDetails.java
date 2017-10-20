package com.northgateps.nds.beis.esb.process;

import java.util.ArrayList;
import java.util.List;

import com.northgateps.nds.beis.api.SaveRegisteredAccountDetailsEsbModel;
import com.northgateps.nds.beis.api.UpdateBeisRegistrationDetails;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsRequest;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsResponse;
import com.northgateps.nds.platform.application.api.validation.ValidationContext;
import com.northgateps.nds.platform.application.api.validation.ValidationDomain;
import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.esb.camel.ValidateCamelRouteBean;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsBusinessException;
import com.northgateps.nds.platform.logger.NdsLogger;


/**
 * To validate request submitted to retrieve registered user details object
 */
public class ValidateSaveRegisteredAccountDetails extends ValidateCamelRouteBean {

    private static final NdsLogger logger = NdsLogger.getLogger(ValidateSaveRegisteredAccountDetails.class);

    /**
     * Validates submitted save registered user details.
     * 
     * @param request
     * @return request - after validation
     * @throws NdsApplicationException
     * @throws NdsBusinessException
     */
    public SaveRegisteredAccountDetailsNdsRequest validate(final SaveRegisteredAccountDetailsNdsRequest request)
            throws NdsApplicationException, NdsBusinessException {

        List<ValidationViolation> violations = new ArrayList<ValidationViolation>();
        logger.info("Validating save registered user details");
        
        if (request.getTenant() == null) {
            throw new NdsApplicationException("Expected tenant but it was null");
        }
        if (request.getRegisteredAccountDetails() == null) {
            throw new NdsApplicationException("Expected account details but it was null");
        }
        if (request.getRegisteredAccountDetails().getUpdateUserDetails() == null) {
            throw new NdsApplicationException("Expected update user details but it was null");
        }
        
        if (request.getRegisteredAccountDetails().getUpdateUserDetails().getUsername() == null) {
            throw new NdsApplicationException("Expected username but it was null");
        }

        request.getRegisteredAccountDetails().validate("registeredAccountDetails", violations,
                new ValidationContext<SaveRegisteredAccountDetailsEsbModel>(new SaveRegisteredAccountDetailsEsbModel() {

                    @Override
                    public UpdateBeisRegistrationDetails getRegisteredAccountDetails() {
                        return request.getRegisteredAccountDetails();
                    }
                }, ValidationDomain.COMPLETE, scriptsLoader, getModelAnnotationsProvider(), null));
        
        if (violations.size() > 0) {
            throw new NdsBusinessException(SaveRegisteredAccountDetailsNdsResponse.class, violations);
        }
       return request;

    }

}
