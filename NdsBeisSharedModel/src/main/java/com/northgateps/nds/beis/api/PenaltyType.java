package com.northgateps.nds.beis.api;

public interface PenaltyType {
    
    String getService();
    
    void setService(String service);
    
    String getCode();
    
    void setCode(String code);
    
    String getDescription();
    
    void setDescription(String description);
    
    String getPwsBreachDescription();
    
    void setPwsBreachDescription(String pwsBreachDescription);
    
    String getMaxValue();
    
    void setMaxValue(String maxValue);
    
    String getExemptionRequired();
    
    void setExemptionRequired(String exemptionRequired);

}
