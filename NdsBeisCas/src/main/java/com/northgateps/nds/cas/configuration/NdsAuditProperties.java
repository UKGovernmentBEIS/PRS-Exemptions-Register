package com.northgateps.nds.cas.configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import com.northgateps.nds.platform.util.configuration.ConfigurationDecoder;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Properties required to make ESB audit service calls
 * These are picked up from the cas.properties file under nds.audit.Xx (follows the naming conventions from that file).
 */
public class NdsAuditProperties implements ConfigurationManager {

    private String url;
    private String keyStore_file;
    private String keyStore_password;
    private String keyStore_type;
    private String trustStore_file;
    private String trustStore_password;
    private String trustStore_type;
    
    private int maxConnectionsPerRoute;
    private int maxConnectionsTotal;
    private int esbServicesTimeLimit;
    private int connectionRetries;
    private int connectionRequestTimeout = -1;
    private int connectionTimeout = -1;
    private int closeExpiredIdleConnectionsTime;
    private int maxIdleTime;
    
	private Boolean singletonConnectionManager = null;
    private Boolean closeExpiredConnections = null;
    private Boolean retryOnIoException = null;
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKeyStore_file() {
        return keyStore_file;
    }

    public void setKeyStore_file(String keyStore_file) {
        this.keyStore_file = keyStore_file;
    }

    public String getKeyStore_password() {
        return keyStore_password;
    }

    public void setKeyStore_password(String keyStore_password) {
        this.keyStore_password = keyStore_password;
    }

    public String getKeyStore_type() {
        return keyStore_type;
    }

    public void setKeyStore_type(String keyStore_type) {
        this.keyStore_type = keyStore_type;
    }

    public String getTrustStore_file() {
        return trustStore_file;
    }

    public void setTrustStore_file(String trustStore_file) {
        this.trustStore_file = trustStore_file;
    }

    public String getTrustStore_password() {
        return trustStore_password;
    }

    public void setTrustStore_password(String trustStore_password) {
        this.trustStore_password = trustStore_password;
    }

    public String getTrustStore_type() {
        return trustStore_type;
    }

    public void setTrustStore_type(String trustStore_type) {
        this.trustStore_type = trustStore_type;
    }

    public int getMaxConnectionsPerRoute(int defaultValue) {
        if (maxConnectionsPerRoute < 1) {
            return defaultValue;
        }
        
        return maxConnectionsPerRoute;
    }
    
