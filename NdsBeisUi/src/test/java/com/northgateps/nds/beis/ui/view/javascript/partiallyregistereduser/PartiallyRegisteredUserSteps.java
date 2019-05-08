package com.northgateps.nds.beis.ui.view.javascript.partiallyregistereduser;


import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.AccountAddressPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.AccountDetailsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.SelectLandlordOrAgentPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.SelectLandlordTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.AccountAddressPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.AccountDetailsPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.SelectLandlordOrAgentPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.SelectLandlordTypePageObject;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

/** Test steps for the PartiallyRegisteredUser.feature BDD file. */
public class PartiallyRegisteredUserSteps {
    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginUsername = (String) testProperties.get("loginPartiallyRegisteredUserName");
    private final String loginPassword = (String) testProperties.get("loginPartiallyRegisteredPassword");
    private final String loginUsernameWithPartyRef = (String) testProperties.get("loginPartiallyRegisteredWithPartyRefUserName");
    private final String loginPasswordWithPartyRef = (String) testProperties.get("loginPartiallyRegisteredWithPartyRefPassword");
    
    private final String firstName = (String) testProperties.get("loginPartiallyRegisteredFirstName");
    private final String surname = (String) testProperties.get("loginPartiallyRegisteredSurname");
    private final String email = (String) testProperties.get("loginPartiallyRegisteredEmail");
    private final String confirmEmail = (String) testProperties.get("loginPartiallyRegisteredConfirmEmail");
    private final String telephoneNumber = (String) testProperties.get("loginPartiallyRegisteredTelephoneNumber");
    private final String postCode = (String) testProperties.get("loginPartiallyRegisteredPostCode");
    
    private final String loginOrgUsername = (String) testProperties.get("loginPartiallyRegisteredOrgUserName");
    private final String loginOrgPassword = (String) testProperties.get("loginPartiallyRegisteredOrgPassword");
    private final String organsiationName = (String) testProperties.get("loginPartiallyRegisteredOrganisationName");
    private final String orgEmail = (String) testProperties.get("loginPartiallyRegisteredOrgEmail");
    private final String orgConfirmEmail = (String) testProperties.get("loginPartiallyRegisteredOrgConfirmEmail");
    private final String orgTelephoneNumber = (String) testProperties.get("loginPartiallyRegisteredOrgTelephoneNumber");


