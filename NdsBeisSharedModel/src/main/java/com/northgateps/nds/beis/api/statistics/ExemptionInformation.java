package com.northgateps.nds.beis.api.statistics;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Exemption details 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExemptionInformation", propOrder = { "ExemptionsReferences","ExemptionType"})        
@XmlRootElement(name = "ExemptionInformation")
public class ExemptionInformation {
    
    private String ExemptionsReferences;  
    private String ExemptionType;
    
    public String getExemptionsReferences() {
        return ExemptionsReferences;
    }
    public void setExemptionsReferences(String exemptionsReferences) {
        ExemptionsReferences = exemptionsReferences;
    }
    public String getExemptionType() {
        return ExemptionType;
    }
    public void setExemptionType(String exemptionType) {
        ExemptionType = exemptionType;
    }
}
