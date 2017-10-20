package com.northgateps.nds.beis.ui.configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.util.configuration.ConfigurationDecoder;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/** 
 * A mutable configuration manager for tests so they can override the default configuration
 * (ie from the properties file) with different/bad settings and check what happens. 
 */
public class MockConfigurationManager implements ConfigurationManager {
    
    private Map<String, String> overrideConfig = new HashMap<String, String>();
    
    /** Add a value to override the default config. */
    public void setString(String key, String value) {
        overrideConfig.put(key, value);
    }
    
    /** 
     * Return the value associated with the key if one has been set, 
     * otherwise fail-over to the real configuration manager rather than having to
     * specify everything from the properties file again. 
     */
    @Override
    public String getString(String key) {
        
        if (overrideConfig.containsKey(key)) {
            return overrideConfig.get(key);
        }
        
        return ConfigurationFactory.getConfiguration().getString(key);
    }
    
    @Override
    public boolean containsKey(String key) {
        return false;
    }

    @Override
    public Object getProperty(String key) {
        return null;
    }

    @Override
    public Iterator<String> getKeys(String prefix) {
        return null;
    }

    @Override
    public Iterator<String> getKeys() {
        return null;
    }

    @Override
    public boolean getBoolean(String key) {
        if (overrideConfig.containsKey(key)) {
            return Boolean.getBoolean(overrideConfig.get(key));
        }
        
        return ConfigurationFactory.getConfiguration().getBoolean(key);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return false;
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return null;
    }

    @Override
    public byte getByte(String key) {
        return 0;
    }

    @Override
    public byte getByte(String key, byte defaultValue) {
        return 0;
    }

    @Override
    public Byte getByte(String key, Byte defaultValue) {
        return null;
    }

    @Override
    public double getDouble(String key) {
        return 0;
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return 0;
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        return null;
    }

    @Override
    public float getFloat(String key) {
        return 0;
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return 0;
    }

    @Override
    public Float getFloat(String key, Float defaultValue) {
        return null;
    }

    @Override
    public int getInt(String key) {
        return 0;
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return 0;
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        return null;
    }

    @Override
    public long getLong(String key) {
        return 0;
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return 0;
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return null;
    }

    @Override
    public short getShort(String key) {
        return 0;
    }

    @Override
    public short getShort(String key, short defaultValue) {
        return 0;
    }

    @Override
    public Short getShort(String key, Short defaultValue) {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String key) {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        return null;
    }

    @Override
    public BigInteger getBigInteger(String key) {
        return null;
    }

    @Override
    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getString(String key, String defaultValue) {
        return null;
    }

    @Override
    public String getEncodedString(String key, ConfigurationDecoder decoder) {
        return null;
    }

    @Override
    public String getAbsolutePathFromClasspath(String key) {
        return null;
    }

    @Override
    public String[] getStringArray(String key) {
        return null;
    }

    @Override
    public List<Object> getList(String key) {
        return null;
    }

    @Override
    public List<Object> getList(String key, List<Object> defaultValue) {
        return null;
    }

    @Override
    public <T> T get(Class<T> cls, String key) {
        return null;
    }

    @Override
    public <T> T get(Class<T> cls, String key, T defaultValue) {
        return null;
    }

    @Override
    public Object getArray(Class<?> cls, String key) {
        return null;
    }

    @Override
    public Object getArray(Class<?> cls, String key, Object defaultValue) {
        return null;
    }

    @Override
    public <T> List<T> getList(Class<T> cls, String key) {
        return null;
    }

    @Override
    public <T> List<T> getList(Class<T> cls, String key, List<T> defaultValue) {
        return null;
    }            
}
