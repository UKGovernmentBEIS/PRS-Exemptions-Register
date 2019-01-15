package com.northgateps.nds.beis.esb;

import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ldaptive.AttributeModification;
import org.ldaptive.AttributeModificationType;
import org.ldaptive.Connection;
import org.ldaptive.DeleteRequest;
import org.ldaptive.LdapAttribute;
import org.ldaptive.LdapEntry;
import org.ldaptive.LdapException;
import org.ldaptive.ModifyRequest;
import org.ldaptive.ResultCode;
import org.ldaptive.SearchScope;

import com.northgateps.nds.platform.esb.directory.ex.DirectoryException;

/*
 * This mock replaces the normal communications with a real LDAP server, with stand-in functionality.
 * 
 * The adapter sets the initial ndsPasswordResetCode and the email address of the given user from LDAP, so here the kernal fetch operation is
 * mocked, the dn of the user and the required attribute from LDAP are checked, and suitable values provided 
 * back to the adapter.
 * 
 * It also updates the ndsPasswordResetCode, so that update is mocked.
 */
public class MockNdsDirectoryKernel extends MockDirectoryKernel {
    
 private Map<String, Collection<LdapAttribute>> accounts = new HashMap<String, Collection<LdapAttribute>>();
    
    public MockNdsDirectoryKernel() {} 
    
       
    @Override
    protected Iterator<LdapEntry> fetch(Connection connection, String baseDn, String searchString, SearchScope searchScope, String... typesOfAttributes)
            throws DirectoryException, InstantiationException, IllegalAccessException, IllegalArgumentException, 
                   InvocationTargetException, NoSuchMethodException, SecurityException, LdapException { 
        
        List<LdapEntry> entries = new ArrayList<LdapEntry>();
        
        Pattern pattern = Pattern.compile("^\\(ndsActivationCode=([^\\)]+)\\)$");
        
        // Look for an account by its activation code.
        if ((searchString != null) && 
                pattern.matcher(searchString).find() &&
                 SearchScope.SUBTREE.equals(searchScope)) {
            
            Matcher matcher = pattern.matcher(searchString);
            matcher.find();
            for (String accountKey : accounts.keySet()) {
                Collection<LdapAttribute> attributes = accounts.get(accountKey);
                for (Iterator<LdapAttribute> attributeIter = attributes.iterator() ; attributeIter.hasNext(); ) {
                    LdapAttribute attribute = attributeIter.next();
                    if ("ndsActivationCode".equals(attribute.getName())) {
                        if (attribute.getStringValue().equals(matcher.group(1))) {
                            LdapEntry ldapEntry = new LdapEntry();
                            ldapEntry.addAttributes(attributes);
                            ldapEntry.setDn(accountKey);
                            entries.add(ldapEntry);
                        }
                    }
                }
            }
            return entries.iterator();
        }
        
        if (baseDn.equals("uid=1366,ou=Users,ou=BEIS,ou=Tenants,ou=NDS,dc=northgateps,dc=com")) {
            LdapEntry ldapEntry = new LdapEntry();
            ldapEntry.addAttributes(accounts.values().iterator().next());
            ldapEntry.setDn(accounts.keySet().iterator().next());
            entries.add(ldapEntry);
            add(connection, baseDn, accounts.values().iterator().next());
            return entries.iterator();
        }
        
        
        if (SearchScope.ONELEVEL.equals(searchScope)) {
            return entries.iterator();
        }
        
        Map<String, Collection<LdapAttribute>>services = getServices(accounts);
        if ((! accounts.containsKey(baseDn)) && (! services.containsKey(baseDn)) && ! "uid=template_nds_username,ou=TemplateUsers,uid=BEIS,ou=Tenants,dc=northgateps,dc=com".equals(baseDn)) {
            userNotFound(baseDn);
        }
        
        if (accounts.containsKey(baseDn)) {
            LdapEntry ldapEntry = new LdapEntry();
            ldapEntry.addAttributes(accounts.get(baseDn));
            ldapEntry.setDn(baseDn);
            entries.add(ldapEntry);
        } else if (services.containsKey(baseDn)) {
            LdapEntry ldapEntry = new LdapEntry();
            ldapEntry.addAttributes(services.get(baseDn));
            ldapEntry.setDn(baseDn);
            entries.add(ldapEntry);
        } else if ( "uid=template_nds_username,ou=TemplateUsers,uid=BEIS,ou=Tenants,dc=northgateps,dc=com".equals(baseDn)) {
            LdapEntry ldapEntry = new LdapEntry() {
                @Override
                public Collection<LdapAttribute> getAttributes() {
                    List<LdapAttribute> attributes = new ArrayList<LdapAttribute>();

                    // Mock the get objectClass attribute 
                    if (Arrays.asList(typesOfAttributes).contains("objectClass") || Arrays.asList(typesOfAttributes).contains("*")) {
                        LdapAttribute ldapAttribute = new LdapAttribute();
                        ldapAttribute.setName("objectClass");
                        ldapAttribute.addStringValue("ndsUser");
                        attributes.add(ldapAttribute);
                    }
                    // Mock the get pwdAccountLockedTime attribute 
                    if (Arrays.asList(typesOfAttributes).contains("pwdAccountLockedTime") || Arrays.asList(typesOfAttributes).contains("*")) {
                        LdapAttribute ldapAttribute = new LdapAttribute();
                        ldapAttribute.setName("pwdAccountLockedTime");
                        ldapAttribute.addStringValue("unset");
                        attributes.add(ldapAttribute);
                    }
                    // Mock the get pwdAccountLockedTime attribute 
                    if (Arrays.asList(typesOfAttributes).contains("pwdPolicySubentry") || Arrays.asList(typesOfAttributes).contains("*")) {
                        LdapAttribute ldapAttribute = new LdapAttribute();
                        ldapAttribute.setName("pwdPolicySubentry");
                        ldapAttribute.addStringValue("test=current");
                        attributes.add(ldapAttribute);
                    }
                    
                    attributes.addAll(super.getAttributes());
                    
                    return attributes;
                };
                
                @Override
                public LdapAttribute getAttribute(final String name) {
                    
                    if (name != null) {
                        Collection<LdapAttribute> attributes = getAttributes();
                        
                        for (Iterator<LdapAttribute> attributeIter = attributes.iterator() ; attributeIter.hasNext() ; ) {
                            LdapAttribute attribute = attributeIter.next();
                            if (name.equals(attribute.getName())) {
                                return attribute;
                            }
                        }
                    }
                    return null;
                }
            };
            ldapEntry.setDn(baseDn);
            entries.add(ldapEntry);
        }
        return entries.iterator();
    }
    
