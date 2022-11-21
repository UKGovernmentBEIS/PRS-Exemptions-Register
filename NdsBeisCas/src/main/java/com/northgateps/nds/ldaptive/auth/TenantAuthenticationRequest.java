package com.northgateps.nds.ldaptive.auth;

import java.util.Arrays;

import org.ldaptive.Credential;
import org.ldaptive.auth.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request for authentication that includes a tenant.
 */
public class TenantAuthenticationRequest extends org.ldaptive.auth.AuthenticationRequest {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    /** Tenant identifier. */
    private String tenant;

    /** Default constructor. */
    public TenantAuthenticationRequest() {
    }

    /**
     * Creates a new authentication request.
     *
     * @param id - identifies the user
     * @param tenant - identifies the tenant that the user is part of
     * @param c - credential to authenticate the user
     * @param attrs - attributes to return
     */
    public TenantAuthenticationRequest(final String id, final String tenant, final Credential c, final String... attrs) {
        setUser(new User(id));
        setTenant(tenant);
        setCredential(c);
        setReturnAttributes(attrs);
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(final String tenant) {
        this.tenant = tenant;
    }

    @Override
    public String toString() {
        return String.format("[%s@%d::user=%s@%s, retAttrs=%s]", getClass().getName(), hashCode(), getUser(), tenant, 
                Arrays.toString(getReturnAttributes()));
    }
}
