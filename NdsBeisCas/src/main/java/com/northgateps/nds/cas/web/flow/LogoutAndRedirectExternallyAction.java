package com.northgateps.nds.cas.web.flow;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apereo.cas.CasProtocolConstants;
import org.apereo.cas.CentralAuthenticationService;
import org.apereo.cas.authentication.principal.Service;
import org.apereo.cas.authentication.principal.ServiceFactory;
import org.apereo.cas.authentication.principal.WebApplicationService;
import org.apereo.cas.authentication.principal.WebApplicationServiceFactory;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.configuration.model.core.logout.LogoutProperties;
import org.apereo.cas.logout.LogoutExecutionPlan;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.RegisteredServiceProperty;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.web.cookie.CasCookieBuilder;
import org.apereo.cas.web.flow.logout.LogoutAction;
import org.apereo.cas.web.support.ArgumentExtractor;
import org.apereo.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Version of CAS's LogoutAction adding the ability to redirect to a URL configured
 * for the service other than the actual service URL.
 * 
 * Extends LogoutAction to ensure we get future changes to that action.
 *
 * To configure :
 * 
 * cas.logout.followServiceRedirects=true must be set in cas.properties for redirects to work.
 * 
 * Set the following in the service .json file to redirect to https://www.northgateps.com :
 * 
 *  "properties" : {
 *      "@class" : "java.util.HashMap",
 *      "logoutRedirectUrl" : {
 *          "@class" : "org.apereo.cas.services.DefaultRegisteredServiceProperty",
 *          "values" : [ "java.util.HashSet", [ "https://www.northgateps.com" ] ]
 *      }
 *  }
 *
 * @see com.northgateps.nds.cas.configuration.model.core.RedirectProperties
 * @see com.northgateps.nds.cas.config.NdsCasWebflowContextConfiguration
 */
public class LogoutAndRedirectExternallyAction extends LogoutAction {
    private static final Logger logger = LoggerFactory.getLogger(LogoutAndRedirectExternallyAction.class);
    
    private final ServicesManager servicesManager;

    private final LogoutProperties logoutProperties;
    
    /** 
     * Constructs a new Action that will redirect to the configured per service logoutRedirectUrl. 
     */
    public LogoutAndRedirectExternallyAction(final CentralAuthenticationService centralAuthenticationService,
          final CasCookieBuilder ticketGrantingTicketCookieGenerator,
          final ArgumentExtractor argumentExtractor, final ServicesManager servicesManager,
          final LogoutExecutionPlan logoutExecutionPlan, final CasConfigurationProperties casProperties) {
        
        super(centralAuthenticationService, ticketGrantingTicketCookieGenerator, argumentExtractor, servicesManager, logoutExecutionPlan, casProperties);
        this.servicesManager = servicesManager;
        this.logoutProperties = casProperties.getLogout();
    }

    @Override
    protected Event doInternalExecute(final HttpServletRequest request, final HttpServletResponse response,
            final RequestContext context) {
        
        // get the result from CAS's version
        Event event = super.doInternalExecute(request, response, context);
        
        // update the URL to redirect to based on which service it is
        final String service = request.getParameter(CasProtocolConstants.PARAMETER_SERVICE);

        if (logoutProperties.isFollowServiceRedirects() && service != null) {
            final Service webAppService = new WebApplicationServiceFactory().createService(service);
            final RegisteredService rService = this.servicesManager.findServiceBy(webAppService);

            // get service properties and redirect if settings are correct and properties exist
            if (rService != null && rService.getAccessStrategy().isServiceAccessAllowed()) {
                Map<String, RegisteredServiceProperty> properties = rService.getProperties();
                RegisteredServiceProperty redirectProperty = properties.get("logoutRedirectUrl");
                
                if (redirectProperty != null) {
                    String redirectUrl = redirectProperty.getValue();
                    logger.debug("Logout redirecting to " + redirectUrl);        
                    WebUtils.putLogoutRedirectUrl(context, redirectUrl);
                }
            }
        }

        return event;
    }
}
