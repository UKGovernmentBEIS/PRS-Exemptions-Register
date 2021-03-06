package com.northgateps.nds.beis.api.metadata;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import com.northgateps.nds.platform.application.api.validation.ValidationContext;
import com.northgateps.nds.platform.application.api.validation.js.AbstractFieldValidator;
import com.northgateps.nds.platform.util.scripts.ResourceLoader;
import com.northgateps.nds.platform.util.scripts.ScriptsLoader;

/**
 * 
 * TODO:CR BEIS-73 this needs the corect class comment, also is this the correct location of this class?
 *
 */
public class EPCReferenceNumberFieldValidator extends AbstractFieldValidator {

    public static final String DEFAULT_MESSAGE = "Validation_Field_must_be_a_EPCREFERENCENUMBER";

    /** Update the default message for this FieldValidator. */
    {
        message = DEFAULT_MESSAGE;
    }

    private String className = "invalidepcref";

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

        if (annotation instanceof EPCReferenceNumberFieldMetadata) {
            message = ((EPCReferenceNumberFieldMetadata) annotation).invalidMessage();
        }
        super.setConfig(annotation);
    }

    @Override
    public List<String> getAttrs() {
        return Arrays.asList(new String[] {});
    }

    @Override
    public String getTestFn(ValidationContext<?> validationContext) {
        ResourceLoader scriptsLoader = validationContext != null ? validationContext.getScriptsLoader() : new ScriptsLoader();
        String script = "var document = this;\n" + scriptsLoader.load("metadata.beis.js") +
                        "\nvar fn = document.validationFunctions['" + this.getTypeName(validationContext) + "'];";
        return script;   	
    	
    }

    @Override
    public String getScriptResourceName() {
        return "metadata.beis.js";
    }
}
