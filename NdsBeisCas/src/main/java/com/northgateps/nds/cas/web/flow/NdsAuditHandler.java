package com.northgateps.nds.cas.web.flow;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apereo.inspektr.audit.AuditActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import com.northgateps.nds.cas.audit.NdsAuditTrailManager;
import com.northgateps.nds.cas.configuration.NdsAuditProperties;
import com.northgateps.nds.platform.api.AbstractNdsResponse;
import com.northgateps.nds.platform.api.NdsResponse;
import com.northgateps.nds.platform.api.PlatformNdsRequest;
import com.northgateps.nds.platform.api.UserAccountAuditDetails;
import com.northgateps.nds.platform.api.model.PlatformCommonContext;
import com.northgateps.nds.platform.api.serviceclient.NdsResponseConsumer;
import com.northgateps.nds.platform.api.serviceclient.PlatformSpringUiServiceClient;
import com.northgateps.nds.platform.api.serviceclient.PlatformUiServiceClientFactory;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClient;
import com.northgateps.nds.platform.api.serviceclient.UiServiceClientFactory;
import com.northgateps.nds.platform.api.useraccountaudit.UserAccountAuditNdsRequest;
import com.northgateps.nds.platform.api.useraccountaudit.UserAccountAuditNdsResponse;

/**
 * Given the audit context details this class will call the ESB to store them. A UI service client is constructed in 
 * the same way as the UI projects do.
 * 
 * NB This expects the standard LDAP pattern, if another LDAP system is used a project specific version
 * of this class should be created.
 */
public class NdsAuditHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected AuditActionContext auditActionContext;
    protected NdsAuditProperties ndsAuditProperties;

    public NdsAuditHandler(AuditActionContext auditActionContext, NdsAuditProperties ndsAuditProperties) {
        this.auditActionContext = auditActionContext;
        this.ndsAuditProperties = ndsAuditProperties;
    }

    /**
     * Call the ESB to store the user audit details
     */
    public void logUserAudit() {

        Map<String, UiServiceClient> uiSvcClientRegistry = new HashMap<String, UiServiceClient>();
        PlatformSpringUiServiceClient serviceClient = new PlatformSpringUiServiceClient();
        serviceClient.setRetryOnIoException(ndsAuditProperties.isRetryOnIoException(false));
        
        serviceClient.setResponsePrototype(new UserAccountAuditNdsResponse());
        uiSvcClientRegistry.put("userAccountAudit", serviceClient);
        UiServiceClientFactory uiServiceClientFactory = new PlatformUiServiceClientFactory(uiSvcClientRegistry,
                ndsAuditProperties);
        UiServiceClient uiSvcClient = uiServiceClientFactory.getInstance(UserAccountAuditNdsResponse.class);

        UserAccountAuditNdsRequest request = new UserAccountAuditNdsRequest();
        PlatformCommonContext commonContext = new PlatformCommonContext();

        commonContext.setRequestGuid(UUID.randomUUID().toString());
        commonContext.setPreferredLanguage("en");
        request.setCommonContext(commonContext);

        request.setUserAccountAuditDetails(getUserAccountAuditDetails());
        setUsernameAndTenant(request);
        
        log.info("Setting up marshaller");
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(UserAccountAuditNdsRequest.class,
                UserAccountAuditNdsResponse.class, PlatformNdsRequest.class,
                AbstractNdsResponse.class);


        try {
            marshaller.afterPropertiesSet();
        } catch (Exception ex) {
            throw new BeanCreationException(ex.getMessage(), ex);
        }

        uiSvcClient.post(ndsAuditProperties.getUrl(), request, marshaller);

        // doesen't look like this is ever called as CAS doesn't have the UI's uiSvcClient consumer
        // framework
        uiSvcClient.setResponseConsumer(new NdsResponseConsumer() {

            @Override
            public boolean consume(NdsResponse response) {
                if (!(uiSvcClient.getResponse() instanceof UserAccountAuditNdsResponse)) {
                    return false;
                }

                UserAccountAuditNdsResponse userAccountAuditResponse = ((UserAccountAuditNdsResponse) (response));

                if (userAccountAuditResponse.isSuccess()) {
                    return true;
                } else if (userAccountAuditResponse.getNdsMessages() != null) {
                    return false;
                } else {
                    return false;
                }
            }
        });
    }

    /* 
     * Depending the type of event, the username and tenant will be in either Principal or ResourceOperatedUpon. The
     * username and tenant are constructed to keep consistent with the style of ESB client requests
     */
    private void setUsernameAndTenant(UserAccountAuditNdsRequest request) {

        if (StringUtils.isNotEmpty(auditActionContext.getActionPerformed())) {

            switch (auditActionContext.getActionPerformed()) {

            case NdsAuditTrailManager.AUTHENTICATION_SUCCESS:
                    
                // "uid=davindert,ou=Users,ou=Harrow,ou=Tenants,ou=NDS,dc=northgateps,dc=com";
                String uid = auditActionContext.getPrincipal();
                log.info("WHO: " + uid);
    
                request.setUsername(uid.substring(uid.indexOf("uid=") + 4, uid.indexOf(",")));
                request.setTenant(uid.substring(StringUtils.ordinalIndexOf(uid, "ou=", 2) + 3,
                        StringUtils.ordinalIndexOf(uid, ",", 3)));
                break;

            case NdsAuditTrailManager.AUTHENTICATION_FAILED:
                
                // Supplied credentials: [davindert@Harrow]
                log.info("WHAT: Supplied credentials: " + auditActionContext.getResourceOperatedUpon());
    
                request.setUsername(auditActionContext.getResourceOperatedUpon().substring(
                        auditActionContext.getResourceOperatedUpon().indexOf("[") + 1,
                        auditActionContext.getResourceOperatedUpon().indexOf("@")));
    
                request.setTenant(auditActionContext.getResourceOperatedUpon().substring(
                        auditActionContext.getResourceOperatedUpon().indexOf("@") + 1,
                        auditActionContext.getResourceOperatedUpon().indexOf("]")));
                break;
            }
        }

        log.info("Extacted username/tenant " + request.getUsername() + " " + request.getTenant());

    }

    private UserAccountAuditDetails getUserAccountAuditDetails() {

        UserAccountAuditDetails userAccountAuditDetails = new UserAccountAuditDetails();

        userAccountAuditDetails.setTimeOfAction(
                auditActionContext.getWhenActionWasPerformed().toInstant().atZone(ZoneId.systemDefault()));

        if (StringUtils.isNotEmpty(auditActionContext.getActionPerformed())) {

            switch (auditActionContext.getActionPerformed()) {

            case NdsAuditTrailManager.AUTHENTICATION_SUCCESS:
                userAccountAuditDetails.setSuccessfulLogin(true);
                break;
            case NdsAuditTrailManager.AUTHENTICATION_FAILED:
                userAccountAuditDetails.setSuccessfulLogin(false);
                break;
            }
        }

        return userAccountAuditDetails;
    }

}