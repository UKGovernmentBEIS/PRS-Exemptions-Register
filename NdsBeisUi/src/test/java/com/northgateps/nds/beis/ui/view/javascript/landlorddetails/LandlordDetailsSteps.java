package com.northgateps.nds.beis.ui.view.javascript.landlorddetails;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedLandlordDetailsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedSelectLandlordTypeAgentPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedLandlordDetailsPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedSelectLandlordTypeAgentPageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class LandlordDetailsSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginAgentUsername = (String) testProperties.get("loginAgentUsername");
    private final String loginAgentPassword = (String) testProperties.get("loginAgentPassword");

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject PersonalisedDashboardPageObject;
    PersonalisedSelectLandlordTypeAgentPageHelper personalisedSelectLandlordTypeAgentPageHelper;
    PersonalisedSelectLandlordTypeAgentPageObject personalisedSelectLandlordTypeAgentPageObject;
    LoginPageHelper loginPageHelper;
    UsedServiceBeforePageHelper firstPageHelper;
    PersonalisedLandlordDetailsPageHelper pageHelper;
    PersonalisedLandlordDetailsPageObject pageObject;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        testHelper.openUrl();
        firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
        dashboardPageHelper = new PersonalisedDashboardPageHelper(testHelper.getScenarioWebDriver());
        PersonalisedDashboardPageObject = dashboardPageHelper.getPageObject();        
    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }
    
    @Given("^I am on the personalised-landlord-details to enter Person details$")
    public void i_am_on_the_personalised_landlord_details_to_enter_Person_details() throws Throwable {
     // Register a custom form filler for login page
        LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

        PageHelperFactory.registerFormFiller("login-form",
                loginPageHelper.createFormFiller(loginAgentUsername, loginAgentPassword));
        
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedLandlordDetailsPageHelper.class);            
            checkOnPage(pageHelper, "personalised-landlord-details");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^I will be taken to the personalised-select-landlord-type-agent page$")
    public void i_will_be_taken_to_the_personalised_select_landlord_type_agent_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-select-landlord-type-agent");
    }

    @Then("^details previously entered will be displayed$")
    public void details_previously_entered_will_be_displayed() throws Throwable {
        personalisedSelectLandlordTypeAgentPageHelper = new PersonalisedSelectLandlordTypeAgentPageHelper(testHelper.getScenarioWebDriver());
        personalisedSelectLandlordTypeAgentPageObject = personalisedSelectLandlordTypeAgentPageHelper.getNewPageObject();
        assertTrue("Checking details",personalisedSelectLandlordTypeAgentPageObject.getWebElementNdsRadiobuttonAccountType_PERSON().isSelected());
    }

    @Given("^I have not entered any data$")
    public void i_have_not_entered_any_data() throws Throwable {
        //nothing entered
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        pageObject.clickNext();
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String validationMessage) throws Throwable {
        BasePageHelper.waitUntilPageLoading(pageHelper.getPageObject().getDriver());    
        assertEquals("Check validation message", validationMessage, pageHelper.findFaultMessage(validationMessage));
    }

    @Then("^I will remain on the personalised-landlord-details page$")
    public void i_will_remain_on_the_personalised_landlord_details_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-landlord-details");
    }

    @Given("^I have supplied an invalid email address as \"(.*?)\"$")
    public void i_have_supplied_an_invalid_email_address_as(String invalidEmail) throws Throwable {
        pageObject.setTextNdsInputEmailAddress(invalidEmail);
    }

    @Given("^I have supplied an invalid confirm email address as \"(.*?)\"$")
    public void i_have_supplied_an_invalid_confirm_email_address_as(String email) throws Throwable {
        pageObject.setTextNdsInputConfirmEmail(email);
    }

    @Given("^I have supplied an invalid phone number that does not consist of numeric or space characters with an optional leading \\+ as \"(.*?)\"$")
    public void i_have_supplied_an_invalid_phone_number_that_does_not_consist_of_numeric_or_space_characters_with_an_optional_leading_as(String phone) throws Throwable {
        pageObject.setTextNdsInputPhoneNumber(phone);
    }

    @When("^I have supplied an email address as \"(.*?)\"$")
    public void i_have_supplied_an_email_address_as(String email) throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.setTextNdsInputEmailAddress(email);
    }

    @When("^I have supplied a different confirm email address as \"(.*?)\"$")
    public void i_have_supplied_a_different_confirm_email_address_as(String email) throws Throwable {
        pageObject.setTextNdsInputConfirmEmail(email);
    }

    @Given("^I have supplied valid data$")
    public void i_have_supplied_valid_data() throws Throwable {
        pageHelper.fillInForm();
    }

    @Then("^I will be taken to the personalised-landlord-address page$")
    public void i_will_be_taken_to_the_personalised_landlord_address_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-landlord-address");
    }

    @Given("^I am on the personalised-landlord-details to enter Organisation name$")
    public void i_am_on_the_personalised_landlord_details_to_enter_Organisation_name() throws Throwable {
        // Register a custom form filler for login page
        LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

        PageHelperFactory.registerFormFiller("login-form",
                loginPageHelper.createFormFiller(loginAgentUsername, loginAgentPassword));
        
        //Set a custom form filler for select-landlord-type page
        PageHelperFactory.registerFormFiller("personalised-select-landlord-type-agent", new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) {
                ((PersonalisedSelectLandlordTypeAgentPageHelper) pageHelper).fillInForm("ORGANISATION");
                
            }
        });

        
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedLandlordDetailsPageHelper.class);            
            checkOnPage(pageHelper, "personalised-landlord-details");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
            PageHelperFactory.unregisterFormFiller("personalised-select-landlord-type-agent");
        }
    }
}
