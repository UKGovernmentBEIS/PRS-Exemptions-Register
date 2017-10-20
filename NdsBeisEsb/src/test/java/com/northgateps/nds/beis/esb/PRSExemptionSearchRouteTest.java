package com.northgateps.nds.beis.esb;

import java.math.BigInteger;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import com.northgateps.nds.beis.api.ExemptionSearch;
import com.northgateps.nds.beis.api.PRSExemptionSearchNdsRequest;
import com.northgateps.nds.beis.api.PRSExemptionSearchNdsResponse;
import com.northgateps.nds.beis.backoffice.service.prsexemptionsearch.PRSExemptionSearchResponse;
import com.northgateps.nds.beis.backoffice.service.prsexemptionsearch.PRSExemptionSearchResponse.Exemptions;
import com.northgateps.nds.beis.backoffice.service.prsexemptionsearch.PRSExemptionSearchResponse.Exemptions.Exemption;
import com.northgateps.nds.platform.esb.camel.NdsFileSystemXmlApplicationContext;
import com.northgateps.nds.platform.esb.security.MockAuthentication;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Unit tests for the PRS Exemption Search Route
 */
@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PRSExemptionSearchRouteTest extends CamelSpringTestSupport {

    private static final NdsLogger LOGGER = NdsLogger.getLogger(PRSExemptionSearchRouteTest.class);

    private static final String routeNameUnderTest = "prsExemptionSearchRoute";

    private static final String TEST_START_NAME = "direct:start";

    private static final String SKIP_EndPoint = "cxf:bean:prsExemptionSearchServiceEndPoint";

    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiEndpoint;

    @Override
    protected AbstractApplicationContext createApplicationContext() {

        // grant test role based security access
        MockAuthentication.setMockAuthentication("ROLE_BEIS_UI");

        return new NdsFileSystemXmlApplicationContext(
                new String[] { "src/main/webapp/WEB-INF/applicationContext-security.xml",
                        "src/main/webapp/WEB-INF/beis-camel-context.xml" });
    }

    private static final String MOCK_BEIS_REQUEST_CHECK = "mock:beis";

    private static final String MOCK_BEIS_RESPONSE_CHECK = "mock:beis-response-check";

    /**
     * Runs the BEIS camel route expecting a successful outcome
     */
    @Test
    public void successPathTestWithPostcode() throws Exception {
        PRSExemptionSearchNdsRequest prsExemptionSearchNdsRequest = new PRSExemptionSearchNdsRequest();
        ExemptionSearch exemptionSearch = new ExemptionSearch();
        exemptionSearch.setExemptionPostcodeCriteria("RG1 1ET");
        prsExemptionSearchNdsRequest.setExemptionSearch(exemptionSearch);
        successPathExemptionSearch(prsExemptionSearchNdsRequest);
    }

    @Test
    public void successPathTestWithLandlordsName() throws Exception {
        PRSExemptionSearchNdsRequest prsExemptionSearchNdsRequest = new PRSExemptionSearchNdsRequest();
        ExemptionSearch exemptionSearch = new ExemptionSearch();
        exemptionSearch.setExemptionLandlordsNameCriteria("TestOrg");
        prsExemptionSearchNdsRequest.setExemptionSearch(exemptionSearch);
        successPathExemptionSearch(prsExemptionSearchNdsRequest);

    }

    @Test
    public void successPathTestWithTown() throws Exception {
        PRSExemptionSearchNdsRequest prsExemptionSearchNdsRequest = new PRSExemptionSearchNdsRequest();
        ExemptionSearch exemptionSearch = new ExemptionSearch();
        exemptionSearch.setTown("Bracknell");
        prsExemptionSearchNdsRequest.setExemptionSearch(exemptionSearch);
        successPathExemptionSearch(prsExemptionSearchNdsRequest);
    }

    @Test
    public void successPathTestWithPropertyDetails() throws Exception {
        PRSExemptionSearchNdsRequest prsExemptionSearchNdsRequest = new PRSExemptionSearchNdsRequest();
        ExemptionSearch exemptionSearch = new ExemptionSearch();
        exemptionSearch.setExemptPropertyDetails("Unit 1, Bracknell Beeches");
        prsExemptionSearchNdsRequest.setExemptionSearch(exemptionSearch);
        successPathExemptionSearch(prsExemptionSearchNdsRequest);
    }

    @Test
    public void successPathTestPropertyExemptionType() throws Exception {
        PRSExemptionSearchNdsRequest prsExemptionSearchNdsRequest = new PRSExemptionSearchNdsRequest();
        ExemptionSearch exemptionSearch = new ExemptionSearch();
        exemptionSearch.setService("PRSN");
        exemptionSearch.setExemptionType_PRSN("WALL");
        prsExemptionSearchNdsRequest.setExemptionSearch(exemptionSearch);
        successPathExemptionSearch(prsExemptionSearchNdsRequest);

    }

    
    @Test
    public void noRecordFoundTest() throws Exception {
        LOGGER.info("Starting success path test");
        LOGGER.info("Using endpoint " + context.resolvePropertyPlaceholders("{{apiPRSExemptionSearchEndpoint}}")
                + " to run unit tests");
        context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_BEIS_RESPONSE_CHECK);
                replaceFromWith("direct:start");

                interceptSendToEndpoint(SKIP_EndPoint).skipSendToOriginalEndpoint().to(MOCK_BEIS_REQUEST_CHECK).process(
                        new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {

                        PRSExemptionSearchResponse appResponse = createPRSExemptionSearchWithNoRecordAndEmptyFieldResponse();

                        exchange.getIn().setBody(appResponse);
                    }
                });

            }
        });

        context.start();
        PRSExemptionSearchNdsResponse response = (PRSExemptionSearchNdsResponse) apiEndpoint.requestBody(
                getRequestForPRSExemptionSearchPostcode());

        assertFalse(response.isSuccess());
        assertNull(response.getGetExemptionSearchResponseDetail());
        assertMockEndpointsSatisfied();

    }

    private PRSExemptionSearchNdsRequest getRequestForPRSExemptionSearchPostcode() {

        PRSExemptionSearchNdsRequest prsExemptionSearchNdsRequest = new PRSExemptionSearchNdsRequest();
        ExemptionSearch exemptionSearch = new ExemptionSearch();
        exemptionSearch.setExemptionPostcodeCriteria("RG11ET");
        prsExemptionSearchNdsRequest.setExemptionSearch(exemptionSearch);
        return prsExemptionSearchNdsRequest;

    }

    private PRSExemptionSearchResponse createPRSExemptionSearchResponse() {
        PRSExemptionSearchResponse prsExemptionSearchNdsResponse = new PRSExemptionSearchResponse();
        prsExemptionSearchNdsResponse.setSuccess(true);
        Exemptions exemptions = new Exemptions();
        Exemption exemptionData = new Exemption();
        exemptionData.setAddress("test");
        exemptionData.setExemptionRefNo(BigInteger.valueOf(12));
        exemptionData.setLandlordName("tree");
        exemptions.getExemption().add(exemptionData);
        prsExemptionSearchNdsResponse.setExemptions(exemptions);

        return prsExemptionSearchNdsResponse;
    }

    private PRSExemptionSearchResponse createPRSExemptionSearchWithNoRecordAndEmptyFieldResponse() {
        PRSExemptionSearchResponse prsExemptionSearchNdsResponse = new PRSExemptionSearchResponse();
        prsExemptionSearchNdsResponse.setSuccess(false);
        return prsExemptionSearchNdsResponse;
    }
    
    public void successPathExemptionSearch(PRSExemptionSearchNdsRequest prsExemptionSearchNdsRequest) throws Exception {
        LOGGER.info("Starting success path test");
        LOGGER.info("Using endpoint " + context.resolvePropertyPlaceholders("{{apiPRSExemptionSearchEndpoint}}")
                + " to run unit tests");
        context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_BEIS_RESPONSE_CHECK);
                replaceFromWith("direct:start");

                interceptSendToEndpoint(SKIP_EndPoint).skipSendToOriginalEndpoint().to(MOCK_BEIS_REQUEST_CHECK).process(
                        new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {

                        PRSExemptionSearchResponse appResponse = createPRSExemptionSearchResponse();

                        exchange.getIn().setBody(appResponse);
                    }
                });

            }
        });

        context.start();
        PRSExemptionSearchNdsResponse response = (PRSExemptionSearchNdsResponse) apiEndpoint.requestBody(
                prsExemptionSearchNdsRequest);

        assertTrue(response.isSuccess());
        assertNotNull(response.getGetExemptionSearchResponseDetail());
        assertMockEndpointsSatisfied();

    }


}
