package com.northgateps.nds.beis.ui.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Adds a Content Security Policy HTTP header to all responses. It allows local
 * scripts and jWuery and no others Other required CDN-supplied libraries may be
 * added here if we trust them.
 * 
 * Intentionally blocks all inline style attributes.
 *
 */
public class ContentSecurityPolicyInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		ModelMap modelMap = modelAndView != null ? modelAndView.getModelMap() : null;
		String endorsedImgUrl = (String) (modelMap != null ? modelMap.get("endorsedImgUrl") : "");
		String endorsedScript = (String) (modelMap != null ? modelMap.get("endorsedScript") : "");
		endorsedImgUrl = (endorsedImgUrl != null) ? endorsedImgUrl : "";
		String commonRules = 
		        "default-src 'self'"
                + "; frame-ancestors 'none'"
                + "; child-src https://www.google.com/recaptcha/ "
                + "; frame-src https://www.google.com/recaptcha/ "
                + "; img-src 'self' data:" + endorsedImgUrl + " https://*.google-analytics.com https://*.googletagmanager.com"
                + "; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com/css"
                + "; font-src data: https://fonts.gstatic.com/s/ptsans/"
				+ "; connect-src https://*.google-analytics.com https://*.analytics.google.com https://*.googletagmanager.com"
                + "; object-src 'none'";

		if (modelMap != null && Boolean.TRUE.equals(modelMap.get("disallowAllJS"))) {
            response.addHeader("Content-Security-Policy", commonRules
                    + "; script-src 'none'");
		} else {
    		response.addHeader("Content-Security-Policy", commonRules
					+ "; script-src 'self'" // '" + cspNonce + "'"
    					+ " https://*.googletagmanager.com"
    					+ " https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.js"
    					+ " https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"
    					+ " https://code.jquery.com/ui/1.13.0/jquery-ui.js"
    					+ " https://code.jquery.com/ui/1.13.0/jquery-ui.min.js"
    					+ " https://www.google.com/recaptcha/"
    					+ " https://www.gstatic.com/recaptcha/ " + endorsedScript); 
		}
		super.postHandle(request, response, handler, modelAndView);
	}

}