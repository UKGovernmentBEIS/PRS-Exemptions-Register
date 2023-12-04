package com.northgateps.nds.beis.esb.dashboard;

import java.math.BigInteger;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

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

import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.beis.api.dashboard.DashboardDetails;
import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsRequest;
import com.northgateps.nds.beis.api.dashboard.GetPRSAccountExemptionsNdsResponse;
import com.northgateps.nds.beis.backoffice.service.getpartydetails.GetPartyDetailsRequest;
import com.northgateps.nds.beis.backoffice.service.getpartydetails.GetPartyDetailsResponse;
import com.northgateps.nds.beis.backoffice.service.getprsaccountexemptions.GetPRSAccountExemptionsRequest;
import com.northgateps.nds.beis.backoffice.service.getprsaccountexemptions.GetPRSAccountExemptionsResponse;
import com.northgateps.nds.beis.backoffice.service.getprsaccountexemptions.GetPRSAccountExemptionsResponse.Exemptions;
import com.northgateps.nds.beis.backoffice.service.getprsaccountexemptions.GetPRSAccountExemptionsResponse.Exemptions.Exemption;
import com.northgateps.nds.platform.api.NdsErrorResponse;
import com.northgateps.nds.platform.esb.camel.NdsFileSystemXmlApplicationContext;
import com.northgateps.nds.platform.esb.security.MockAuthentication;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * 
 * Class to test the Dashboard details route
 *
 */
