package com.northgateps.nds.beis.ui.view.javascript.landlordaddress;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedLandlordAddressPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedLandlordDetailsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedSelectLandlordTypeAgentPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedLandlordAddressPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedLandlordDetailsPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedSelectLandlordTypeAgentPageObject;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class LandlordAddressSteps {

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
    PersonalisedLandlordDetailsPageHelper personalisedLandlordDetailsPageHelper;
    PersonalisedLandlordDetailsPageObject personalisedLandlordDetailsPageObject;
    PersonalisedLandlordAddressPageHelper pageHelper;
    PersonalisedLandlordAddressPageObject pageObject;
    

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
    
    @Given("^I am on the personalised-landlord-address page$")
    public void i_am_on_the_personalised_landlord_address_page() throws Throwable {
        // Register a custom form filler for login page
        LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

        PageHelperFactory.registerFormFiller("login-form",
                loginPageHelper.createFormFiller(loginAgentUsername, loginAgentPassword));
        
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedLandlordAddressPageHelper.class);            
            checkOnPage(pageHelper, "personalised-landlord-address");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^I will be taken to the personalised-landlord-details page$")
    public void i_will_be_taken_to_the_personalised_landlord_details_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-landlord-details");
    }

    @Then("^details previously entered will be displayed$")
    public void details_previously_entered_will_be_displayed() throws Throwable {
        personalisedLandlordDetailsPageHelper = new PersonalisedLandlordDetailsPageHelper(pageObject.getDriver());
        personalisedLandlordDetailsPageObject = personalisedLandlordDetailsPageHelper.getPageObject();
        assertEquals("First name", "John", personalisedLandlordDetailsPageObject.getTextNdsInputFirstname());
        assertEquals("Surname", "Smith", personalisedLandlordDetailsPageObject.getTextNdsInputSurname());
        assertEquals("Email", "nds-dummyOne@northgateps.com", personalisedLandlordDetailsPageObject.getTextNdsInputEmailAddress());
        assertEquals("Confirm Email", "nds-dummyOne@northgateps.com",
                personalisedLandlordDetailsPageObject.getTextNdsInputConfirmEmail());
        assertEquals("Telephone Number", "0115 921 0200", personalisedLandlordDetailsPageObject.getTextNdsInputPhoneNumber());
    }

    @Given("^I have selected or entered a valid address$")
    public void i_have_selected_or_entered_a_valid_address() throws Throwable {      
        pageHelper.fillInForm();
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        pageObject.clickNext();
    }

    @Then("^I must move to the personalised-select-property-type page$")
    public void i_must_move_to_the_personalised_select_property_type_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-select-property-type");
    }
}
