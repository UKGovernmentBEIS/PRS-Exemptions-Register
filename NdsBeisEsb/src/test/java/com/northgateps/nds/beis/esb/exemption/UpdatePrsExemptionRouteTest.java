package com.northgateps.nds.beis.esb.exemption;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.northgateps.nds.beis.api.UpdateExemptionDetails;
import com.northgateps.nds.beis.api.updateexemptiondetails.UpdateExemptionDetailsNdsRequest;
import com.northgateps.nds.beis.api.updateexemptiondetails.UpdateExemptionDetailsNdsResponse;
import com.northgateps.nds.beis.backoffice.service.updateprsexemption.UpdatePRSExemptionResponse;
import com.northgateps.nds.beis.esb.RouteTestUtils;
import com.northgateps.nds.platform.api.NdsErrorResponse;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Unit test for the UpdateExemption Service route
 * 
 */
@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)

public class UpdatePrsExemptionRouteTest extends CamelSpringTestSupport {

    private static final NdsLogger logger = NdsLogger.getLogger(UpdatePrsExemptionRouteTest.class);
    
    private static final String routeNameUnderTest = "prsExemptionUpdateRoute";
    
    private static final String TEST_START_NAME = "direct:startPrsExemptionUpdate";

    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiEndpoint;

    
    private static final String MOCK_UPDATE_EXEMPTION_REQUEST_CHECK = "mock:prsExemptionUpdateRoute";

    
    private static final String MOCK_UPDATE_EXEMPTION_RESPONSE_CHECK = "mock:prsExemptionUpdateRoute-response-check";
    private static final String SKIP_EndPoint = "cxf:bean:prsExemptionUpdateServiceEndPoint";

    @Override
    protected AbstractApplicationContext createApplicationContext() {
		return RouteTestUtils.createApplicationContext(routeNameUnderTest);
    }

    @Test
    public void successPathTest() throws Exception {
        logger.info("Starting success path test");
        logger.info("Using endpoint " + context.resolvePropertyPlaceholders("{{apiPRSExemptionUpdateEndpoint}}")
                + " to run unit tests");
        AdviceWith.adviceWith(context.getRouteDefinition(routeNameUnderTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_UPDATE_EXEMPTION_RESPONSE_CHECK);
                replaceFromWith(TEST_START_NAME);

                interceptSendToEndpoint(SKIP_EndPoint).skipSendToOriginalEndpoint().to(
                        MOCK_UPDATE_EXEMPTION_REQUEST_CHECK).process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {

                        UpdatePRSExemptionResponse appResponse = createUpdatePrsExemptionResponse();

                        exchange.getIn().setBody(appResponse);
                    }

                    
                    private UpdatePRSExemptionResponse createUpdatePrsExemptionResponse() {
                        UpdatePRSExemptionResponse updatePRSExemptionResponse = new UpdatePRSExemptionResponse();
                        updatePRSExemptionResponse.setSuccess(true);
                        return updatePRSExemptionResponse;
                    }

                });

            }
        });

        context.start();
        NdsErrorResponse r = (NdsErrorResponse) apiEndpoint.requestBody(createUpdateExemptionDetailsNdsRequest());
        
        if (r instanceof UpdateExemptionDetailsNdsResponse) {
        	UpdateExemptionDetailsNdsResponse response = (UpdateExemptionDetailsNdsResponse)r; 
        	assertTrue(response.isSuccess());
        	assertMockEndpointsSatisfied();
        } else {
        	fail(r.getNdsMessages().getExceptionCaught());
        }
    }

    private UpdateExemptionDetailsNdsRequest createUpdateExemptionDetailsNdsRequest() {
        UpdateExemptionDetailsNdsRequest request = new UpdateExemptionDetailsNdsRequest();
        request.setUserName("selenium");
        request.setTenant("beis");
        UpdateExemptionDetails updateExemptionDetails = new UpdateExemptionDetails();        
        updateExemptionDetails.setReferenceId("894944");
        updateExemptionDetails.setEndDate(ZonedDateTime.of(2018, 01, 10, 0, 0, 0,0,ZoneId.systemDefault()));
        request.setUpdateExemptionDetails(updateExemptionDetails);
        return request;
    }

}
