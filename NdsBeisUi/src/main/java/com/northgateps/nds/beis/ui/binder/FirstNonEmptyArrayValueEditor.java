package com.northgateps.nds.beis.ui.binder;

import java.beans.PropertyEditorSupport;

/** For dependent selects, the submitted values might be an Array of Strings, for example in non-JS mode.
 * To deal with this, we can assign FirstNonEmptyArrayValueEditor to the field, to take just the first
 * non-empty value. 
 */
public class FirstNonEmptyArrayValueEditor extends PropertyEditorSupport {
    /**
     * Converts a String to itself, and a String array to the first non empty string
     */
    @Override
    public void setValue(Object value) {
        if (value instanceof String[]) {
            for (String val : (String[])value) {
                if ((val != null) && (val.length() > 0)) {
                    super.setValue(val);
                    break;
                }
            }
        } else if (value instanceof String) {
            super.setValue(value);
        }
    }
    
    @Override
    public void setAsText(String text) throws java.lang.IllegalArgumentException {
        setValue(text);
    }
}