package com.northgateps.nds.cas.authentication;

import java.io.Serializable;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apereo.cas.authentication.credential.RememberMeUsernamePasswordCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Gets username, password and tenant from the UI (ie CAS login screen via Spring Webflow) for NDS login.
 * (@see NdsDefaultWebflowConfig)
 * 
 * If the tenant is missing then defaults to the defaultTenant which in this version is hard coded to BEIS.
 */
public class UsernamePasswordTenantCredential extends RememberMeUsernamePasswordCredential implements Serializable {
    private static final long serialVersionUID = 202110061418000000L;
    private static final String defaultTenant = "BEIS";
    
    @Size(min=1, message = "required.tenant")
    private String tenant;
    
    /** Default constructor. */
    public UsernamePasswordTenantCredential() {
        super();
        this.tenant = defaultTenant;
    }
    
    /**
     * Creates a new instance with the given username, password and tenant.
     *
     * @param userName Non-null user name.
     * @param password Non-null password.
     * @param tenant Non-null tenant.
     */
    public UsernamePasswordTenantCredential(final String userName, final String password, final String tenant) {
        setUsername(userName);
        setPassword(password.toCharArray());
        
        if (tenant == null) {
        	this.tenant = defaultTenant;
        } else {
        	this.tenant = tenant;
        }
    }

    public final String getTenant() {
        return this.tenant;
    }

    public final void setTenant(final String tenant) {
        this.tenant = tenant;
    }

    @Override
    public String toString() {
        return getUsername() + "@" + this.tenant;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final UsernamePasswordTenantCredential that = (UsernamePasswordTenantCredential) o;

        if (tenant != null ? !tenant.equals(that.tenant) : that.tenant != null) {
            return false;
        }

        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(tenant)
                .append(super.hashCode())
                .toHashCode();
    }
    
    /**
     * @return username@tenant or else the empty string
     */
    @Override
    public String getId() {
        if (getUsername() != null) {
            return getUsername() + "@" + this.tenant;
        }
        
        return "";
    }
}