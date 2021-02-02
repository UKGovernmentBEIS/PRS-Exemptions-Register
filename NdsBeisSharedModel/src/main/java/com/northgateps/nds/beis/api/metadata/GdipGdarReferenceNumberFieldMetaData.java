package com.northgateps.nds.beis.api.metadata;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.northgateps.nds.platform.application.api.depend.FieldDependency;
import com.northgateps.nds.platform.application.api.format.js.FieldFormatter;
import com.northgateps.nds.platform.application.api.format.js.UpperCaseFieldFormatter;
import com.northgateps.nds.platform.application.api.metadata.FieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.FieldMetadataTagApplicator;
import com.northgateps.nds.platform.application.api.metadata.UserAgentEvent;
import com.northgateps.nds.platform.application.api.validation.js.AllElementsFilter;
import com.northgateps.nds.platform.application.api.validation.js.FieldValidator;
import com.northgateps.nds.platform.application.api.validation.js.FieldValidatorListFilter;
import com.northgateps.nds.platform.application.api.validation.js.ValidatorJoiner;
import com.northgateps.nds.platform.application.api.validation.js.VoidJoiner;

/**
  * Annotation to mark a field as a GDIP / GDAR filed field
 * 
 * @author david.lee
 */
@Inherited
@Retention(RUNTIME)
@Target({ FIELD })
@FieldMetadata
public @interface GdipGdarReferenceNumberFieldMetaData {

    public Class<? extends FieldFormatter> formatter() default UpperCaseFieldFormatter.class;

    public Class<? extends FieldValidator> validator() default GdipGdarReferenceNumberFieldValidator.class;

    public Class<? extends FieldValidatorListFilter> filter() default AllElementsFilter.class;

    public Class<? extends ValidatorJoiner> joiner() default VoidJoiner.class;

    public Class<? extends FieldMetadataTagApplicator> applicator() default GdipGdarFieldMetadataTagApplicator.class;

    public String invalidMessage() default GdipGdarReferenceNumberFieldValidator.DEFAULT_MESSAGE;

    public FieldDependency[] dependencies() default {};
    
    public UserAgentEvent[] userAgentValidateEvents() default {};

}
