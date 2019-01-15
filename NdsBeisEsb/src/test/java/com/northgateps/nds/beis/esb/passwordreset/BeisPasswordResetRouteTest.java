package com.northgateps.nds.beis.esb.passwordreset;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Before;
import org.junit.Test;
import org.ldaptive.Connection;
import org.ldaptive.LdapAttribute;
import org.ldaptive.LdapEntry;
import org.ldaptive.LdapException;
import org.ldaptive.ModifyRequest;
import org.ldaptive.SearchScope;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import com.northgateps.nds.beis.esb.beisregistration.BeisRegistrationCamelSpringTestSupport;
import com.northgateps.nds.beis.esb.registration.RetrieveRegisteredDetailsRetrieveAccountIdLdapComponent;
import com.northgateps.nds.beis.esb.registration.UpdateEmailAddressInLdapComponent;
import com.northgateps.nds.beis.api.BeisRegistrationDetails;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsRequest;
import com.northgateps.nds.beis.api.beisregistration.BeisRegistrationNdsResponse;
import com.northgateps.nds.beis.backoffice.service.getpartydetails.GetPartyDetailsRequest;
import com.northgateps.nds.beis.backoffice.service.getpartydetails.GetPartyDetailsResponse;
import com.northgateps.nds.beis.esb.MockNdsDirectoryKernel;
import com.northgateps.nds.platform.api.PasswordResetDetails;
import com.northgateps.nds.platform.api.passwordreset.PasswordResetNdsRequest;
import com.northgateps.nds.platform.api.passwordreset.PasswordResetNdsResponse;
import com.northgateps.nds.platform.application.api.utils.PlatformTokenFactory;
import com.northgateps.nds.beis.esb.MockEndpointInitializer;
import com.northgateps.nds.platform.esb.directory.DirectoryManager;
import com.northgateps.nds.platform.esb.directory.UserManager;
import com.northgateps.nds.platform.esb.directory.ex.DirectoryException;
import com.northgateps.nds.platform.esb.email.EmailTemplateLoader;
import com.northgateps.nds.platform.esb.passwordreset.PasswordResetEmailAdapter;
import com.northgateps.nds.platform.esb.passwordreset.PasswordResetLdapComponent;
import com.northgateps.nds.platform.esb.util.xml.JaxbXmlMarshaller;
import com.northgateps.nds.platform.logger.NdsLogger;

import org.apache.camel.EndpointInject;
import static org.junit.Assume.assumeTrue;

/**
 * Test for beis password reset route.
 */
