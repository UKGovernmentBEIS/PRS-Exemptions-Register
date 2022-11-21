package com.northgateps.nds.ldaptive.auth;

import org.ldaptive.LdapException;
import org.ldaptive.auth.AuthenticationHandler;
import org.ldaptive.auth.AuthenticationResponse;
import org.ldaptive.auth.DnResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Authenticates tenant users against an NDS LDAP directory.
 * Must be configured with a TenantDnResolver.
 */
public class TenantAuthenticator extends org.ldaptive.auth.Authenticator {

    /** Logger for this class. */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public TenantAuthenticator(final DnResolver resolver, final AuthenticationHandler handler) {
        super(resolver, handler);
    }
    
    /**
     * This will attempt to find the DN for the supplied user. {@link DnResolver#resolve(String)} is invoked to perform
     * this operation.
     *
     * @param user to find DN for
     * @param tenant - to find DN for 
     * @return user DN
     * @throws LdapException if an LDAP error occurs during resolution
     */
    public String resolveDn(final String user, String tenant) throws LdapException {
        return ((TenantDnResolver)getDnResolver()).resolve(user, tenant);
    }

    /**
     * Authenticate the user in the supplied request.
     * @param request authentication request
     * @return response containing the ldap entry of the user authenticated
     * @throws LdapException if an LDAP error occurs
     */
    public AuthenticationResponse authenticate(final TenantAuthenticationRequest request) throws LdapException {
        AuthenticationResponse response = authenticate(resolveDn(request.getUser().getIdentifier(), request.getTenant()), request);
        if (response != null) {
        	logger.info("  Authentication result code: " + response.getResultCode());
        }
        return response;
    }
    
}
