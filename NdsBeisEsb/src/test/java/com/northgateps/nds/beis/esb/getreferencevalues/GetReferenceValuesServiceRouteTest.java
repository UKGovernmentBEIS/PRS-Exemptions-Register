package com.northgateps.nds.beis.esb.getreferencevalues;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;

import org.apache.camel.component.cxf.common.CxfPayload;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.northgateps.nds.beis.api.getreferencevalues.GetReferenceValuesNdsResponse;
import com.northgateps.nds.beis.esb.RouteTestUtils;

import com.northgateps.nds.platform.esb.util.xml.JaxbXmlMarshaller;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Unit test for the GetReferenceValues Service route
 * 
 * In previous versions of Camel the cache data seems to have been cleared between tests.  That doesn't
 * appear to be happening and after much effort, the easiest thing to do seems to be to split up the
 * tests into separate files.
 * If we could specify a different cache configuration that would work, but I can't work it out.
 */
@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GetReferenceValuesServiceRouteTest extends CamelSpringTestSupport {

    private static final NdsLogger LOGGER = NdsLogger.getLogger(GetReferenceValuesServiceRouteTest.class);
    
    private static final String routeNameUnderTest = "GetReferenceValuesRoute";
    private static final String MOCK_GSPS_RESPONSE_CHECK = "mock:gsps-response-check";
    private static final String MOCK_GSPS_REQUEST_CHECK = "mock:beis";
    private static final String TEST_START_NAME = "direct:start";

    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiEndpoint;
 
    /**
     * Creates the application context from our normal existing spring XML file
     * 
     * @return the application context
     */
    @Override
    protected AbstractApplicationContext createApplicationContext() {

      return RouteTestUtils.createApplicationContext(routeNameUnderTest);
    }
    
     /**
     * Runs the Get Reference Values Service camel route expecting a successful outcome
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void successPathTest() throws Exception {
        LOGGER.info("Starting success path test");
        LOGGER.info("Using endpoint " + context.resolvePropertyPlaceholders("{{apiGetReferenceValuesEndpoint}}") + " to run unit tests");
        // mock out bits of the route to test that we are sending the right content to BEIS
        // and that we simulate the response from BEIS, and finally so that we can check
        // that the response back to the caller is correct
        AdviceWith.adviceWith(context.getRouteDefinition(routeNameUnderTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_GSPS_RESPONSE_CHECK);
                replaceFromWith("direct:start");
                interceptSendToEndpoint("cxf:bean:beisGetReferenceValuesService").skipSendToOriginalEndpoint().to(
                        MOCK_GSPS_REQUEST_CHECK).process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody(CreateGetReferenceValuesTestData.createGetReferenceValuesResponse());
                    }
                });
            }
        });

        // manually start the camel context.
        context.start();

        MockEndpoint getSysParametersServiceMock = getMockEndpoint(MOCK_GSPS_REQUEST_CHECK);
        getSysParametersServiceMock.expectedHeaderReceived(CxfConstants.OPERATION_NAME, getOperationName());

        MockEndpoint gspsRouteResponseMock = getMockEndpoint(MOCK_GSPS_RESPONSE_CHECK);
        gspsRouteResponseMock.expectedMessageCount(1);
  
        gspsRouteResponseMock.expectedBodiesReceived(JaxbXmlMarshaller.convertToPrettyPrintXml(
                CreateGetReferenceValuesTestData.createGetReferenceValuesNdsResponse(), GetReferenceValuesNdsResponse.class));
        
        apiEndpoint.sendBody(CreateGetReferenceValuesTestData.createGetReferenceValuesNdsRequest());
        assertMockEndpointsSatisfied();
    }
    
    private final static String getOperationName() {
        return "GetReferenceValuesWSDL";
    }
}
