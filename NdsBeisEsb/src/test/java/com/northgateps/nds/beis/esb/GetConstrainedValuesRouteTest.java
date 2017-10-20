package com.northgateps.nds.beis.esb;

import java.util.TreeMap;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;

import com.northgateps.nds.beis.api.getconstrainedvalues.GetConstrainedValuesNdsRequest;
import com.northgateps.nds.beis.api.getconstrainedvalues.GetConstrainedValuesNdsResponse;
import com.northgateps.nds.platform.api.ConstrainedValue;
import com.northgateps.nds.platform.esb.camel.NdsFileSystemXmlApplicationContext;
import com.northgateps.nds.platform.esb.security.MockAuthentication;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Test class for Constrained Values route
 */
public class GetConstrainedValuesRouteTest extends CamelSpringTestSupport {

	private static final NdsLogger logger = NdsLogger.getLogger(GetConstrainedValuesRouteTest.class);
	private static final String routeNameUnderTest = "getConstrainedValuesRoute";
	private static final String TEST_START_NAME = "direct:startGetConstrainedValues";

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
	public void testGetConstrainedValuesForCountryListName() throws Exception {
		logger.info("getConstrainedValuesRoute test started");

		context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

			@Override
			public void configure() throws Exception {
				replaceFromWith(TEST_START_NAME);
			}
		});

		context.start();
		GetConstrainedValuesNdsResponse response = (GetConstrainedValuesNdsResponse) apiEndpoint
				.requestBody(getRequestForCountryOnly());

		assertTrue(response.isSuccess());
		assertNotNull(response.getConstrainedValues());
		checkValuesForCountry(response.getConstrainedValues());

		assertMockEndpointsSatisfied();
	}

	private void checkValuesForCountry(TreeMap<String, ConstrainedValue[]> treeMap) {

		// if csv removes this parent then this test will need changing

		for (ConstrainedValue[] innerTreeMap : treeMap.values()) {

			for (ConstrainedValue constrainedValue : innerTreeMap) {

				logger.debug(constrainedValue.getCode() + "--" + constrainedValue.getListName() + "--"
						+ constrainedValue.getMeaning() + "--" + constrainedValue.getParentCode());

				assertTrue("code is empty", StringUtils.isNotBlank(constrainedValue.getCode()));
				assertTrue("listname is empty", StringUtils.isNotBlank(constrainedValue.getListName()));
				assertTrue("listnames should be all COUNTRY", constrainedValue.getListName().equals("COUNTRY"));

			}

		}

	}

	private GetConstrainedValuesNdsRequest getRequestForCountryOnly() {

		GetConstrainedValuesNdsRequest request = new GetConstrainedValuesNdsRequest();

		request.getRequestedListNames().add("COUNTRY");

		return request;
	}

}
