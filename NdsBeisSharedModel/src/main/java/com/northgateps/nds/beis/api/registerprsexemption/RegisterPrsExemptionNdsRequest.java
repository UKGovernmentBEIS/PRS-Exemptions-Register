package com.northgateps.nds.beis.api.registerprsexemption;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.NdsRequest;

/**
 * Request to register PRS Exemption
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisterPrsExemptionNdsRequest", propOrder = { "userName", "tenant", "accountId", "emailAddress",
        "registerPrsExemptionDetails","pwsText","maxPenaltyValue" })
@XmlRootElement(name = "RegisterPrsExemptionNdsRequest")
public class RegisterPrsExemptionNdsRequest implements NdsRequest {

    private String userName;
    private String tenant;
    private String accountId;
    private String emailAddress;
    private String pwsText;
    private RegisterPrsExemptionDetails registerPrsExemptionDetails;
    private String maxPenaltyValue;

    
    public String getMaxPenaltyValue() {
        return maxPenaltyValue;
    }

    
    public void setMaxPenaltyValue(String maxPenaltyValue) {
        this.maxPenaltyValue = maxPenaltyValue;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public RegisterPrsExemptionDetails getRegisterPrsExemptionDetails() {
        return registerPrsExemptionDetails;
    }

    public void setRegisterPrsExemptionDetails(RegisterPrsExemptionDetails registerPrsExemptionDetails) {
        this.registerPrsExemptionDetails = registerPrsExemptionDetails;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    
    public String getPwsText() {
        return pwsText;
    }

    
    public void setPwsText(String pwsText) {
        this.pwsText = pwsText;
    }
}