    private Map<String, Collection<LdapAttribute>> getServices(Map<String, Collection<LdapAttribute>> accounts) {
        
        LdapAttribute serviceNameLdapAttribute = new LdapAttribute();
        serviceNameLdapAttribute.setName("cn");
        serviceNameLdapAttribute.addStringValue("FOUNDATION_LAYER_PARTY_SERVICE");
        Map<String, Collection<LdapAttribute>> services = new HashMap<String, Collection<LdapAttribute>>(); 
        for (String accountKey : accounts.keySet()) {
            String serviceKey = "cn=FOUNDATION_LAYER_PARTY_SERVICE," + accountKey;
            Collection<LdapAttribute> attributes = accounts.get(accountKey);
            List<LdapAttribute> attributeList = new ArrayList<LdapAttribute>();
            for (LdapAttribute attribute : attributes) {
                if ("cn".equals(attribute.getName())) {
                    attributeList.add(serviceNameLdapAttribute);
                } else { 
                    attributeList.add(attribute);
                }
            }
            services.put(serviceKey, attributeList);
        }
        return services;
    }
    
    // Mock the add account 
    @Override
    protected void add(Connection connection, String dn, Collection<LdapAttribute> attributes) throws LdapException {
        // check the password validity
        for (Iterator<LdapAttribute> attributeIter = attributes.iterator() ; attributeIter.hasNext() ; ) {
            LdapAttribute attribute = attributeIter.next();
            if ("userPassword".equals(attribute.getName())) {
                if (attribute.getStringValue().length() < 10) {
                    throw new LdapException("javax.naming.directory.InvalidAttributeValueException: [LDAP: error code 19 - Password fails quality checking policy]; "
                            + "remaining name '" + dn + "'", ResultCode.CONSTRAINT_VIOLATION);
                }
                Pattern pattern = Pattern.compile("[" + Pattern.quote("! #$%&’()*+,-./:;<=>?@[\\]^_‘{|}~") + "]");
                if (! pattern.matcher(attribute.getStringValue()).find()) {
                    throw new LdapException("javax.naming.directory.InvalidAttributeValueException: [LDAP: error code 19 - Password fails quality checking policy]; "
                            + "remaining name '" + dn + "'", ResultCode.CONSTRAINT_VIOLATION);
                }
            }
        }
        
        accounts.put(dn, attributes);
    }
    
