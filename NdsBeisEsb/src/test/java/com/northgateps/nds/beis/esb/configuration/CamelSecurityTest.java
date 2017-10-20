package com.northgateps.nds.beis.esb.configuration;

import org.junit.Test;

import com.northgateps.nds.platform.test.configuration.PlatformCamelSecurityTest;

/** 
 * Check camel route for security configuration issues.
 * @See PlatformCamelSecurityTest for more details 
 */
public class CamelSecurityTest extends PlatformCamelSecurityTest {

    /** 
     * List of routes by their ID that are exempt from requiring a <policy> tag.
     * Do not add to this list if you can at all help it.
     * @See PlatformCamelSecurityTest for more details.
     */
    {
        exceptionIds.add("testRouteForIncomingMsg");  // Test route for PE-41 will go away once Connect will send us response for a Incident
        exceptionIds.add("receivedIncidentUpdateRoute");  // started from ActiveMQ
        exceptionIds.add("purgeDocumentRoute");  // started from Camel timer
        exceptionIds.add("PurgeBatchJobRoute");  // started from Camel timer
    }
    
    @Test
    public void test() throws Exception {
        findAndTestCamelFiles("src/main/webapp/WEB-INF");
    }
}
