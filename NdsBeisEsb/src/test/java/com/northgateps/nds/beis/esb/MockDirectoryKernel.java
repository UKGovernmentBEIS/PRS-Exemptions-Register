package com.northgateps.nds.beis.esb;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import org.ldaptive.Connection;
import org.ldaptive.DeleteRequest;
import org.ldaptive.LdapAttribute;
import org.ldaptive.LdapEntry;
import org.ldaptive.LdapException;
import org.ldaptive.ModifyRequest;
import org.ldaptive.SearchScope;

import com.northgateps.nds.platform.esb.directory.DirectoryKernel;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryException;

/**
 * A starting class for DirectoryKernel mocks. All methods throw UnsupportedOperationException 
 */
public class MockDirectoryKernel extends DirectoryKernel {
    
    @Override
    protected Iterator<LdapEntry> fetch(Connection connection, String baseDn, String searchString, SearchScope searchScope, String... typesOfAttributes)
            throws DirectoryException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, LdapException {
        throw new UnsupportedOperationException("fetch");
    }
    
    @Override
    protected void authenticate(Connection connection, String userDn, String credentials) 
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, LdapException {
        throw new UnsupportedOperationException("authenticate");
    }
    
    @Override
    protected void add(Connection connection, String dn, Collection<LdapAttribute> attributes) 
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, LdapException {
        throw new UnsupportedOperationException("add");
    }
    
    @Override
    protected void update(Connection connection, ModifyRequest changes) 
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, LdapException {
        throw new UnsupportedOperationException("update");
    }
    
    @Override
    protected void discard(Connection connection, DeleteRequest changes) 
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, LdapException {
        throw new UnsupportedOperationException("discard");
    }


}
