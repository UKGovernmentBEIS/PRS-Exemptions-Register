package com.northgateps.nds.cas.config;

import org.apereo.cas.audit.AuditTrailExecutionPlan;
import org.apereo.cas.audit.AuditTrailExecutionPlanConfigurer;
import org.apereo.cas.audit.spi.plan.DefaultAuditTrailExecutionPlan;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.inspektr.audit.AuditTrailManager;
import org.apereo.inspektr.audit.AuditTrailManager.AuditFormats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.northgateps.nds.cas.audit.NdsAuditTrailManager;
import com.northgateps.nds.cas.configuration.NdsConfigurationProperties;

/**
 * Nds audit configuration that overrides the default cas default
 */
@Configuration("ndsAuditConfiguration")
@EnableAspectJAutoProxy
@EnableConfigurationProperties(NdsConfigurationProperties.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class NdsAuditConfiguration {

    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    private NdsConfigurationProperties ndsProperties;
    
    @Bean(name="auditTrailExecutionPlan")
    public AuditTrailExecutionPlan auditTrailExecutionPlan(java.util.List<AuditTrailExecutionPlanConfigurer> configurers) {
        AuditTrailExecutionPlan plan = new DefaultAuditTrailExecutionPlan();
        plan.registerAuditTrailManager(ndsAuditTrailManager());
        return plan;
    }

    // @Bean(name = { "ndsAuditTrailManager", "auditTrailManager" })
    public AuditTrailManager ndsAuditTrailManager() {
        final NdsAuditTrailManager mgmr = new NdsAuditTrailManager(ndsProperties.getAudit());
        mgmr.setUseSingleLine(casProperties.getAudit().getSlf4j().isUseSingleLine());
        mgmr.setEntrySeparator(casProperties.getAudit().getSlf4j().getSinglelineSeparator());
        mgmr.setAuditFormat(AuditFormats.DEFAULT);
        return mgmr;
    }

}
