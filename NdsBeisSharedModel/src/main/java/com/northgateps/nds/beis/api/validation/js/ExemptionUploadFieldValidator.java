package com.northgateps.nds.beis.api.validation.js;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.northgateps.nds.platform.api.Upload;
import com.northgateps.nds.platform.api.model.utilities.ModelField;
import com.northgateps.nds.platform.application.api.metadata.GenericFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.BaseValidationProperty;
import com.northgateps.nds.platform.application.api.validation.Field;
import com.northgateps.nds.platform.application.api.validation.ValidationContext;
import com.northgateps.nds.platform.application.api.validation.js.AbstractFieldValidator;
import com.northgateps.nds.platform.application.api.validation.js.CustomValidator;
import com.northgateps.nds.platform.application.api.validation.js.FieldValidator;

/*
 *  Class to handle validation of Exemption upload file
 */
public class ExemptionUploadFieldValidator extends AbstractFieldValidator {

    public static final String DEFAULT_MESSAGE = "Validation_Field_An_Uploaded_File_Is_Required";

    /** Update the default message for this FieldValidator. */
    {
        message = DEFAULT_MESSAGE;
    }

    private String className = "invalidupload";
    private String minDocumentPath;

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getMessage() {
        return message;
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

    @Override
    public void setConfig(Annotation annotation) {
        if (annotation instanceof GenericFieldMetadata) {
            minDocumentPath = ((GenericFieldMetadata) annotation).dependencies()[0].path();
        }
        super.setConfig(annotation);
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
                int minDocumentValue = Integer.parseInt(
                        (String) new ModelField().setPath(minDocumentPath).getValue(validationContext.getModel()));
                Upload fieldValue = (Upload) field.getObjectValue();
                return ((fieldValue.getResources().size() >= minDocumentValue) || (fieldValue.getTentative() != null));
            }

        };
    }

}
