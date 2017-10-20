package com.northgateps.nds.beis.api.validation.js;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.northgateps.nds.platform.api.Upload;
import com.northgateps.nds.platform.application.api.metadata.GenericFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.BaseValidationProperty;
import com.northgateps.nds.platform.application.api.validation.Field;
import com.northgateps.nds.platform.application.api.validation.ValidationContext;
import com.northgateps.nds.platform.application.api.validation.js.AbstractFieldValidator;
import com.northgateps.nds.platform.application.api.validation.js.CustomValidator;
import com.northgateps.nds.platform.application.api.validation.js.FieldValidator;

/**
 * 
 * Validator to verify minimum 1 file upload or not.
 *
 */
public class EPCUploadFieldValidator extends AbstractFieldValidator {

    public static final String DEFAULT_MESSAGE = "Validation_Field_An_Uploaded_File_Is_Required";

    /** Update the default message for this FieldValidator. */
    {
        message = DEFAULT_MESSAGE;
    }

    private String className = "invalidupload";

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setConfig(Annotation annotation) {
        if (annotation instanceof GenericFieldMetadata) {

            String invalidMessage = ((GenericFieldMetadata) annotation).invalidMessage();
            if (!invalidMessage.trim().isEmpty()) {
                message = invalidMessage;
            }
        }
        super.setConfig(annotation);
    }

    @Override
    public List<BaseValidationProperty> getProperties() {
        List<BaseValidationProperty> properties = new ArrayList<BaseValidationProperty>();
        properties.addAll(super.getProperties());
        return properties;
    }

    @Override
    public boolean useFieldPath() {
        return false;
    }

    @Override
    public String getInvalidClassQualifier() {
        return "";
    }

    @Override
    public List<String> getAttrs() {
        return Arrays.asList(new String[] {});
    }

    /**
     * A server side validator that checks that a file has been uploaded
     * 
     */
    @Override
    public CustomValidator getCustomValidator() {
        return new CustomValidator() {

            @Override
            public boolean validate(FieldValidator validator, Field field, ValidationContext<?> validationContext) {
                Upload fieldValue = (Upload) field.getObjectValue();
                return (!fieldValue.getResources().isEmpty()) || (fieldValue.getTentative() != null);
            }

        };
    }

}
