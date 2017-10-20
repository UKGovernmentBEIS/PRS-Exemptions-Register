package com.northgateps.nds.beis.esb.util;

import javax.net.ssl.TrustManager;

import org.apache.activemq.ActiveMQSslConnectionFactory;

/**
 * 
 * @author mustafa.attaree
 *
 */
public class NdsActiveMQSslConnectionFactory extends ActiveMQSslConnectionFactory {
    
    protected TrustManager[] trustManager;

    public TrustManager[] getTrustManager() {
        return trustManager;
    }
    
    public void setTrustManager(TrustManager[] trustManager) {
        setKeyAndTrustManagers(null, trustManager, null);
        this.trustManager = trustManager;
    }
    
}
