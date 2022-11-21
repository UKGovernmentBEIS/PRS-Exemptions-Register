package com.northgateps.nds.cas.configuration;

import org.apereo.cas.configuration.model.support.ldap.LdapAuthenticationProperties;

/**
 * Extends the CAS LdapAuthenticationProperties with our own customisations. 
 * 
 * @see org.apereo.cas.configuration.model.support.ldap.LdapAuthenticationProperties
 */
public class NdsLdapAuthenticationProperties extends LdapAuthenticationProperties {
    
    /** 
     * The name of the tenant attribute to be put into the ticket sent to the client.  
     * Must match the client-side configuration.
     */
    private String tenantAttributeName;
  
    public String getTenantAttributeName() {
        return tenantAttributeName;
    }
    
    public void setTenantAttributeName(String tenantAttributeName) {
        this.tenantAttributeName = tenantAttributeName;
    }
}
