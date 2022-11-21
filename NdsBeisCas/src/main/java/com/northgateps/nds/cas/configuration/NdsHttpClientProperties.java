package com.northgateps.nds.cas.configuration;

import java.security.KeyStore;

import org.apereo.cas.configuration.model.core.authentication.HttpClientProperties;
import org.apereo.cas.configuration.model.core.authentication.HttpClientTrustStoreProperties;

/**
 * NDS version of HttpClientProperties in order to add Truststore.type into the configuration.
 */
public class NdsHttpClientProperties extends HttpClientProperties {

    private Truststore truststore = new Truststore();
    
    public Truststore getTruststore() {
        return truststore;
    }

    public void setTruststore(final Truststore truststore) {
        this.truststore = truststore;
    }
    
    public static class Truststore extends HttpClientTrustStoreProperties {
        private String type = KeyStore.getDefaultType();
        
        public String getType() {
            return type;
        }
        
        public HttpClientTrustStoreProperties setType(String type) {
            this.type = type;
            return this;
        }
    }
}