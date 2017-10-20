package com.northgateps.nds.beis.api.metadata;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.northgateps.nds.platform.application.api.depend.FieldDependency;
import com.northgateps.nds.platform.application.api.format.js.FieldFormatter;
import com.northgateps.nds.platform.application.api.metadata.FieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.FieldMetadataTagApplicator;
import com.northgateps.nds.platform.application.api.validation.js.AllElementsFilter;
import com.northgateps.nds.platform.application.api.validation.js.FieldValidator;
import com.northgateps.nds.platform.application.api.validation.js.FieldValidatorListFilter;
import com.northgateps.nds.platform.application.api.validation.js.ValidatorJoiner;
import com.northgateps.nds.platform.application.api.validation.js.VoidJoiner;

/**
 * TODO:CR BEIS-73 this needs the corect comment, also is this the correct location of this class?
 * Annotation to mark a field as a National Insurance Number (NINO) field
 * 
 * @author yogesh.bhalerao
 */
@Inherited
@Retention(RUNTIME)
@Target({ FIELD })
@FieldMetadata
public @interface EPCReferenceNumberFieldMetadata {

    public Class<? extends FieldFormatter> formatter() default EPCReferenceNumberFieldFormatter.class;

    public Class<? extends FieldValidator> validator() default EPCReferenceNumberFieldValidator.class;

    public Class<? extends FieldValidatorListFilter> filter() default AllElementsFilter.class;

    public Class<? extends ValidatorJoiner> joiner() default VoidJoiner.class;

    public Class<? extends FieldMetadataTagApplicator> applicator() default EPCReferenceNumberFieldMetadataTagApplicator.class;

    public String invalidMessage() default EPCReferenceNumberFieldValidator.DEFAULT_MESSAGE;

    public FieldDependency[] dependencies() default {};
}
