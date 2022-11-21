package com.northgateps.nds.beis.esb;

import java.math.BigInteger;

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
import com.northgateps.nds.beis.api.PRSPenaltySearchNdsRequest;
import com.northgateps.nds.beis.api.PRSPenaltySearchNdsResponse;
import com.northgateps.nds.beis.api.PenaltySearch;
import com.northgateps.nds.beis.backoffice.service.prspenaltysearch.PRSPenaltySearchResponse;
import com.northgateps.nds.beis.backoffice.service.prspenaltysearch.PRSPenaltySearchResponse.Penalties;
import com.northgateps.nds.beis.backoffice.service.prspenaltysearch.PRSPenaltySearchResponse.Penalties.Penalty;

import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Unit tests for the PRS Penalty Search Route
 */
@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PRSPenaltySearchRouteTest extends CamelSpringTestSupport {

    private static final NdsLogger LOGGER = NdsLogger.getLogger(PRSPenaltySearchRouteTest.class);

    private static final String routeNameUnderTest = "prsPenaltySearchRoute";

    private static final String TEST_START_NAME = "direct:start";

    private static final String SKIP_EndPoint = "cxf:bean:prsPenaltySearchServiceEndPoint";

    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiEndpoint;

    @Override
    protected AbstractApplicationContext createApplicationContext() {

      return RouteTestUtils.createApplicationContext(routeNameUnderTest);
    }

    private static final String MOCK_BEIS_REQUEST_CHECK = "mock:beis";

    private static final String MOCK_BEIS_RESPONSE_CHECK = "mock:beis-response-check";

    /**
     * Runs the BEIS camel route expecting a successful outcome
     *
     * @throws Exception if an error occurs
     */

    @Test
    public void successPathTestWithPostcode() throws Exception {
        PRSPenaltySearchNdsRequest prsPenaltySearchNdsRequest = new PRSPenaltySearchNdsRequest();
        PenaltySearch penaltySearch = new PenaltySearch();
        penaltySearch.setPenaltyPostcodesCriteria("RG1 1PB");
        prsPenaltySearchNdsRequest.setPenaltySearch(penaltySearch);
        successPathPenaltySearch(prsPenaltySearchNdsRequest);
    }

    @Test
    public void successPathTestWithLandlordsName() throws Exception {
        PRSPenaltySearchNdsRequest prsPenaltySearchNdsRequest = new PRSPenaltySearchNdsRequest();
        PenaltySearch penaltySearch = new PenaltySearch();
        penaltySearch.setPenaltyLandlordsNameCriteria("TestOrg");
        prsPenaltySearchNdsRequest.setPenaltySearch(penaltySearch);
        successPathPenaltySearch(prsPenaltySearchNdsRequest);

    }

    @Test
    public void successPathTestWithTown() throws Exception {
        PRSPenaltySearchNdsRequest prsPenaltySearchNdsRequest = new PRSPenaltySearchNdsRequest();
        PenaltySearch penaltySearch = new PenaltySearch();
        penaltySearch.setTown("Bracknell");
        prsPenaltySearchNdsRequest.setPenaltySearch(penaltySearch);
        successPathPenaltySearch(prsPenaltySearchNdsRequest);
    }

    @Test
    public void successPathTestWithPropertyDetails() throws Exception {
        PRSPenaltySearchNdsRequest prsPenaltySearchNdsRequest = new PRSPenaltySearchNdsRequest();
        PenaltySearch penaltySearch = new PenaltySearch();
        penaltySearch.setRentalPropertyDetails("Unit 1, Bracknell Beeches");
        prsPenaltySearchNdsRequest.setPenaltySearch(penaltySearch);
        successPathPenaltySearch(prsPenaltySearchNdsRequest);
    }

    @Test
    public void successPathTestPropertyExemptionType() throws Exception {
        PRSPenaltySearchNdsRequest prsPenaltySearchNdsRequest = new PRSPenaltySearchNdsRequest();
        PenaltySearch penaltySearch = new PenaltySearch();
        penaltySearch.setPropertyType("PRSD");
        penaltySearch.setPenaltyType_PRSD("SUBLESS3");
        prsPenaltySearchNdsRequest.setPenaltySearch(penaltySearch);
        successPathPenaltySearch(prsPenaltySearchNdsRequest);

    }
    
    @Test
    public void successPathTestPenaltyRefNo() throws Exception {
        PRSPenaltySearchNdsRequest prsPenaltySearchNdsRequest = new PRSPenaltySearchNdsRequest();
        PenaltySearch penaltySearch = new PenaltySearch();
        penaltySearch.setPenaltyRefNo("12");
        prsPenaltySearchNdsRequest.setPenaltySearch(penaltySearch);
        successPathPenaltySearch(prsPenaltySearchNdsRequest);

    }
    
    
    @Test
    public void noRecordFoundTest() throws Exception {
        LOGGER.info("Starting success path test");
        LOGGER.info("Using endpoint " + context.resolvePropertyPlaceholders("{{apiPRSPenaltySearchEndpoint}}")
                + " to run unit tests");
        AdviceWith.adviceWith(context.getRouteDefinition(routeNameUnderTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_BEIS_RESPONSE_CHECK);
                replaceFromWith("direct:start");

                interceptSendToEndpoint(SKIP_EndPoint).skipSendToOriginalEndpoint().to(MOCK_BEIS_REQUEST_CHECK).process(
                        new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {

                        PRSPenaltySearchResponse appResponse = createPRSPenaltySearchWithNoRecordResponse();

                        exchange.getIn().setBody(appResponse);
                    }
                });
            }
        });

        context.start();
        PRSPenaltySearchNdsResponse response = (PRSPenaltySearchNdsResponse) apiEndpoint.requestBody(
                getPostCodeRequestForPRSPenaltySearch());

        assertFalse(response.isSuccess());
        assertNull(response.getGetPenaltySearchResponseDetail());
        assertMockEndpointsSatisfied();

    }
    
   

    private PRSPenaltySearchNdsRequest getPostCodeRequestForPRSPenaltySearch() {

        PRSPenaltySearchNdsRequest prsPenaltySearchNdsRequest = new PRSPenaltySearchNdsRequest();
        PenaltySearch penaltySearch = new PenaltySearch();
        penaltySearch.setPenaltyPostcodesCriteria("xl38da");
        penaltySearch.setPropertyType("");
        prsPenaltySearchNdsRequest.setPenaltySearch(penaltySearch);
        return prsPenaltySearchNdsRequest;

    }
    
    private PRSPenaltySearchResponse createPRSPenaltySearchResponse() {
        PRSPenaltySearchResponse prsPenaltySearchNdsResponse = new PRSPenaltySearchResponse();
        prsPenaltySearchNdsResponse.setSuccess(true);
        Penalties penalties = new Penalties();
        Penalty PenaltyData = new Penalty();
        PenaltyData.setAddress("test");
        PenaltyData.setPenaltyRefNo(BigInteger.valueOf(127879024));
        PenaltyData.setLandLordName("tree");
        penalties.getPenalty().add(PenaltyData);
        prsPenaltySearchNdsResponse.setPenalties(penalties);

        return prsPenaltySearchNdsResponse;
    }

    private PRSPenaltySearchResponse createPRSPenaltySearchWithNoRecordResponse() {
        PRSPenaltySearchResponse prsPenaltySearchNdsResponse = new PRSPenaltySearchResponse();
        prsPenaltySearchNdsResponse.setSuccess(false);
        return prsPenaltySearchNdsResponse;
    }

    public void successPathPenaltySearch(PRSPenaltySearchNdsRequest prsPenaltySearchNdsRequest) throws Exception {
        LOGGER.info("Starting success path test");
        LOGGER.info("Using endpoint " + context.resolvePropertyPlaceholders("{{apiPRSPenaltySearchEndpoint}}")
                + " to run unit tests");
        AdviceWith.adviceWith(context.getRouteDefinition(routeNameUnderTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_BEIS_RESPONSE_CHECK);
                replaceFromWith("direct:start");

                interceptSendToEndpoint(SKIP_EndPoint).skipSendToOriginalEndpoint().to(MOCK_BEIS_REQUEST_CHECK).process(
                        new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {

                        PRSPenaltySearchResponse appResponse = createPRSPenaltySearchResponse();

                        exchange.getIn().setBody(appResponse);
                    }
                });
            }
        });

        context.start();
        PRSPenaltySearchNdsResponse response = (PRSPenaltySearchNdsResponse) apiEndpoint.requestBody(
                prsPenaltySearchNdsRequest);

        assertTrue(response.isSuccess());
        assertNotNull(response.getGetPenaltySearchResponseDetail());
        assertMockEndpointsSatisfied();

    }

    public void successPathPenaltySearchPenaltyRefNo(PRSPenaltySearchNdsRequest prsPenaltySearchNdsRequest) throws Exception {
        LOGGER.info("Starting success path test");
        LOGGER.info("Using endpoint " + context.resolvePropertyPlaceholders("{{apiPRSPenaltySearchEndpoint}}")
                + " to run unit tests");
        AdviceWith.adviceWith(context.getRouteDefinition(routeNameUnderTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_BEIS_RESPONSE_CHECK);
                replaceFromWith("direct:start");

                interceptSendToEndpoint(SKIP_EndPoint).skipSendToOriginalEndpoint().to(MOCK_BEIS_REQUEST_CHECK).process(
                        new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {

                        PRSPenaltySearchResponse appResponse = createPRSPenaltySearchResponse();

                        exchange.getIn().setBody(appResponse);
                    }
                });
            }
        });

        context.start();
        PRSPenaltySearchNdsResponse response = (PRSPenaltySearchNdsResponse) apiEndpoint.requestBody(
                prsPenaltySearchNdsRequest);

        assertTrue(response.isSuccess());
        assertNotNull(response.getGetPenaltySearchResponseDetail());
        assertEquals("Penalty search response size should be one", Integer.parseInt("1"),
                response.getGetPenaltySearchResponseDetail().getPenalties().size());  
        assertMockEndpointsSatisfied();

    }

}
