package com.northgateps.nds.cas.authentication;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * NDS version of FileTrustStoreSslSocketFactory to trust certificates signed with our truststore
 * rather than checking the full X509 path etc.
 */
public class NdsTrustStoreSslSocketFactory extends SSLConnectionSocketFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(NdsTrustStoreSslSocketFactory.class);
    
    
    /**
     * Instantiates a new trusted proxy authentication trust store ssl socket factory.
     * @param trustStoreFile - the trust store file
     * @param trustStorePassword - the trust store password
     * @param trustStoreType - the trust store type
     */
    public NdsTrustStoreSslSocketFactory(final Resource trustStoreFile, 
                                          final String trustStorePassword,
                                          final String trustStoreType) {
        super(getTrustedSslContext(trustStoreFile, trustStorePassword, trustStoreType));
    }

    /**
     * Gets the trusted ssl context.
     *
     * @param trustStoreFile - the trust store file
     * @param trustStorePassword - the trust store password
     * @param trustStoreType - the trust store type
     * @return the trusted ssl context
     */
    private static SSLContext getTrustedSslContext(final Resource trustStoreFile, final String trustStorePassword,
            final String trustStoreType) {
        
        try {

            final KeyStore casTrustStore = KeyStore.getInstance(trustStoreType);
            final char[] trustStorePasswordCharArray = trustStorePassword.toCharArray();

            try (InputStream casStream = trustStoreFile.getInputStream()) {
                casTrustStore.load(casStream, trustStorePasswordCharArray);
            }
            
            // trust our own certificates
            final SSLContext context = SSLContexts.custom()
                    .loadTrustMaterial(casTrustStore, new TrustSelfSignedStrategy())
                    .setProtocol("SSL")
                    .build();
            
            return context;

        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
