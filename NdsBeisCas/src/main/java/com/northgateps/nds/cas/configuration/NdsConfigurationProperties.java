package com.northgateps.nds.cas.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Rather than constantly splicing into CAS' properties and increasing the maintenance overhead
 * when upgrading (ie making sure our modified versions keep in sync with CAS'), this provides
 * our own root property where we can add our own customisations.
 * 
 * @see com.northgateps.nds.cas.config.NdsCasWebflowContextConfiguration
 */
@ConfigurationProperties(value = "nds")
public class NdsConfigurationProperties {

    @NestedConfigurationProperty
    private NdsAuthenticationProperties authn = new NdsAuthenticationProperties();

    @NestedConfigurationProperty
    private NdsAuditProperties audit = new NdsAuditProperties();

    @NestedConfigurationProperty
    private NdsHttpClientProperties httpClient = new NdsHttpClientProperties();

    @NestedConfigurationProperty
    private NdsTicketRegistryMongoProperties ticketRegistryMongo = new NdsTicketRegistryMongoProperties();

    private String applicationPhase;
    private String feedbackEmail;
    private String accountLock;
    private String pwdPolicySubentry;
    private String defaultTenant;
    
    public NdsAuthenticationProperties getAuthn() {
        return authn;
    }

    public void setAuthn(NdsAuthenticationProperties authn) {
        this.authn = authn;
    }
    
    public NdsHttpClientProperties getHttpClient() {
        return httpClient;
    }
    
    public void setHttpClient(NdsHttpClientProperties httpClient) {
        this.httpClient = httpClient;
    }

    public NdsAuditProperties getAudit() {
        return audit;
    }

    public void setAudit(NdsAuditProperties audit) {
        this.audit = audit;
    }
    
    public NdsTicketRegistryMongoProperties getTicketRegistryMongo() {
        return ticketRegistryMongo;
    }

    public void setTicketRegistryMongo(NdsTicketRegistryMongoProperties ticketRegistryMongo) {
        this.ticketRegistryMongo = ticketRegistryMongo;
    }
   
    public String getApplicationPhase() {
        return applicationPhase;
    }

    public void setApplicationPhase(String applicationPhase) {
        this.applicationPhase = applicationPhase;
    }

    public String getFeedbackEmail() {
        return feedbackEmail;
    }

    public void setFeedbackEmail(String feedbackEmail) {
        this.feedbackEmail = feedbackEmail;
    }

    public String getAccountLock() {
        return accountLock;
    }

    public void setAccountLock(String accountLock) {
        this.accountLock = accountLock;
    }

    public String getPwdPolicySubentry() {
        return pwdPolicySubentry;
    }

    public void setPwdPolicySubentry(String pwdPolicySubentry) {
        this.pwdPolicySubentry = pwdPolicySubentry;
    }

    public String getDefaultTenant() {
        return defaultTenant;
    }

    public void setDefaultTenant(String defaultTenant) {
        this.defaultTenant = defaultTenant;
    }
}
