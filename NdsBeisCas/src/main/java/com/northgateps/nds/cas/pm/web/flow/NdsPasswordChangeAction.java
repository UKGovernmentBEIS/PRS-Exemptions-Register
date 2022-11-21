package com.northgateps.nds.cas.pm.web.flow;

import java.util.HashMap;
import org.apereo.cas.authentication.credential.UsernamePasswordCredential;
import org.apereo.cas.configuration.model.support.pm.LdapPasswordManagementProperties;
import org.apereo.cas.configuration.model.support.pm.PasswordManagementProperties;
import org.apereo.cas.util.LdapUtils;
import org.apereo.cas.web.support.WebUtils;
import org.ldaptive.ConnectionFactory;
import org.ldaptive.Result;
import org.ldaptive.control.PasswordPolicyControl;
import org.ldaptive.extended.ExtendedOperation;
import org.ldaptive.extended.PasswordModifyRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.action.EventFactorySupport;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import com.google.common.base.Strings;
import com.northgateps.nds.cas.authentication.UsernamePasswordTenantCredential;
import com.northgateps.nds.cas.configuration.NdsConfigurationProperties;
import com.northgateps.nds.cas.pm.ChangePasswordDetails;
import com.northgateps.nds.ldaptive.auth.TenantDnResolver;
import com.northgateps.nds.platform.api.ExpiredPasswordPolicyUtlity;

/**
 * Class is used to validate and update expired password. 
 * We are not extending PasswordChangeAction class here as,it is using default bean and
 * cas validation method. Here,we customised to use Nds password policy and validation.
 */
public class NdsPasswordChangeAction extends AbstractAction {

    public static final String PASSWORD_UPDATE_SUCCESS = "passwordUpdateSuccess";
    private static final String PASSWORD_VALIDATION_FAILURE_CODE = "pm.validationFailure";
    private static final String DEFAULT_MESSAGE = "Could not update the account password";    
    private static final String INVALID_PASSWORD = "Current password is invalid";
    private static final String PASSWORD_QUALITY_CHECK_FAILS = "pm.passwordPolicyFailure";  
    
    private static final Logger logger = LoggerFactory.getLogger(NdsPasswordChangeAction.class);       

    private final PasswordManagementProperties passwordManagementProperties;
    private final NdsConfigurationProperties ndsProperties;

    public NdsPasswordChangeAction(PasswordManagementProperties passwordManagementProperties, NdsConfigurationProperties ndsProperties) {       
        this.passwordManagementProperties = passwordManagementProperties;
        this.ndsProperties = ndsProperties;
    }
   
    @Override
    protected Event doExecute(final RequestContext requestContext) {
    	logger.debug("Checking password change request");
    	
        try {
            final UsernamePasswordTenantCredential c = (UsernamePasswordTenantCredential) WebUtils.getCredential(requestContext);
            final ChangePasswordDetails bean = requestContext.getFlowScope().get(
                    NdsPasswordManagementWebflowConfigurer.FLOW_VAR_ID_PASSWORD, ChangePasswordDetails.class); 

            logger.debug("  checking password change for " + c.getUsername());
            
            /* 
             * don't get old password from c.getOldPassword as that's the one from login form
             * not the one from our change password form which we want to use to re-verify the
             * password.
             */
            String oldPassword = bean.getOldPassword();
            String newPassword = bean.getNewPassword();
            
            //Checking new password and confirm password are same
            if (!Strings.isNullOrEmpty(newPassword) && !Strings.isNullOrEmpty(bean.getConfirmPassword()) && ! newPassword.equals(bean.getConfirmPassword())) {
            	return getErrorEvent(requestContext, PASSWORD_VALIDATION_FAILURE_CODE, "You must make the passwords the same");
            }
            
            // new and old password must be different
            if (newPassword.equals(oldPassword)) {
            	return getErrorEvent(requestContext, PASSWORD_VALIDATION_FAILURE_CODE, "New password cannot be the same as current password");
            }
            
            // We only need to get the first item as we expect to use only one item (which is the first item)
            // ie although properties file allows multiple LDAP connections we only use one, so get those properties
            final LdapPasswordManagementProperties ldap = passwordManagementProperties.getLdap().get(0);
            
            // find the user's details and prepare update to password policy entry
            final TenantDnResolver resolver = new TenantDnResolver(ldap.getSearchFilter());
            String dn = resolver.resolve(c.getUsername(), c.getTenant());
            final ConnectionFactory factory = LdapUtils.newLdaptivePooledConnectionFactory(ldap);            
            LdapExpiredPasswordUtility userManager = new LdapExpiredPasswordUtility(ndsProperties, factory);
            HashMap<String, String> passwordPolicyChanges = new HashMap<String, String>();           
            ExpiredPasswordPolicyUtlity.handleUpdatingExpiredPasswordPolicy(userManager, dn, passwordPolicyChanges);
            
            String message = null; 

            final ExtendedOperation operation = new ExtendedOperation(factory);
            Result result = operation.execute(new PasswordModifyRequest(dn, oldPassword, newPassword));

            switch (result.getResultCode()) {

                case SUCCESS:                
                    logger.debug("Successfully updated the account password for [{}]", c.getUsername());
                    WebUtils.putCredential(requestContext, new UsernamePasswordCredential(c.getUsername(), bean.getNewPassword()));

                    // update the user's password policy
                    if (passwordPolicyChanges.size() > 0) {
                        userManager.update(dn, passwordPolicyChanges);
                    }           

                    return new EventFactorySupport().event(this, PASSWORD_UPDATE_SUCCESS); 
                    
                case CONSTRAINT_VIOLATION:
                    message=PASSWORD_QUALITY_CHECK_FAILS;
                    logger.warn("Constraint violation updating a password - returning " + message);
                    break;
                    
                case INVALID_CREDENTIALS:
                    message = result.getResultCode().toString();
                    logger.warn("Invalid credentials updating a password - returning " + message);
                    break;
                    
                case UNWILLING_TO_PERFORM:   
                    message = INVALID_PASSWORD;
                    logger.warn("Unwilling to perform received whilst updating a password - returning " + message);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown response " + result.getResultCode());
                }

            // allow password policy details to override the exception message
            final PasswordPolicyControl control = (PasswordPolicyControl) result.getControl(PasswordPolicyControl.OID);

            if ((control != null) && (control.getError() != null)) {
                message = control.getError().getMessage();
            }
            
            return getErrorEvent(requestContext, PASSWORD_VALIDATION_FAILURE_CODE, message);

        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            return getErrorEvent(requestContext, "pm.updateFailure", DEFAULT_MESSAGE);
        }
    }

    private Event getErrorEvent(final RequestContext ctx, final String code, final String message) {  
    	
    	final MessageBuilder ERROR_MSG_BUILDER = new MessageBuilder().error();
    	
        if (PASSWORD_QUALITY_CHECK_FAILS.equals(message)){
            ctx.getMessageContext().addMessage(ERROR_MSG_BUILDER.code(message).build());
        } else {
            ctx.getMessageContext().addMessage(ERROR_MSG_BUILDER.code(code).defaultText(message).build());
        }     
        
        return error();
    }
}
