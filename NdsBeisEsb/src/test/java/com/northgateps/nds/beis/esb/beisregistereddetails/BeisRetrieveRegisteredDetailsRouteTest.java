package com.northgateps.nds.beis.esb.beisregistereddetails;

import static org.junit.Assume.assumeTrue;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsResponse;
import com.northgateps.nds.beis.esb.MockNdsDirectoryKernel;
import com.northgateps.nds.beis.esb.beisregistration.BeisRegistrationCamelSpringTestSupport;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Tests the registered user's details are retrieved from directory
 */
@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BeisRetrieveRegisteredDetailsRouteTest extends BeisRegistrationCamelSpringTestSupport {

    private static final NdsLogger logger = NdsLogger.getLogger(BeisRetrieveRegisteredDetailsRouteTest.class);
        
    private static final String TEST_START_NAME = "direct:startRetrieveRegisteredDetailsServiceRoute";

    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiEndpoint;


    @Test
    public void testRetrieveRegisteredUserDetails() throws Exception {
        logger.info("RetrieveRegisteredUserDetailsRoute test started");

        assumeTrue(connection);
        MockNdsDirectoryKernel mockNdsDirectoryKernel = new MockNdsDirectoryKernel();

        final BeisRegistrationDetails registeredUser = registerNewUser(null, mockNdsDirectoryKernel);
       
        RetrieveRegisteredDetailsNdsResponse response = retrieveRegistrationDetails(registeredUser.getTenant(), registeredUser.getUserDetails().getUsername(), mockNdsDirectoryKernel);
        
        assertTrue(response.isSuccess());
        assertNotNull(response.getBeisRegistrationDetails());
      
        assertMockEndpointsSatisfied();
        
    }
}