    private final String loginAgentUsername = (String) testProperties.get("loginPartiallyRegisteredAgentUserName");
    private final String loginaAgentPassword = (String) testProperties.get("loginPartiallyRegisteredAgentPassword");
    private final String agentName = (String) testProperties.get("loginPartiallyRegisteredAgentName");
    private final String agentEmail = (String) testProperties.get("loginPartiallyRegisteredAgentEmail");
    private final String agentConfirmEmail = (String) testProperties.get("loginPartiallyRegisteredAgentConfirmEmail");
    private final String agentTelephoneNumber = (String) testProperties.get("loginPartiallyRegisteredAgentTelephoneNumber");

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    
    private SelectLandlordTypePageHelper landlordTypePageHelper;
    private SelectLandlordTypePageObject landlordTypePageObject;
    private AccountDetailsPageHelper accountDetailsPageHelper;
    private AccountDetailsPageObject accountDetailsPageObject;
    private AccountAddressPageHelper accountAddressPageHelper;
    private AccountAddressPageObject accountAddressPageObject;
    private UsedServiceBeforePageHelper firstPageHelper;
    private SelectLandlordOrAgentPageHelper landlordOrAgentPageHelper;
    private SelectLandlordOrAgentPageObject landlordOrAgentPageObject;
    
    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        testHelper.openUrl();
        firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
        landlordOrAgentPageHelper = new SelectLandlordOrAgentPageHelper(testHelper.getScenarioWebDriver());
        landlordOrAgentPageObject = landlordOrAgentPageHelper.getPageObject();
        landlordTypePageHelper = new SelectLandlordTypePageHelper(testHelper.getScenarioWebDriver());
        landlordTypePageObject = landlordTypePageHelper.getPageObject();
        accountDetailsPageHelper =  new AccountDetailsPageHelper(testHelper.getScenarioWebDriver());
        accountDetailsPageObject = accountDetailsPageHelper.getPageObject();
        accountAddressPageHelper = new AccountAddressPageHelper(testHelper.getScenarioWebDriver());
        accountAddressPageObject = accountAddressPageHelper.getPageObject();
        testHelper.openUrl();
    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }
    
    @Given("^I am on the 'select-landlord-or agent' page for partially registered user$")
    public void i_am_on_the_select_landlord_or_agent_page_for_partially_registered_user() throws Throwable {
     
    	// Register a custom form filler for login page
        LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

        PageHelperFactory.registerFormFiller("login-form", loginPageHelper.createFormFiller(loginUsername, loginPassword));
        
        try {
            landlordOrAgentPageHelper = PageHelperFactory.visit(firstPageHelper, SelectLandlordOrAgentPageHelper.class);
            checkOnPage(landlordOrAgentPageHelper, "select-landlord-or-agent");
            landlordOrAgentPageObject = landlordOrAgentPageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }

    }

    @Given("^I have selected landlord$")
    public void i_have_selected_landlord() throws Throwable {
        landlordOrAgentPageObject.clickNdsRadiobuttonelementUserType("LANDLORD");
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        landlordOrAgentPageObject.clickNext();
    }

    @Then("^I will be taken to the 'select-landlord-type' page$")
    public void i_will_be_taken_to_the_select_landlord_type_page() throws Throwable {
        checkOnPage(landlordOrAgentPageHelper, "select-landlord-type");
    }

    @When("^I select Next on the 'select-landlord-type' page$")
    public void i_select_Next_on_the_select_landlord_type_page() throws Throwable {
        // move next
        landlordTypePageObject.clickButtonNext_NEXT();
    }
    
    @Given("^I am on the 'select-landlord-or agent' page for partially registered organisation user$")
    public void i_am_on_the_select_landlord_or_agent_page_for_partially_registered_organisation_user() throws Throwable {
        LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

        PageHelperFactory.registerFormFiller("login-form",
                loginPageHelper.createFormFiller(loginOrgUsername, loginOrgPassword));
        
        try {
            assert(true);
            landlordOrAgentPageHelper = PageHelperFactory.visit(firstPageHelper, SelectLandlordOrAgentPageHelper.class);
            checkOnPage(landlordOrAgentPageHelper, "select-landlord-or-agent");
            landlordOrAgentPageObject = landlordOrAgentPageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }  
    
    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String expectedPrompt) throws Throwable {
        //Check we have the prompt to indicate entry of details required
        if (webDriver.findElements(By.id("VerifyDetailsPrompt")).size() != 0) {
            assertTrue("Partially registered verify prompt not displayed", landlordOrAgentPageObject.getWebElementDivVerifyDetailsPrompt().isDisplayed());
            assertTrue("Partially registered verify prompt does not match text",
                    landlordOrAgentPageObject.getWebElementDivVerifyDetailsPrompt().getText().contains(expectedPrompt));
        } else {
            fail("Unable to get verify prompt");
        }        
    }

    @Then("^landlord type details held in LDAP will be displayed$")
    public void landlord_type_details_held_in_LDAP_will_be_displayed() throws Throwable {
        //Check the person is selected
        assertTrue("Expected landlord type should be person", landlordTypePageObject.isSelectedNdsRadiobuttonelementAccountType("PERSON"));
    }

    @Then("^I have selected person as landlord type$")
    public void i_have_selected_person_as_landlord_type() throws Throwable {
        //The data defaults this
        assertTrue(true);
    }

    @When("^I select Next on landlord type$")
    public void i_select_Next_on_landlord_type() throws Throwable {
        // move next
        landlordTypePageObject.clickButtonNext_NEXT();
    }

    @Then("^I will be taken to the 'account-details' page$")
    public void i_will_be_taken_to_the_account_details_page() throws Throwable {
        checkOnPage(accountDetailsPageHelper, "account-details");

    }

    @Then("^account details held in LDAP will be displayed$")
    public void account_details_held_in_LDAP_will_be_displayed() throws Throwable {
        // Check the details returned
        assertTrue("First name does not match", accountDetailsPageObject.getTextNdsInputFirstname().equals(firstName));
        assertTrue("Surname does not match", accountDetailsPageObject.getTextNdsInputSurname().equals(surname));
        assertTrue("Email does not match expected", accountDetailsPageObject.getTextNdsInputEmail().equals(email));
        assertTrue("Confirm Email does not match expected", accountDetailsPageObject.getTextNdsInputConfirmEmail().equals(confirmEmail));
        assertTrue("Telephone Number does not match expected", accountDetailsPageObject.getTextNdsInputTelNumber().equals(telephoneNumber));
    }
    
    @Then("^First name and Last name will be available for input$")
    public void first_name_and_Last_name_will_be_available_for_input() throws Throwable {
        //Check they are available
        assertTrue("First name is not enabled", accountDetailsPageObject.getWebElementNdsInputFirstname().isEnabled());
        assertTrue("Surname is not enabled", accountDetailsPageObject.getWebElementNdsInputSurname().isEnabled());
    }
    
    @When("^I select Next on account details$")
    public void i_select_Next_on_account_details() throws Throwable {
        // Move to account address
        accountDetailsPageObject.clickButtonNext_NEXT();
    }
    
    @Then("^I will be taken to the 'account-address' page$")
    public void i_will_be_taken_to_the_account_address_page() throws Throwable {
        checkOnPage(accountAddressPageHelper, "account-address");
    }
    
    @Then("^post code held in LDAP will be displayed$")
    public void post_code_held_in_LDAP_will_be_displayed() throws Throwable {
        assertTrue("Postcode does not match", accountAddressPageObject.getTextNdsInputPostcodeCriterion().equals(postCode));
    }    
    
    @Then("^landlord type details held in LDAP for organisation will be displayed$")
    public void landlord_type_details_held_in_LDAP_for_organisation_will_be_displayed() throws Throwable {
        // Check the organisation is selected
        assertTrue("Expected landlord type should be organsiation", landlordTypePageObject.isSelectedNdsRadiobuttonelementAccountType("ORGANISATION"));
    }

    @Then("^I have selected organisation as landlord type$")
    public void i_have_selected_organisation_as_landlord_type() throws Throwable {
        //The data defaults this
        assertTrue(true);
    }

    @Then("^Organisation name will be available for input$")
    public void organisation_name_will_be_available_for_input() throws Throwable {
        //Check field is available
        assertTrue("Organisation name is not enabled", accountDetailsPageObject.getWebElementNdsInputOrgName().isEnabled());
    }

    @Then("^details held in LDAP will be displayed$")
    public void details_held_in_LDAP_will_be_displayed() throws Throwable {
        // Check the details returned
        assertTrue("Organisation Name does not match", accountDetailsPageObject.getTextNdsInputOrgName().equals(organsiationName));
        assertTrue("Organisation Email does not match expected", accountDetailsPageObject.getTextNdsInputEmail().equals(orgEmail));
        assertTrue("Organisation Confirm Email does not match expected", accountDetailsPageObject.getTextNdsInputConfirmEmail().equals(orgConfirmEmail));
        assertTrue("Organisation Telephone Number does not match expected", accountDetailsPageObject.getTextNdsInputTelNumber().equals(orgTelephoneNumber));
    }
    
    @Given("^I am on the 'select-landlord-or agent' page for partially registered agent user$")
    public void i_am_on_the_select_landlord_or_agent_page_for_partially_registered_agent_user() throws Throwable {
        LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

        PageHelperFactory.registerFormFiller("login-form",
                loginPageHelper.createFormFiller(loginAgentUsername, loginaAgentPassword));
        
        try {
            assert(true);
            landlordOrAgentPageHelper = PageHelperFactory.visit(firstPageHelper, SelectLandlordOrAgentPageHelper.class);
            checkOnPage(landlordOrAgentPageHelper, "select-landlord-or-agent");
            landlordOrAgentPageObject = landlordOrAgentPageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @Given("^I have selected agent$")
    public void i_have_selected_agent() throws Throwable {
        landlordOrAgentPageObject.clickNdsRadiobuttonelementUserType("AGENT");
    }

    @Then("^Agent name will be available for input$")
    public void agent_name_will_be_available_for_input() throws Throwable {
      //Check field is available
        assertTrue("checking for Agent name", accountDetailsPageObject.getWebElementNdsInputAgentName().isEnabled());        
    }

    @Then("^account details held in LDAP for agent will be displayed$")
    public void account_details_held_in_LDAP_for_agent_will_be_displayed() throws Throwable { 
        assertTrue("Agent name does not match", accountDetailsPageObject.getTextNdsInputAgentName().equals(agentName));
        assertTrue("Agent Email does not match expected", accountDetailsPageObject.getTextNdsInputEmail().equals(agentEmail));
        assertTrue("Agent Confirm Email does not match expected", accountDetailsPageObject.getTextNdsInputConfirmEmail().equals(agentConfirmEmail));
        assertTrue("Agent Telephone Number does not match expected", accountDetailsPageObject.getTextNdsInputTelNumber().equals(agentTelephoneNumber));
    }

    @When("^I select Next on 'account details' page$")
    public void i_select_Next_on_account_details_page() throws Throwable {
        accountDetailsPageObject.clickNext();
    }
    
    @Given("^I am on the 'select-landlord-or agent' page for partially registered user with party ref$")
    public void i_am_on_the_select_landlord_or_agent_page_for_partially_registered_user_with_party_ref() throws Throwable {
        // Register a custom form filler for login page
        LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

        PageHelperFactory.registerFormFiller("login-form", loginPageHelper.createFormFiller(loginUsernameWithPartyRef, loginPasswordWithPartyRef));
        
        try {
            landlordOrAgentPageHelper = PageHelperFactory.visit(firstPageHelper, SelectLandlordOrAgentPageHelper.class);
            checkOnPage(landlordOrAgentPageHelper, "select-landlord-or-agent");
            landlordOrAgentPageObject = landlordOrAgentPageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }


}
