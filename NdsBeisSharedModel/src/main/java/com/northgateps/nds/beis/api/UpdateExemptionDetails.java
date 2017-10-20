package com.northgateps.nds.beis.api;

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;
/**
 * Class to contain updated exemption information 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateExemptionDetails", propOrder = { "referenceId", "endDate" })
@XmlRootElement(name = "UpdateExemptionDetails")
public class UpdateExemptionDetails extends AbstractValidatableModel {   
    
    private String referenceId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime endDate;
    
    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    
}
