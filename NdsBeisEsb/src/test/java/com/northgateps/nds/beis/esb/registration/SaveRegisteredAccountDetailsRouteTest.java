package com.northgateps.nds.beis.esb.registration;

import static org.junit.Assume.assumeTrue;

import java.math.BigInteger;
import java.util.ArrayList;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

import com.northgateps.nds.beis.api.AccountType;
import com.northgateps.nds.beis.api.AgentNameDetails;
import com.northgateps.nds.beis.api.BeisAccountDetails;
import com.northgateps.nds.beis.api.BeisAddressDetail;
import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.BeisUpdateUserDetails;
import com.northgateps.nds.beis.api.PersonNameDetail;
import com.northgateps.nds.beis.api.UpdateBeisRegistrationDetails;
import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.beis.api.retrieveregistereddetails.RetrieveRegisteredDetailsNdsResponse;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsRequest;
import com.northgateps.nds.beis.api.saveregisteredaccountdetails.SaveRegisteredAccountDetailsNdsResponse;
import com.northgateps.nds.beis.backoffice.service.maintainpartydetails.MaintainPartyDetailsRequest;
import com.northgateps.nds.beis.backoffice.service.maintainpartydetails.MaintainPartyDetailsResponse;
import com.northgateps.nds.beis.esb.MockNdsDirectoryKernel;
import com.northgateps.nds.beis.esb.beisregistration.BeisRegistrationCamelSpringTestSupport;
import com.northgateps.nds.platform.api.ActivateRegistrationDetails;
import com.northgateps.nds.platform.api.activateregistration.ActivateRegistrationNdsRequest;
import com.northgateps.nds.platform.api.updateemail.UpdateEmailNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsDirectoryComponent;
import com.northgateps.nds.platform.esb.directory.DirectoryAccessConnection;
import com.northgateps.nds.platform.esb.directory.DirectoryConnectionConfig;
import com.northgateps.nds.platform.esb.directory.DirectoryManager;
import com.northgateps.nds.platform.esb.directory.ServiceAccessDetails;
import com.northgateps.nds.platform.esb.directory.UserManager;
import com.northgateps.nds.platform.esb.registration.ActivateRegistrationLdapComponent;
import com.northgateps.nds.platform.esb.updateemail.UpdateEmailLdapComponent;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * This is a test class to check the Save Registered Account Details Route works
 * as expected.
 */
public class SaveRegisteredAccountDetailsRouteTest extends BeisRegistrationCamelSpringTestSupport {

    private static final NdsLogger logger = NdsLogger.getLogger(BeisRegistrationCamelSpringTestSupport.class);
    public static final String routeNameUnderTest = "saveRegisteredAccountDetailsServiceRoute";
    private static final String TEST_START_NAME = "direct:startSaveRegisteredAccountDetailsServiceRoute";
    private static final String EXTERNAL_END_POINT_TO_MOCK = "cxf:bean:beisMaintainPartyDetailsService";
    private static final String MOCK_MAINTAIN_PARTY = "mock:maintain-party";
    private static final String MOCK_SEND_EMAIL_NEW_SERVICE_CHECK = "mock:send-email-new-service-check";
    private static final String MOCK_SEND_EMAIL_OLD_SERVICE_CHECK = "mock:send-email-old-service-check";
    private static final String NEW_ACCOUNT_ID = "1366";   

    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiEndpoint;

    @EndpointInject(uri = TEST_ACTIVATE_REGISTRATION_START_NAME)
    private ProducerTemplate apiActivateRegistationEndpoint;

    @EndpointInject(uri = MOCK_SEND_EMAIL_NEW_SERVICE_CHECK)
    private MockEndpoint sendEmailToNewAddressService;

    @EndpointInject(uri = MOCK_SEND_EMAIL_OLD_SERVICE_CHECK)
    private MockEndpoint sendEmailToOldAddressService;

    @Test
    public void testBackOfficeFailureForSaveRegisteredDetailsForLandlordPartialRegistration() throws Exception {
        logger.info("testSaveRegisteredDetailsForPartialRegistration test started");

        partyRefToUse = NEW_ACCOUNT_ID;
        backofficeFailureForSaveRegisteredDetailsForAgentLandlordPartialRegistration(partyRefToUse, UserType.LANDLORD);
    }

