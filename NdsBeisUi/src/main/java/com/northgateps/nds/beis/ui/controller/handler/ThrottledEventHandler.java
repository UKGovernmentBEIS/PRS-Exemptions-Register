package com.northgateps.nds.beis.ui.controller.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.view.AbstractViewEventHandler;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

public class ThrottledEventHandler extends AbstractViewEventHandler {
    @Autowired
    JCacheCacheManager cacheManager;
    
    static ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();
    protected final NdsLogger logger = NdsLogger.getLogger(getClass());
	
	/** throttles requests by email and ip adress and redirects to error page if needed */
    protected boolean doThrottling(String emailAddress, ControllerState controllerState) {
    	
        if (emailAddress == null) {
        	return true;
        }
        	
    	// do throttling to slow down potential DOS attacks
	    Cache cacheUsername = cacheManager.getCache("throttling_username");
	    Cache cacheIp = cacheManager.getCache("throttling_ip");
	    boolean fail = false;
	    
	    // check both so they can both get cache entries before returning
    	if (! checkThrottlingCache(cacheUsername, emailAddress)) {
    		Integer throttlingForgotUsernameTtlMinutes = configurationManager.getInt("throttling.forgotCredentials.ttlMinutes", 0);
    		logger.warn("Too many forgotten username requests for email " + emailAddress + " with cache TTL " + throttlingForgotUsernameTtlMinutes + " minutes");
    		
    		// this tells the page to show the failed message and the time to wait
    		controllerState.getAttributes().put("throttled", throttlingForgotUsernameTtlMinutes.toString() + " minutes");
    		fail = true;
    	}
    	
    	HttpServletRequest webRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    	String remoteHost = webRequest.getRemoteAddr();
    	
    	// check both so they can both get cache entries before returning
    	if (! checkThrottlingCache(cacheIp, remoteHost)) {
    		Integer throttlingForgotFromIpTtlSeconds = configurationManager.getInt("throttling.forgotFromIp.ttlSeconds", 0);
    		logger.warn("Too many forgotten Username requests from ip " + remoteHost + " with cache TTL " + throttlingForgotFromIpTtlSeconds + " seconds");

    		// this tells the page to show the failed message and the time to wait
    		controllerState.getAttributes().put("throttled", throttlingForgotFromIpTtlSeconds.toString() + " seconds");
    		fail = true;
    	}

    	if (fail) {
    		return false;
    	}
    	
    	return true;
    }
    
    /** 
     * Returns true if there's no data in the cache for this value.
     * (Cache TTL config manages expiry to allow for repeated requests after a period of time.) 
     */
	protected boolean checkThrottlingCache(Cache cache, String value) {
	
		// if cache fails skip this
        if (cache == null) {
        	logger.warn("One of the throttling caches is null");  // shouldn't happen
        	return true;
        }
        
        // check cache
        ValueWrapper cachedValue = cache.get(value);
        
        // cache miss - add the value and return true
        if (cachedValue == null) {
        	cache.put(value, Integer.valueOf(1));
        	return true;
        }
        
        // cache hit, update the value and return false as we've already got an entry
        int times = (Integer)cachedValue.get();
       	cache.put(value, Integer.valueOf(++ times));
       	return false;
	}
}
