
package com.northgateps.nds.beis.api.getreferencevalues;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
/**
 * Class to contain Translation language and text
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Translation", propOrder = { "language", "text" })
public class Translation {

    private String language;
    private String text;

    /**
     * Gets the value of the language property.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *            allowed object is
     *            {@link String }
     * 
     */
    public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *         possible object is
     *         {@link String }
     * 
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *            allowed object is
     *            {@link String }
     * 
     */
    public void setText(String value) {
        this.text = value;
    }
}