    @Test
    public void testBackOfficeFailureForSaveRegisteredDetailsForAgentPartialRegistration() throws Exception {
        logger.info("testSaveRegisteredDetailsForAgentPartialRegistration test started");

        partyRefToUse = NEW_ACCOUNT_ID;
        backofficeFailureForSaveRegisteredDetailsForAgentLandlordPartialRegistration(partyRefToUse, UserType.AGENT);

    }

    @Test
    public void testSaveRegisteredDetailsForPartialRegistration() throws Exception {
        logger.info("testSaveRegisteredDetailsForPartialRegistration test started");

        partyRefToUse = NEW_ACCOUNT_ID;
        saveRegisteredDetailsForAgentLandlordPartialRegistration(partyRefToUse, UserType.LANDLORD);
    }

    @Test
    public void testSaveRegisteredDetailsForAgentPartialRegistration() throws Exception {
        logger.info("testSaveRegisteredDetailsForPartialRegistration test started");

        partyRefToUse = NEW_ACCOUNT_ID;
        saveRegisteredDetailsForAgentLandlordPartialRegistration(partyRefToUse, UserType.AGENT);

    }

    /**
     * Helper function to get the foundation layer account id
     * 
     * @param Username - username used for logging on
     * @return - accountId (aka partyref) for the username provided
     * @throws Exception
     */
    private String getAccountIdForUserName(String Username, MockNdsDirectoryKernel mockDirectoryKernel) throws Exception {

        String accountId = null;

        // Get the service name
        String serviceName = configurationManager.getString("service.foundationLayerPartyService.name");

        String userDn = NdsDirectoryComponent.getUserDn(configurationManager, Username, TENANT);

        // Get configuration
        DirectoryConnectionConfig directoryConnectionConfig = new DirectoryConnectionConfig(configurationManager);
               
        //Create a connection and retrieve the service details for the user
        try (DirectoryAccessConnection directoryConnection = mockDirectoryKernel.getDirectoryConnection(directoryConnectionConfig)) {
            
            directoryConnection.open();
            DirectoryManager directoryManager = new DirectoryManager();
            directoryManager.setKernel(mockDirectoryKernel);
            UserManager userManager = new UserManager();
            userManager.setDirectoryManager(directoryManager);
            
            // lookup service details
            ServiceAccessDetails serviceAccessDetails = userManager.serviceLookup(directoryConnection, userDn,
                    serviceName, true, true, false);

            accountId = serviceAccessDetails.getUserId();
        }

        return accountId;
    }

    /**
     * 
     * @param success indicates if a successful response is required
     * @param mockPartyRef - This is the party ref wanted in the mocked out response
     * @return
     */
    private MaintainPartyDetailsResponse generateMockMaintainPartyDetailsResponse(Boolean success,
            String mockPartyRef) {

        MaintainPartyDetailsResponse response = new MaintainPartyDetailsResponse();

        response.setSuccess(success);
        response.setPartyRef(BigInteger.valueOf(Long.parseLong(mockPartyRef)));

        return response;
    }

