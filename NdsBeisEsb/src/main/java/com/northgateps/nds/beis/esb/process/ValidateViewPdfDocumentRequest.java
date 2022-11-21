package com.northgateps.nds.beis.esb.process;

import java.util.ArrayList;
import java.util.List;

import com.northgateps.nds.beis.api.viewdocument.ViewPdfNdsRequest;
import com.northgateps.nds.beis.esb.exception.InvalidReferenceNumberException;
import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.esb.camel.ValidateCamelRouteBean;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * To validate request submitted to view pdf document object
 */
public class ValidateViewPdfDocumentRequest extends ValidateCamelRouteBean {

    private static final NdsLogger logger = NdsLogger.getLogger(ValidateViewPdfDocumentRequest.class);

    /**
     * Validates submitted view pdf request.
     * 
     * @param request consists of data to be validated
     * @return request - after validation
     * @throws InvalidReferenceNumberException if there's a violation found
     */
    public ViewPdfNdsRequest validate(final ViewPdfNdsRequest request)
            throws InvalidReferenceNumberException {

        logger.info("Validating view pdf document Request");

        List<ValidationViolation> violations = new ArrayList<ValidationViolation>();
        if (request.getReferenceNumber() == null) {
            violations.add(new ValidationViolation("referenceNumber",
                    "Validation_Malformed_View_Pdf_Request"));
        } 
        if (violations.size() > 0) {
            throw new InvalidReferenceNumberException(violations);
        }

        return request;       

    }

}