    public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    }

    public int getMaxConnectionsTotal(int defaultValue) {
        if (maxConnectionsTotal < 1) {
            return defaultValue;
        }
        
        return maxConnectionsTotal;
    }
        
    public void setMaxConnectionsTotal(int maxConnectionsTotal) {
        this.maxConnectionsTotal = maxConnectionsTotal;
    }

    public int getEsbServicesTimeLimit(int defaultValue) {
        if (esbServicesTimeLimit < 1) {
            return defaultValue;
        }
        
        return esbServicesTimeLimit;
    }

    public void setEsbServicesTimeLimit(int esbServicesTimeLimit) {
        this.esbServicesTimeLimit = esbServicesTimeLimit;
    }

    public int getConnectionRetries(int defaultValue) {
        if (connectionRetries < 1) {
            return defaultValue;
        }
        
        return connectionRetries;
    }
    
    public void setConnectionRetries(int connectionRetries) {
        this.connectionRetries = connectionRetries;
    }
    
    public int getConnectionRequestTimeout(int defaultValue) {
    	if (connectionRequestTimeout < 0) {
    		return defaultValue;
    	}
    	
    	return connectionRequestTimeout;
    }
    
    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }
    
    public int getConnectionTimeout(int defaultValue) {
    	if (connectionTimeout < 0) {
    		return defaultValue;
    	}
    	
    	return connectionTimeout;
    }
    
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
    
    public boolean isSingletonConnectionManager(boolean defaultValue) {
    	if (singletonConnectionManager == null) {
    		return defaultValue;
    	}
    	
    	return singletonConnectionManager;
    }
    
    public void setSingletonConnectionManager(boolean singletonConnectionManager) {
    	this.singletonConnectionManager = singletonConnectionManager;
    }
    
    public boolean isCloseExpiredConnections(boolean defaultValue) {
    	if (closeExpiredConnections == null) {
    		return defaultValue;
    	}
    	
    	return closeExpiredConnections;
    }
    
    public void setCloseExpiredConnections(boolean closeExpiredConnections) {
    	this.closeExpiredConnections = closeExpiredConnections;
    }

    public boolean isRetryOnIoException(boolean defaultValue) {
    	if (retryOnIoException == null) {
    		return defaultValue;
    	}
    	
    	return retryOnIoException;
    }
    
    public void setRetryOnIoException(boolean retryOnIoException) {
    	this.retryOnIoException = retryOnIoException;
    }
    
    public int getCloseExpiredIdleSleepTime(int defaultValue) {
    	if (closeExpiredIdleConnectionsTime < 1) {
    		return defaultValue;
    	}
    	
    	return closeExpiredIdleConnectionsTime;
    }
    
    public void setCloseExpiredIdleConnectionsTime(int closeIdleConnectionsTime) {
    	this.closeExpiredIdleConnectionsTime = closeIdleConnectionsTime;
    }
    
    public int getMaxIdleTime(int defaultValue) {
    	if (maxIdleTime < 1) {
    		return defaultValue;
    	}
    	
    	return maxIdleTime;
    }
    
    public void setMaxIdleTime(int maxIdleTime) {
    	this.maxIdleTime = maxIdleTime;
    }
    
    /**
     * The Nds ESB client uses a defined set of property names to retrieve security data so need to be interpreted from CAS
     * style names. If ESB client introduces any further properties, they will need to be interpreted here
     */
    @Override
    public String getString(String key) {

        switch (key) {
            case "url":
                return getUrl();
            case "client.keyStore.file":
                return getKeyStore_file();
            case "client.keyStore.password":
                return getKeyStore_password();
            case "client.keyStore.type":
                return getKeyStore_type();
            case "client.trustStore.file":
                return getTrustStore_file();
            case "client.trustStore.password":
                return getTrustStore_password();
            case "client.trustStore.type":
                return getTrustStore_type();
        }

        return null;
    }

    @Override
    public Object getProperty(String key) {
        if (url.equals(key)) {
            return getUrl();
        } else {
            return null;
        }
    }

    @Override
    public int getInt(String key, int defaultValue) {
        switch (key) {
            case "app.server.maxConnectionsTotal":
                return getMaxConnectionsTotal(defaultValue);
            case "app.server.maxConnectionsPerRoute":
                return getMaxConnectionsPerRoute(defaultValue);
            case "app.server.esbServicesTimeLimit":
                return getEsbServicesTimeLimit(defaultValue);
            case "app.connection.retries":
                return getConnectionRetries(defaultValue);
            case "app.server.connectionRequestTimeout":
            	return getConnectionRequestTimeout(defaultValue);
            case "app.server.connectionTimeout":
            	return getConnectionTimeout(defaultValue);
            case "app.server.closeExpiredIdleSleepTime":
            	return getCloseExpiredIdleSleepTime(defaultValue);
            case "app.server.maxIdleTime":
            	return getMaxIdleTime(defaultValue);
            default:
                throw new NdsAuditPropertiesException("Unknown property " + key);
        }
    }
    
    /* 
     * The following methods are not implemented and throw an exception to fail fast rather than mask errors. 
     */

    @Override
    public boolean containsKey(String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public Iterator<String> getKeys(String prefix) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public Iterator<String> getKeys() {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public boolean getBoolean(String key) {
    	throw new NdsAuditPropertiesException();	
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
    	switch(key) {
	        case "app.server.singletonConnectionManager":
	        	return isSingletonConnectionManager(false);
	        case "app.server.closeIdleExpiredConnections":
	        	return isCloseExpiredConnections(false);
	        case "app.server.retryOnIoException":
	        	return isRetryOnIoException(false);
	        default :
	        	throw new NdsAuditPropertiesException();
		}
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public byte getByte(String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public byte getByte(String key, byte defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public Byte getByte(String key, Byte defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public double getDouble(String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public float getFloat(String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public Float getFloat(String key, Float defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public int getInt(String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public long getLong(String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public long getLong(String key, long defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public short getShort(String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public short getShort(String key, short defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public Short getShort(String key, Short defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public BigDecimal getBigDecimal(String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public BigInteger getBigInteger(String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public String getString(String key, String defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public String getEncodedString(String key, ConfigurationDecoder decoder) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public String getAbsolutePathFromClasspath(String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public String[] getStringArray(String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public List<Object> getList(String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public List<Object> getList(String key, List<Object> defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public <T> T get(Class<T> cls, String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public <T> T get(Class<T> cls, String key, T defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public Object getArray(Class<?> cls, String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public Object getArray(Class<?> cls, String key, Object defaultValue) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public <T> List<T> getList(Class<T> cls, String key) {
        throw new NdsAuditPropertiesException();
    }

    @Override
    public <T> List<T> getList(Class<T> cls, String key, List<T> defaultValue) {
        throw new NdsAuditPropertiesException();
    }
    
    /** Exception for un-implemented non-default-providing properties. */
    @SuppressWarnings("serial")
    class NdsAuditPropertiesException extends RuntimeException {
        NdsAuditPropertiesException() {
            super("Method not implemented");
        }
        
        NdsAuditPropertiesException(String message) {
            super(message);
        }
    }
}