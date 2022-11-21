package com.northgateps.nds.beis.esb.exemption;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.northgateps.nds.beis.api.BeisAddressDetail;
import com.northgateps.nds.beis.api.EnergyPerformanceCertificate;
import com.northgateps.nds.beis.api.ExemptionDetail;
import com.northgateps.nds.beis.api.ExemptionTypeDetails;
import com.northgateps.nds.beis.api.printexemptiondetails.PrintExemptionDetailsNdsRequest;
import com.northgateps.nds.beis.api.printexemptiondetails.PrintExemptionDetailsNdsResponse;
import com.northgateps.nds.beis.esb.RouteTestUtils;
import com.northgateps.nds.platform.api.FileDetails;
import com.northgateps.nds.platform.api.Upload;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Unit test for printing an excemption with a £ sign which checks it doesn't trip up on that unicode character.
 * 
 * Example command line run (you may need to "clean compile" too if not done already) :
 * 
 * mvn test-compile surefire:test -Dtest=com.northgateps.nds.beis.esb.exemption.PrintExemptionRouteTest
 */
@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PrintExemptionRouteTest extends CamelSpringTestSupport {

    private static final NdsLogger logger = NdsLogger.getLogger(UpdatePrsExemptionRouteTest.class);
    private static final String ROUTE_NAME_UNDER_TEST = "printExemptionDetailsRoute";
    private static final String TEST_START_NAME = "direct:startPrintExemptionDetailsRoute";
    private static final String MOCK_RESPONSE = "mock:" + ROUTE_NAME_UNDER_TEST + "-response-check";
    
    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiEndpoint;

    // debug output of the file for help with debugging
    private Path savePath = Paths.get("printExceptionRouteTest.pdf");
    private boolean saveOutput = false;
    
    @Override
    protected AbstractApplicationContext createApplicationContext() {
		return RouteTestUtils.createApplicationContext(ROUTE_NAME_UNDER_TEST);
    }

    @Test
    public void printExemptionTest() throws Exception {
        logger.info("Starting print exemption test");

        AdviceWith.adviceWith(context.getRouteDefinition(ROUTE_NAME_UNDER_TEST), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
            	replaceFromWith(TEST_START_NAME);
            	weaveAddLast().to(MOCK_RESPONSE);
            }
        });

        context.start();
        PrintExemptionDetailsNdsResponse response = (PrintExemptionDetailsNdsResponse) apiEndpoint.requestBody(createNdsRequest());

        assertTrue(response.isSuccess());
        assertEquals("exemption-details.pdf", response.getFileName());
        
        // save the pdf for checking
        if (saveOutput) {
        	byte[] pdf = response.getSource();
        	assertNotNull(pdf);
        	
        	logger.info("Saving to " + savePath.toString());
	        Files.write(savePath, pdf);
	        logger.info("Saved to " + savePath.toString());
	        assertNotNull(Files.exists(savePath));
        }
        
        assertMockEndpointsSatisfied();
    }
    
    private PrintExemptionDetailsNdsRequest createNdsRequest() {
    	PrintExemptionDetailsNdsRequest request = new PrintExemptionDetailsNdsRequest();
    	
        request.setTenant("beis");
        request.setPropertyType("propertyType");
        
        ExemptionDetail detail = new ExemptionDetail();
        EnergyPerformanceCertificate epc = new EnergyPerformanceCertificate();
        Upload files = new Upload();
        FileDetails fileDetails = new FileDetails();
        fileDetails.setFileName("made_up_for_test.jpg");
        files.setTentative(fileDetails);
        files.setResources(Arrays.asList(fileDetails));
        epc.setFiles(files);
        BeisAddressDetail address = new BeisAddressDetail();
        address.setSingleLineAddressPostcode("SingleLineAddressPostcode!");
        epc.setPropertyAddress(address);
        detail.setEpc(epc);
        request.setExemptionDetail(detail);
        
        ExemptionTypeDetails typeDetails = new ExemptionTypeDetails();
        typeDetails.setCode("code");
        typeDetails.setConfirmationPagetitle("confirmationPagetitle");
        typeDetails.setConfirmationwording("confirmationwording");
        typeDetails.setDescription("description");
        typeDetails.setDocumentsPwsLabel("documentsPwsLabel");
        typeDetails.setDocumentsPwsText("documentsPwsText");
        typeDetails.setTextPwsText("textPwsText");
        typeDetails.setDocumentsRequired("documentsRequired");
        typeDetails.setDuration("duration");
        typeDetails.setPwsText("pwsText");
        typeDetails.setTextPwsText("textPwsText");
        
        // provokes the issue?
        typeDetails.setPwsDescription("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWork smarter £x not harderrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
        //typeDetails.setPwsDescription("Work smarter not harder");
        
        request.setExemptionType(typeDetails);
        return request;
    }
}
