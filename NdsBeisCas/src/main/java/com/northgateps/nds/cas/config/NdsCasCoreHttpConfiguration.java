package com.northgateps.nds.cas.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.configuration.model.support.mongo.ticketregistry.MongoDbTicketRegistryProperties;
import org.apereo.cas.ticket.TicketCatalog;
import org.apereo.cas.ticket.TicketDefinition;
import org.apereo.cas.ticket.TicketGrantingTicket;
import org.apereo.cas.ticket.registry.MongoDbTicketRegistry;
import org.apereo.cas.ticket.registry.TicketRegistry;
import org.apereo.cas.ticket.serialization.TicketSerializationManager;
import org.apereo.cas.util.CoreTicketUtils;
import org.apereo.cas.util.MongoDbTicketRegistryFacilitator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import com.mongodb.client.MongoClient;
import com.northgateps.nds.cas.authentication.NdsTrustStoreSslSocketFactory;
import com.northgateps.nds.cas.configuration.NdsConfigurationProperties;
import com.northgateps.nds.cas.configuration.NdsTicketRegistryMongoProperties;
import com.northgateps.nds.platform.application.mongo.Mongo3AvailabilityChecker;
import com.northgateps.nds.platform.application.mongo.Mongo3TrustAllMongoClient;

/**
 * Provides Spring beans in addition to and overriding those that CAS provides
 * (ie our own versions).
 * This saves us extending or overriding CAS configuration and property classes which
 * causes a maintenance penalty on upgrades.
 * 
 * This configuration is for core HTTP customisations.
 * 
 * @see spring.factories which loads the configuration
 */
@Configuration("ndsCasCoreHttpConfiguration")
@EnableConfigurationProperties(NdsConfigurationProperties.class)
public class NdsCasCoreHttpConfiguration {
  private static final Logger logger = LoggerFactory.getLogger(NdsCasCoreHttpConfiguration.class);

    @Autowired
    private NdsConfigurationProperties ndsProperties;
    @Autowired
    private CasConfigurationProperties casProperties;
    @Autowired
    @Qualifier("ticketSerializationManager")
    private ObjectProvider<TicketSerializationManager> ticketSerializationManager;

    /**
     * Override bean in CAS's MongoDbTicketRegistryConfiguration as it calls
     * #mongoDbTicketRegistryTemplate directly rather than via the bean (which we've replaced so
     * also have to replace this method now that it's used on startup).
     * MongoDbTicketRegistryConfiguration is overridden because of this bean.
     */
    @RefreshScope
    @Bean
    @Autowired
    public TicketRegistry ticketRegistry(
        @Qualifier("ticketCatalog") final TicketCatalog ticketCatalog)
        throws KeyManagementException, NoSuchAlgorithmException {

      logger.info("Loading our version of ticketRegistry bean");

      MongoDbTicketRegistryProperties mongo = casProperties.getTicket().getRegistry().getMongo();
      MongoTemplate mongoTemplate = mongoDbTicketRegistryTemplate();
      MongoDbTicketRegistry registry = new MongoDbTicketRegistry(ticketCatalog, mongoTemplate,
          ticketSerializationManager.getObject());
      registry.setCipherExecutor(
          CoreTicketUtils.newTicketRegistryCipherExecutor(mongo.getCrypto(), "mongo"));
      new MongoDbTicketRegistryFacilitator(ticketCatalog, mongoTemplate, mongo.isDropCollection(), false, mongo.isDropIndexes())
          .createTicketCollections();
      return registry;
    }

    /** @see org.apereo.cas.config.CasCoreHttpConfiguration. */
    @RefreshScope
    @Bean
    public SSLConnectionSocketFactory trustStoreSslSocketFactory() {
        return new NdsTrustStoreSslSocketFactory(ndsProperties.getHttpClient().getTruststore().getFile(),
                ndsProperties.getHttpClient().getTruststore().getPsw(),
                ndsProperties.getHttpClient().getTruststore().getType());
    }
    
