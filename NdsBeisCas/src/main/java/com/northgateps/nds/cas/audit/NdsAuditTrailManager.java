package com.northgateps.nds.cas.audit;

import java.time.LocalDate;
import java.util.Set;

import org.apereo.inspektr.audit.AuditActionContext;
import org.apereo.inspektr.audit.support.AbstractStringAuditTrailManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.northgateps.nds.cas.configuration.NdsAuditProperties;
import com.northgateps.nds.cas.web.flow.NdsAuditHandler;

/**
 * Intercepts the audit context record and decides whether to store the audit details
 */
public class NdsAuditTrailManager extends AbstractStringAuditTrailManager {

    public static final String AUTHENTICATION_SUCCESS = "AUTHENTICATION_SUCCESS";
    public static final String AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";

    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected NdsAuditProperties ndsAuditProperties;

    public NdsAuditTrailManager(NdsAuditProperties ndsAuditProperties) {
        this.ndsAuditProperties = ndsAuditProperties;
    }

    @Override
    public void record(final AuditActionContext auditActionContext) {

        if (auditActionContext.getActionPerformed().equals(AUTHENTICATION_SUCCESS)
                || auditActionContext.getActionPerformed().equals(AUTHENTICATION_FAILED)) {

            NdsAuditHandler ndsAuditHandler = new NdsAuditHandler(auditActionContext, ndsAuditProperties);
            ndsAuditHandler.logUserAudit();

        }

    }

    @Override
    public Set<AuditActionContext> getAuditRecordsSince(LocalDate arg0) {
        throw new RuntimeException("Method not yet supported");
    }

    @Override
    public void removeAll() {
        throw new RuntimeException("Method not yet supported");
    }
}
