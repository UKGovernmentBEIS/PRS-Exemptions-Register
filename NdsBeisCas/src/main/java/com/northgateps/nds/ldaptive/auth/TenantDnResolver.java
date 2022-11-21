package com.northgateps.nds.ldaptive.auth;

import org.ldaptive.LdapAttribute;
import org.ldaptive.LdapException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extends FormatDnResolver to returns the NDS user DN based on two arguments (user and tenant) rather
 * than just one (user).  The two arguments will populate two placeholder values in the format string 
 * (eg ldap.authn.format=userid=%s,ou=Users,ou=%s,ou=Tenants,ou=NDS,dc=northgateps,dc=com)
 */
public class TenantDnResolver extends org.ldaptive.auth.FormatDnResolver {

    /** log for this class. */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public TenantDnResolver(final String format) {
        super(format);
    }

    /**
     * Returns a DN for the supplied user by applying it to a format string.
     *
     * @param user - to format dn for
     * @param tenant - to format dn for
     * @return user DN
     *
     * @throws LdapException never
     */
    public String resolve(final String user, final String tenant) throws LdapException {
        String dn = null;
        
        if (user == null || "".equals(user) || tenant == null || "".equals(tenant)) {
            logger.debug("User input was empty or null");
        } else {
            
            // escape input
            final String escapedUser = getEscapeUser() ? LdapAttribute.escapeValue(user) : user;
            final String escapedTenant = getEscapeUser() ? LdapAttribute.escapeValue(tenant) : user;
            final String format = getFormat();
                    
            logger.debug("Formatting DN for {} with {}", escapedUser, format);
            dn = String.format(format, escapedUser, escapedTenant);
        }
        
        return dn;
    }
}
