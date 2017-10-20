package com.northgateps.nds.beis.esb.registerprsexemption;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.northgateps.nds.beis.api.AccountType;
import com.northgateps.nds.beis.api.BeisAddressDetail;
import com.northgateps.nds.beis.api.EnergyPerformanceCertificate;
import com.northgateps.nds.beis.api.ExemptionDetail;
import com.northgateps.nds.beis.api.LandlordDetails;
import com.northgateps.nds.beis.api.OrganisationNameDetail;
import com.northgateps.nds.beis.api.PropertyType;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionDetails;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsRequest;
import com.northgateps.nds.beis.api.registerprsexemption.RegisterPrsExemptionNdsResponse;
import com.northgateps.nds.beis.backoffice.service.registerprsexemption.RegisterPRSExemptionResponse;
import com.northgateps.nds.platform.esb.camel.NdsFileSystemXmlApplicationContext;
import com.northgateps.nds.platform.esb.security.MockAuthentication;
import com.northgateps.nds.platform.esb.util.xml.JaxbXmlMarshaller;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Test for register prs exemption route
 */
@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class RegisterPrsExemptionRouteTest extends CamelSpringTestSupport {

    private static final NdsLogger logger = NdsLogger.getLogger(RegisterPrsExemptionRouteTest.class);
    private static final String TEST_START_NAME = "direct:start";
    private static final String routeNameUnderTest = "Register_PRS_Exemption_Route";

    private static final String MOCK_RE_REQUEST_CHECK = "mock:registerPrsExemption";
    private static final String MOCK_RE_RESPONSE_CHECK = "mock:registerPrsExemption-response-check";
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

    /**
     * Runs the Register Prs exemption Service camel route expecting a successful outcome
     */
    @Test
    public void testRegisteringAPrsExemption() throws Exception {
        logger.info("RegisterPrsExemption test started");
        context = getContextConfiguredWithRoute();
        context.start();
        MockEndpoint registerPrsExemptionServiceMock = getMockEndpoint(MOCK_RE_REQUEST_CHECK);
        registerPrsExemptionServiceMock.expectedMessageCount(2);

        MockEndpoint registerPrsExemptionResponseMock = getMockEndpoint(MOCK_RE_RESPONSE_CHECK);
        registerPrsExemptionResponseMock.expectedMessageCount(1);

        registerPrsExemptionResponseMock.expectedBodiesReceived(JaxbXmlMarshaller.convertToPrettyPrintXml(
                createRegisterPrsExemptionNdsResponse(), RegisterPrsExemptionNdsResponse.class));

        apiEndpoint.sendBody(createRegisterPrsExemptionNdsRequest());
        assertMockEndpointsSatisfied();
    }
    
    @Test
    public void testRegisteringAPrsExemptionWithScottishPostcode() throws Exception {
        logger.info("RegisterPrsExemptionWithScottishPostcode test started");
        context = getContextConfiguredWithRoute();  
        context.start();       
        RegisterPrsExemptionNdsResponse response = (RegisterPrsExemptionNdsResponse) apiEndpoint.requestBody(
             createRegisterPrsExemptionWithScottishPostcodeNdsRequest("TD1 3PU"));
        assertNotNull(response);
        assertNotNull(response.getNdsMessages().getViolations());
        assertTrue("Invalid postcode error received",
              response.getNdsMessages().getViolations().get(0).getMessage().contains(
                   "This address has a postcode that is not in England or Wales. You can only register an exemption for a property in England or Wales"));            
    }    
    
    /**
     * Runs the Register Prs exemption Service camel route  as agent expecting a successful outcome
     */
    @Test
    public void testExemptionRegistrationAsAgent() throws Exception {
        logger.info("RegisterPrsExemption test started");        
        context = getContextConfiguredWithRoute();
        context.start();
        MockEndpoint registerPrsExemptionServiceMock = getMockEndpoint(MOCK_RE_REQUEST_CHECK);
        registerPrsExemptionServiceMock.expectedMessageCount(3);

        MockEndpoint registerPrsExemptionResponseMock = getMockEndpoint(MOCK_RE_RESPONSE_CHECK);
        registerPrsExemptionResponseMock.expectedMessageCount(1);

        registerPrsExemptionResponseMock.expectedBodiesReceived(JaxbXmlMarshaller.convertToPrettyPrintXml(
                createRegisterPrsExemptionNdsResponse(), RegisterPrsExemptionNdsResponse.class));

        apiEndpoint.sendBody(createRegisterPrsExemptionAsAgentNdsRequest());
        assertMockEndpointsSatisfied();
    }
    /*
     * return context configured with route which is common for all tests
     */
    public ModelCamelContext getContextConfiguredWithRoute() throws Exception{
        context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_RE_RESPONSE_CHECK);
                replaceFromWith(TEST_START_NAME);

                interceptSendToEndpoint("cxf:bean:exemptionRegistrationService").skipSendToOriginalEndpoint().to(
                        MOCK_RE_REQUEST_CHECK).process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody(createRegisterPrsResponse());
                    }
                });

                interceptSendToEndpoint("smtp://{{smtp.host.server}}").skipSendToOriginalEndpoint().to(
                        MOCK_RE_REQUEST_CHECK).process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {
                    }
                });
            }
        });
        
        return context;
    }    

    private RegisterPrsExemptionNdsRequest createRegisterPrsExemptionAsAgentNdsRequest() {
        RegisterPrsExemptionNdsRequest request = new RegisterPrsExemptionNdsRequest();
        request.setUserName("seleniumagentuser");
        request.setTenant("beis");
        request.setAccountId("10042");
        RegisterPrsExemptionDetails registerPrsExemptionDetails = new RegisterPrsExemptionDetails();
        ExemptionDetail exemptionDetails = new ExemptionDetail();
        exemptionDetails.setReferenceId("dehdshdsjk");
        exemptionDetails.setPropertyType(PropertyType.PRSN);
        exemptionDetails.setExemptionType("NOIMP");
        exemptionDetails.setExemptionReason("");
        EnergyPerformanceCertificate epc = new EnergyPerformanceCertificate();
        BeisAddressDetail address = new BeisAddressDetail();
        List<String> line = new ArrayList<String>();
        line.add("Unit 1");
        line.add("Bracknell Beeches");
        address.setLine(line);
        address.setTown("Bracknell");
        address.setCounty("Berkshire");
        epc.setPropertyAddress(address);
        address.setPostcode("RG12 7BW");
        address.setCountry("GB");
        exemptionDetails.setEpc(epc);
        LandlordDetails landlordDetails = new LandlordDetails();        
        landlordDetails.setAccountType(AccountType.ORGANISATION);
        OrganisationNameDetail organisationNameDetail = new OrganisationNameDetail();
        organisationNameDetail.setOrgName("Agent Organisation Test");
        landlordDetails.setOrganisationNameDetail(organisationNameDetail);
        landlordDetails.setEmailAddress("dummy@northgateps.com");
        landlordDetails.setLandlordAddress(address);
        landlordDetails.setPhoneNumber("0123456789");        
        exemptionDetails.setLandlordDetails(landlordDetails);
        registerPrsExemptionDetails.setExemptionDetails(exemptionDetails);
        request.setRegisterPrsExemptionDetails(registerPrsExemptionDetails);
        return request;
    }

    private RegisterPrsExemptionNdsRequest createRegisterPrsExemptionNdsRequest() {
        RegisterPrsExemptionNdsRequest request = new RegisterPrsExemptionNdsRequest();
        request.setUserName("faridatest");
        request.setTenant("beis");
        request.setAccountId("9703");
        RegisterPrsExemptionDetails registerPrsExemptionDetails = new RegisterPrsExemptionDetails();
        ExemptionDetail exemptionDetails = new ExemptionDetail();
        exemptionDetails.setReferenceId("10");
        exemptionDetails.setPropertyType(PropertyType.PRSN);
        exemptionDetails.setExemptionType("NOIMP");
        exemptionDetails.setExemptionReason("");
        EnergyPerformanceCertificate epc = new EnergyPerformanceCertificate();
        BeisAddressDetail propertyAddress = new BeisAddressDetail();
        List<String> line = new ArrayList<String>();
        line.add("Unit 1");
        line.add("Bracknell Beeches");
        propertyAddress.setLine(line);
        propertyAddress.setTown("Bracknell");
        propertyAddress.setCounty("Berkshire");
        epc.setPropertyAddress(propertyAddress);
        propertyAddress.setPostcode("RG12 7BW");
        propertyAddress.setCountry("GB");
        exemptionDetails.setEpc(epc);
        registerPrsExemptionDetails.setExemptionDetails(exemptionDetails);
        request.setRegisterPrsExemptionDetails(registerPrsExemptionDetails);
        return request;
    }
    
    private RegisterPrsExemptionNdsRequest createRegisterPrsExemptionWithScottishPostcodeNdsRequest(String postcode) {
        RegisterPrsExemptionNdsRequest request = new RegisterPrsExemptionNdsRequest();
        request.setUserName("faridatest");
        request.setTenant("beis");
        request.setAccountId("9703");
        RegisterPrsExemptionDetails registerPrsExemptionDetails = new RegisterPrsExemptionDetails();
        ExemptionDetail exemptionDetails = new ExemptionDetail();
        exemptionDetails.setReferenceId("10");
        exemptionDetails.setPropertyType(PropertyType.PRSN);
        exemptionDetails.setExemptionType("NOIMP");
        exemptionDetails.setExemptionReason("");
        EnergyPerformanceCertificate epc = new EnergyPerformanceCertificate();
        BeisAddressDetail propertyAddress = new BeisAddressDetail();
        List<String> line = new ArrayList<String>();
        line.add("Unit 1");
        line.add("Bracknell Beeches");
        propertyAddress.setLine(line);
        propertyAddress.setTown("Bracknell");
        propertyAddress.setCounty("Berkshire");
        epc.setPropertyAddress(propertyAddress);
        propertyAddress.setPostcode(postcode);
        propertyAddress.setCountry("GB");
        exemptionDetails.setEpc(epc);
        registerPrsExemptionDetails.setExemptionDetails(exemptionDetails);
        request.setRegisterPrsExemptionDetails(registerPrsExemptionDetails);
        return request;
    }

    private RegisterPRSExemptionResponse createRegisterPrsResponse() {
        RegisterPRSExemptionResponse registerPRSExemptionResponse = new RegisterPRSExemptionResponse();
        registerPRSExemptionResponse.setSuccess(true);
        return registerPRSExemptionResponse;
    }

    private RegisterPrsExemptionNdsResponse createRegisterPrsExemptionNdsResponse() {
        RegisterPrsExemptionNdsResponse registerPrsExemptionNdsResponse = new RegisterPrsExemptionNdsResponse();
        registerPrsExemptionNdsResponse.setSuccess(true);
        return registerPrsExemptionNdsResponse;
    }
}
