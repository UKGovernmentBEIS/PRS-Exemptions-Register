package com.northgateps.nds.beis.api.getpartiallyregistereddetails;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.api.NdsRequest;

/**
 * 
 * Request to get details of a partially registered account.
 * At time of writing:
 * This is an account that does not have an associated 
 * FOUNDATION_LAYER_PARTY_SERVICE uid in LDAP 
 * to match with the back office. e.g. 1234 (no hyphens)
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetPartiallyRegisteredDetailsNdsRequest", propOrder = { "username", "tenant"})
@XmlRootElement(name = "GetPartiallyRegisteredDetailsNdsRequest")
public class GetPartiallyRegisteredDetailsNdsRequest implements NdsRequest {

    protected String username;
    
    protected String tenant;
    
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
    
}
