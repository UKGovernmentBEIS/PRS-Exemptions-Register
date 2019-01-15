package com.northgateps.nds.beis.esb.configuration;

import org.junit.Test;

import com.northgateps.nds.platform.test.configuration.PlatformCamelSecurityTest;

/** 
 * Check camel route for security configuration issues.
 * @See PlatformCamelSecurityTest for more details 
 */
public class CamelSecurityTest extends PlatformCamelSecurityTest {

    /** 
     * List of schemes of From uris in routes that are therefore exempt from requiring a <policy> tag because
     * they either are not run in the context of a user, or they are subroutes of a route that is either
     * verified as having a <policy> tag or is itself exempt.
     * 
     * @See PlatformCamelSecurityTest for more details.
     */
    {
        registerOmissionScheme("direct");   // (subRoutes)
        registerOmissionScheme("activemq"); // (routes started by connect messages)
        registerOmissionScheme("timer");    // (routes started by scheduler)
    }
    
    @Test
    public void test() throws Exception {
        findAndTestCamelFiles("src/main/webapp/WEB-INF");
    }
}
