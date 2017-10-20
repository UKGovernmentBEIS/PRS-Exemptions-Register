package com.northgateps.nds.beis.api.validation.js;

import java.util.Arrays;
import java.util.List;

import java.time.ZonedDateTime;
import org.springframework.util.StringUtils;

import com.northgateps.nds.beis.api.ExemptionTypeEsbModel;
import com.northgateps.nds.platform.application.api.validation.BaseValidationProperty;
import com.northgateps.nds.platform.application.api.validation.Field;
import com.northgateps.nds.platform.application.api.validation.SimpleValidationProperty;
import com.northgateps.nds.platform.application.api.validation.ValidationContext;
import com.northgateps.nds.platform.application.api.validation.js.AbstractFieldValidator;
import com.northgateps.nds.platform.application.api.validation.js.CustomValidator;
import com.northgateps.nds.platform.application.api.validation.js.FieldValidator;

/**
 * 
 * Class to handle validation of Exemption Start date
 * E.g. If today's date is 28-02-2017 then the earliest date a start could be would be 29-08-2016
 * If today's date is 01-03-2017 then the earliest date a start could be would be 02-09-2016
 * If today's date is 15-03-2017 then the earliest date a start could be would be 16-09-2016
 *
 */
public class StartDateValidator extends AbstractFieldValidator {

    public static final String DEFAULT_MESSAGE = "Validation_Field_must_not_be_outside_exemption_duration";

    /** Update the default message for this FieldValidator. */
    {
        message = DEFAULT_MESSAGE;
    }

    private String className = "exemptionrestriction";

    private String duration;
    private String unit;

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<String> getAttrs() {
        return Arrays.asList(new String[] { "duration", "unit" });
    }

    @Override
    public List<BaseValidationProperty> getProperties() {
        List<BaseValidationProperty> properties = super.getProperties();
        properties.add(
                new SimpleValidationProperty().setParameter("duration").setValue(String.valueOf(duration), null));
        String unitDescription = "";
        if (!StringUtils.isEmpty(unit)) {
            if (unit.equals("M")) {
                if (duration.equals("1"))
                {
                unitDescription = "month";
                }
                else
                {
                    unitDescription = "months";
                }
            }
            if (unit.equals("Y")) {
                if (duration.equals("1"))
                {
                    unitDescription = "year";
                }
                else
                {
                    unitDescription = "years";
                }
            }
        }
        properties.add(
                new SimpleValidationProperty().setParameter("unit").setValue(String.valueOf(unitDescription), null));

        return properties;
    }

    @Override
    public CustomValidator getCustomValidator() {
        return new CustomValidator() {

            @Override
            public boolean validate(FieldValidator validator, Field field, ValidationContext<?> validationContext) {
                if (!(field.getValue() instanceof String)) {
                    return false;
                }
                if (StringUtils.isEmpty(field.getValue())) {
                    return false;
                }

                ExemptionTypeEsbModel beisAllModel = (ExemptionTypeEsbModel) validationContext.getModel();
                ZonedDateTime today = ZonedDateTime.now();
                ZonedDateTime alloweStartDate = ZonedDateTime.now();
                if (field.getObjectValue() == null) {
                    return false;
                }
                ZonedDateTime startDate = (ZonedDateTime) field.getObjectValue();

                unit = beisAllModel.getExemptionType().getDurationUnit();
                duration = beisAllModel.getExemptionType().getDuration();

                if (unit.equals("M")) {
                    alloweStartDate = today.minusMonths(Integer.parseInt(duration));
                } else if (unit.equals("Y")) {
                    alloweStartDate = today.minusMonths(12 * Integer.parseInt(duration));
                }

                if (startDate.isBefore(alloweStartDate)) {
                    return false;
                }
                return true;
            }
        };
    }
}
