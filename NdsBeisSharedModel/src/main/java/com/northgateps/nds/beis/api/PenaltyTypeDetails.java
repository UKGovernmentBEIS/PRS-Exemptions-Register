package com.northgateps.nds.beis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;

/**
 * Class to contain all the information for an exemption type
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PenaltyTypeDetails", propOrder = { "code", "service","description", "pwsBreachDescription", "maxValue",
        "exemptionRequired"})
public class PenaltyTypeDetails extends AbstractValidatableModel
implements PenaltyType {
    
    private String code;
    private String service;
    private String description;
    private String pwsBreachDescription;
    private String maxValue;
    private String exemptionRequired;
    
    @Override
    public String getService() {
        return service;
    }
    
    @Override
    public void setService(String service) {
        this.service = service;
    }
    
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public void setCode(String code) {
        this.code = code;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String getPwsBreachDescription() {
        return pwsBreachDescription;
    }
    
    @Override
    public void setPwsBreachDescription(String pwsBreachDescription) {
        this.pwsBreachDescription = pwsBreachDescription;
    }
    
    @Override
    public String getMaxValue() {
        return maxValue;
    }
    
    @Override
    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }
    
    @Override
    public String getExemptionRequired() {
        return exemptionRequired;
    }
    
    @Override
    public void setExemptionRequired(String exemptionRequired) {
        this.exemptionRequired = exemptionRequired;
    }

   
}
