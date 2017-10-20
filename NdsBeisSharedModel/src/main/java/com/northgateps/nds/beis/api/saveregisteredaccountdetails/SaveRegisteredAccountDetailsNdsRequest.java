package com.northgateps.nds.beis.api.saveregisteredaccountdetails;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.beis.api.BeisPartyRequest;
import com.northgateps.nds.beis.api.UpdateBeisRegistrationDetails;
import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.StringLengthFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.UserNameFieldMetadata;

/**
 * Request to save registered account details.
 * Extends the BeisPartyRequest to get tenant and user name and enforce
 * NdsRequest interface which is used in platform adapters to identify request classes
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SaveRegisteredAccountDetailsNdsRequest", propOrder = {"tenant", "username", "registeredAccountDetails", "partiallyRegistered","publicWebAddress","emailTemplateName"})
@XmlRootElement(name = "SaveRegisteredAccountDetailsNdsRequest")
public class SaveRegisteredAccountDetailsNdsRequest implements BeisPartyRequest {

    @RequiredFieldMetadata
    protected String tenant;  
    
    @UserNameFieldMetadata
    @RequiredFieldMetadata
    @StringLengthFieldMetadata(maxLength = 100)
    protected String username;

    protected UpdateBeisRegistrationDetails registeredAccountDetails;

    protected Boolean partiallyRegistered;
    
    protected String publicWebAddress;
    
    protected String emailTemplateName;

    
    public String getEmailTemplateName() {
        return emailTemplateName;
    }

    
    public void setEmailTemplateName(String emailTemplateName) {
        this.emailTemplateName = emailTemplateName;
    }
    
    public UpdateBeisRegistrationDetails getRegisteredAccountDetails() {
        return registeredAccountDetails;
    }

    public void setRegisteredAccountDetails(UpdateBeisRegistrationDetails registeredAccountDetails) {
        this.registeredAccountDetails = registeredAccountDetails;
    }

    @Override
    public String getTenant() {
        return tenant;
    }

    @Override
    public void setTenant(String tenant) {
        this.tenant = tenant;        
    }
    
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;        
    }    
    
    public Boolean getPartiallyRegistered() {
        return partiallyRegistered;
    }

    public void setPartiallyRegistered(Boolean partiallyRegistered) {
        this.partiallyRegistered = partiallyRegistered;
    }

    
    public String getPublicWebAddress() {
        return publicWebAddress;
    }

    
    public void setPublicWebAddress(String publicWebAddress) {
        this.publicWebAddress = publicWebAddress;
    }
	
}