    /**
     * Helper method to setup the route for the majority of the tests
     * 
     * @throws Exception
     */
    private void setupUpdateMockedRoute(MockNdsDirectoryKernel mockDirectoryKernel) throws Exception {
     // setup the route
        context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                replaceFromWith(TEST_START_NAME);
                
                // replace the directory kernel that the adapter uses to communicate with LDAP with a mocked class,
                // checking the values supplied by the route via the adapter and avoiding dependence of the test on  
                // a running LDAP server.
                DirectoryManager directoryManager = new DirectoryManager();
                directoryManager.setKernel(mockDirectoryKernel);
                UserManager userManager = new UserManager();
                userManager.setDirectoryManager(directoryManager);
                SaveRegisteredAccountDetailsAccountIdLdapComponent ldapComponent = new SaveRegisteredAccountDetailsAccountIdLdapComponent();
                ldapComponent.setDirectoryManager(directoryManager);
                ldapComponent.setUserManager(userManager);
                SaveRegisteredAccountDetailsRetrieveAccountIdLdapComponent retrieveldapComponent = new SaveRegisteredAccountDetailsRetrieveAccountIdLdapComponent();
                retrieveldapComponent.setDirectoryManager(directoryManager);
                retrieveldapComponent.setUserManager(userManager);
                SaveRegisteredAccountDetailsComponent accountDetailsComponent = new SaveRegisteredAccountDetailsComponent();
                accountDetailsComponent.setDirectoryManager(directoryManager);
                accountDetailsComponent.setUserManager(userManager);
                UpdateEmailLdapComponent updateEmailLdapComponent = new UpdateEmailLdapComponent();
                updateEmailLdapComponent.setDirectoryManager(directoryManager);
                updateEmailLdapComponent.setUserManager(userManager);
                
                weaveById("saveRegisteredAccountDetailsAccountIdLdapComponent.process").replace().bean(ldapComponent, "process");
                weaveById("saveRegisteredAccountDetailsAccountIdLdapComponent.processResponse").replace().bean(ldapComponent, "processResponse");
                weaveById("saveRegisteredAccountDetailsRetrieveAccountIdLdapComponent.process").replace().bean(retrieveldapComponent, "process");
                weaveById("saveRegisteredAccountDetailsLdapComponent.process").replace().bean(accountDetailsComponent, "process");
                weaveById("beisUpdateEmailLdapComponent.fetchEntryFromLdap").replace().bean(updateEmailLdapComponent, "fetchEntryFromLdap");                
                

                interceptSendToEndpoint(EXTERNAL_END_POINT_TO_MOCK).skipSendToOriginalEndpoint().to(
                        MOCK_MAINTAIN_PARTY).process(exchange -> {

                    MaintainPartyDetailsRequest request = (MaintainPartyDetailsRequest) exchange.getIn().getBody();

                    // Check that an update request was created by the converter
                    assertNotNull("Check request is for update ", request.getUpdate());
                    assertNull("Check request is not for create ", request.getCreate());
                    // Check the party ref matches
                    assertEquals("Check the party ref is set correctly", request.getUpdate().getPartyRef(),
                            PARTY_REF_FOR_NEW_USERS);

                    // Mock out a successful response with a new party ref
                    exchange.getIn().setBody(generateMockMaintainPartyDetailsResponse(true, PARTY_REF_FOR_NEW_USERS));
                });

                interceptSendToEndpoint("direct:sendEmailToNewAddressSubRoute").skipSendToOriginalEndpoint().to(
                        MOCK_SEND_EMAIL_NEW_SERVICE_CHECK).process(exchange -> {
                    // Assume the call to the update email service was successful
                    UpdateEmailNdsResponse body = new UpdateEmailNdsResponse();
                    body.setSuccess(true);
                    exchange.getIn().setBody(body);
                });

                interceptSendToEndpoint("direct:sendEmailToOldAddressSubRoute").skipSendToOriginalEndpoint().to(
                        MOCK_SEND_EMAIL_OLD_SERVICE_CHECK).process(exchange -> {
                    // Assume the call to the update email service was successful
                    UpdateEmailNdsResponse body = new UpdateEmailNdsResponse();
                    body.setSuccess(true);
                    exchange.getIn().setBody(body);
                });
            }
        });
    }

    @Test
    public void testSaveAddressDetailsForLandlord() throws Exception {
        logger.info("RetrieveRegisteredUserDetailsRoute test started");
        saveAddressDetailsForAgentLandlord(UserType.LANDLORD);

    }

    @Test
    public void testSaveAddressDetailsForAgent() throws Exception {
        logger.info("RetrieveRegisteredUserDetailsRoute test started");
        saveAddressDetailsForAgentLandlord(UserType.AGENT);

    }

    @Test
    public void testSaveAccountDetailsForLandlord() throws Exception {
        logger.info("RetrieveRegisteredUserDetailsRoute test started");
        saveAccountDetailsForAgentLandlord(UserType.LANDLORD);

    }

    @Test
    public void testSaveAccountDetailsForAgent() throws Exception {
        logger.info("RetrieveRegisteredUserDetailsRoute test started");
        saveAccountDetailsForAgentLandlord(UserType.AGENT);

    }

    @Test
    public void testValidationFailure() throws Exception {
        logger.info("RetrieveRegisteredUserDetailsRoute test started");
        
        assumeTrue(connection);
        
        MockNdsDirectoryKernel mockNdsDirectoryKernel = new MockNdsDirectoryKernel();

        // register a brand new user for the purposes of this test
        final BeisRegistrationDetails registeredUser = registerNewUser(null,mockNdsDirectoryKernel);

        // setup the route
        setupUpdateMockedRoute(mockNdsDirectoryKernel);

        // setup the update details
        UpdateBeisRegistrationDetails updateDetails = new UpdateBeisRegistrationDetails();
        // set the account details but do not set teh account type
        updateDetails.setAccountDetails(getTestAccountDetailsForPerson(false, UserType.LANDLORD));

        context.start();
        Exception exception = null;
        try {
            SaveRegisteredAccountDetailsNdsResponse response = (SaveRegisteredAccountDetailsNdsResponse) apiEndpoint.requestBody(
                    createNewSaveRegisteredAccountDetailsNdsRequest(registeredUser.getUserDetails().getUsername(),
                            registeredUser.getUserDetails().getEmail(), updateDetails, false, UserType.LANDLORD));
        } catch (CamelExecutionException e) {
            exception = e.getExchange().getException();

        }

    }

    @Test
    public void testUserDoesntExist() throws Exception {
        logger.info("RetrieveRegisteredUserDetailsRoute test started");

        assumeTrue(connection);
        
        MockNdsDirectoryKernel mockNdsDirectoryKernel = new MockNdsDirectoryKernel();

        // setup the route
        setupUpdateMockedRoute(mockNdsDirectoryKernel);

        // setup the update details
        UpdateBeisRegistrationDetails updateDetails = new UpdateBeisRegistrationDetails();
        // set the account details
        updateDetails.setAccountDetails(getTestAccountDetailsForPerson(true, UserType.LANDLORD));

        context.start();

        Exception exception = null;

        try {
            apiEndpoint.requestBody(createNewSaveRegisteredAccountDetailsNdsRequest("DOESNTEXIST",
                    "Mike.Smith@wibble.com", updateDetails, false, UserType.LANDLORD));
        } catch (CamelExecutionException e) {
            exception = e.getExchange().getException();
        }

        assertNotNull("Exception should not be null ", exception);
    }

    @Test
    public void testLdapDown() throws Exception {
        logger.info("RetrieveRegisteredUserDetailsRoute test started");

        assumeTrue(connection);

        MockNdsDirectoryKernel mockNdsDirectoryKernel = new MockNdsDirectoryKernel();

        // setup the route
        setupUpdateMockedRoute(mockNdsDirectoryKernel);
        
        // setup the update details
        UpdateBeisRegistrationDetails updateDetails = new UpdateBeisRegistrationDetails();
        
        // set the account details
        updateDetails.setAccountDetails(getTestAccountDetailsForPerson(true, UserType.LANDLORD));

        context.start();

        Exception exception = null;

        try {
            apiEndpoint.requestBody(createNewSaveRegisteredAccountDetailsNdsRequest("DOESNTEXIST",
                    "Mike.Smith@wibble.com", updateDetails, false, UserType.LANDLORD));
        } catch (CamelExecutionException e) {
            exception = e.getExchange().getException();
        }

        assertNotNull("Exception should not be null ", exception);
    }

    @Test
    public void testUpdateEmailForLandlord() throws Exception {
        logger.info("saveRegisteredAccountDetailsServiceRoute with an update email test started");
        updateEmailForAgentLandlord(UserType.LANDLORD);

    }

    @Test
    public void testUpdateEmailForAgent() throws Exception {
        logger.info("saveRegisteredAccountDetailsServiceRoute with an update email test started");
        updateEmailForAgentLandlord(UserType.AGENT);
    }

    private SaveRegisteredAccountDetailsNdsRequest createUpdateEmailRequest(BeisRegistrationDetails registeredUser,
            UserType userType) {
        UpdateBeisRegistrationDetails updateDetails = new UpdateBeisRegistrationDetails();

        BeisUpdateUserDetails updateUserDetails = new BeisUpdateUserDetails();
        updateUserDetails.setEmail("new_" + registeredUser.getUserDetails().getEmail());
        updateUserDetails.setUsername(registeredUser.getUserDetails().getUsername());
        updateUserDetails.setPassword(registeredUser.getUserDetails().getPassword());
        updateUserDetails.setUpdatingEmail(true);
        updateUserDetails.setUserType(userType);

        // Now set on the request
        updateDetails.setUpdateUserDetails(updateUserDetails);

        final SaveRegisteredAccountDetailsNdsRequest request = new SaveRegisteredAccountDetailsNdsRequest();

        request.setTenant(TENANT);
        request.setRegisteredAccountDetails(updateDetails);

        // Set new property for username which is on the BeisPartyRequest abstract class
        request.setUsername(registeredUser.getUserDetails().getUsername());

        // Set new property for partial registration
        request.setPartiallyRegistered(false);
        return request;
    }

    private BeisRegistrationDetails registerAndActivateNewUser(MockNdsDirectoryKernel mockNdsDirectoryKernel) throws Exception {
        // register a brand new user for the purposes of this test
        final BeisRegistrationDetails registeredUser = registerNewUser(null,mockNdsDirectoryKernel);

        // set up the activation route
        context.getRouteDefinition(ACTIVATE_REGISTRATION_ROUTE_NAME_UNDER_TEST).adviceWith(context,
                new AdviceWithRouteBuilder() {

                    @Override
                    public void configure() throws Exception {
                        replaceFromWith(TEST_ACTIVATE_REGISTRATION_START_NAME);
                        // replace the directory kernel that the adapter uses to communicate with LDAP with a mocked class,
                        // checking the values supplied by the route via the adapter and avoiding dependence of the test on  
                        // a running LDAP server.
                        DirectoryManager directoryManager = new DirectoryManager();
                        directoryManager.setKernel(mockNdsDirectoryKernel);
                        UserManager userManager = new UserManager();
                        userManager.setDirectoryManager(directoryManager);
                        ActivateRegistrationLdapComponent activateldapComponent = new ActivateRegistrationLdapComponent();
                        activateldapComponent.setDirectoryManager(directoryManager);
                        activateldapComponent.setUserManager(userManager);
                        weaveById("activateRegistrationLdapComponent.process").replace().bean(activateldapComponent, "process");
                        weaveById("activateRegistrationLdapComponent.processResponse").replace().bean(activateldapComponent, "processResponse");
                    }
                });

        final String activationCode = registeredUser.getActivationCode();

        // this must work without error (don't put this in a try-catch block) - activate the newly created account
        apiActivateRegistationEndpoint.requestBody(createActivateRegistrationNdsRequest(activationCode));
        return registeredUser;
    }

    private ActivateRegistrationNdsRequest createActivateRegistrationNdsRequest(String activationCode) {
        ActivateRegistrationNdsRequest request = new ActivateRegistrationNdsRequest();
        ActivateRegistrationDetails activateRegistrationDetails = new ActivateRegistrationDetails();
        activateRegistrationDetails.setActivationCode(activationCode);
        activateRegistrationDetails.setTenant(TENANT);
        request.setActivateRegistrationDetails(activateRegistrationDetails);
        return request;
    }

    SaveRegisteredAccountDetailsNdsRequest createNewSaveRegisteredAccountDetailsNdsRequest(String username,
            String email, UpdateBeisRegistrationDetails registeredAccountDetails, Boolean partiallyRegistered,
            UserType userType) {

        final SaveRegisteredAccountDetailsNdsRequest request = new SaveRegisteredAccountDetailsNdsRequest();

        BeisUpdateUserDetails updateUserDetails = new BeisUpdateUserDetails();
        updateUserDetails.setEmail(email);
        updateUserDetails.setUsername(username);
        updateUserDetails.setUserType(userType);

        registeredAccountDetails.setUpdateUserDetails(updateUserDetails);

        request.setTenant(TENANT);
        request.setRegisteredAccountDetails(registeredAccountDetails);

        // Set new property for username which is on the BeisPartyRequest abstract class
        request.setUsername(username);

        // Set new property for partial registration
        request.setPartiallyRegistered(partiallyRegistered);

        return request;
    }

    void checkValues(UpdateBeisRegistrationDetails updateDetails, BeisRegistrationDetails registrationDetails) {
        String firstName = "beisAA";
        String surname = "beiscc";

        String line0 = "streetlineone";
        String line1 = null;
        String town = "town";
        String county = null;
        String postcode = "HA5 2LW";

        if (updateDetails.getAccountDetails() != null) {
            firstName = updateDetails.getAccountDetails().getPersonNameDetail().getFirstname();
            surname = updateDetails.getAccountDetails().getPersonNameDetail().getSurname();
        }

        if (updateDetails.getContactAddress() != null) {
            line0 = updateDetails.getContactAddress().getLine().get(0);
            line1 = updateDetails.getContactAddress().getLine().get(1);
            town = updateDetails.getContactAddress().getTown();
            county = updateDetails.getContactAddress().getCounty();
            postcode = updateDetails.getContactAddress().getPostcode();
        }

        assertEquals(firstName, registrationDetails.getAccountDetails().getPersonNameDetail().getFirstname());
        assertEquals(surname, registrationDetails.getAccountDetails().getPersonNameDetail().getSurname());

        assertEquals(line0, registrationDetails.getContactAddress().getLine().get(0));
        assertEquals(line1, registrationDetails.getContactAddress().getLine().get(1));
        assertEquals(town, registrationDetails.getContactAddress().getTown());
        assertEquals(county, registrationDetails.getContactAddress().getCounty());
        assertEquals(postcode, registrationDetails.getContactAddress().getPostcode());
    }

    /**
     * Create a contact address with some test data
     * 
     * @return a populated address detail for use by the tests
     */
    private BeisAddressDetail getTestAddressDetails() {

        BeisAddressDetail contactAddress = new BeisAddressDetail();

        contactAddress.setLine(new ArrayList<String>());
        contactAddress.getLine().add("123");
        contactAddress.getLine().add("Test Street");
        contactAddress.setTown("Test Town");
        contactAddress.setCounty("Test County");
        contactAddress.setPostcode("TE1 2ST");

        return contactAddress;

    }

    /**
     * Create account details with some test data
     * 
     * @return a populated address detail for use by the tests
     */
    private BeisAccountDetails getTestAccountDetailsForPerson(Boolean setAccountType, UserType userType) {
        BeisAccountDetails accountDetails = new BeisAccountDetails();
        if (userType == UserType.AGENT) {

            accountDetails.setAgentNameDetails(new AgentNameDetails());
            accountDetails.getAgentNameDetails().setAgentName("Test Agent");
            accountDetails.setTelNumber("01234567890");
        }

        else {
            accountDetails.setPersonNameDetail(new PersonNameDetail());
            accountDetails.getPersonNameDetail().setFirstname("Mike");
            accountDetails.getPersonNameDetail().setSurname("Smith");
            accountDetails.setTelNumber("01234567890");

            if (setAccountType) {
                accountDetails.setAccountType(AccountType.PERSON);
            }
        }

        return accountDetails;

    }

    private void backofficeFailureForSaveRegisteredDetailsForAgentLandlordPartialRegistration(String partyRefToUse,
            UserType userType) throws Exception {
        // We need LDAP
        assumeTrue(connection);
        
        MockNdsDirectoryKernel mockDirectoryKernel = new MockNdsDirectoryKernel();

        // register a brand new user for the purposes of this test
        final BeisRegistrationDetails registeredUser = registerNewUser(null,mockDirectoryKernel);  

        returnSuccess = false;
        
        setupUpdateMockedRoute(mockDirectoryKernel);
        
        // setup the update details - No need to do much here because we are not hitting the actual back office.
        UpdateBeisRegistrationDetails updateDetails = new UpdateBeisRegistrationDetails();

        // Set the address details
        updateDetails.setContactAddress(getTestAddressDetails());

        context.start();
        SaveRegisteredAccountDetailsNdsResponse response = (SaveRegisteredAccountDetailsNdsResponse) apiEndpoint.requestBody(
                createNewSaveRegisteredAccountDetailsNdsRequest(registeredUser.getUserDetails().getUsername(),
                        registeredUser.getUserDetails().getEmail(), updateDetails, true, userType));

        assertFalse("Expected failure from MaintainPartyDetails back office call", response.isSuccess());
        assertNotNull("Expected NdsMessages to not be null", response.getNdsMessages());
        assertEquals("Expected cause ", failureMessage, response.getNdsMessages().getExceptionCaught());
    }

    private void saveRegisteredDetailsForAgentLandlordPartialRegistration(String partyRefToUse, UserType userType)
            throws Exception {
        // We need LDAP
        assumeTrue(connection);
        
        MockNdsDirectoryKernel mockDirectoryKernel = new MockNdsDirectoryKernel();
        
        // register a brand new user for the purposes of this test
        final BeisRegistrationDetails registeredUser = registerNewUser(null,mockDirectoryKernel);

        /* The issue is the SaveRegisteredAccountDetailsRouteTest calls common code to add a new user which uses the
         * beisRegistrationServiceRoute. This test calls saveRegisteredAccountDetailsServiceRoute. Both routes mock out
         * the end point cxf:bean:beisMaintainPartyDetailsService. it does not work because the first mock is taking
         * precedence.
         * This means the same response and therefore party ref is returned and
         * testSaveRegisteredDetailsForPartialRegistration
         * requires different values to in test testSaveRegisteredDetailsForPartialRegistration.
         * A partial registration writes the new party ref to LDAP.
         * Set this protected class member in BeisRegistrationCamelSpringTestSupport after invoking registerNewUser to
         * change the
         * party ref returned in the mock response. */

        // setup the route
        context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                replaceFromWith(TEST_START_NAME);
                // No need to mock cxf:bean:beisMaintainPartyDetailsService because it is already mocked as a result of
                // registerNewUser
                
                
                // replace the directory kernel that the adapter uses to communicate with LDAP with a mocked class,
                // checking the values supplied by the route via the adapter and avoiding dependence of the test on  
                // a running LDAP server.
                DirectoryManager directoryManager = new DirectoryManager();
                directoryManager.setKernel(mockDirectoryKernel);
                UserManager userManager = new UserManager();
                userManager.setDirectoryManager(directoryManager);
                SaveRegisteredAccountDetailsAccountIdLdapComponent ldapComponent = new SaveRegisteredAccountDetailsAccountIdLdapComponent();
                ldapComponent.setDirectoryManager(directoryManager);
                ldapComponent.setUserManager(userManager);
                SaveRegisteredAccountDetailsRetrieveAccountIdLdapComponent retrieveldapComponent = new SaveRegisteredAccountDetailsRetrieveAccountIdLdapComponent();
                retrieveldapComponent.setDirectoryManager(directoryManager);
                retrieveldapComponent.setUserManager(userManager);
                SaveRegisteredAccountDetailsComponent accountDetailsComponent = new SaveRegisteredAccountDetailsComponent();
                accountDetailsComponent.setDirectoryManager(directoryManager);
                accountDetailsComponent.setUserManager(userManager);
                UpdateEmailLdapComponent updateEmailLdapComponent = new UpdateEmailLdapComponent();
                updateEmailLdapComponent.setDirectoryManager(directoryManager);
                updateEmailLdapComponent.setUserManager(userManager);
                
                weaveById("saveRegisteredAccountDetailsAccountIdLdapComponent.process").replace().bean(ldapComponent, "process");
                weaveById("saveRegisteredAccountDetailsAccountIdLdapComponent.processResponse").replace().bean(ldapComponent, "processResponse");
                weaveById("saveRegisteredAccountDetailsRetrieveAccountIdLdapComponent.process").replace().bean(retrieveldapComponent, "process");
                weaveById("saveRegisteredAccountDetailsLdapComponent.process").replace().bean(accountDetailsComponent, "process");
                weaveById("beisUpdateEmailLdapComponent.fetchEntryFromLdap").replace().bean(updateEmailLdapComponent, "fetchEntryFromLdap");
            }
        });
        
        

        // setup the update details - No need to do much here because we are not hitting the actual back office.
        UpdateBeisRegistrationDetails updateDetails = new UpdateBeisRegistrationDetails();

        // Set the address details
        updateDetails.setContactAddress(getTestAddressDetails());

        context.start();
        SaveRegisteredAccountDetailsNdsResponse response = (SaveRegisteredAccountDetailsNdsResponse) apiEndpoint.requestBody(
                createNewSaveRegisteredAccountDetailsNdsRequest(registeredUser.getUserDetails().getUsername(),
                        registeredUser.getUserDetails().getEmail(), updateDetails, true, userType));

        assertTrue("Successful response from Maintain Party", response.isSuccess());

        // retrieve the details to ensure the updates worked
        RetrieveRegisteredDetailsNdsResponse retrieveResponse = retrieveRegistrationDetails(TENANT,
                registeredUser.getUserDetails().getUsername(),mockDirectoryKernel);

        assertTrue(retrieveResponse.isSuccess());
        assertNotNull(retrieveResponse.getBeisRegistrationDetails());

        // Get the LDAP user and check the party ref has been updated to 12345
        String accountId = getAccountIdForUserName(registeredUser.getUserDetails().getUsername(),mockDirectoryKernel);

        assertEquals("Check the accountId is set correctly in LDAP", partyRefToUse, accountId);

        assertMockEndpointsSatisfied();
    }

    private void saveAddressDetailsForAgentLandlord(UserType userType) throws Exception {
        assumeTrue(connection);
        
        MockNdsDirectoryKernel mockDirectoryKernel = new MockNdsDirectoryKernel();

        // register a brand new user for the purposes of this test
        final BeisRegistrationDetails registeredUser = registerNewUser(null,mockDirectoryKernel);

        // setup the route
        setupUpdateMockedRoute(mockDirectoryKernel);

        // setup the update details
        UpdateBeisRegistrationDetails updateDetails = new UpdateBeisRegistrationDetails();
        // Set the address details
        updateDetails.setContactAddress(getTestAddressDetails());

        context.start();
        SaveRegisteredAccountDetailsNdsResponse response = (SaveRegisteredAccountDetailsNdsResponse) apiEndpoint.requestBody(
                createNewSaveRegisteredAccountDetailsNdsRequest(registeredUser.getUserDetails().getUsername(),
                        registeredUser.getUserDetails().getEmail(), updateDetails, false, userType));

        assertTrue(response.isSuccess());

        // retrieve the details to ensure the updates worked
        RetrieveRegisteredDetailsNdsResponse retrieveResponse = retrieveRegistrationDetails(TENANT,
                registeredUser.getUserDetails().getUsername(),mockDirectoryKernel);

        assertTrue(retrieveResponse.isSuccess());
        assertNotNull(retrieveResponse.getBeisRegistrationDetails());

        assertMockEndpointsSatisfied();
    }

    private void saveAccountDetailsForAgentLandlord(UserType userType) throws Exception {        
        assumeTrue(connection);
        MockNdsDirectoryKernel mockDirectoryKernel = new MockNdsDirectoryKernel();
        
        // register a brand new user for the purposes of this test
        final BeisRegistrationDetails registeredUser = registerNewUser(null, mockDirectoryKernel);
       
        // setup the route
        setupUpdateMockedRoute(mockDirectoryKernel);
        
        // setup the update details
        UpdateBeisRegistrationDetails updateDetails = new UpdateBeisRegistrationDetails();

        // set the account details
        updateDetails.setAccountDetails(getTestAccountDetailsForPerson(true, userType)); 

        context.start();
        SaveRegisteredAccountDetailsNdsResponse response = (SaveRegisteredAccountDetailsNdsResponse) apiEndpoint.requestBody(
                createNewSaveRegisteredAccountDetailsNdsRequest(registeredUser.getUserDetails().getUsername(),
                        registeredUser.getUserDetails().getEmail(), updateDetails, false, userType));

        assertTrue(response.isSuccess());

        // retrieve the details to ensure the updates worked
        RetrieveRegisteredDetailsNdsResponse retrieveResponse = retrieveRegistrationDetails(TENANT,
                registeredUser.getUserDetails().getUsername(),mockDirectoryKernel);

        assertTrue(retrieveResponse.isSuccess());
        assertNotNull(retrieveResponse.getBeisRegistrationDetails());

        assertMockEndpointsSatisfied();
    }

    private void updateEmailForAgentLandlord(UserType userType) throws Exception {
        
        assumeTrue(connection);        
        
        MockNdsDirectoryKernel mockDirectoryKernel = new MockNdsDirectoryKernel();
        
        final BeisRegistrationDetails registeredUser = registerAndActivateNewUser(mockDirectoryKernel);

        // set up the save registered account details route
        setupUpdateMockedRoute(mockDirectoryKernel);
        sendEmailToNewAddressService.expectedMessageCount(1);
        sendEmailToOldAddressService.expectedMessageCount(1);

        context.start();

        // set up the update details
        final SaveRegisteredAccountDetailsNdsRequest request = createUpdateEmailRequest(registeredUser, userType);

        SaveRegisteredAccountDetailsNdsResponse response = (SaveRegisteredAccountDetailsNdsResponse) apiEndpoint.requestBody(
                request);

        assertTrue(response.isSuccess());

        // retrieve the details to ensure the updates worked
        RetrieveRegisteredDetailsNdsResponse retrieveResponse = retrieveRegistrationDetails(TENANT,
                registeredUser.getUserDetails().getUsername(), mockDirectoryKernel);

        assertTrue(retrieveResponse.isSuccess());
        assertNotNull(retrieveResponse.getBeisRegistrationDetails());

        assertMockEndpointsSatisfied();
    }
     
}
