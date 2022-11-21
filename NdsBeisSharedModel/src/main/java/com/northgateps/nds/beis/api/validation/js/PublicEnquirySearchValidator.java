package com.northgateps.nds.beis.api.validation.js;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.northgateps.nds.platform.application.api.metadata.StringLengthFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.BaseValidationProperty;
import com.northgateps.nds.platform.application.api.validation.Field;
import com.northgateps.nds.platform.application.api.validation.SimpleValidationProperty;
import com.northgateps.nds.platform.application.api.validation.ValidationContext;
import com.northgateps.nds.platform.application.api.validation.js.AbstractFieldValidator;
import com.northgateps.nds.platform.application.api.validation.js.CustomValidator;
import com.northgateps.nds.platform.application.api.validation.js.FieldValidator;

public class PublicEnquirySearchValidator extends AbstractFieldValidator {

    public static final String DEFAULT_MESSAGE = "Validation_Field_must_be_a_valid_publicenquirysearch";

    /** Update the default message for this FieldValidator. */
    {
        message = DEFAULT_MESSAGE;
    }

    private String className = "invalidsearchusingwildcard";

    private int maxLength = 0;
    private int minLength = 0;

    public class DataProperties {

        public Object getMaxlength() {
            return maxLength;
        }

        public Object getMinlength() {
            return minLength;
        }
    };

    @Override
    public Object getDataProperties() {
        return new DataProperties();
    }

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
        if (annotation instanceof StringLengthFieldMetadata) {
            minLength = ((StringLengthFieldMetadata) annotation).minLength();
            maxLength = ((StringLengthFieldMetadata) annotation).maxLength();
            message = ((StringLengthFieldMetadata) annotation).invalidMessage();
        }
        super.setConfig(annotation);
    }

    @Override
    public List<BaseValidationProperty> getProperties() {
        List<BaseValidationProperty> properties = super.getProperties();
        properties.add(
                new SimpleValidationProperty().setParameter("minLength").setValue(String.valueOf(minLength), null));
        properties.add(
                new SimpleValidationProperty().setParameter("maxLength").setValue(String.valueOf(maxLength), null));
        return properties;
    }

    @Override
    public List<String> getAttrs() {
        return Arrays.asList(new String[] {});
    }

    /**
     * A server side validator that checks whether the search value contains a wildcard or not
     * 
     */
    @Override
    public CustomValidator getCustomValidator() {
        return new CustomValidator() {

            @Override
            public boolean validate(FieldValidator validator, Field field, ValidationContext<?> validationContext) {
                if (!(field.getValue() instanceof String)) {
                    return false;
                }

                String str = (String) field.getValue();
                int length = ((String) field.getValue()).length();
                int count = StringUtils.countMatches(str, "*");
                int lengthWithoutWildcard = length - count;
                
                if (count == 0) {
                    if (lengthWithoutWildcard > 0 && lengthWithoutWildcard < minLength) {
                        message = "Validation_Field_must_be_a_valid_publicenquirysearch_without_wildcard";
                        return false;
                    }


                } else if (lengthWithoutWildcard == 0
                        || (lengthWithoutWildcard > 0 && lengthWithoutWildcard < minLength)) {
                    return false;

                } 

                return true;

            }
        };
    }

}
