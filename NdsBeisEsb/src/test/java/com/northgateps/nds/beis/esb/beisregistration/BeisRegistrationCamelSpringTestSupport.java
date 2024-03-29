package com.northgateps.nds.beis.esb.beisregistration;

import java.math.BigInteger;
import java.util.ArrayList;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.BeforeClass;
import org.springframework.context.support.AbstractApplicationContext;

import com.northgateps.nds.beis.api.AccountType;
import com.northgateps.nds.beis.api.AgentNameDetails;
import com.northgateps.nds.beis.api.BeisAccountDetails;
import com.northgateps.nds.beis.api.BeisAddressDetail;
import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.BeisUserDetails;
import com.northgateps.nds.beis.api.PersonNameDetail;
import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsRequest;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsResponse;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsRequest;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsResponse;
import com.northgateps.nds.beis.backoffice.service.core.MessageStructure;
import com.northgateps.nds.beis.backoffice.service.core.MessagesStructure;
import com.northgateps.nds.beis.backoffice.service.getpartydetails.GetPartyDetailsResponse;
import com.northgateps.nds.beis.backoffice.service.maintainpartydetails.MaintainPartyDetailsResponse;
import com.northgateps.nds.beis.esb.MockEndpointInitializer;
import com.northgateps.nds.beis.esb.MockNdsDirectoryKernel;
import com.northgateps.nds.beis.esb.RouteTestUtils;
import com.northgateps.nds.beis.esb.getpartiallyregistereddetails.GetPartiallyRegisteredDetailsRouteTest;
import com.northgateps.nds.beis.esb.passwordreset.BeisPasswordResetRouteTest;
import com.northgateps.nds.beis.esb.registration.RetrieveRegisteredDetailsRetrieveAccountIdLdapComponent;
import com.northgateps.nds.beis.esb.registration.SaveRegisteredAccountDetailsRouteTest;
import com.northgateps.nds.platform.application.api.utils.PlatformTokenFactory;

import com.northgateps.nds.platform.esb.directory.DirectoryConnection;
import com.northgateps.nds.platform.esb.directory.DirectoryConnectionConfig;
import com.northgateps.nds.platform.esb.directory.DirectoryManager;
import com.northgateps.nds.platform.esb.directory.UserManager;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.esb.util.xml.JaxbXmlMarshaller;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

import static org.junit.Assume.assumeTrue;

public class BeisRegistrationCamelSpringTestSupport extends CamelSpringTestSupport {

    /**
     * True if a connection to the datastore can be established.
     * If not, tests may be skipped since attempts to connect will fail.
     */
    protected static boolean connection = false;

    protected static ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();

    private static final NdsLogger logger = NdsLogger.getLogger(BeisRegistrationCamelSpringTestSupport.class);

    protected static final String PARTY_REF_FOR_NEW_USERS = "1366";
    protected static final String REGISTRATION_ROUTE_NAME_UNDER_TEST = "beisRegistrationServiceRoute";

    protected static final String REGISTRATION_SUB_ROUTE_NAME_UNDER_TEST = "maintainPartyDetails_SubRouteForRegistration";
    protected static final String RETRIEVE_SUB_ROUTE_NAME_UNDER_TEST = "getPartyDetails_SubRouteForRetrievingRegistrationDetails";
    protected static final String INTEGRATION_TEST_USERNAME_PREFIX = "Beistest-";

    protected static final String NEW_USER_PASSWORD = "Qwertyuio123!";

    private static final String REGISTRATION_TEST_START = "direct:start";

    protected static final String MOCK_REGISTRATION_RESPONSE_CHECK = "mock:registration-response-check";

    protected static final String MOCK_REGISTRATION_FL_RESPONSE_CHECK = "mock:registration-fl-response-check";

    protected static final String MOCK_REGISTRATION_FL_REQUEST_CHECK = "mock:registration-fl-request-check";

    private static final String MOCK_RETRIEVE_FL_RESPONSE_CHECK = "mock:fl-response-check";
    private static final String MOCK_RETRIEVE_FL_REQUEST_CHECK = "mock:fl-request-check";

    // Dummy placeholder where we can intercept Registration request for when it sends an email
    protected static final String MOCK_INTERCEPTED_SMTP_SERVER = "mock:intercepted-smtp-server";

    @EndpointInject(uri = REGISTRATION_TEST_START)
    private ProducerTemplate apiEndpoint;

    protected static final String retrieveRouteName = "retrieveRegisteredDetailsServiceRoute";
    private static final String restrieveStartName = "direct:startRetrieveRegisteredDetailsServiceRoute";

