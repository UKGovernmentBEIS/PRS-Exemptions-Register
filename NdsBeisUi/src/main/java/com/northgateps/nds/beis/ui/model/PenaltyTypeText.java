package com.northgateps.nds.beis.ui.model;

import java.io.Serializable;

import com.northgateps.nds.beis.api.PenaltyType;
import com.northgateps.nds.platform.ui.model.ReferenceValue;

/**
 * A single value from a list of penalty type text values.
 */
public class PenaltyTypeText extends ReferenceValue implements PenaltyType, Serializable {

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
