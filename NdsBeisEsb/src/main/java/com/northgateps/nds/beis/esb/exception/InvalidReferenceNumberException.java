package com.northgateps.nds.beis.esb.exception;

import java.util.List;

import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.RequestMessageViolationException;

public class InvalidReferenceNumberException extends NdsApplicationException implements RequestMessageViolationException {

    /** Version id for serialisation */
    private static final long serialVersionUID = 1L;
    private List<ValidationViolation> violations;

    public InvalidReferenceNumberException(List<ValidationViolation> violations) {
        super();
        
        this.violations = violations;
    }
    
    @Override
    public String getMessage() {
        return "Request data was incorrect";
    }
    
    @Override
    public List<ValidationViolation> getViolations() {
        return violations;
    }
}