    /**
     * Tenent information is dependent on the system we're testing against and is needed
     * for all user operations to identify the user.
     */
    protected static String TENANT = configurationManager.getString("ldap.tenant");

    protected static final String TEST_ACTIVATE_REGISTRATION_START_NAME = "direct:startActReg";
    protected static final String ACTIVATE_REGISTRATION_ROUTE_NAME_UNDER_TEST = "activateRegistrationServiceRoute";
    
    // This is used so that test classes which extend this class can change the reference used
    protected static String partyRefToUse = PARTY_REF_FOR_NEW_USERS;
    //This is used so that test classes which extend this class can force a failure
    protected static Boolean returnSuccess = true;
    protected static String failureMessage = "Some Failure";

    @EndpointInject(uri = TEST_ACTIVATE_REGISTRATION_START_NAME)
    private ProducerTemplate apiActivateRegistationEndpoint;

    @EndpointInject(uri = restrieveStartName)
    private ProducerTemplate apiRetrieveEndpoint;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return RouteTestUtils.createApplicationContext(REGISTRATION_ROUTE_NAME_UNDER_TEST,
        		ACTIVATE_REGISTRATION_ROUTE_NAME_UNDER_TEST, REGISTRATION_SUB_ROUTE_NAME_UNDER_TEST, 
        		RETRIEVE_SUB_ROUTE_NAME_UNDER_TEST, SaveRegisteredAccountDetailsRouteTest.routeNameUnderTest,
        		retrieveRouteName, GetPartiallyRegisteredDetailsRouteTest.routeNameUnderTest, 
        		BeisPasswordResetRouteTest.routeNameUnderTest);
    }

    /**
     * Ensure connection to directory service is possible before any tests are run.
     */
    @BeforeClass()
    public static void checkConnectionIsPossible() {

        // Establish a connection to the directory service
        final DirectoryConnectionConfig directoryConnectionConfig = new DirectoryConnectionConfig(configurationManager);

        try (DirectoryConnection directoryConnectionImpl = new DirectoryConnection(directoryConnectionConfig)) {
            connection = true;
        } catch (Exception e) {
            connection = false;
        }
    }

    public BeisRegistrationDetails registerNewUser(String emailText) throws Exception {
        MockNdsDirectoryKernel mockDirectoryKernel = new MockNdsDirectoryKernel();        
        return registerNewUser(emailText, mockDirectoryKernel);
    } 

    /**
     * Extract the activation code for use in other tests.
     */
    protected String extractActivationCodeFromBody(String body, String username) {
        final String emailTextLine2 = ">Activate your account</a>";
        final String activationCodeText = "activationCode=";

        int indexOfCodeLabel = body.indexOf(activationCodeText);
        int indexOfCodeValue = indexOfCodeLabel + activationCodeText.length();

        int indexOfEmailTextLine2 = body.indexOf(emailTextLine2);

        final String activationCode = body.substring(indexOfCodeValue, indexOfEmailTextLine2 - 1).trim();

        logger.info("User " + username + " activationCode is " + activationCode);
        return activationCode;
    }

    /**
     * Creates a request for a new LDAP user suitable for integration tests.
     *
     * @return a BeisRegistrationNdsRequest with preset data
     */
    public static BeisRegistrationNdsRequest createRegistrationNdsRequestWithNewUserName() {

        final String latestRandomNewUserName = PlatformTokenFactory.generateInternalUniqueReferenceWithPrefix(
                INTEGRATION_TEST_USERNAME_PREFIX);

        BeisRegistrationNdsRequest request = new BeisRegistrationNdsRequest();
        BeisRegistrationDetails registrationDetails = new BeisRegistrationDetails();
        registrationDetails.setAccountDetails(new BeisAccountDetails());
        registrationDetails.setUserDetails(new BeisUserDetails());
        registrationDetails.getAccountDetails().setPersonNameDetail(new PersonNameDetail());        
        registrationDetails.getAccountDetails().setAccountType(AccountType.PERSON);
        registrationDetails.getAccountDetails().getPersonNameDetail().setFirstname("beisAA");
        registrationDetails.getAccountDetails().getPersonNameDetail().setSurname("beiscc");
        registrationDetails.getUserDetails().setUsername(latestRandomNewUserName);
        registrationDetails.getUserDetails().setEmail("a@beis.com");
        registrationDetails.getUserDetails().setConfirmEmail("a@beis.com");
        registrationDetails.getUserDetails().setPassword(NEW_USER_PASSWORD);
        registrationDetails.getUserDetails().setConfirmPassword(NEW_USER_PASSWORD);
        registrationDetails.getUserDetails().setUserType(UserType.LANDLORD);
        registrationDetails.setTenant(TENANT);
        registrationDetails.getAccountDetails().setTelNumber("01234567890");
        registrationDetails.getAccountDetails().setAccountType(AccountType.PERSON);
        BeisAddressDetail contactAddress = new BeisAddressDetail();
        registrationDetails.setContactAddress(contactAddress);
        contactAddress.setLine(new ArrayList<String>());
        contactAddress.getLine().add("streetlineone");
        contactAddress.setTown("town");
        contactAddress.setPostcode("HA5 2LW");
        contactAddress.setCountry("country");

        request.setRegistrationDetails(registrationDetails);

        return request;
    }

    public BeisRegistrationNdsResponse createSuccessfullNdsRegistrationResponse() {
        BeisRegistrationNdsResponse response = new BeisRegistrationNdsResponse();
        response.setSuccess(true);
        return response;
    }
    
    
    public BeisRegistrationNdsRequest createRegistrationNdsRequestForAgent() {
        
        final String latestRandomNewUserName = PlatformTokenFactory.generateInternalUniqueReferenceWithPrefix(
                INTEGRATION_TEST_USERNAME_PREFIX);

        BeisRegistrationNdsRequest request = new BeisRegistrationNdsRequest();
        BeisRegistrationDetails registrationDetails = new BeisRegistrationDetails();
        registrationDetails.setAccountDetails(new BeisAccountDetails());
        registrationDetails.setUserDetails(new BeisUserDetails());
        registrationDetails.getAccountDetails().setAgentNameDetails(new AgentNameDetails());   
        registrationDetails.getAccountDetails().getAgentNameDetails().setAgentName("Agent test user");
        registrationDetails.getUserDetails().setUsername(latestRandomNewUserName);
        registrationDetails.getUserDetails().setEmail("a@beis.com");
        registrationDetails.getUserDetails().setConfirmEmail("a@beis.com");
        registrationDetails.getUserDetails().setPassword(NEW_USER_PASSWORD);
        registrationDetails.getUserDetails().setConfirmPassword(NEW_USER_PASSWORD);
        registrationDetails.getUserDetails().setUserType(UserType.AGENT);
        registrationDetails.setTenant(TENANT);
        registrationDetails.getAccountDetails().setTelNumber("01234567890");
        BeisAddressDetail contactAddress = new BeisAddressDetail();
        registrationDetails.setContactAddress(contactAddress);
        contactAddress.setLine(new ArrayList<String>());
        contactAddress.getLine().add("streetlineone");
        contactAddress.setTown("town");
        contactAddress.setPostcode("HA5 2LW");
        contactAddress.setCountry("country");

        request.setRegistrationDetails(registrationDetails);

        return request;
    }

    static GetPartyDetailsResponse createGetPartyDetailsResponse() {

        GetPartyDetailsResponse response = new GetPartyDetailsResponse();
        response.setSuccess(true);
        return response;
    }

    /**
     * Creates a request for a new LDAP user that has an invalid email address.
     *
     * @return a BeisRegistrationNdsRequest with preset data consisting of invalid email address
     */
    public static BeisRegistrationNdsRequest createRegistrationNdsRequestWithAnInvalidEmailAddress() {

        final String latestRandomNewUserName = PlatformTokenFactory.generateInternalUniqueReferenceWithPrefix(
                INTEGRATION_TEST_USERNAME_PREFIX);

        BeisRegistrationNdsRequest request = new BeisRegistrationNdsRequest();
        BeisRegistrationDetails registrationDetails = new BeisRegistrationDetails();
        registrationDetails.setAccountDetails(new BeisAccountDetails());
        registrationDetails.setUserDetails(new BeisUserDetails());

        registrationDetails.getAccountDetails().setAccountType(AccountType.PERSON);
        registrationDetails.getAccountDetails().getPersonNameDetail().setFirstname("AA");
        registrationDetails.getAccountDetails().getPersonNameDetail().setSurname("CCC");
        registrationDetails.getUserDetails().setConfirmEmail("a@b.com.com");
        registrationDetails.getUserDetails().setUsername(latestRandomNewUserName);
        registrationDetails.getUserDetails().setEmail("a@b.com.com");
        registrationDetails.getUserDetails().setPassword(NEW_USER_PASSWORD);
        registrationDetails.getUserDetails().setConfirmPassword(NEW_USER_PASSWORD);
        registrationDetails.getUserDetails().setUserType(UserType.LANDLORD);
        registrationDetails.setTenant(TENANT);
        registrationDetails.getAccountDetails().setTelNumber("01234567890");
        request.setRegistrationDetails(registrationDetails);

        return request;
    }

    protected static String cleanErrorEventRef(String message) {
        return message.replaceAll("<pmod\\:ErrorEventReference>.*?</pmod\\:ErrorEventReference>",
                "<pmod:ErrorEventReference></pmod:ErrorEventReference>");
    }

    public MaintainPartyDetailsResponse generateMaintainPartyDetailsResponse() {

        MaintainPartyDetailsResponse mockResponse = new MaintainPartyDetailsResponse();        
        mockResponse.setSuccess(returnSuccess);               
        mockResponse.setPartyRef(BigInteger.valueOf(Long.parseLong(partyRefToUse)));
        
        if(returnSuccess == false){
            //Mockup a failure message
            MessagesStructure messages = new MessagesStructure();
            MessageStructure message = new MessageStructure();
            message.setText(failureMessage);        
            messages.getMessage().add(message);        
            mockResponse.setMessages(messages);
            //Set this back as most tests want success, otherwise each unit test in a class
            //would have to ensure this was set correctly.
            returnSuccess = true;
        }
                
        return mockResponse;

    }

    public BeisRegistrationDetails registerNewUser(String emailText, MockNdsDirectoryKernel mockDirectoryKernel,
            BeisRegistrationNdsRequest requestNewUser, MockEndpointInitializer mockEndpointInitializer) throws Exception {
        assumeTrue(connection);
        
        logger.debug("Starting to register new user");
        
        
        BeisRegistrationDetails userRegistration = requestNewUser.getRegistrationDetails();

        AdviceWith.adviceWith(context.getRouteDefinition(REGISTRATION_ROUTE_NAME_UNDER_TEST), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {

                // Mock out the email
                weaveAddLast().to(MOCK_REGISTRATION_RESPONSE_CHECK);
                replaceFromWith(REGISTRATION_TEST_START);

                // Mock out the call to foundation layer for the sub route
                weaveAddLast().to(MOCK_REGISTRATION_FL_RESPONSE_CHECK);
                replaceFromWith(REGISTRATION_TEST_START);

                interceptSendToEndpoint("smtp://{{smtp.host.server}}").skipSendToOriginalEndpoint().to(
                        MOCK_INTERCEPTED_SMTP_SERVER).process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {
                        // nothing to change
                    }
                });
                
                // replace the directory kernel that the adapter uses to communicate with LDAP with a mocked class,
                // checking the values supplied by the route via the adapter and avoiding dependence of the test on  
                // a running LDAP server.
                DirectoryManager directoryManager = new DirectoryManager();
                directoryManager.setKernel(mockDirectoryKernel);
                UserManager userManager = new UserManager();
                userManager.setDirectoryManager(directoryManager);
                BeisRegistrationLdapComponent ldapComponent = new BeisRegistrationLdapComponent();
                ldapComponent.setDirectoryManager(directoryManager);
                ldapComponent.setUserManager(userManager);                 
                BeisRegistrationUpdateAccountIdLdapComponent accountIdComponent = new BeisRegistrationUpdateAccountIdLdapComponent();
                accountIdComponent.setDirectoryManager(directoryManager);
                accountIdComponent.setUserManager(userManager);
                weaveById("beisRegistrationLdapComponent.checkUsername").replace().bean(ldapComponent, "checkUsername");
                weaveById("beisRegistrationLdapComponent.process").replace().bean(ldapComponent, "process");
                weaveById("beisRegistrationLdapComponent.processResponse").replace().bean(ldapComponent, "processResponse");
                weaveById("beisRegistrationUpdateAccountIdLdapComponent.process").replace().bean(accountIdComponent, "process");
                weaveById("beisRegistrationUpdateAccountIdLdapComponent.processResponse").replace().bean(accountIdComponent, "processResponse");                   


                // This sub route is invoked by registration and requires mocking
                AdviceWith.adviceWith(context.getRouteDefinition(REGISTRATION_SUB_ROUTE_NAME_UNDER_TEST), context, new AdviceWithRouteBuilder() {
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
        MockEndpoint requestMock = getMockEndpoint(MOCK_INTERCEPTED_SMTP_SERVER);
        mockEndpointInitializer.request(requestMock);

        // setup expected ESB response
        MockEndpoint registrationResponseMock = getMockEndpoint(MOCK_REGISTRATION_RESPONSE_CHECK);
        mockEndpointInitializer.response(registrationResponseMock);

        apiEndpoint.sendBody(requestNewUser);

        assertMockEndpointsSatisfied();

        return userRegistration;
    }
    

    public BeisRegistrationDetails registerNewUser(String emailText, MockNdsDirectoryKernel mockDirectoryKernel) throws Exception {
        
        logger.debug("Starting to register new user");

        // setup the request and record the username
        final BeisRegistrationNdsRequest requestNewUser = createRegistrationNdsRequestWithNewUserName();
             
        return registerNewUser(emailText, mockDirectoryKernel, requestNewUser, new MockEndpointInitializer() {

            @Override
            public void request(MockEndpoint requestMock) throws Exception {
                BeisRegistrationDetails userRegistration = requestNewUser.getRegistrationDetails();
                requestMock.expectedMessageCount(1);
                requestMock.expectedHeaderReceived("subject", "Account activation");
                requestMock.expectedHeaderReceived("to", "a@beis.com");
                requestMock.whenAnyExchangeReceived(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String body = exchange.getIn().getBody(String.class);

                        final String expectedUsername = userRegistration.getUserDetails().getUsername();
                        final String expectedActivationCode = userRegistration.getActivationCode();

                        assertNotNull(expectedUsername);
                        assertNotNull(expectedActivationCode);

                        assertTrue("Email doesn't contain correct username : " + expectedUsername + " but instead contains "
                                + body, body.contains(expectedUsername));

                        if (emailText != null) {
                            assertTrue("Email doesn't contain correct text : " + emailText + " but instead contains " + body,
                                    body.contains(emailText));
                        }

                        final String newUserActivationCode = extractActivationCodeFromBody(body, expectedUsername);
                        assertEquals("Email doesn't contain correct activation code : " + expectedActivationCode
                                + ".  Text was " + body, expectedActivationCode, newUserActivationCode);
                    }
                });
            }

            @Override
            public void response(MockEndpoint registrationResponseMock) throws ClassNotFoundException {
               
                registrationResponseMock.expectedMessageCount(1);
                registrationResponseMock.expectedBodiesReceived(JaxbXmlMarshaller.convertToPrettyPrintXml(
                        createSuccessfullNdsRegistrationResponse(), BeisRegistrationNdsResponse.class));
            }
            
        });
    }
    
    /**
     * Call the retrieve camel route to get the details out  
     *
     * @param tenant data used for the request
     * @param username data used for the request
     * @param mockNdsDirectoryKernel kernel
     * @return RetrieveRegisteredDetailsNdsResponse
     * @throws Exception if an error occurs
     */
    public RetrieveRegisteredDetailsNdsResponse retrieveRegistrationDetails(String tenant, String username,MockNdsDirectoryKernel mockNdsDirectoryKernel)
            throws Exception {

        assumeTrue(connection);

        AdviceWith.adviceWith(context.getRouteDefinition(retrieveRouteName), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith(restrieveStartName);

                // Mock out the call to foundation layer from the sub route
                weaveAddLast().to(MOCK_RETRIEVE_FL_RESPONSE_CHECK);
                replaceFromWith(restrieveStartName);

                // This sub route is invoked by registration and requires mocking
                AdviceWith.adviceWith(context.getRouteDefinition(RETRIEVE_SUB_ROUTE_NAME_UNDER_TEST), context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        interceptSendToEndpoint("cxf:bean:beisGetPartyDetailsService").skipSendToOriginalEndpoint().to(
                                MOCK_RETRIEVE_FL_REQUEST_CHECK).process(new Processor() {

                            @Override
                            public void process(Exchange exchange) throws Exception {
                                // Mock out a response
                                exchange.getIn().setBody(createGetPartyDetailsResponse());
                            }
                        });                        
                    }
                });
                
                DirectoryManager directoryManager = new DirectoryManager();
                directoryManager.setKernel(mockNdsDirectoryKernel);
                UserManager userManager = new UserManager();
                userManager.setDirectoryManager(directoryManager);
                RetrieveRegisteredDetailsRetrieveAccountIdLdapComponent retrieveLdapComponent = new RetrieveRegisteredDetailsRetrieveAccountIdLdapComponent();
                retrieveLdapComponent.setDirectoryManager(directoryManager);
                retrieveLdapComponent.setUserManager(userManager);
                weaveById("retrieveRegisteredDetailsRetrieveAccountIdLdapComponent.process").replace().bean(retrieveLdapComponent, "process");
            }
        });

        context.start();

        // create and make the request
        RetrieveRegisteredDetailsNdsRequest request = new RetrieveRegisteredDetailsNdsRequest();
        request.setTenant(tenant);
        request.setUsername(username);

        RetrieveRegisteredDetailsNdsResponse response = (RetrieveRegisteredDetailsNdsResponse) apiRetrieveEndpoint.requestBody(
                request);

        return response;
    }
}