    // For tests, to pre-populate the mocked LDAP repository
    public void prime(String dn, LdapAttribute... attributes) {
        List<LdapAttribute> attributeList = new ArrayList<LdapAttribute>();
        attributeList.addAll(Arrays.asList(attributes)); // populate a mutable list 
        accounts.put(dn, attributeList);
    }
    
    // Mock the ldap update operations 
    @Override
    protected void update(Connection connection, ModifyRequest changes) 
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, LdapException {
        
        Collection<LdapAttribute> attributes = null;
                
        if(changes.getDn().contains("cn=FOUNDATION_LAYER_PARTY_SERVICE")) {
            attributes = accounts.get(filterAccountDnFromServiceDn(changes.getDn()));
        } else {
            attributes = accounts.get(changes.getDn());
        }
      
        if (attributes != null) {
            for (AttributeModification mod : changes.getAttributeModifications()) {
                if (AttributeModificationType.ADD.equals(mod.getAttributeModificationType())) {
                    attributes.add(mod.getAttribute());
                } else if (AttributeModificationType.REMOVE.equals(mod.getAttributeModificationType())) {
                    for (LdapAttribute attribute : attributes) {
                        if (attribute.getName().equals(mod.getAttribute().getName())) {
                            attributes.remove(attribute);
                            break;                                
                        }
                    }
                } else if (AttributeModificationType.REPLACE.equals(mod.getAttributeModificationType())) {
                    for (LdapAttribute attribute : attributes) {
                        if (attribute.getName().equals(mod.getAttribute().getName())) {
                            attributes.remove(attribute);
                            attributes.add(mod.getAttribute());
                            break;                                
                        }
                    }
                } 
            }
        } else {
            fail("account not found");
        }
        
    }
    
    private String filterAccountDnFromServiceDn(String serviceDn) {
        return serviceDn.substring(34);
    }

    @Override
    protected void authenticate(Connection connection, String userDn, String credentials) 
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, LdapException {
        Collection<LdapAttribute> attributes = accounts.get(userDn);
        boolean found = false;
        boolean unlocked = false;
        if (attributes != null) {
            for (Iterator<LdapAttribute> attributeIter = attributes.iterator() ; attributeIter.hasNext(); ) {
                LdapAttribute attribute = attributeIter.next();
                if ("userPassword".equals(attribute.getName())) {
                    if (! attribute.getStringValue().equals(credentials)) {
                        LdapException ldapException = new LdapException("javax.naming.AuthenticationException: [LDAP: error code 49 - Invalid Credentials]", ResultCode.INVALID_CREDENTIALS);
                        throw ldapException;
                    }
                    found = true;
                }
                if ("pwdAccountLockedTime".equals(attribute.getName())) {
                    // locked time value must be in the past
                   if ((attribute.getStringValue() != null) && attribute.getStringValue().startsWith("1995")) {
                       unlocked = true;
                   }
                }
            }
        }
        if ((! found) || (! unlocked)) {
            LdapException ldapException = new LdapException("javax.naming.AuthenticationException: [LDAP: error code 49 - Invalid Credentials]", ResultCode.INVALID_CREDENTIALS);
            throw ldapException;
        }
    }
    
    @Override
    protected void discard(Connection connection, DeleteRequest changes) 
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, LdapException {
        accounts.remove(changes.getDn());
    }

    private String userNotFound(String baseDn) throws LdapException {
        LdapException ldapException = new LdapException("javax.naming.NameNotFoundException: [LDAP: error code 32 - No Such Object]; remaining name '" + baseDn + "'", ResultCode.NO_SUCH_OBJECT);
        throw ldapException;
    }

};
