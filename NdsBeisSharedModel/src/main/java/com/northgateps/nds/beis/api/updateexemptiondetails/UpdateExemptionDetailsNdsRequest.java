package com.northgateps.nds.beis.api.updateexemptiondetails;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.time.ZonedDateTime;

import com.northgateps.nds.beis.api.UpdateExemptionDetails;
import com.northgateps.nds.platform.api.NdsRequest;

/**
 * Request to update exemption details
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateExemptionDetailsNdsRequest", propOrder = { "updateExemptionDetails","accountId","userName","tenant" })
@XmlRootElement(name = "UpdateExemptionDetailsNdsRequest")
public class UpdateExemptionDetailsNdsRequest implements NdsRequest {

    private UpdateExemptionDetails updateExemptionDetails;  
   
    private String accountId;
    
    private String userName;
    private String tenant;


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public UpdateExemptionDetails getUpdateExemptionDetails() {
        return updateExemptionDetails;
    }

    
    public void setUpdateExemptionDetails(UpdateExemptionDetails updateExemptionDetails) {
        this.updateExemptionDetails = updateExemptionDetails;
    }

    
}
