package com.northgateps.nds.beis.esb.exemption;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.northgateps.nds.beis.api.ExemptionTypeDetails;
import com.northgateps.nds.beis.api.getexemptiontypetext.GetExemptionTypeTextNdsRequest;
import com.northgateps.nds.beis.api.getexemptiontypetext.GetExemptionTypeTextNdsResponse;
import com.northgateps.nds.beis.esb.RouteTestUtils;

import com.northgateps.nds.platform.logger.NdsLogger;
/**
 * Unit test for the GetExemptionTypeText Service route
 * 
 * 
 */
@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GetExemptionTypeTextRouteText extends CamelSpringTestSupport {

    private static final NdsLogger logger = NdsLogger.getLogger(GetExemptionTypeTextRouteText.class);
    private static final String routeNameUnderTest = "getExemptionTypeTextRoute";
    private static final String TEST_START_NAME = "direct:startGetExemptionTypeText";

    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiEndpoint;

    @Override
    protected AbstractApplicationContext createApplicationContext() {

       return RouteTestUtils.createApplicationContext(routeNameUnderTest);
    }

    @Test
    public void SuccessPathTest() throws Exception {
        logger.info("getExemptionTypeText test started");

        context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                replaceFromWith(TEST_START_NAME);
            }
        });

        context.start();
        GetExemptionTypeTextNdsResponse response = (GetExemptionTypeTextNdsResponse) apiEndpoint.requestBody(
                createGetExemptionTypeTextNdsRequest());
        assertTrue(response.isSuccess());
        assertNotNull(response.getExemptionTypeTextList());
        checkValuesForExemptionTypeText(response.getExemptionTypeTextList());
        assertMockEndpointsSatisfied();
    }

    private GetExemptionTypeTextNdsRequest createGetExemptionTypeTextNdsRequest() {
        GetExemptionTypeTextNdsRequest request = new GetExemptionTypeTextNdsRequest();
        ArrayList<String> filterCriteria = new ArrayList<String>();
        filterCriteria.add("");
        request.setFilterCriteria(filterCriteria);
        return request;
    }

    private void checkValuesForExemptionTypeText(List<ExemptionTypeDetails> exemptionTypeDetailsList) {
        for (ExemptionTypeDetails exemptionTypeDetails : exemptionTypeDetailsList) {
            if (exemptionTypeDetails.getCode() != null) {
                logger.debug(exemptionTypeDetails.getCode() + "--" + exemptionTypeDetails.getPwsDescription() + "--"
                        + exemptionTypeDetails.getPwsText() + "--" + exemptionTypeDetails.getService());
                assertTrue("code is empty", StringUtils.isNotBlank(exemptionTypeDetails.getCode()));
                assertTrue("pwsDescription is empty", StringUtils.isNotBlank(exemptionTypeDetails.getPwsDescription()));
                assertTrue("pwsText is empty", StringUtils.isNotBlank(exemptionTypeDetails.getPwsText()));
            }
        }
    }
}
