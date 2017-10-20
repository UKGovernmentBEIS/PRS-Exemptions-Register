package com.northgateps.nds.beis.api.validation.js;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.northgateps.nds.platform.application.api.validation.ValidationViolation;
import com.northgateps.nds.platform.application.api.validation.js.ObjectValidatorConsolidationJoiner;
import com.northgateps.nds.platform.application.api.validation.js.RequiredFieldValidator;

/**
 * A joiner of validation error messages for addresses, so that if all of the first address line,
 * town and postcode are empty, only a single validation failure is shown
 */
public class AddressConsolidationWithCountryJoiner implements ObjectValidatorConsolidationJoiner {

    public static final String DEFAULT_MESSAGE = "Validation_Field_address_is_required";

    boolean invalid = false;

    @Override
    public boolean isInvalid() {
        return invalid;
    }

    @Override
    public String getMessage() {
        return DEFAULT_MESSAGE;
    }

    @Override
    public void setConfig(Annotation annotation) {
    }

    @Override
    public boolean assess(String fieldPath, List<ValidationViolation> violations) {
        List<ValidationViolation> removalCandidates = new ArrayList<ValidationViolation>();
        boolean line = false;
        boolean town = false;
        boolean postcode = false;
        boolean country = false;
        for (ValidationViolation violation : violations) {
            if (violation.getMessage().equals(RequiredFieldValidator.DEFAULT_MESSAGE)) {
                if (violation.getFieldPath().equals(fieldPath + ".line[0]")) {
                    line = true;
                    removalCandidates.add(violation);
                }
                if (violation.getFieldPath().equals(fieldPath + ".town")) {
                    town = true;
                    removalCandidates.add(violation);
                }
                if (violation.getFieldPath().equals(fieldPath + ".postcode")) {
                    postcode = true;
                    removalCandidates.add(violation);
                }

                if (violation.getFieldPath().equals(fieldPath + ".country")) {
                    country = true;
                    removalCandidates.add(violation);
                }
            }
        }
        if (line && town && postcode && country) {
            violations.removeAll(removalCandidates);
            invalid = true;
        }
        return invalid;
    }

}