@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DashboardDetailsRouteTest extends CamelSpringTestSupport {

    private static final NdsLogger logger = NdsLogger.getLogger(DashboardDetailsRouteTest.class);
    private static final String routeNameUnderTest = "getPrsAccountExemptionsServiceRoute";
    private static final String TEST_START_NAME = "direct:startDashboardDetails";
    protected static final String MOCK_GET_EXEMPTIONS = "mock:get-exemptions";
    protected static final String MOCK_GET_PARTYDETAILS = "mock:get-partyDetails";
    
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

    @Test
    public void SuccessPathTestWithLandlord() throws Exception {
        logger.info("dashboardDetails success test started");

        AdviceWith.adviceWith(context.getRouteDefinition(routeNameUnderTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith(TEST_START_NAME);

                interceptSendToEndpoint("cxf:bean:beisGetPrsAccountExemptionsService").skipSendToOriginalEndpoint().to(MOCK_GET_EXEMPTIONS).process(new Processor() {
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            //check that the request got the party ref added
                            GetPRSAccountExemptionsRequest request = (GetPRSAccountExemptionsRequest) exchange.getIn().getBody();
                            assertEquals("Checking party ref", BigInteger.valueOf(9000), request.getLandlordPartyRef());
                            
                            // Mock out a response
                            exchange.getIn().setBody(createGetPrsAccountExemptionsResponse());
                        }
                    });

                // This sub route is invoked by get prs exemptions and requires mocking
                AdviceWith.adviceWith(context.getRouteDefinition("getPartyDetails_SubRouteForRetrievingRegistrationDetails"), context, new AdviceWithRouteBuilder() {
                	
                    @Override
                    public void configure() throws Exception {
                        interceptSendToEndpoint("cxf:bean:beisGetPartyDetailsService").skipSendToOriginalEndpoint().to(MOCK_GET_PARTYDETAILS).process(new Processor() {
                        	
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                //check that the request got the party ref added
                                GetPartyDetailsRequest request = (GetPartyDetailsRequest) exchange.getIn().getBody();
                                assertEquals("Checking party ref", BigInteger.valueOf(9000), request.getPartyRef());
                                
                                // Mock out a response 
                                exchange.getIn().setBody(createGetPartyDetailsResponseForLandlord());
                            }
                        });
                    }
                });
                
            }
        });

        context.start();
        
        NdsErrorResponse response = (NdsErrorResponse) apiEndpoint.requestBody(createDashboardNdsRequest("seleniumorg"));
        assertTrue(response.isSuccess());
        
        if (response instanceof GetPRSAccountExemptionsNdsResponse) {
        	GetPRSAccountExemptionsNdsResponse exemptionResponse = (GetPRSAccountExemptionsNdsResponse) response;
        	assertNotNull(exemptionResponse.getDashboardDetails());
        	checkValues(exemptionResponse.getDashboardDetails());
        	assertEquals(UserType.LANDLORD, exemptionResponse.getUserType());
        } else {
        	fail("Response was not a GetPRSAccountExemptionsNdsResponse : " + response.getNdsMessages());
        }
        assertMockEndpointsSatisfied();
    }
    
    @Test
    public void SuccessPathTestWithAgent() throws Exception {
        logger.info("dashboardDetails success test started");

        AdviceWith.adviceWith(context.getRouteDefinition(routeNameUnderTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith(TEST_START_NAME);

                interceptSendToEndpoint("cxf:bean:beisGetPrsAccountExemptionsService").skipSendToOriginalEndpoint().to(MOCK_GET_EXEMPTIONS).process(new Processor() {

                            @Override
                            public void process(Exchange exchange) throws Exception {

                                //check that the request got the party ref added
                                GetPRSAccountExemptionsRequest request = (GetPRSAccountExemptionsRequest) exchange.getIn().getBody();
                                assertEquals("Checking party ref", BigInteger.valueOf(10042), request.getAgentPartyRef());
                                
                                // Mock out a response
                                exchange.getIn().setBody(createGetPrsAccountExemptionsResponse());
                            }
                        });

                // This sub route is invoked by get prs exemptions and requires mocking
                AdviceWith.adviceWith(context.getRouteDefinition("getPartyDetails_SubRouteForRetrievingRegistrationDetails"), context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        interceptSendToEndpoint("cxf:bean:beisGetPartyDetailsService").skipSendToOriginalEndpoint().to(MOCK_GET_PARTYDETAILS).process(new Processor() {

                            @Override
                            public void process(Exchange exchange) throws Exception {
                                
                            	// check that the request got the party ref added
                                GetPartyDetailsRequest request = (GetPartyDetailsRequest) exchange.getIn().getBody();
                                assertEquals("Checking party ref", BigInteger.valueOf(10042), request.getPartyRef());
                                
                                // Mock out a response
                                exchange.getIn().setBody(createGetPartyDetailsResponseForAgent());
                            }
                        });
                    }
                });
            }
        });

        context.start();
        
        NdsErrorResponse response = (NdsErrorResponse) apiEndpoint.requestBody(createDashboardNdsRequest("seleniumagentuser"));
        assertTrue(response.isSuccess());
        
        if (response instanceof GetPRSAccountExemptionsNdsResponse) {
        	GetPRSAccountExemptionsNdsResponse exemptionResponse = (GetPRSAccountExemptionsNdsResponse) response;
        	assertNotNull(exemptionResponse.getDashboardDetails());
        	checkValues(exemptionResponse.getDashboardDetails());
        	assertEquals(UserType.AGENT, exemptionResponse.getUserType());
        } else {
			fail("Response was not a GetPRSAccountExemptionsNdsResponse : " + response.getNdsMessages());
		}
        
        assertMockEndpointsSatisfied();
    }

    static GetPRSAccountExemptionsNdsRequest createDashboardNdsRequest(String username) {

        GetPRSAccountExemptionsNdsRequest request = new GetPRSAccountExemptionsNdsRequest();
        request.setUsername(username);
        request.setTenant("BEIS");

        return request;
    }
    
    static GetPRSAccountExemptionsResponse createGetPrsAccountExemptionsResponse() throws DatatypeConfigurationException {
        GetPRSAccountExemptionsResponse response = new GetPRSAccountExemptionsResponse();

        Exemptions exemptions = new Exemptions();
        response.setExemptions(exemptions);

        // current exemption
        Exemption exemption = new Exemption();
        exemption.setAddress("TEST ADDRESS");
        //Use hard coded dates to avoid any BST conversion issues to do with adding 5 years.
        exemption.setRegisteredDate(DatatypeFactory.newInstance().newXMLGregorianCalendar("2022-03-27"));
        exemption.setEndDate(DatatypeFactory.newInstance().newXMLGregorianCalendar("2027-03-27"));
        exemption.setExemptionReasonCode("CODE");
        exemption.setPWSDescription("REASON");

        exemptions.getExemption().add(exemption);

        // ended exemption
        exemption = new Exemption();
        exemption.setAddress("TEST ADDRESS 2");
        exemption.setRegisteredDate(DatatypeFactory.newInstance().newXMLGregorianCalendar("2015-03-27"));
        exemption.setEndDate(DatatypeFactory.newInstance().newXMLGregorianCalendar("2015-05-27"));
        exemption.setExemptionReasonCode("CODE 2");
        exemption.setPWSDescription("REASON 2");

        exemptions.getExemption().add(exemption);

        return response;
    }
    
    // this mocks response from uri="cxf:bean:beisGetPartyDetailsService" not the whole sub route!
    private GetPartyDetailsResponse createGetPartyDetailsResponseForLandlord() {
        GetPartyDetailsResponse response = new GetPartyDetailsResponse();
        response.setUserType(UserType.LANDLORD.toString());
        response.setSuccess(true);
        return response;
    }
    
    private GetPartyDetailsResponse createGetPartyDetailsResponseForAgent() {
        GetPartyDetailsResponse response = new GetPartyDetailsResponse();
        response.setUserType(UserType.AGENT.toString());
        response.setSuccess(true);
        return response;
    }
    
    static void checkValues(DashboardDetails details) {

        assertEquals("Checking current exemptions", 1, details.getCurrentExemptionCount());
        assertEquals("Checking expired exemptions", 1, details.getExpiredExemptionCount());

        assertEquals("Checking address", "TEST ADDRESS", details.getCurrentExemptions().get(0).getAddress());
        assertEquals("Checking description", "REASON", details.getCurrentExemptions().get(0).getDescription());
        
        assertEquals("Checking start date",ZonedDateTime.of(2022, 3, 27, 0, 0, 0, 0, ZoneId.systemDefault()),
                details.getCurrentExemptions().get(0).getStartDate());
        assertEquals("Checking end date", ZonedDateTime.of(2027, 3, 27, 0, 0, 0, 0, ZoneId.systemDefault()),
                details.getCurrentExemptions().get(0).getEndDate());

        assertEquals("Checking address", "TEST ADDRESS 2", details.getExpiredExemptions().get(0).getAddress());
        assertEquals("Checking description", "REASON 2", details.getExpiredExemptions().get(0).getDescription());
        assertEquals("Checking start date", ZonedDateTime.of(2015, 3, 27, 0, 0, 0, 0, ZoneId.systemDefault()),
                details.getExpiredExemptions().get(0).getStartDate());
        assertEquals("Checking end date", ZonedDateTime.of(2015, 5, 27, 0, 0, 0, 0, ZoneId.systemDefault()),
                details.getExpiredExemptions().get(0).getEndDate());
    }
    
    
    @Test
    public void ToRetrieveCurrentExemptionTest() throws Exception {
        logger.info("To get current selected exemption success test started");

        AdviceWith.adviceWith(context.getRouteDefinition(routeNameUnderTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith(TEST_START_NAME);

                interceptSendToEndpoint("cxf:bean:beisGetPrsAccountExemptionsService").skipSendToOriginalEndpoint()
                        .to(MOCK_GET_EXEMPTIONS).process(new Processor() {

                            @Override
                            public void process(Exchange exchange) throws Exception {

                                GetPRSAccountExemptionsRequest request = (GetPRSAccountExemptionsRequest) exchange
                                        .getIn().getBody();
                                assertEquals("Checking party ref", BigInteger.valueOf(9000), request.getLandlordPartyRef());
                                assertNotNull(request.getExemptionRefNo());
                                assertEquals("Checking exemption ref no", "1234",request.getExemptionRefNo().toString());
                                // Mock out a response
                                exchange.getIn().setBody(createSingleExemptionNdsResponse(1234));
                            }
                        });
                
                // This sub route is invoked by get prs exemptions and requires mocking
                AdviceWith.adviceWith(context.getRouteDefinition("getPartyDetails_SubRouteForRetrievingRegistrationDetails"), context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {

                        interceptSendToEndpoint("cxf:bean:beisGetPartyDetailsService").skipSendToOriginalEndpoint().to(MOCK_GET_PARTYDETAILS).process(new Processor() {

                            @Override
                            public void process(Exchange exchange) throws Exception {
                                
                                //check that the request got the party ref added
                                GetPartyDetailsRequest request = (GetPartyDetailsRequest) exchange.getIn().getBody();
                                assertEquals("Checking party ref", BigInteger.valueOf(9000), request.getPartyRef());
                                
                                // Mock out a response
                                exchange.getIn().setBody(createGetPartyDetailsResponseForLandlord());
                            }
                        });
                    }
                });
            }
        });

        context.start();
        GetPRSAccountExemptionsNdsResponse response = (GetPRSAccountExemptionsNdsResponse) apiEndpoint.requestBody(createSingleExemptionNdsRequest("seleniumorg","1234"));
        assertTrue(response.isSuccess());
        assertNotNull(response.getDashboardDetails());
        assertEquals("checking reference value",response.getDashboardDetails().getExemptionDetailList().get(0).getReferenceId(),"1234");
        assertEquals("checking response size",response.getDashboardDetails().getExemptionDetailList().size(),1);        
        assertMockEndpointsSatisfied();
    }
    
   
    @Test
    public void ToRetrieveCurrentExemptionTestForAgent() throws Exception {
        logger.info("To get current selected exemption success test started");

        AdviceWith.adviceWith(context.getRouteDefinition(routeNameUnderTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith(TEST_START_NAME);

                interceptSendToEndpoint("cxf:bean:beisGetPrsAccountExemptionsService").skipSendToOriginalEndpoint()
                        .to(MOCK_GET_EXEMPTIONS).process(new Processor() {

                            @Override
                            public void process(Exchange exchange) throws Exception {

                                GetPRSAccountExemptionsRequest request = (GetPRSAccountExemptionsRequest) exchange
                                        .getIn().getBody();
                                assertEquals("Checking party ref", BigInteger.valueOf(10042), request.getAgentPartyRef());
                                assertNotNull(request.getExemptionRefNo());
                                assertEquals("Checking exemption ref no", "4321",request.getExemptionRefNo().toString());
                                // Mock out a response
                                exchange.getIn().setBody(createSingleExemptionNdsResponse(4321));
                            }
                        });
                
                // This sub route is invoked by get prs exemptions and requires mocking
                AdviceWith.adviceWith(context.getRouteDefinition("getPartyDetails_SubRouteForRetrievingRegistrationDetails"), context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {

                        interceptSendToEndpoint("cxf:bean:beisGetPartyDetailsService").skipSendToOriginalEndpoint().to(MOCK_GET_PARTYDETAILS).process(new Processor() {

                            @Override
                            public void process(Exchange exchange) throws Exception {
                                
                            	//check that the request got the party ref added
                                GetPartyDetailsRequest request = (GetPartyDetailsRequest) exchange.getIn().getBody();
                                assertEquals("Checking party ref", BigInteger.valueOf(10042) , request.getPartyRef());
                                
                                // Mock out a response
                                exchange.getIn().setBody(createGetPartyDetailsResponseForAgent());
                            }
                        });
                    }
                });
            }
        });

        context.start();
        NdsErrorResponse response = (NdsErrorResponse) apiEndpoint.requestBody(createSingleExemptionNdsRequest("seleniumagentuser","4321"));
        assertTrue(response.isSuccess());
        
        if (response instanceof GetPRSAccountExemptionsNdsResponse) {
        	GetPRSAccountExemptionsNdsResponse exemptionResponse = (GetPRSAccountExemptionsNdsResponse) response;
        	assertNotNull(exemptionResponse.getDashboardDetails());
        	
	        assertEquals("checking reference value", exemptionResponse.getDashboardDetails().getExemptionDetailList().get(0).getReferenceId(), "4321");
	        assertEquals("checking response size", exemptionResponse.getDashboardDetails().getExemptionDetailList().size(), 1);
        } else {
			fail("Response was not a GetPRSAccountExemptionsNdsResponse : " + response.getNdsMessages());
		}
        
        assertMockEndpointsSatisfied();
    }
    
    static GetPRSAccountExemptionsNdsRequest createSingleExemptionNdsRequest(String username,String exemptionRefNo) {

        GetPRSAccountExemptionsNdsRequest request = new GetPRSAccountExemptionsNdsRequest();
        request.setUsername(username);
        request.setTenant("BEIS");      
        request.setExemptionRefNo(exemptionRefNo);
        request.setAccountId(BigInteger.valueOf(9000).toString());
        return request;
    }
    
   static GetPRSAccountExemptionsResponse createSingleExemptionNdsResponse(long exemptionRefNo)
            throws DatatypeConfigurationException {
        GetPRSAccountExemptionsResponse response = new GetPRSAccountExemptionsResponse();

        Exemptions exemptions = new Exemptions();
        response.setExemptions(exemptions);

        // current exemption
        Exemption exemption = new Exemption();
        exemption.setExemptionRefNo(BigInteger.valueOf(exemptionRefNo));
        exemption.setAddress("TEST ADDRESS");
        exemption.setRegisteredDate(DatatypeFactory.newInstance().newXMLGregorianCalendar("2022-03-27"));
        exemption.setEndDate(DatatypeFactory.newInstance().newXMLGregorianCalendar("2027-03-27"));
        exemption.setExemptionReasonCode("CODE");
        exemption.setPWSDescription("REASON");

        exemptions.getExemption().add(exemption);

        
        return response;
    }   
}
