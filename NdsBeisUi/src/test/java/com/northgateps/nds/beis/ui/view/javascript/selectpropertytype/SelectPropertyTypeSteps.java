package com.northgateps.nds.beis.ui.view.javascript.selectpropertytype;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedLandlordAddressPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedSelectPropertyTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedLandlordAddressPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedSelectPropertyTypePageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

/** Test steps for the SelectPropertyType.feature BDD file. */
public class SelectPropertyTypeSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");
    private final String loginAgentUsername = (String) testProperties.get("loginAgentUsername");
    private final String loginAgentPassword = (String) testProperties.get("loginAgentPassword");

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject PersonalisedDashboardPageObject;
    PersonalisedSelectPropertyTypePageHelper pageHelper;
    PersonalisedSelectPropertyTypePageObject pageObject;
    LoginPageHelper loginPageHelper;
    UsedServiceBeforePageHelper firstPageHelper;
    PersonalisedLandlordAddressPageHelper personalisedLandlordAddressPageHelper;
    PersonalisedLandlordAddressPageObject personalisedLandlordAddressPageObject;

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
        testHelper.openUrl();
    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }

    @Given("^I am on the personalised-dashboard page$")
    public void i_am_on_the_dashboard_page() throws Throwable {
    	
		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller(loginUsername, loginPassword));
        
        try {
            dashboardPageHelper = PageHelperFactory.visit(firstPageHelper, PersonalisedDashboardPageHelper.class);
            checkOnPage(dashboardPageHelper, "personalised-dashboard");
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @When("^I select 'Register a new exemption'$")
    public void i_select_Register_a_new_exemption() throws Throwable {
        PersonalisedDashboardPageObject.clickButtonNext_NEXT();
    }

    @Then("^I will be taken to the personalised-select-property-type page$")
    public void i_will_be_taken_to_the_select_property_type_page() throws Throwable {
        checkOnPage(dashboardPageHelper, "personalised-select-property-type");
    }

    @Given("^I am on the 'personalised-select-property-type' page$")
    public void i_am_on_the_select_property_type_page() throws Throwable {
    	
		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller(loginUsername, loginPassword));
		
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedSelectPropertyTypePageHelper.class);
            checkOnPage(pageHelper, "personalised-select-property-type");
            pageObject= pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^I will be taken to the 'personalised-dashboard' page$")
    public void i_will_be_taken_to_the_dashboard_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-dashboard");
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String validationMessage) throws Throwable {
        assertEquals("Check validation message", validationMessage, pageHelper.getFirstSummaryFaultMessage());
    }

    @Given("^I have not entered any data$")
    public void i_have_not_entered_any_data() throws Throwable {
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.clickButtonNext_NEXT();
    }

    @Then("^I will remain on the 'personalised-select-property-type' page$")
    public void i_will_remain_on_the_select_property_type_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-select-property-type");
    }

    @When("^I select 'More about property types'$")
    public void i_select_More_about_property_types() throws Throwable {
        pageObject.clickSummaryLink();
    }

    @Then("^help text will be displayed$")
    public void help_text_will_be_displayed() throws Throwable {
        assertEquals(true,
                testHelper.getScenarioWebDriver().findElement(By.id("select_property_name_div")).isDisplayed());
    }

    @Then("^help text will be hidden$")
    public void help_text_will_be_hidden() throws Throwable {
        assertEquals(false,
                testHelper.getScenarioWebDriver().findElement(By.id("select_property_name_div")).isDisplayed());
    }

    @Given("^I have selected a property type$")
    public void i_have_selected_a_property_type() throws Throwable {
        pageObject.clickNdsRadiobuttonPropertyType_PRSN();
    }

    @Then("^I will be taken to the 'personalised-select-exemption-type' page$")
    public void i_will_be_taken_to_the_select_exemption_type_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-select-exemption-type");
        BasePageHelper.waitUntilPageLoading(pageHelper.getPageObject().getDriver());
    }

    @Then("^exemptions for the type of property selected will be displayed$")
    public void exemptions_for_the_type_of_property_selected_will_be_displayed() throws Throwable {
        assertEquals(true,
                testHelper.getScenarioWebDriver().findElement(By.className("selection-button-radio")).isDisplayed());
    }

    @When("^I select the \"(.*?)\" option$")
    public void i_select_the_option(String option) throws Throwable {
        if (option.equals("PRSN")) {
            pageObject.clickNdsRadiobuttonPropertyType_PRSN();
        } else {
            pageObject.clickNdsRadiobuttonPropertyType_PRSD();
        }

    }

   

    @Then("^the Next button will be hidden$")
    public void the_Next_button_will_be_hidden() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        assertEquals(false, pageObject.getWebElementButtonNext_NEXT().isDisplayed());
    }

    @Then("^the Next button will be shown$")
    public void the_Next_button_will_be_shown() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        assertEquals(true, pageObject.getWebElementButtonNext_NEXT().isDisplayed());
    }
    
    @Given("^I have signed in as an agent$")
    public void i_have_signed_in_as_an_agent() throws Throwable {
     // Register a custom form filler for login page
        LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

        PageHelperFactory.registerFormFiller("login-form",
                loginPageHelper.createFormFiller(loginAgentUsername, loginAgentPassword));
        
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedSelectPropertyTypePageHelper.class);            
            checkOnPage(pageHelper, "personalised-select-property-type");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @Given("^I am on the personalised-select-property-type page$")
    public void i_am_on_the_personalised_select_property_type_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-select-property-type");
    }
    
    @Then("^I will be taken to the 'personalised-landlord-address' page$")
    public void i_will_be_taken_to_the_personalised_landlord_address_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-landlord-address");
    }

    @Then("^details previously entered will be displayed$")
    public void details_previously_entered_will_be_displayed() throws Throwable {
        personalisedLandlordAddressPageHelper = new PersonalisedLandlordAddressPageHelper(webDriver);
        personalisedLandlordAddressPageObject = personalisedLandlordAddressPageHelper.getPageObject();
        assertEquals("building and street", "Flat 1, Projection West", personalisedLandlordAddressPageObject.getTextNdsInputLine0());
        assertEquals("building and street", "Merchants Place", personalisedLandlordAddressPageObject.getTextNdsInputLine1());
        assertEquals("town", "READING", personalisedLandlordAddressPageObject.getTextNdsInputTown());
        assertEquals("country", "", personalisedLandlordAddressPageObject.getTextNdsInputCounty());
        assertEquals("postcode", "RG1 1ET", personalisedLandlordAddressPageObject.getTextNdsInputPostcode());
    }


}
