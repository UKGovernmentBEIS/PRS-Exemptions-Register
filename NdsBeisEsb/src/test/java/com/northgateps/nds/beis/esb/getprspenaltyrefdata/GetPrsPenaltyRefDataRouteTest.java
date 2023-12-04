package com.northgateps.nds.beis.esb.getprspenaltyrefdata;

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
import com.northgateps.nds.beis.api.PenaltyTypeDetails;
import com.northgateps.nds.beis.api.getprspenaltyrefdata.GetPrsPenaltyRefDataNdsRequest;
import com.northgateps.nds.beis.api.getprspenaltyrefdata.GetPrsPenaltyRefDataNdsResponse;
import com.northgateps.nds.beis.backoffice.service.getprspenaltyreferencedata.GetPRSPenaltyReferenceDataResponse;
import com.northgateps.nds.beis.backoffice.service.getprspenaltyreferencedata.GetPRSPenaltyReferenceDataResponse.ServiceDetails;
import com.northgateps.nds.beis.backoffice.service.getprspenaltyreferencedata.GetPRSPenaltyReferenceDataResponse.ServiceDetails.ServiceDetail;
import com.northgateps.nds.beis.backoffice.service.getprspenaltyreferencedata.GetPRSPenaltyReferenceDataResponse.ServiceDetails.ServiceDetail.PenaltyTypes;
import com.northgateps.nds.beis.backoffice.service.getprspenaltyreferencedata.GetPRSPenaltyReferenceDataResponse.ServiceDetails.ServiceDetail.PenaltyTypes.PenaltyType;
import com.northgateps.nds.beis.esb.RouteTestUtils;

import com.northgateps.nds.platform.esb.util.xml.JaxbXmlMarshaller;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Unit test for the GetPrsPenaltyRefData Service route
 */
@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GetPrsPenaltyRefDataRouteTest extends CamelSpringTestSupport {

    private static final NdsLogger logger = NdsLogger.getLogger(GetPrsPenaltyRefDataRouteTest.class);
    private static final String routeNameUnderTest = "GetPrsPenaltyRefDataRoute";
    private static final String TEST_START_NAME = "direct:startGetPrsPenaltyRefData";
    private static final String MOCK_GRD_REQUEST_CHECK = "mock:getPrsPenaltyRefData";
    private static final String MOCK_GRD_RESPONSE_CHECK = "mock:getPrspenaltyRefData-response-check";

    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiEndpoint;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
    	return RouteTestUtils.createApplicationContext(routeNameUnderTest);
    }

     @Test
    public void SuccessPathTest() throws Exception {
        logger.info("getPenaltyTypeText test started");

        AdviceWith.adviceWith(context.getRouteDefinition(routeNameUnderTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_GRD_RESPONSE_CHECK);
                replaceFromWith("direct:startGetPrsPenaltyRefData");
                interceptSendToEndpoint("cxf:bean:getPrsPenaltyRefDataEndPoint").skipSendToOriginalEndpoint().to(
                        MOCK_GRD_REQUEST_CHECK).process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody(createGetPrsPenaltyRefDataResponse());
                    }
                });
            }
        });

        context.start();
        MockEndpoint getPrspenaltyRefDataServiceMock = getMockEndpoint(MOCK_GRD_REQUEST_CHECK);
        getPrspenaltyRefDataServiceMock.expectedMessageCount(1);
        getPrspenaltyRefDataServiceMock.expectedHeaderReceived(CxfConstants.OPERATION_NAME, getOperationName());

        MockEndpoint getPrsPenaltyRefDataResponseMock = getMockEndpoint(MOCK_GRD_RESPONSE_CHECK);
        getPrsPenaltyRefDataResponseMock.expectedMessageCount(1);

        getPrsPenaltyRefDataResponseMock.expectedBodiesReceived(JaxbXmlMarshaller.convertToPrettyPrintXml(
                createGetPrsPenaltyRefDataNdsResponse(), GetPrsPenaltyRefDataNdsResponse.class));

        apiEndpoint.sendBody(createGetPrsPenaltyRefDataNdsRequest());
        assertMockEndpointsSatisfied();
    }

    private GetPrsPenaltyRefDataNdsRequest createGetPrsPenaltyRefDataNdsRequest() {
        GetPrsPenaltyRefDataNdsRequest request = new GetPrsPenaltyRefDataNdsRequest();
        return request;
    }

    private GetPrsPenaltyRefDataNdsResponse createGetPrsPenaltyRefDataNdsResponse() {
        GetPrsPenaltyRefDataNdsResponse response = new GetPrsPenaltyRefDataNdsResponse();
        response.setSuccess(true);
        List<PenaltyTypeDetails> penaltyTypeTextList = new ArrayList<PenaltyTypeDetails>();
        PenaltyTypeDetails penaltyTypeDetail = new PenaltyTypeDetails();
        penaltyTypeDetail.setService("PRSN");
        penaltyTypeDetail.setCode("WALL");
        penaltyTypeDetail.setDescription("No improvements can be made");
        penaltyTypeDetail.setPwsBreachDescription("Registering an penalty under the regulation 29(1)(b) exception");
        penaltyTypeDetail.setExemptionRequired("false");
        penaltyTypeDetail.setMaxValue("1");
        penaltyTypeTextList.add(penaltyTypeDetail);
        response.setPenaltyTypeTextList(penaltyTypeTextList);
        return response;
    }

    private GetPRSPenaltyReferenceDataResponse createGetPrsPenaltyRefDataResponse() {
        GetPRSPenaltyReferenceDataResponse getPRSpenaltyReferenceDataResponse = new GetPRSPenaltyReferenceDataResponse();
        getPRSpenaltyReferenceDataResponse.setSuccess(true);
        ServiceDetails serviceDetails = new ServiceDetails();
        List<ServiceDetail> serviceDetailList = serviceDetails.getServiceDetail();
        ServiceDetail serviceDetail = new ServiceDetail();
        serviceDetail.setService("PRSN");
        PenaltyTypes penaltyTypes = new PenaltyTypes();
        List<PenaltyType> penaltyTypeList = penaltyTypes.getPenaltyType();
        PenaltyType penaltyType = new PenaltyType();
        penaltyType.setPenaltyReasonCode("WALL");
        penaltyType.setDescription("No improvements can be made");
        penaltyType.setPWSBreachDescription("Registering an penalty under the regulation 29(1)(b) exception");
        penaltyType.setExemptionRequired(false);
        penaltyType.setMaximumValue(BigInteger.ONE);
        penaltyTypeList.add(penaltyType);
        serviceDetail.setPenaltyTypes(penaltyTypes);
        serviceDetailList.add(serviceDetail);
        getPRSpenaltyReferenceDataResponse.setServiceDetails(serviceDetails);
        return getPRSpenaltyReferenceDataResponse;
    }

    private final static String getOperationName() {
        return "GetPRSPenaltyReferenceDataWSDL";
    }
}
