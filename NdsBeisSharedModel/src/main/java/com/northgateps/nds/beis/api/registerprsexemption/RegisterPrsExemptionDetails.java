package com.northgateps.nds.beis.api.registerprsexemption;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.beis.api.ExemptionDetail;
/**
 * Class to contain exemption information
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExemptionDetail", propOrder = { "exemptionDetails" })
public class RegisterPrsExemptionDetails implements Serializable {
    private ExemptionDetail exemptionDetails;

    
    public ExemptionDetail getExemptionDetails() {
        return exemptionDetails;
    }

    
    public void setExemptionDetails(ExemptionDetail exemptionDetails) {
        this.exemptionDetails = exemptionDetails;
    }
}
