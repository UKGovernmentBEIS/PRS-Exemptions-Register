package com.northgateps.nds.beis.api.metadata;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.northgateps.nds.platform.application.api.metadata.AbstractFieldMetadataTagApplicator;
import com.northgateps.nds.platform.application.api.validation.ValidationContext;
import com.northgateps.nds.platform.application.api.validation.ValidationEvaluator;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Add pattern and title attribute to html input control
 * 
 */

public class EPCReferenceNumberFieldMetadataTagApplicator extends AbstractFieldMetadataTagApplicator {

    private static final NdsLogger logger = NdsLogger.getLogger(EPCReferenceNumberFieldMetadataTagApplicator.class);

    private String message = EPCReferenceNumberFieldValidator.DEFAULT_MESSAGE;

    /**
     * Implement HTML5 "pattern" and "title" attribute.
     */
    @Override
    public void apply(Object tag, ValidationContext<?> validationContext) {
        try {
            Method m = tag.getClass().getMethod("setPattern", String.class);
            m.invoke(tag, ValidationEvaluator.getRegEx(new EPCReferenceNumberFieldValidator(), validationContext));
        } catch (NoSuchMethodException e) {
            // There may be no such method, it's not an error. Just do nothing
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Falied to set pattern property of tag", e);
        }
        try {
            Method m = tag.getClass().getMethod("setTitle", String.class);
            m.invoke(tag, message);
        } catch (NoSuchMethodException e) {
            // There may be no such method, it's not an error. Just do nothing
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Falied to set title property of tag", e);
        }
    }

}