@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BeisPasswordResetRouteTest extends BeisRegistrationCamelSpringTestSupport {
    
    private static final NdsLogger logger = NdsLogger.getLogger(BeisPasswordResetRouteTest.class);

    public static final String routeNameUnderTest = "beisPasswordResetServiceRoute";
    private static final String TEST_START_NAME = "direct:beisPasswordResetServiceRoute";
    private static final String MOCK_PASSWORD_RESET_REQUEST_CHECK = "mock:beisPasswordResetRequest";
    private static final String MOCK_PASSWORD_RESET_RESPONSE_CHECK = "mock:beisPasswordResetRequest-response-check";
    private static final String getPartyDetailsRouteName = "getPartyDetails_SubRouteForRetrievingRegistrationDetails";
    protected static final String MOCK_GET_PARTYDETAILS = "mock:get-partyDetails";

    @EndpointInject(uri = TEST_ACTIVATE_REGISTRATION_START_NAME)
    private ProducerTemplate apiActivateRegistationEndpoint;

    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiEndpoint;

    private final String emailTemplateName = "BeisEmailTemplate.xml";
    
    private ZonedDateTime runDateTime = ZonedDateTime.now(ZoneOffset.UTC);
    private int hoursExpiry = 48;
    
    private TestMockDirectoryKernel mockDirectoryKernel = new TestMockDirectoryKernel();

    
    /*
     * This mock replaces the normal communications with a real LDAP server, with stand-in functionality.
     * 
     * The adapter sets the initial ndsPasswordResetCode and the email address of the given user from LDAP, so here the kernal fetch operation is
     * mocked, the dn of the user and the required attribute from LDAP are checked, and suitable values provided 
     * back to the adapter.
     * 
     * It also updates the ndsPasswordResetCode, so that update is mocked.
     */
    private class TestMockDirectoryKernel extends MockNdsDirectoryKernel {

        private String resetCode = "";

        @Override
        protected Iterator<LdapEntry> fetch(Connection connection, String baseDn, String searchString,
                SearchScope searchScope, String... typesOfAttributes) throws DirectoryException, InstantiationException,
                        IllegalAccessException, IllegalArgumentException, InvocationTargetException,
                        NoSuchMethodException, SecurityException, LdapException {

            // Check for account presence first
            Iterator<LdapEntry> iter = super.fetch(connection, baseDn, searchString, searchScope, typesOfAttributes);

            Pattern pattern = Pattern.compile(
                    "^uid=(Beistest-[^@]+),ou=Users,ou=BEIS,ou=Tenants,ou=NDS,dc=northgateps,dc=com$");

            if (!pattern.matcher(baseDn).find()) {
                return iter;
            }

            // if found, then fake LDAP entries
            List<LdapEntry> entries = new ArrayList<LdapEntry>();

            LdapEntry ldapEntry = new LdapEntry() {

                @Override
                public Collection<LdapAttribute> getAttributes() {
                    List<LdapAttribute> attributes = new ArrayList<LdapAttribute>();

                    // Mock the get ndsPasswordResetCode attribute
                    if (Arrays.asList(typesOfAttributes).contains("ndsPasswordResetCode")
                            || Arrays.asList(typesOfAttributes).contains("*")) {
                        LdapAttribute ldapAttribute = new LdapAttribute() {

                            @Override
                            public String getName(boolean withOptions) {
                                return "ndsPasswordResetCode";
                            }

                            @Override
                            public String getStringValue() {
                                return resetCode;
                            }
                        };
                        attributes.add(ldapAttribute);
                    }
                    // Mock the get mail attribute
                    if (Arrays.asList(typesOfAttributes).contains("mail")
                            || Arrays.asList(typesOfAttributes).contains("*")) {
                        LdapAttribute ldapAttribute = new LdapAttribute() {

                            @Override
                            public String getName(boolean withOptions) {
                                return "mail";
                            }

                            @Override
                            public String getStringValue() {
                                return "a@beis.com";
                            }
                        };
                        attributes.add(ldapAttribute);
                    }

                    return attributes;
                };
            };
            ldapEntry.setDn(baseDn);
            entries.add(ldapEntry);
            return entries.iterator();
        }

        // Mock the add ndsPasswordResetCode attribute
        @Override
        protected void update(Connection connection, ModifyRequest changes)
                throws InstantiationException, IllegalAccessException, IllegalArgumentException,
                InvocationTargetException, NoSuchMethodException, SecurityException, LdapException {
            super.update(connection, changes);
            if (changes.getAttributeModifications().length == 1) {
                if ("ndsPasswordResetCode".equals(changes.getAttributeModifications()[0].getAttribute().getName())) {
                    resetCode = changes.getAttributeModifications()[0].getAttribute().getStringValue();
                }
            }
        }
    };

    //Prepares the camel context beisPasswordResetServiceRoute
    @Before
    public void prepareCamelContext() throws Exception {
        ModelCamelContext camelContext = context;

        camelContext.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                weaveAddLast().to(MOCK_PASSWORD_RESET_RESPONSE_CHECK);
                replaceFromWith(TEST_START_NAME);
                interceptSendToEndpoint("smtp://{{smtp.host.server}}").skipSendToOriginalEndpoint().to(
                        MOCK_PASSWORD_RESET_REQUEST_CHECK).process(new Processor() {

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

                PasswordResetLdapComponent ldapComponent = new PasswordResetLdapComponent();
                ldapComponent.setDirectoryManager(directoryManager);

                ldapComponent.setUserManager(userManager);

                weaveById("beisPasswordResetLdapComponent.process").replace().bean(ldapComponent, "process");

                weaveById("beisPasswordResetLdapComponent.processResponse").replace().bean(ldapComponent,
                        "processResponse");

                RetrieveRegisteredDetailsRetrieveAccountIdLdapComponent retrieveLdapComponent = new RetrieveRegisteredDetailsRetrieveAccountIdLdapComponent();
                retrieveLdapComponent.setDirectoryManager(directoryManager);
                retrieveLdapComponent.setUserManager(userManager);
                weaveById("retrieveRegisteredDetailsRetrieveAccountId.process").replace().bean(retrieveLdapComponent,
                        "process");

                UpdateEmailAddressInLdapComponent updateEmailLdapComponent = new UpdateEmailAddressInLdapComponent();
                updateEmailLdapComponent.setDirectoryManager(directoryManager);
                updateEmailLdapComponent.setUserManager(userManager);

                weaveById("updateEmailAddressLdapComponent.process").replace().bean(updateEmailLdapComponent,
                        "process");

                PasswordResetEmailAdapter emailAdapter = new PasswordResetEmailAdapter();
                emailAdapter.setDirectoryManager(directoryManager);

                emailAdapter.setUserManager(userManager);
                emailAdapter.setTemplateLoader(new EmailTemplateLoader());

                weaveById("beisPasswordResetEmailAdapter.processRequest").replace().bean(emailAdapter,
                        "processRequest");

            }
        });

        camelContext.start();
    }

    //test for passwordReset with different email address in ldap and backoffice
    @Test
    public void testPasswordResetWithDifferentEmailAddressInBOAndLdap() throws Exception {

        final String emailTextLine1 = "You asked for your PRS exemptions account password to be reset. You now need to set a new password.";

        //assume connection is done for ldap
        assumeTrue(connection);

        final BeisRegistrationDetails registeredUser = registerANewUser(mockDirectoryKernel);

        String username = registeredUser.getUserDetails().getUsername();
        
        //mocking getPartyDetails endpoint
        context.getRouteDefinition(getPartyDetailsRouteName).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {

                interceptSendToEndpoint("cxf:bean:beisGetPartyDetailsService").skipSendToOriginalEndpoint().to(
                        MOCK_GET_PARTYDETAILS).process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {
                        GetPartyDetailsRequest request = (GetPartyDetailsRequest) exchange.getIn().getBody();
                        assertEquals("Checking party ref", BigInteger.valueOf(1366), request.getPartyRef());
                        
                        //set a different email address in backoffice response
                        exchange.getIn().setBody(createGetPartyDetailsResponse("changeemail@beis.com"));

                    }
                });

            }
        });

        context.start();

        // Ensure email request data is present and correct
        MockEndpoint requestMock = getMockEndpoint(MOCK_PASSWORD_RESET_REQUEST_CHECK);
        requestMock.reset();
        requestMock.expectedMessageCount(1);
        requestMock.expectedHeaderValuesReceivedInAnyOrder("subject", "Password reset");
        requestMock.whenAnyExchangeReceived(new Processor() {
      
            @Override
            public void process(Exchange exchange) throws Exception {
                String body = exchange.getIn().getBody(String.class);
                assertTrue("Check emailTextLine1", body.contains(emailTextLine1));
                assertEquals("Check email is sent to changed email address", "changeemail@beis.com", 
                              exchange.getIn().getHeader("to"));

            }
        });

        MockEndpoint passwordResetResponseMock = getMockEndpoint(MOCK_PASSWORD_RESET_RESPONSE_CHECK);
        passwordResetResponseMock.expectedMessageCount(1);
        passwordResetResponseMock.expectedBodiesReceived(JaxbXmlMarshaller.convertToPrettyPrintXml(
                createSuccessfulNdsPasswordResetResponse(), PasswordResetNdsResponse.class));

        String resetCode = generateResetCode(username);
        apiEndpoint.sendBody(createResetPasswordNdsRequestWithResetCode(username, resetCode));
        assertMockEndpointsSatisfied();

    }
    
    
    //test for passwordReset with same email address in ldap and backoffice
    @Test
    public void testPasswordResetWithSameEmailAddressInBOAndLdap() throws Exception {

        final String emailTextLine1 = "You asked for your PRS exemptions account password to be reset. You now need to set a new password.";

        //assume connection is done for ldap
        assumeTrue(connection);

        final BeisRegistrationDetails registeredUser = registerANewUser(mockDirectoryKernel);

        String username = registeredUser.getUserDetails().getUsername();
        
        context.getRouteDefinition(getPartyDetailsRouteName).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {

                interceptSendToEndpoint("cxf:bean:beisGetPartyDetailsService").skipSendToOriginalEndpoint().to(
                        MOCK_GET_PARTYDETAILS).process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {
                        GetPartyDetailsRequest request = (GetPartyDetailsRequest) exchange.getIn().getBody();
                        assertEquals("Checking party ref", BigInteger.valueOf(1366), request.getPartyRef());
                        
                        //set the same email in response which is present in ldap
                        exchange.getIn().setBody(createGetPartyDetailsResponse("a@beis.com"));

                    }
                });

            }
        });

        context.start();

        MockEndpoint requestMock = getMockEndpoint(MOCK_PASSWORD_RESET_REQUEST_CHECK);
        requestMock.reset();
        requestMock.expectedMessageCount(1);
        requestMock.expectedHeaderValuesReceivedInAnyOrder("subject", "Password reset");
        requestMock.whenAnyExchangeReceived(new Processor() {

            @Override
            public void process(Exchange exchange) throws Exception {
                String body = exchange.getIn().getBody(String.class);
                assertTrue("Check emailTextLine1", body.contains(emailTextLine1));
                assertEquals("Check email is sent to email address present in ldap", "a@beis.com", 
                        exchange.getIn().getHeader("to"));

            }
        });

        MockEndpoint passwordResetResponseMock = getMockEndpoint(MOCK_PASSWORD_RESET_RESPONSE_CHECK);
        passwordResetResponseMock.expectedMessageCount(1);
        passwordResetResponseMock.expectedBodiesReceived(JaxbXmlMarshaller.convertToPrettyPrintXml(
                createSuccessfulNdsPasswordResetResponse(), PasswordResetNdsResponse.class));

        String resetCode = generateResetCode(username);
        apiEndpoint.sendBody(createResetPasswordNdsRequestWithResetCode(username, resetCode));
        assertMockEndpointsSatisfied();

    }
    
    
    //test passwordReset for unknown /invalid username
    @Test
    public void testPasswordResetUnknownUser() throws Exception {
        logger.info("Checking that password reset is not initiated when username is unknown");
        
        MockNdsDirectoryKernel mockDirectoryKernel = new MockNdsDirectoryKernel();
        
        final BeisRegistrationDetails registeredUser = registerANewUser(mockDirectoryKernel);
        
        String username = registeredUser.getUserDetails().getUsername();
        
        // Ensure email request data is not present
        MockEndpoint requestMock = getMockEndpoint(MOCK_PASSWORD_RESET_REQUEST_CHECK);
        requestMock.reset();
        requestMock.expectedMessageCount(0);
         
        MockEndpoint passwordResetResponseMock = getMockEndpoint(MOCK_PASSWORD_RESET_RESPONSE_CHECK);
        passwordResetResponseMock.reset();
        passwordResetResponseMock.expectedMessageCount(0);

        String resetCode = generateResetCode(username);
        
        PasswordResetNdsResponse userError = (PasswordResetNdsResponse) apiEndpoint.requestBody(
                createResetPasswordNdsRequestWithResetCode("a" + username, resetCode));

        assertFalse(userError.isSuccess());

        assertEquals("Check if it contains invalid message",
                "Username or activation code is invalid.",
                userError.getNdsMessages().getViolations().get(0).getMessage());
    }
    
    //method to register a new user and return registered details
    private BeisRegistrationDetails registerANewUser(MockNdsDirectoryKernel mockDirectoryKernel)
            throws Exception {

        final BeisRegistrationNdsRequest requestNewUser = createRegistrationNdsRequestWithNewUserName();

        BeisRegistrationDetails registeredUser = registerNewUser(null, mockDirectoryKernel, requestNewUser,
                new MockEndpointInitializer() {

                    @Override
                    public void request(MockEndpoint requestMock) throws Exception {
                        requestMock.expectedMessageCount(1);

                    }

                    @Override
                    public void response(MockEndpoint responseMock) throws Exception {
                        responseMock.expectedBodiesReceived(JaxbXmlMarshaller.convertToPrettyPrintXml(
                                createSuccessfulBeisRegistrationResponse(), BeisRegistrationNdsResponse.class));

                    }

                });

        return registeredUser;

    }

    protected BeisRegistrationNdsResponse createSuccessfulBeisRegistrationResponse() {
        BeisRegistrationNdsResponse response = new BeisRegistrationNdsResponse();
        response.setSuccess(true);
        return response;
    }

    private PasswordResetNdsResponse createSuccessfulNdsPasswordResetResponse() {
        PasswordResetNdsResponse response = new PasswordResetNdsResponse();
        response.setSuccess(true);
        return response;
    }

    private String generateResetCode(String username) {
        ZonedDateTime expiry = runDateTime.plusHours(hoursExpiry);
        return PlatformTokenFactory.generateActivationToken(username, expiry);
    }

    protected PasswordResetNdsRequest createResetPasswordNdsRequestWithResetCode(String username, String resetCode) {
        PasswordResetNdsRequest request = new PasswordResetNdsRequest();
        PasswordResetDetails details = new PasswordResetDetails();
        details.setUsername(username);
        details.setTenant(TENANT);
        details.setActivationCode(resetCode);
        request.setPasswordResetDetails(details);
        request.setEmailTemplateName(emailTemplateName);
        return request;
    }

    private GetPartyDetailsResponse createGetPartyDetailsResponse(String emailAddress) {
        GetPartyDetailsResponse response = new GetPartyDetailsResponse();
        response.setSuccess(true);
        response.setEmailAddress(emailAddress);
        return response;
    }

}
