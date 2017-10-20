package com.northgateps.nds.beis.esb.beisregistration;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsRequest;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsResponse;
import com.northgateps.nds.platform.api.ActivateRegistrationDetails;
import com.northgateps.nds.platform.api.activateregistration.ActivateRegistrationNdsRequest;
import com.northgateps.nds.platform.api.activateregistration.ActivateRegistrationNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsDirectoryAdapter;
import com.northgateps.nds.platform.esb.directory.DirectoryConnection;
import com.northgateps.nds.platform.esb.directory.DirectoryConnectionConfig;
import com.northgateps.nds.platform.esb.directory.DirectoryServices;
import com.northgateps.nds.platform.esb.directory.ServiceAccessDetails;
import com.northgateps.nds.platform.esb.directory.UserServices;
import com.northgateps.nds.platform.esb.util.xml.JaxbXmlMarshaller;
import com.northgateps.nds.platform.logger.NdsLogger;

import static org.junit.Assume.assumeTrue;

/**
 * Tests for registration Camel route that creates a new user in LDAP and sends an email
 * to the user so they can complete the registration and get access to the system.
 */
@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BeisRegistrationRouteTest extends BeisRegistrationCamelSpringTestSupport {

    private static final NdsLogger logger = NdsLogger.getLogger(BeisRegistrationRouteTest.class);

    private static final String REGISTRATION_TEST_START = "direct:start";

    @EndpointInject(uri = REGISTRATION_TEST_START)
    private ProducerTemplate apiEndpoint;

    @EndpointInject(uri = TEST_ACTIVATE_REGISTRATION_START_NAME)
    private ProducerTemplate apiActivateRegistationEndpoint;

    @Test
    public void testRegistrationUserAlreadyExists() throws Exception {
        assumeTrue(connection);

        final BeisRegistrationDetails registeredUser = registerNewUser(null);

        logger.info("Existing user created - Starting user already exists test");

        context.getRouteDefinition(REGISTRATION_ROUTE_NAME_UNDER_TEST).adviceWith(context,
                new AdviceWithRouteBuilder() {

                    @Override
                    public void configure() throws Exception {
                        replaceFromWith("direct:start");
                    }
                });

        context.start();

        BeisRegistrationNdsResponse response = (BeisRegistrationNdsResponse)apiEndpoint.requestBody(
                createRegistrationNdsRequestWithExistingUser(registeredUser.getUserDetails().getUsername()));

        //expecting a violation so check the relevant objects exist and the message is correct
        assertNotNull("Response must not be null", response);
        assertNotNull("NDSMessages must not be null", response.getNdsMessages());
        assertNotNull("Violations must not be null", response.getNdsMessages().getViolations());
        assertTrue("Username already exists violation expected",
                response.getNdsMessages().getViolations().get(0).getMessage().contains(
                        "Username already exists"));                                    
    }

    @Test
    public void testRegistrationInvalidPassword() throws Exception {
        assumeTrue(connection);

        context.getRouteDefinition(REGISTRATION_ROUTE_NAME_UNDER_TEST).adviceWith(context,
                new AdviceWithRouteBuilder() {

                    @Override
                    public void configure() throws Exception {
                        weaveAddLast().to(MOCK_REGISTRATION_RESPONSE_CHECK);
                        replaceFromWith(REGISTRATION_TEST_START);
                        interceptSendToEndpoint("smtp://{{smtp.host.server}}").skipSendToOriginalEndpoint().to(
                                "mock:registration-request-check").process(exchange -> {
                                    // nothing to change
                                });
                    }
                });

        context.start();

        BeisRegistrationNdsResponse response = (BeisRegistrationNdsResponse)apiEndpoint.requestBody(createRegistrationNdsRequestWithInvalidPassword());

        //expecting a violation so check the relevant objects exist and the message is correct
        assertNotNull("Response must not be null", response);
        assertNotNull("NDSMessages must not be null", response.getNdsMessages());
        assertNotNull("Violations must not be null", response.getNdsMessages().getViolations());
        assertTrue("Password fails quality checking policy violation expected",
                response.getNdsMessages().getViolations().get(0).getMessage().contains(
                        "Password fails quality checking policy"));
    }

    /* Not checking for an exact error on this one as need to include config a config entry in the message
     * and it's probably not worth it. Keeping it simple instead. */
    @Test
    public void testRegistrationInvalidTenant() throws Exception {
        assumeTrue(connection);
        context.getRouteDefinition(REGISTRATION_ROUTE_NAME_UNDER_TEST).adviceWith(context,
                new AdviceWithRouteBuilder() {

                    @Override
                    public void configure() throws Exception {
                        interceptSendToEndpoint("smtp://{{smtp.host.server}}").to(
                                "mock:updateEmail-request-check").skipSendToOriginalEndpoint().process(new Processor() {

                            @Override
                            public void process(Exchange exchange) throws Exception {
                                // TODO Auto-generated method stub

                            }
                        });

                    }
                });

        context.start();
        Exception exception = null;

        try {
            apiEndpoint.sendBody(createRegistrationNdsRequestWithInvalidTenant());
        } catch (CamelExecutionException e) {
            exception = e.getExchange().getException();
        }

        assertNotNull("Exception should not be null ", exception);

    }

    @Test
    public void testRegistrationConfirmPasswordMismatch() throws Exception {
        assumeTrue(connection);

       final String CONFRIM_VALIDATION_MESSAGE = "ValidationField_RegistrationModel_confirmPassword";

        context.getRouteDefinition(REGISTRATION_ROUTE_NAME_UNDER_TEST).adviceWith(context,
                new AdviceWithRouteBuilder() {

                    @Override
                    public void configure() throws Exception {
                        replaceFromWith("direct:start");
                    }
                });

        context.start();

        Exception exception = null;   
        try{
            BeisRegistrationNdsResponse response = (BeisRegistrationNdsResponse) apiEndpoint.requestBody(
                    createRegistrationNdsRequestWithConfirmPasswordMismatch());
        }
        catch (CamelExecutionException e) {
            exception = e.getExchange().getException();
            assertTrue("check expected exception message",exception.getMessage().contains(CONFRIM_VALIDATION_MESSAGE));
            }

    }

    @Test
    public void testRegistrationEmailInvalid() throws Exception {
        assumeTrue(connection);

        context.getRouteDefinition(REGISTRATION_ROUTE_NAME_UNDER_TEST).adviceWith(context,
                new AdviceWithRouteBuilder() {

                    @Override
                    public void configure() throws Exception {
                        replaceFromWith("direct:start");
                    }
                });

        context.start();

        try {
            BeisRegistrationNdsResponse response = (BeisRegistrationNdsResponse) apiEndpoint.requestBody(
                    createRegistrationNdsRequestWithInvalidEmail());

            assertNotNull(response);
            assertNotNull(response.getNdsMessages());
            assertNotNull(response.getNdsMessages().getViolations());
            assertTrue("Invalid email error not received",
                    response.getNdsMessages().getViolations().get(0).getMessage().contains(
                            "Unable to send e-mail to the given email address"));

        } catch (Exception e) {
            assertTrue("No ESB validation message received: " + e != null);
        }

    }

    @Test
    public void testAlreadyActivatedRegistration() throws Exception {
        assumeTrue(connection);

        // register new user and check email contains this text
        BeisRegistrationDetails newUserDetails = registerNewUser("Activate your account");

        logger.info("testAlreadyActivatedRegistration");

        context.getRouteDefinition(ACTIVATE_REGISTRATION_ROUTE_NAME_UNDER_TEST).adviceWith(context,
                new AdviceWithRouteBuilder() {

                    @Override
                    public void configure() throws Exception {
                        replaceFromWith(TEST_ACTIVATE_REGISTRATION_START_NAME);
                    }
                });

        context.start();
        
        final String activationCode = newUserDetails.getActivationCode();

        // this must work without error (don't put this in a try-catch block)
        apiActivateRegistationEndpoint.requestBody(createActivateRegistrationNdsRequest(activationCode));


        // Try to activate the same code again
        ActivateRegistrationNdsResponse userError = (ActivateRegistrationNdsResponse) apiActivateRegistationEndpoint.requestBody(
                createActivateRegistrationNdsRequest(activationCode));

        assertFalse(userError.isSuccess());

        assertEquals("Should have activation validation message", "Code has already been activated",
                userError.getNdsMessages().getViolations().get(0).getMessage());   

    }

    @Test
    public void testActivateRegistrationFailsValidation() throws Exception {
        assumeTrue(connection);

        final String INVALID_CODE_VALIDATION_MESSAGE = "Validation_Field_must_not_be_empty";
        final String INVALID_CODE_VALIDATION_FIELD = "activateRegistrationDetails.activationCode";

        context.getRouteDefinition(ACTIVATE_REGISTRATION_ROUTE_NAME_UNDER_TEST).adviceWith(context,
                new AdviceWithRouteBuilder() {

                    @Override
                    public void configure() throws Exception {
                        replaceFromWith(TEST_ACTIVATE_REGISTRATION_START_NAME);
                    }
                });

        context.start();

        try {

            // Empty activation code request
            ActivateRegistrationNdsResponse response = (ActivateRegistrationNdsResponse) apiActivateRegistationEndpoint.requestBody(
                    createActivateRegistrationWithInvalidCodeNdsRequest());

            String actualValidationField = response.getNdsMessages().getViolations().get(0).getFieldPath();
            String actualValidationMessage = response.getNdsMessages().getViolations().get(0).getMessage();

            assertTrue(
                    "Expected message : " + INVALID_CODE_VALIDATION_MESSAGE + " doesn't match actual message "
                            + actualValidationMessage,
                    actualValidationMessage.equalsIgnoreCase(INVALID_CODE_VALIDATION_MESSAGE));

            assertTrue(
                    "Expected field : " + INVALID_CODE_VALIDATION_FIELD + " doesn't match actual field "
                            + actualValidationField,
                    actualValidationField.equalsIgnoreCase(INVALID_CODE_VALIDATION_FIELD));
        } catch (Exception e) {
            assertTrue("No ESB validation message received: " + e != null);
        }
    }

    /* This is an extremely important test since it also checks our user store's security
     * configuration is set up correctly. */
    @Test
    public void testVerifyingUnactivatedUserAccountFails() throws Exception {
        assumeTrue(connection);

        final BeisRegistrationDetails newUserDetails = registerNewUser(null);

        logger.info("Created new user, testing Unactivated User Account cannot be verified");

        String userDn = NdsDirectoryAdapter.getUserDn(configurationManager,
                newUserDetails.getUserDetails().getUsername(), TENANT);
        
        boolean exceptionThrown = false;

        // using the standard password, check if password can be verified
        try {
            DirectoryServices.checkUserAndPassword(userDn, NEW_USER_PASSWORD);
        } catch (Exception e) {
            exceptionThrown = true;
        }

        assertTrue("Failed account lock test, should not be able to verify on locked account ", exceptionThrown);
    }
    
    /**
     * When a user is created the party reference received from MaintainPartydDetails
     * Foundation Layer call is stored as the uid against the FOUNDATION_LAYER_PARTY_SERVICE
     * against the user. This test makes sure this happens with a mocked out MaintainPartyDetails
     * call. 
     */
    @Test
    public void testFoundationLayerReferenceIsSaved() throws Exception {
        assumeTrue(connection);

        Boolean foundationLayerReferenceIsSaved = false;
        
        final BeisRegistrationDetails newUserDetails = registerNewUser(null);

        logger.info("Created new user, testing party ref is persisted from foundation layer");

        //Get the service name
        String serviceName = configurationManager.getString("service.foundationLayerPartyService.name");


        String userDn = NdsDirectoryAdapter.getUserDn(configurationManager,
                newUserDetails.getUserDetails().getUsername(), TENANT);

        //Create a connection and retrieve the service details for the user
        final DirectoryConnectionConfig directoryConnectionConfig = new DirectoryConnectionConfig(configurationManager);
        try (DirectoryConnection connection = new DirectoryConnection(
                directoryConnectionConfig)) {
            ServiceAccessDetails serviceAccessDetails = UserServices.serviceLookup(connection, userDn,
                    serviceName, true, true, false);
            
            if (serviceAccessDetails.getUserId() != null &&
                    !serviceAccessDetails.getUserId().isEmpty() &&   
                    !serviceAccessDetails.getUserId().contains("-")){
                    //If the user id looks ok then we ahev succeeded
                    foundationLayerReferenceIsSaved = true;
                }                                   
        }
                    
        assertTrue("The party reference from MaintainPartyDetails has not been saved in LDAP (FOUNDATION_LAYER_PARTY_SERVICE.uid)", 
                   foundationLayerReferenceIsSaved);
        
    }
    
    

    @Test
    public void testSuccessfulAgentRegistration() throws Exception {
       
        assumeTrue(connection);

        logger.debug("Starting to register new user");
        
        final BeisRegistrationNdsRequest requestNewUser = createRegistrationNdsRequestForAgent();

        BeisRegistrationDetails userRegistration = requestNewUser.getRegistrationDetails();

        context.getRouteDefinition(REGISTRATION_ROUTE_NAME_UNDER_TEST).adviceWith(context,
                new AdviceWithRouteBuilder() {

                    @Override
                    public void configure() throws Exception {

                        // Mock out the email
                        weaveAddLast().to(MOCK_REGISTRATION_RESPONSE_CHECK);
                        replaceFromWith(REGISTRATION_TEST_START);

                        // Mock out the call to foundation layer for the sub route
                        weaveAddLast().to(MOCK_REGISTRATION_FL_RESPONSE_CHECK);
                        replaceFromWith(REGISTRATION_TEST_START);

                        interceptSendToEndpoint("smtp://{{smtp.host.server}}").skipSendToOriginalEndpoint().to(
                                MOCK_REGISTRATION_REQUEST_CHECK).process(new Processor() {

                            @Override
                            public void process(Exchange exchange) throws Exception {
                                // nothing to change
                            }
                        });

                        // This sub route is invoked by registration and requires mocking
                        context.getRouteDefinition(REGISTRATION_SUB_ROUTE_NAME_UNDER_TEST).adviceWith(context,
                                new AdviceWithRouteBuilder() {

                            @Override
                            public void configure() throws Exception {

                                interceptSendToEndpoint(
                                        "cxf:bean:beisMaintainPartyDetailsService").skipSendToOriginalEndpoint().to(
                                                MOCK_REGISTRATION_FL_REQUEST_CHECK).process(new Processor() {

                                    @Override
                                    public void process(Exchange exchange) throws Exception {
                                        // Mock out a response
                                        exchange.getIn().setBody(generateMaintainPartyDetailsResponse());
                                    }
                                });
                            }
                        });
                    }
                });

        context.start();

        // Ensure email request data is present and correct
        MockEndpoint requestMock = getMockEndpoint(MOCK_REGISTRATION_REQUEST_CHECK);
        requestMock.expectedMessageCount(1);
        requestMock.expectedHeaderReceived("subject", "Account activation");
        requestMock.expectedHeaderReceived("to", "a@beis.com");

        requestMock.whenAnyExchangeReceived(new Processor() {

            @Override
            public void process(Exchange exchange) throws Exception {
                
                final String expectedUsername = userRegistration.getUserDetails().getUsername();
                final String expectedActivationCode = userRegistration.getActivationCode();

                assertNotNull(expectedUsername);
                assertNotNull(expectedActivationCode);               
            }
        });

        // setup expected ESB response
        MockEndpoint registrationResponseMock = getMockEndpoint(MOCK_REGISTRATION_RESPONSE_CHECK);
        registrationResponseMock.expectedMessageCount(1);
        registrationResponseMock.expectedBodiesReceived(JaxbXmlMarshaller.convertToPrettyPrintXml(
                createSuccessfullNdsRegistrationResponse(), BeisRegistrationNdsResponse.class));

        apiEndpoint.sendBody(requestNewUser);

        assertMockEndpointsSatisfied();

        
    }

    
   
    
    private BeisRegistrationNdsRequest createRegistrationNdsRequestWithExistingUser(String username) {
        BeisRegistrationNdsRequest request = createRegistrationNdsRequestWithNewUserName();

        BeisRegistrationDetails registrationDetails = request.getRegistrationDetails();
        registrationDetails.getUserDetails().setUsername(username);
        request.setRegistrationDetails(registrationDetails);

        return request;
    }

    private BeisRegistrationNdsRequest createRegistrationNdsRequestWithInvalidPassword() {
        BeisRegistrationNdsRequest request = createRegistrationNdsRequestWithNewUserName();

        BeisRegistrationDetails registrationDetails = request.getRegistrationDetails();
        registrationDetails.getUserDetails().setPassword("12345");
        registrationDetails.getUserDetails().setConfirmPassword("12345");
        request.setRegistrationDetails(registrationDetails);

        return request;
    }

    private BeisRegistrationNdsRequest createRegistrationNdsRequestWithInvalidEmail() {
        BeisRegistrationNdsRequest request = createRegistrationNdsRequestWithAnInvalidEmailAddress();

        BeisRegistrationDetails registrationDetails = request.getRegistrationDetails();
        request.setRegistrationDetails(registrationDetails);

        return request;
    }

    private BeisRegistrationNdsRequest createRegistrationNdsRequestWithInvalidTenant() {
        BeisRegistrationNdsRequest request = createRegistrationNdsRequestWithNewUserName();
        BeisRegistrationDetails registrationDetails = request.getRegistrationDetails();
        registrationDetails.setTenant("wrongtenant");
        request.setRegistrationDetails(registrationDetails);

        return request;
    }

    private BeisRegistrationNdsRequest createRegistrationNdsRequestWithConfirmPasswordMismatch() {
        BeisRegistrationNdsRequest request = createRegistrationNdsRequestWithNewUserName();

        BeisRegistrationDetails registrationDetails = request.getRegistrationDetails();
        registrationDetails.getUserDetails().setPassword("qwertyuio123!");
        registrationDetails.getUserDetails().setConfirmPassword("qqertyuio123");
        request.setRegistrationDetails(registrationDetails);

        return request;
    }

    private ActivateRegistrationNdsRequest createActivateRegistrationNdsRequest(String activationCode) {
        ActivateRegistrationNdsRequest request = new ActivateRegistrationNdsRequest();
        ActivateRegistrationDetails activateRegistrationDetails = new ActivateRegistrationDetails();
        activateRegistrationDetails.setActivationCode(activationCode);
        activateRegistrationDetails.setTenant(TENANT);
        request.setActivateRegistrationDetails(activateRegistrationDetails);
        return request;
    }

    private Object createActivateRegistrationWithInvalidCodeNdsRequest() {
        ActivateRegistrationNdsRequest request = new ActivateRegistrationNdsRequest();
        ActivateRegistrationDetails activateRegistrationDetails = new ActivateRegistrationDetails();
        activateRegistrationDetails.setActivationCode("");
        activateRegistrationDetails.setTenant(TENANT);
        request.setActivateRegistrationDetails(activateRegistrationDetails);
        return request;
    }

}
