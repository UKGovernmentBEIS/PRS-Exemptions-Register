package com.northgateps.nds.beis.ui.view.javascript.base;

import com.northgateps.nds.platform.ui.selenium.core.AbstractSeleniumTest;

/**
 * A base class with a method to get the URL to open to view the required page
 * extend this class when your page isn't part of the main application and has
 * its own URL
 *
 */
public abstract class AlternateUrlBaseSteps {

	/**
	 * Get the URL to the required page
	 * @param pageName the name of the page
	 * @return the full URL
	 */
	protected String GetUrl(String pageName) {

        String baseUrlFromPom = System.getProperty("baseUrl");

        if (baseUrlFromPom == null) {
            final String protocol = AbstractSeleniumTest.getPomProperty("nds.integrationTest.protocol");
            final String host = AbstractSeleniumTest.getPomProperty("nds.integrationTest.serverHost");
            final String port = AbstractSeleniumTest.getPomProperty("nds.integrationTest.serverPort");
            final String path = AbstractSeleniumTest.getPomProperty("nds.integrationTest.serverPath");            
            baseUrlFromPom = protocol + "://" + host + ":" + port + "/" + path + "/";
        }

        baseUrlFromPom = baseUrlFromPom.substring(0, baseUrlFromPom.indexOf("used-service-before"));
        baseUrlFromPom = baseUrlFromPom + pageName;

        return baseUrlFromPom;
    }
}
