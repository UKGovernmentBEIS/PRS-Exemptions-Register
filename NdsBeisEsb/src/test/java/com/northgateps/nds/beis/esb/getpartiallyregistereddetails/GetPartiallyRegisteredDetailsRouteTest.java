package com.northgateps.nds.beis.esb.getpartiallyregistereddetails;

import static org.junit.Assume.assumeTrue;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.getpartiallyregistereddetails.GetPartiallyRegisteredDetailsNdsRequest;
import com.northgateps.nds.beis.api.getpartiallyregistereddetails.GetPartiallyRegisteredDetailsNdsResponse;
import com.northgateps.nds.beis.esb.MockNdsDirectoryKernel;
import com.northgateps.nds.beis.esb.beisregistration.BeisRegistrationCamelSpringTestSupport;
import com.northgateps.nds.platform.esb.directory.DirectoryManager;
import com.northgateps.nds.platform.esb.directory.UserManager;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Test for get partially registered details route  
 *
 */
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GetPartiallyRegisteredDetailsRouteTest extends BeisRegistrationCamelSpringTestSupport{

    private static final NdsLogger logger = NdsLogger.getLogger(GetPartiallyRegisteredDetailsRouteTest.class);
    public static final String routeNameUnderTest = "getPartiallyRegisteredDetailsServiceRoute";
    private static final String TEST_START_NAME = "direct:getPartiallyRegisteredDetails";

    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiEndpoint;    

    @Test
    public void testPassingInvalidDetailsUsernameFailsValidation() throws Exception{
       logger.info("testPassingInvalidDetailsUsernameFailsValidation route test started");
        
        assumeTrue(connection);
        
        MockNdsDirectoryKernel mockNdsDirectoryKernel = new MockNdsDirectoryKernel();
               
        Exception exception = null;   
        try{
            GetPartiallyRegisteredDetailsNdsResponse response = getPartiallyRegisteredDetails("BEIS", null, mockNdsDirectoryKernel);
        }
        catch (CamelExecutionException e) {
            exception = e.getExchange().getException();
        }

        assertNotNull("Exception should not be null ", exception);
        assertTrue("Expected ", exception.getMessage().contains("Expected the username but it was null"));
                                     
        assertMockEndpointsSatisfied();
        
        logger.info("testPassingInvalidDetailsUsernameFailsValidation route test complete");
    }
    
    @Test
    public void testPassingInvalidDetailsTenantFailsValidation() throws Exception{
       logger.info("testPassingInvalidDetailsTenantFailsValidation route test started");
               
        assumeTrue(connection);
        
        MockNdsDirectoryKernel mockNdsDirectoryKernel = new MockNdsDirectoryKernel();
               
        Exception exception = null;   
        try{
            GetPartiallyRegisteredDetailsNdsResponse response = getPartiallyRegisteredDetails(null, "USERNAME", mockNdsDirectoryKernel);
        }
        catch (CamelExecutionException e) {
            exception = e.getExchange().getException();
        }

        assertNotNull("Exception should not be null ", exception);
        assertTrue("Expected ", exception.getMessage().contains("Expected the tenant but it was null"));
                                     
        assertMockEndpointsSatisfied();
        
        logger.info("testPassingInvalidDetailsTenantFailsValidation route test complete");
    }


        
    
    
    @Test
    public void testGetPartiallyRegisteredDetailsSuccessPath() throws Exception {

        logger.info("testGetPartiallyRegisteredDetailsSuccessPath route test started");
        
        assumeTrue(connection);
        
        MockNdsDirectoryKernel mockNdsDirectoryKernel = new MockNdsDirectoryKernel();
        
        //Create a new user
        final BeisRegistrationDetails registeredUser = registerNewUser(null,mockNdsDirectoryKernel);
       
        GetPartiallyRegisteredDetailsNdsResponse response = getPartiallyRegisteredDetails(registeredUser.getTenant(), registeredUser.getUserDetails().getUsername(),mockNdsDirectoryKernel);
        
        assertTrue(response.isSuccess());
        assertNotNull(response.getPartiallyRegisteredDetails());
      
        assertMockEndpointsSatisfied();
        
        logger.info("testGetPartiallyRegisteredDetailsSuccessPath route test complete");
        
    }  
    
    /**
     * This private function will call the route for the newly added user and 
     * return the details for confirmation. The Ldap calls are not mocked because
     * even for test is guaranteed to be there otherwise nothing would work.
     * 
     * @param tenant - Multi tenant design so provide the tenancy of interest
     * @param userName - The user name with in the tenancy to get
     * @return - The partially registered details.
     */
    private GetPartiallyRegisteredDetailsNdsResponse getPartiallyRegisteredDetails (String tenant, String userName, MockNdsDirectoryKernel mockNdsDirectoryKernel) throws Exception {
        
        
        GetPartiallyRegisteredDetailsNdsRequest request = new GetPartiallyRegisteredDetailsNdsRequest();
                        
        request.setTenant(tenant);
        request.setUsername(userName);
        
        context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                replaceFromWith(TEST_START_NAME);
                DirectoryManager directoryManager = new DirectoryManager();
                directoryManager.setKernel(mockNdsDirectoryKernel);
                UserManager userManager = new UserManager();
                userManager.setDirectoryManager(directoryManager);
                GetPartiallyRegisteredDetailsLdapComponent ldapComponent = new GetPartiallyRegisteredDetailsLdapComponent();
                ldapComponent.setDirectoryManager(directoryManager);
                ldapComponent.setUserManager(userManager);
                weaveById("getPartiallyRegisteredDetailsLdapComponent.process").replace().bean(ldapComponent, "process");
                weaveById("getPartiallyRegisteredDetailsLdapComponent.processResponse").replace().bean(ldapComponent, "processResponse");
            }
        });

        //Start the route
        context.start();
        
        //Make the request to get the response out
        GetPartiallyRegisteredDetailsNdsResponse response = (GetPartiallyRegisteredDetailsNdsResponse) apiEndpoint.requestBody(
                request);

        return response;
        
    }
 }
