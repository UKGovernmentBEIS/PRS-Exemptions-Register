package com.northgateps.nds.beis.esb;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;


import com.northgateps.nds.platform.logger.NdsLogger;


/**
 * Integration test class to send a message to purge route
 */

@Ignore
public class PurgeDocumentsRouteTest extends CamelSpringTestSupport{
    private static final NdsLogger logger = NdsLogger.getLogger(PurgeDocumentsRouteTest.class);

    public static String TENANT = "BEIS";

    private static final String routeNameUnderTest = "purgeDocumentRoute";
    private static final String TEST_START_NAME = "direct:purgeDocumentRoute";

    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiPurgeDocumentsRoute;

    
    
    /**
     * Creates Spring's application context.
     */
    @Override
    protected AbstractApplicationContext createApplicationContext() {
       return RouteTestUtils.createApplicationContext(routeNameUnderTest);
    }

    @Test
    public void testPurgeDocuments() throws Exception {

        logger.info("PurgeDocumentsRouteTest test started");

        AdviceWith.adviceWith(context.getRouteDefinition(routeNameUnderTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {

            }
        });

        context.start();

        apiPurgeDocumentsRoute.sendBody("start");

        assertMockEndpointsSatisfied();
    }
}
