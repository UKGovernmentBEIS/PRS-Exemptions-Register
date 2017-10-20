package com.northgateps.nds.beis.api.retrieveregistereddetails;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.NdsRequest;
import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;

/**
 * Request to retrieve registered user details of a user.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrieveRegisteredDetailsNdsRequest", propOrder = { "username", "tenant","accountId" })
@XmlRootElement(name = "RetrieveRegisteredDetailsNdsRequest")
public class RetrieveRegisteredDetailsNdsRequest implements NdsRequest {

   
    protected String username;
    
    @RequiredFieldMetadata
    protected String tenant;
    
    /**
     * For BEIS this is the PartyRef from the foundation layer and currently is held in LDAP
     *                     as the uid against the FOUNDATION_LAYER_PARTY_SERVICE against each user.
     */
    private String accountId;


    
    public String getTenant() {
        return tenant;
    }

    
    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
   
    public String getAccountId() {
        return accountId;
    }

  
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

}