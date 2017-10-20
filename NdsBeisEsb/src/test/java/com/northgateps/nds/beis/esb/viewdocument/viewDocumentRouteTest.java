package com.northgateps.nds.beis.esb.viewdocument;

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

import com.northgateps.nds.beis.api.viewdocument.ViewPdfNdsRequest;
import com.northgateps.nds.beis.api.viewdocument.ViewPdfNdsResponse;
import com.northgateps.nds.beis.backoffice.service.core.MessageStructure;
import com.northgateps.nds.beis.backoffice.service.core.MessagesStructure;
import com.northgateps.nds.beis.backoffice.service.core.SeverityType;
import com.northgateps.nds.beis.backoffice.service.viewdocumentgdipgdar.ViewPdfResponse;
import com.northgateps.nds.beis.backoffice.service.viewdocumentgdipgdar.ViewPdfResponse.Document;

import com.northgateps.nds.platform.esb.camel.NdsFileSystemXmlApplicationContext;
import com.northgateps.nds.platform.esb.security.MockAuthentication;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Unit test for the viewDocumentGdipgdar Service route
 */
@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class viewDocumentRouteTest extends CamelSpringTestSupport {

    private static final NdsLogger logger = NdsLogger.getLogger(viewDocumentRouteTest.class);
    private static final String routeNameUnderTest = "viewDocumentGdipgdarRoute";
    private static final String TEST_START_NAME = "direct:startViewDocumentGdipgdarRoute";

    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiEndpoint;

    private static final String MOCK_VIEW_DOCUMENT_REQUEST_CHECK = "mock:viewDocumentGdipgdarRoute";

    private static final String MOCK_VIEW_DOCUMENT_RESPONSE_CHECK = "mock:viewDocumentGdipgdarRoute-response-check";
    private static final String SKIP_EndPoint = "cxf:bean:viewDocumentGDIPGDARServiceEndPoint";
    
    
    /**
     * Creates Spring's application context.
     */
    @Override
    protected AbstractApplicationContext createApplicationContext() {

        // grant test role based security access
        MockAuthentication.setMockAuthentication("ROLE_BEIS_UI");

        return new NdsFileSystemXmlApplicationContext(
                new String[] { "src/main/webapp/WEB-INF/applicationContext-security.xml",
                        "src/main/webapp/WEB-INF/beis-camel-context.xml" });
    }
    
    //test to view the green deal pdf
    @Test
    public void testViewPdfSuccessPath() throws Exception {
        
        logger.info("ViewDocumentGdipgdarRoute test started");       
       
        context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_VIEW_DOCUMENT_RESPONSE_CHECK);
                replaceFromWith(TEST_START_NAME);

                interceptSendToEndpoint(SKIP_EndPoint).skipSendToOriginalEndpoint().to(
                        MOCK_VIEW_DOCUMENT_REQUEST_CHECK).process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {

                        ViewPdfResponse appResponse = createViewPdfResponse();

                        exchange.getIn().setBody(appResponse);
                    }
                   
                });
            }
        });

        context.start();
        ViewPdfNdsResponse response = (ViewPdfNdsResponse) apiEndpoint.requestBody(
                createNewViewPdfRequest());

        assertTrue(response.isSuccess());
        assertNotNull(response.getDocument());
        assertFalse(response.getSuperseded());
        
        assertMockEndpointsSatisfied();
    }
    
    //test to make a Gdip-Gdar search
    @Test
    public void testSearchGdarGdipSuccessPath() throws Exception {
        
        logger.info("ViewDocumentGdipgdarRoute test started");       
       
        context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_VIEW_DOCUMENT_RESPONSE_CHECK);
                replaceFromWith(TEST_START_NAME);

                interceptSendToEndpoint(SKIP_EndPoint).skipSendToOriginalEndpoint().to(
                        MOCK_VIEW_DOCUMENT_REQUEST_CHECK).process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {

                        ViewPdfResponse appResponse = createViewPdfSearchResponse();

                        exchange.getIn().setBody(appResponse);
                    }
                   
                });
            }
        });

        context.start();
        ViewPdfNdsResponse response = (ViewPdfNdsResponse) apiEndpoint.requestBody(
                createNewViewPdfSearchRequest());

        assertTrue(response.isSuccess());
        assertNull(response.getDocument().getBinaryData());
        assertFalse(response.getSuperseded());
        
        assertMockEndpointsSatisfied();
    }
    
    
    @Test
    public void testFailurePath() throws Exception {
        
        logger.info("ViewDocumentGdipgdarRoute Failure test started");       
       
        context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_VIEW_DOCUMENT_RESPONSE_CHECK);
                replaceFromWith(TEST_START_NAME);

                interceptSendToEndpoint(SKIP_EndPoint).skipSendToOriginalEndpoint().to(
                        MOCK_VIEW_DOCUMENT_REQUEST_CHECK).process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {

                        ViewPdfResponse appResponse = createViewPdfResponseWithFailures();

                        exchange.getIn().setBody(appResponse);
                    }                 
                   
                });
            }
        });

        context.start();
        ViewPdfNdsResponse response = (ViewPdfNdsResponse) apiEndpoint.requestBody(
                createNewViewPdfFailureRequest());

        assertFalse(response.isSuccess());
        assertFalse(response.getSuperseded());
      
        assertMockEndpointsSatisfied();
    }
    
    @Test
    public void testNoResultsPath() throws Exception {
        
        logger.info("ViewDocumentGdipgdarRoute Failure test started");       
       
        context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_VIEW_DOCUMENT_RESPONSE_CHECK);
                replaceFromWith(TEST_START_NAME);

                interceptSendToEndpoint(SKIP_EndPoint).skipSendToOriginalEndpoint().to(
                        MOCK_VIEW_DOCUMENT_REQUEST_CHECK).process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {

                        ViewPdfResponse appResponse = createViewPdfResponseWithNoResultss();

                        exchange.getIn().setBody(appResponse);
                    }                 
                   
                });
            }
        });

        context.start();
        ViewPdfNdsResponse response = (ViewPdfNdsResponse) apiEndpoint.requestBody(
                createNewViewPdfFailureRequest());

        assertTrue(response.isSuccess());
        assertFalse(response.getSuperseded());
      
        assertMockEndpointsSatisfied();
    }
    
    @Test
    public void testSuperseededCondition() throws Exception {
        logger.info("ViewDocumentGdipgdarRoute Failure test started");       
        
        context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_VIEW_DOCUMENT_RESPONSE_CHECK);
                replaceFromWith(TEST_START_NAME);

                interceptSendToEndpoint(SKIP_EndPoint).skipSendToOriginalEndpoint().to(
                        MOCK_VIEW_DOCUMENT_REQUEST_CHECK).process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {

                        ViewPdfResponse appResponse = createViewPdfResponseWithSupersededError();

                        exchange.getIn().setBody(appResponse);
                    }
     
                   
                });
            }
        });

        context.start();
        ViewPdfNdsResponse response = (ViewPdfNdsResponse) apiEndpoint.requestBody(
                createSuperseededViewPdfRequest());

        assertTrue(response.isSuccess());
        assertTrue(response.getSuperseded());
      
        assertMockEndpointsSatisfied();
    }

    private Object createSuperseededViewPdfRequest() {
        ViewPdfNdsRequest viewPdfNdsRequest=new ViewPdfNdsRequest();
        viewPdfNdsRequest.setReferenceNumber("0000-1111-2222-3333-0000");
        return viewPdfNdsRequest;
    }
    
    private ViewPdfNdsRequest createNewViewPdfFailureRequest() {
        ViewPdfNdsRequest viewPdfNdsRequest=new ViewPdfNdsRequest();
        viewPdfNdsRequest.setReferenceNumber("12");
        return viewPdfNdsRequest;
    }
    private ViewPdfNdsRequest createNewViewPdfRequest(){

        ViewPdfNdsRequest viewPdfNdsRequest=new ViewPdfNdsRequest();
        viewPdfNdsRequest.setReferenceNumber("1234-4567-1111-2222-1111");
        viewPdfNdsRequest.setIncludeDocument(true);
        return viewPdfNdsRequest;
    }
    
    private ViewPdfNdsRequest createNewViewPdfSearchRequest(){

        ViewPdfNdsRequest viewPdfNdsRequest=new ViewPdfNdsRequest();
        viewPdfNdsRequest.setReferenceNumber("1234-4567-1111-2222-1111");
        viewPdfNdsRequest.setIncludeDocument(false);
        return viewPdfNdsRequest;
    }
    
    private ViewPdfResponse createViewPdfResponse() throws Exception {
        ViewPdfResponse viewPdfResponse = new ViewPdfResponse();
        viewPdfResponse.setSuccess(true);
        Document documentDetails = new Document();
        documentDetails.setDocumentCode("GDIPGDARPDF");
        documentDetails.setDocumentDescription("Green Deal Advice Report");
        documentDetails.setDocumentName("Test.pdf");
        documentDetails.setDocumentFileType("PDF");
        documentDetails.setDocumentReference("1234-1111-1111-1111-1111_20170124044420");
        //TODO CR:BEIS-61 you don't really need a file here, you could have just created a byte array with dummy data in
        documentDetails.setBinaryData(loadFileAsBytesArray(this.getClass().getClassLoader().getResource("config/Test.pdf").getFile())); 
        viewPdfResponse.setDocument(documentDetails);
        return viewPdfResponse;
    }
    
    private ViewPdfResponse createViewPdfSearchResponse() throws Exception {
        ViewPdfResponse viewPdfResponse = new ViewPdfResponse();
        viewPdfResponse.setSuccess(true);
        Document documentDetails = new Document();
        documentDetails.setDocumentCode("GDIPGDARPDF");
        documentDetails.setDocumentDescription("Green Deal Advice Report");
        documentDetails.setDocumentName("Test.pdf");
        documentDetails.setDocumentFileType("PDF");
        documentDetails.setDocumentReference("1234-1111-1111-1111-1111_20170124044420");
        viewPdfResponse.setDocument(documentDetails);
        return viewPdfResponse;
    }
    
    private ViewPdfResponse createViewPdfResponseWithFailures() {
        ViewPdfResponse viewPdfResponse = new ViewPdfResponse();
        viewPdfResponse.setSuccess(false);   
        MessagesStructure messagesStructure = new MessagesStructure();      
        MessageStructure messageStructure = new MessageStructure();
        messageStructure.setText("RRN format is invalid, please check and re-enter");
        messageStructure.setSeverity(SeverityType.ERR);
        messageStructure.setCode("FL-30242");       
        messagesStructure.getMessage().add(messageStructure);        
        viewPdfResponse.setMessages(messagesStructure);
        
        return viewPdfResponse;
    }
    
    private ViewPdfResponse createViewPdfResponseWithNoResultss() {
        ViewPdfResponse viewPdfResponse = new ViewPdfResponse();
        viewPdfResponse.setSuccess(false);   
        MessagesStructure messagesStructure = new MessagesStructure();      
        MessageStructure messageStructure = new MessageStructure();
        messageStructure.setText("No data found for specified RRN");
        messageStructure.setSeverity(SeverityType.ERR);
        messageStructure.setCode("BEI-30245");       
        messagesStructure.getMessage().add(messageStructure);        
        viewPdfResponse.setMessages(messagesStructure);
        
        return viewPdfResponse;
    }
    
    

    private ViewPdfResponse createViewPdfResponseWithSupersededError() {
        ViewPdfResponse viewPdfResponse = new ViewPdfResponse();
        viewPdfResponse.setSuccess(false);   
        MessagesStructure messagesStructure = new MessagesStructure();      
        MessageStructure messageStructure = new MessageStructure();
        messageStructure.setText("The RRN you have entered belongs to an Occupancy Assessment that has been superseded by a more recent one. Please enter the RRN of the most recent Occupancy Assessment created for the property");
        messageStructure.setSeverity(SeverityType.ERR);
        messageStructure.setCode("BEI-30255");       
        messagesStructure.getMessage().add(messageStructure);        
        viewPdfResponse.setMessages(messagesStructure);
        
        return viewPdfResponse;
    }            

   /**
    * Following method convert file into bytes which need to pass into back-office response
    * @param fileName
    * @return
    * @throws Exception
    */
    public static byte[] loadFileAsBytesArray(String fileName) throws Exception {        
        java.io.File file = new java.io.File(fileName);
        int length = (int) file.length();
        java.io.BufferedInputStream reader = new java.io.BufferedInputStream(new java.io.FileInputStream(file));
        byte[] bytes = new byte[length];
        reader.read(bytes, 0, length);
        reader.close();
        return bytes;
 
    }

    
}
