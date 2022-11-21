package org.apereo.cas.config;

import javax.net.ssl.SSLContext;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.logout.LogoutManager;
import org.apereo.cas.ticket.registry.DefaultTicketRegistryCleaner;
import org.apereo.cas.ticket.registry.NoOpTicketRegistryCleaner;
import org.apereo.cas.ticket.registry.TicketRegistry;
import org.apereo.cas.ticket.registry.TicketRegistryCleaner;
import org.apereo.cas.ticket.serialization.TicketSerializationManager;
import org.apereo.cas.util.lock.LockRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.northgateps.nds.platform.logger.NdsLogger;

import lombok.extern.slf4j.Slf4j;

/**
 * Replace the CAS version of this file in order to get the ticketRegistry bean to use the
 * overridden method mongoDbTicketRegistryTemplate @see NdsCasCoreHttpConfiguration. Both methods
 * are removed from this file.
 */
@Configuration("mongoTicketRegistryConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
@Slf4j
public class MongoDbTicketRegistryConfiguration {
  private static final NdsLogger logger = NdsLogger.getLogger(MongoDbTicketRegistryConfiguration.class);
  
  @Autowired
  private CasConfigurationProperties casProperties;

  @Autowired
  @Qualifier("ticketSerializationManager")
  private ObjectProvider<TicketSerializationManager> ticketSerializationManager;

  @Autowired
  @Qualifier("sslContext")
  private ObjectProvider<SSLContext> sslContext;

  @Autowired
  @Bean
  public TicketRegistryCleaner ticketRegistryCleaner(
      @Qualifier(LockRepository.BEAN_NAME) final LockRepository lockRepository,
      @Qualifier("logoutManager") final LogoutManager logoutManager,
      @Qualifier("ticketRegistry") final TicketRegistry ticketRegistry) {
	  
    boolean isCleanerEnabled = casProperties.getTicket().getRegistry().getCleaner().getSchedule().isEnabled();
    
    if (isCleanerEnabled) {
      logger.debug("Ticket registry cleaner for MongoDb is enabled.");
      return new DefaultTicketRegistryCleaner(lockRepository, logoutManager, ticketRegistry);
    }
    
    logger.debug("Ticket registry cleaner for MongoDb is not enabled. "
        + "Expired tickets are not forcefully collected and cleaned by CAS. It is up to the ticket registry itself to "
        + "clean up tickets based on expiration and eviction policies.");
    
    return NoOpTicketRegistryCleaner.getInstance();
  }


  // We override this in NdsCasCoreHttpConfiguration so no point in specifying it here
  // public MongoTemplate mongoDbTicketRegistryTemplate() {
}
