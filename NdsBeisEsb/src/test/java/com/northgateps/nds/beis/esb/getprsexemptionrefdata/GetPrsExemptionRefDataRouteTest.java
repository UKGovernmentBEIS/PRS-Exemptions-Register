package com.northgateps.nds.beis.esb.getprsexemptionrefdata;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

import com.northgateps.nds.beis.api.ExemptionTypeDetails;
import com.northgateps.nds.beis.api.getprsexemptionrefdata.GetPrsExemptionRefDataNdsRequest;
import com.northgateps.nds.beis.api.getprsexemptionrefdata.GetPrsExemptionRefDataNdsResponse;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataResponse;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataResponse.ServiceDetails;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataResponse.ServiceDetails.ServiceDetail;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataResponse.ServiceDetails.ServiceDetail.ExemptionTypes;
import com.northgateps.nds.beis.backoffice.service.getprsexemptionreferencedata.GetPRSExemptionReferenceDataResponse.ServiceDetails.ServiceDetail.ExemptionTypes.ExemptionType;
import com.northgateps.nds.beis.esb.RouteTestUtils;

import com.northgateps.nds.platform.esb.util.xml.JaxbXmlMarshaller;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Unit test for the GetPrsExemptionRefData Service route
 */
@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GetPrsExemptionRefDataRouteTest extends CamelSpringTestSupport {

    private static final NdsLogger logger = NdsLogger.getLogger(GetPrsExemptionRefDataRouteTest.class);
    private static final String routeNameUnderTest = "GetPrsExemptionRefDataRoute";
    private static final String TEST_START_NAME = "direct:startGetPrsExemptionRefData";
    private static final String MOCK_GRD_REQUEST_CHECK = "mock:getPrsExemptionRefData";
    private static final String MOCK_GRD_RESPONSE_CHECK = "mock:getPrsExemptionRefData-response-check";

    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiEndpoint;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
    	return RouteTestUtils.createApplicationContext(routeNameUnderTest);
    }

     @Test
    public void SuccessPathTest() throws Exception {
        logger.info("getExemptionTypeText test started");

        AdviceWith.adviceWith(context.getRouteDefinition(routeNameUnderTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_GRD_RESPONSE_CHECK);
                replaceFromWith("direct:startGetPrsExemptionRefData");
                interceptSendToEndpoint("cxf:bean:getPrsExemptionRefDataEndPoint").skipSendToOriginalEndpoint().to(
                        MOCK_GRD_REQUEST_CHECK).process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody(createGetPrsExemptionRefDataResponse());
                    }
                });
            }
        });

        context.start();
        MockEndpoint getPrsExmRefDataServiceMock = getMockEndpoint(MOCK_GRD_REQUEST_CHECK);
        getPrsExmRefDataServiceMock.expectedMessageCount(1);
        getPrsExmRefDataServiceMock.expectedHeaderReceived(CxfConstants.OPERATION_NAME, getOperationName());

        MockEndpoint getPrsExmRefDataResponseMock = getMockEndpoint(MOCK_GRD_RESPONSE_CHECK);
        getPrsExmRefDataResponseMock.expectedMessageCount(1);

        getPrsExmRefDataResponseMock.expectedBodiesReceived(JaxbXmlMarshaller.convertToPrettyPrintXml(
                createGetPrsExemptionRefDataNdsResponse(), GetPrsExemptionRefDataNdsResponse.class));

        apiEndpoint.sendBody(createGetPrsExemptionRefDataNdsRequest());
        assertMockEndpointsSatisfied();
    }

    private GetPrsExemptionRefDataNdsRequest createGetPrsExemptionRefDataNdsRequest() {
        GetPrsExemptionRefDataNdsRequest request = new GetPrsExemptionRefDataNdsRequest();
        return request;
    }

    private GetPrsExemptionRefDataNdsResponse createGetPrsExemptionRefDataNdsResponse() {
        GetPrsExemptionRefDataNdsResponse response = new GetPrsExemptionRefDataNdsResponse();
        response.setSuccess(true);
        List<ExemptionTypeDetails> exemptionTypeTextList = new ArrayList<ExemptionTypeDetails>();
        ExemptionTypeDetails exemptionTypeDetail = new ExemptionTypeDetails();
        exemptionTypeDetail.setService("PRSN");
        exemptionTypeDetail.setCode("WALL");
        exemptionTypeDetail.setDescription("No improvements can be made");
        exemptionTypeDetail.setPwsDescription("Registering an exemption under the regulation 29(1)(b) exception");
        exemptionTypeDetail.setPwsText(
                "You must supply a copy of the relevant report to demonstrate that no improvements can be made (if separate to the relevant EPC).");
        exemptionTypeDetail.setDocumentsPwsLabel("test");
        exemptionTypeDetail.setDocumentsPwsText("some description about file requirements and ...max of 10 files");
        exemptionTypeDetail.setDurationUnit("Y");
        exemptionTypeDetail.setDuration("1");
        exemptionTypeDetail.setMaxDocuments("1");
        exemptionTypeDetail.setMinDocuments("1");
        exemptionTypeDetail.setStartDateRequired("false");
        exemptionTypeDetail.setTextRequired("false");
        exemptionTypeDetail.setFrvRequired("false");
        exemptionTypeDetail.setDocumentsRequired("false");
        exemptionTypeDetail.setSequence("1");
        exemptionTypeTextList.add(exemptionTypeDetail);
        response.setExemptionTypeTextList(exemptionTypeTextList);
        return response;
    }

    private GetPRSExemptionReferenceDataResponse createGetPrsExemptionRefDataResponse() {
        GetPRSExemptionReferenceDataResponse getPRSExemptionReferenceDataResponse = new GetPRSExemptionReferenceDataResponse();
        getPRSExemptionReferenceDataResponse.setSuccess(true);
        ServiceDetails serviceDetails = new ServiceDetails();
        List<ServiceDetail> serviceDetailList = serviceDetails.getServiceDetail();
        ServiceDetail serviceDetail = new ServiceDetail();
        serviceDetail.setService("PRSN");
        ExemptionTypes exemptionTypes = new ExemptionTypes();
        List<ExemptionType> exemptionTypeList = exemptionTypes.getExemptionType();
        ExemptionType exemptionType = new ExemptionType();
        exemptionType.setExemptionReasonCode("WALL");
        exemptionType.setDescription("No improvements can be made");
        exemptionType.setPWSDescription("Registering an exemption under the regulation 29(1)(b) exception");
        exemptionType.setPWSText(
                "You must supply a copy of the relevant report to demonstrate that no improvements can be made (if separate to the relevant EPC).");
        exemptionType.setDocumentsPWSLabel("test");
        exemptionType.setDocumentsPWSText("some description about file requirements and ...max of 10 files");      
        exemptionType.setDurationUnit("Y");
        exemptionType.setDuration(BigInteger.ONE);
        exemptionType.setMinDocuments(BigInteger.ONE);
        exemptionType.setMaxDocuments(BigInteger.ONE);
        exemptionType.setStartDateRequired(false);
        exemptionType.setTextRequired(false);
        exemptionType.setFRVRequired(false);
        exemptionType.setDocumentsRequired(false);
        exemptionType.setSequence(BigInteger.ONE);
        exemptionTypeList.add(exemptionType);
        serviceDetail.setExemptionTypes(exemptionTypes);
        serviceDetailList.add(serviceDetail);
        getPRSExemptionReferenceDataResponse.setServiceDetails(serviceDetails);
        return getPRSExemptionReferenceDataResponse;
    }

    private final static String getOperationName() {
        return "GetPRSExemptionReferenceDataWSDL";
    }
}
