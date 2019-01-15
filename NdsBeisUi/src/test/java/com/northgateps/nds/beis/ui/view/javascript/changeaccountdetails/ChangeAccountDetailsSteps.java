package com.northgateps.nds.beis.ui.view.javascript.changeaccountdetails;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedAccountSummaryPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedChangeAccountDetailsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedAccountSummaryPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedChangeAccountDetailsPageObject;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

/** Test steps for the ChangeAccountDetails.feature BDD file. */
public class ChangeAccountDetailsSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());
    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");
    private final String loginAgentUsername = (String) testProperties.get("loginAgentUsername");
    private final String loginAgentPassword = (String) testProperties.get("loginAgentPassword");

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedChangeAccountDetailsPageHelper pageHelper;
    PersonalisedChangeAccountDetailsPageObject pageObject;
    PersonalisedAccountSummaryPageHelper accountSummaryPageHelper;
    PersonalisedAccountSummaryPageObject PersonalisedAccountSummaryPageObject;
    PersonalisedDashboardPageHelper dashboardPageHelper;
    UsedServiceBeforePageHelper firstPageHelper;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
        pageHelper = new PersonalisedChangeAccountDetailsPageHelper(testHelper.getScenarioWebDriver());
        pageObject = pageHelper.getPageObject();
        testHelper.openUrl();
    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }

    @Given("^I am on the personalised-account-summary page$")
    public void i_am_on_the_account_summary_page() throws Throwable {
        //visit target page
        accountSummaryPageHelper = PageHelperFactory.visit(firstPageHelper, PersonalisedAccountSummaryPageHelper.class);   
        PersonalisedAccountSummaryPageObject = accountSummaryPageHelper.getPageObject();
        checkOnPage(pageHelper, "personalised-account-summary");
    }

    @When("^I select Change details$")
    public void i_select_Change_details() throws Throwable {
        PersonalisedAccountSummaryPageObject.clickButtonChangeOrganisationDetails_NEXTEditPersonalDetails();
    }

    @Then("^I will be taken to the personalised-change-account-details page$")
    public void i_will_be_taken_to_the_change_account_details_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-change-account-details");
        pageObject = pageHelper.getPageObject();
    }

    @Given("^I am on the personalised-change-account-details page$")
    public void i_am_on_the_change_account_details_page() throws Throwable {
        accountSummaryPageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedAccountSummaryPageHelper.class);   
        PersonalisedAccountSummaryPageObject = accountSummaryPageHelper.getPageObject();
        checkOnPage(pageHelper, "personalised-account-summary");
        pageHelper = accountSummaryPageHelper.skipToChangeAccountDetailsPage();
        checkOnPage(pageHelper,"personalised-change-account-details");
        pageObject = pageHelper.getPageObject();
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^I will be taken to the personalised-account-summary page$")
    public void i_will_be_taken_to_the_account_summary_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-account-summary");
    }

    @Given("^Organisation name is available for input$")
    public void organisation_name_is_available_for_input() throws Throwable {
        assertTrue(pageObject.getWebElementNdsInputOrgName().isDisplayed());
    }

    @Given("^I have not entered any data$")
    public void i_have_not_entered_any_data() throws Throwable {
        pageHelper.clearAllFields();

    }

    @When("^I select Submit$")
    public void i_select_Submit() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.clickButtonOrg_NEXT();
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String validationMessage) throws Throwable {
        pageHelper.waitUntilValidationMessageSeen(validationMessage);
    }

    @Then("^I will remain on the personalised-change-account-details page$")
    public void i_will_remain_on_the_change_account_details_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-change-account-details");
    }
   
    @Given("^I have supplied an invalid phone number as \"(.*?)\" which does not consist of numeric or space characters with an optional leading \\+$")
    public void i_have_supplied_an_invalid_phone_number_as_which_does_not_consist_of_numeric_or_space_characters_with_an_optional_leading(String phone) throws Throwable {
        pageObject.setTextNdsInputTelNumber(phone);
    }

    @Given("^supplied valid organisation name as \"(.*?)\"$")
    public void supplied_valid_organisation_name_as(String orgName) throws Throwable {
       pageObject.setTextNdsInputOrgName(orgName);
    }

    @Given("^I have supplied valid phone number as \"(.*?)\"$")
    public void i_have_supplied_valid_phone_number_as(String phone) throws Throwable {
        pageObject.setTextNdsInputTelNumber(phone);
    }

    @Given("^I am on the personalised-change-account-details page for person Category$")
    public void i_am_on_the_change_account_details_page_for_person_Category() throws Throwable {

		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller(loginUsername, loginPassword));
        
        try {
            accountSummaryPageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedAccountSummaryPageHelper.class);
            checkOnPage(accountSummaryPageHelper, "personalised-account-summary");
            
            /*This cannot be achieved with form filler and steers, because there are two paths to target page 
            i.e change account details,logging using person and other as org which is not a normal pattern*/
            
            pageHelper = accountSummaryPageHelper.skipToChangeAccountDetailsPageForPerson();
            checkOnPage(pageHelper,"personalised-change-account-details");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }

    }

    @Given("^First name and Last name are available for input$")
    public void first_name_and_Last_name_are_available_for_input() throws Throwable {
        assertTrue(pageObject.getWebElementNdsInputFirstname().isDisplayed());
        assertTrue(pageObject.getWebElementNdsInputSurname().isDisplayed());
    }

    @Given("^I have not entered any data for person$")
    public void i_have_not_entered_any_data_for_person() throws Throwable {
        pageHelper.clearAllFieldsForPerson();
    }

    @When("^I Submit details$")
    public void i_Submit_details() throws Throwable {
        pageObject.clickButtonPerson_NEXT();
    }

    @Then("^details for the account will be displayed$")
    public void details_for_the_account_will_be_displayed() throws Throwable {
        // TODO: this must be checked
    }
    
    @Given("^I am on the personalised-account-summary page for an agent$")
    public void i_am_on_the_personalised_account_summary_page_for_an_agent() throws Throwable {
     // Register a custom form filler for login page
        LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

        PageHelperFactory.registerFormFiller("login-form",
                loginPageHelper.createFormFiller(loginAgentUsername, loginAgentPassword));
        
        try {
            accountSummaryPageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedAccountSummaryPageHelper.class);
            checkOnPage(accountSummaryPageHelper, "personalised-account-summary");
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @When("^I select the 'Change your agent details' link$")
    public void i_select_the_Change_your_agent_details_link() throws Throwable {
        PersonalisedAccountSummaryPageObject = accountSummaryPageHelper.getPageObject();
        PersonalisedAccountSummaryPageObject.clickButtonChangeAgentDetails_NEXTEditPersonalDetails();
    }

    @Then("^heading will be 'Change your agent details'$")
    public void heading_will_be_Change_your_agent_details() throws Throwable {
        assertEquals("check heading","Change your agent details",pageObject.getTextSectionForminfowrap().trim());
    }

    @Then("^Agent name will be displayed$")
    public void agent_name_will_be_displayed() throws Throwable {
       assertEquals("check agent name displayed","Test Agent",pageObject.getTextNdsInputAgentName().trim());
    }
    

    @Given("^I am on the personalised-change-account-details page for an agent$")
    public void i_am_on_the_personalised_change_account_details_page_for_an_agent() throws Throwable {
     // Register a custom form filler for login page
        LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

        PageHelperFactory.registerFormFiller("login-form",
                loginPageHelper.createFormFiller(loginAgentUsername, loginAgentPassword));
        
        try {
            accountSummaryPageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedAccountSummaryPageHelper.class);
            checkOnPage(accountSummaryPageHelper, "personalised-account-summary");
            
            /*This cannot be achieved with form filler and steers, because there are two paths to target page 
            i.e change account details,logging using person and other as org which is not a normal pattern*/
            
            pageHelper = accountSummaryPageHelper.skipToChangeAccountDetailsPageForAgent();
            checkOnPage(pageHelper,"personalised-change-account-details");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @Given("^I have not supplied Agent name$")
    public void i_have_not_supplied_Agent_name() throws Throwable {
        pageObject.setTextNdsInputAgentName("");
    }
    
    @When("^I click Submit$")
    public void i_click_Submit() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.clickButtonOrg_NEXT();
    }
}
