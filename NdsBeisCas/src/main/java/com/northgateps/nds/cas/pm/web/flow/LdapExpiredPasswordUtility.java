package com.northgateps.nds.cas.pm.web.flow;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ldaptive.AttributeModification;
import org.ldaptive.ConnectionFactory;
import org.ldaptive.LdapAttribute;
import org.ldaptive.LdapEntry;
import org.ldaptive.LdapException;
import org.ldaptive.ModifyOperation;
import org.ldaptive.ModifyRequest;
import org.ldaptive.SearchOperation;
import org.ldaptive.SearchRequest;
import org.ldaptive.SearchScope;

import com.northgateps.nds.cas.configuration.NdsConfigurationProperties;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.userattributesgetter.UserAttributesGetter;

/**
 * Utility class to retrieve user attributes or policy handling attribute
 * from Ldap to check rules of expired password functionality. 
 */
public class LdapExpiredPasswordUtility implements UserAttributesGetter {
    
    private static final NdsLogger logger = NdsLogger.getLogger(LdapExpiredPasswordUtility.class);   
     
    private final ConnectionFactory connection;
    private final NdsConfigurationProperties ndsProperties;    
    
    LdapExpiredPasswordUtility(NdsConfigurationProperties ndsProperties, ConnectionFactory connection) {
        this.ndsProperties = ndsProperties;
        this.connection = connection;
    }
    
    
    /**
     * Get map of the entry's attributes and values
     */
    @Override
    public Map<String, String> userAttributes(String userDn) {
        Map<String, String> attributeIdsAndValues = new HashMap<>();  
        Iterator<LdapEntry> it = null;
        
        try {
            it = fetch(connection, userDn, "(objectclass=*)", SearchScope.OBJECT, ndsProperties.getAccountLock(), ndsProperties.getPwdPolicySubentry());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | LdapException e) {
            logger.error(e.getMessage());
        } 
        
        if (it.hasNext()) {
            final LdapEntry userEntry = it.next();        
            convertToMap(attributeIdsAndValues, userEntry);
        }        
        return attributeIdsAndValues;
    }
    
    @Override
    public String getPasswordPolicySubEntryAttribute() {        
        return ndsProperties.getPwdPolicySubentry();
    }    
    
    /**
     * Retrieve user entry from ldap
     * @param connection- the connection to use for the source
     * @param baseDn-dn
     * @param searchString-search criteria including attribute and attribute value to use for searching
     * @param searchScope-type of search scope
     * @param typesOfAttributes-attributes required to be returned
     * @return LdapEntryWithAttributesValues 
     */
    protected Iterator<LdapEntry> fetch(ConnectionFactory connection, String baseDn, String searchString, SearchScope searchScope, String... typesOfAttributes)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, LdapException {
        
        final SearchOperation searchOperation = new SearchOperation(connection);
        SearchRequest request = new SearchRequest(baseDn, searchString, typesOfAttributes);
        request.setSearchScope(searchScope);
        return searchOperation.execute(request).getEntries().iterator();
    }
    
    /**
     * Modifies or adds changes to the specified ldap entry 
     */    
    protected void update(String baseDn, Map<String, String> changes) {            
        Iterator<LdapEntry> entries;
        try {            
            entries = fetch(connection, baseDn, "(objectclass=*)", SearchScope.OBJECT, ndsProperties.getAccountLock(), ndsProperties.getPwdPolicySubentry());
            ModifyOperation modify = new ModifyOperation(connection);          
            modify.execute(setAttributes(baseDn, changes, entries.next()));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | LdapException e) {
            logger.error(e.getMessage());
        } 
    }
    
    /**
     * Create ModifyRequest to modify attribute
     * @param baseDn - the user's location in LDAP
     * @param source - the source ldap entry to modify
     * @param changes- the changes to apply
     * @return- ModifyRequest
     */
    protected ModifyRequest setAttributes(String baseDn, Map<String, String> changes, LdapEntry source) {
        List<AttributeModification> actions = new ArrayList<>();        
        for (String key : changes.keySet()) {
            
            final String value = changes.get(key);

            /* If the key already exists then update or delete it,
             * if it doesn't then add it, unless the value is null, in which case it can be ignored. */
            if (source.getAttribute(key) != null) {
                if (value == null) {
                    actions.add(new AttributeModification(AttributeModification.Type.DELETE, new LdapAttribute(key)));
                } else {
                    actions.add(new AttributeModification(AttributeModification.Type.REPLACE,
                            new LdapAttribute(key, value)));
                }
            } else if (value != null) {
                actions.add(new AttributeModification(AttributeModification.Type.ADD, new LdapAttribute(key, value)));
            }
        }
       return  new ModifyRequest(baseDn,actions.toArray(new AttributeModification[] {}));   
    }
    
    /**
     * Extracts attributes from an entry.
     */
    public void convertToMap(Map<String, String> attributeIdsAndValues, LdapEntry entry) {
        Collection<LdapAttribute> attributes = entry.getAttributes();

        for (LdapAttribute attribute : attributes) {

            if (!attribute.isBinary()) {
                attributeIdsAndValues.put(attribute.getName(false), attribute.getStringValue());
            }
        }
    }
    
}
