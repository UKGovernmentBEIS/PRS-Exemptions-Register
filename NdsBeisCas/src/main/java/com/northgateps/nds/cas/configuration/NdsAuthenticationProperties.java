package com.northgateps.nds.cas.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Mirrors the CAS AuthenticationProperties without the maintenance overhead of needing
 * to keep in sync with CAS during upgrades.
 */
public class NdsAuthenticationProperties {

    @NestedConfigurationProperty
    private List<NdsLdapAuthenticationProperties> ldap = new ArrayList<>();
    
    public List<NdsLdapAuthenticationProperties> getLdap() {
        return ldap;
    }

    public void setLdap(final List<NdsLdapAuthenticationProperties> ldap) {
        this.ldap = ldap;
    }
}