    /**
     * Overrides the one in MongoDbTicketRegistryConfiguration to provide a mongo client that uses
     * NdsTrustAllMongoClient so we can use self-signed certs.
     * 
     * Uses @see Mongo3TrustAllMongoClient.
     */
    @Qualifier("mongoDbTicketRegistryTemplate")
    @Bean
    @DependsOn("mongoAvailabilityChecker")  // don't set up connections until MongoDb available
    public MongoTemplate mongoDbTicketRegistryTemplate() throws KeyManagementException, NoSuchAlgorithmException {
      logger.debug("Getting MongoDbTicketRegistryTemplate");
        final NdsTicketRegistryMongoProperties properties = ndsProperties.getTicketRegistryMongo();
        
        MongoClient mongoClient = Mongo3TrustAllMongoClient.getMongoClient(properties.getHosts(),
            properties.getPorts(),
            properties.getUserId(), properties.getPassword(),
            properties.getAuthenticationDatabaseName());
        
        final MongoDatabaseFactory mongoDbFactory =
            new SimpleMongoClientDatabaseFactory(mongoClient, properties.getDatabaseName());

        return new MongoTemplate(mongoDbFactory, null);
    }

    /**
     * Blocks startup until Mongo is available.  If any beans need or set up access to Mongo on
     * initialisation, they must add the attribute depends-on="mongoAvailabilityChecker".
     */
    @Autowired
    @Bean
    public Mongo3AvailabilityChecker mongoAvailabilityChecker(TicketCatalog ticketCatalog)
        throws KeyManagementException, NoSuchAlgorithmException {
        verifyBootstrapAndCasPropertiesExist();
        
        final NdsTicketRegistryMongoProperties properties = ndsProperties.getTicketRegistryMongo();
        final TicketDefinition ticketDef = ticketCatalog.find(TicketGrantingTicket.PREFIX);
        final String collectionName = ticketDef.getProperties().getStorageName();
        
        MongoClient mongoClient = Mongo3TrustAllMongoClient.getMongoClient(properties.getHosts(),
            properties.getPorts(), properties.getUserId(), properties.getPassword(),
            properties.getAuthenticationDatabaseName());

        return new Mongo3AvailabilityChecker(mongoClient, properties.getDatabaseName(),
            collectionName);
    }

    private void verifyBootstrapAndCasPropertiesExist() {
        InputStream bootstrap = this.getClass().getClassLoader().getResourceAsStream("bootstrap.properties");
        if (bootstrap == null) {
            throw new RuntimeException("The file 'bootstrap.properties' does not exist. It should be manually created from 'bootstrap.properties.sample'.");
        } else {
            try {
                String casPath = System.getProperty("cas.standalone.configurationDirectory");
                if (casPath == null) {
                    Properties prop = new Properties();
                    prop.load(bootstrap);
                    casPath = prop.getProperty("cas.standalone.configurationDirectory", null);
                }
                if (casPath == null) {
                    throw new RuntimeException("The property 'cas.standalone.configurationDirectory' is not defined as a system property or in the file 'bootstrap.properties'. "
                            + "The property should be a path to another properties file, manually created from 'cas.properties.sample'.");
                }
                File casFile = new File(casPath + "/cas.properties");
                if (! casFile.exists()) {
                    throw new RuntimeException("The 'cas.properties' file at '" + casPath + "' does not exist. It should be a properties file, manually created from 'cas.properties.sample'.");
                }
            } catch (IOException e) {
                throw new RuntimeException("The file 'bootstrap.properties' could not be read. It should be a properties file, manually created from 'bootstrap.properties.sample'.");
            } finally {
                try {
                    bootstrap.close();
                } catch (IOException e) {
                    throw new RuntimeException("The file 'bootstrap.properties' could not be closed.");
                }
            }
        }
    }
    
}

