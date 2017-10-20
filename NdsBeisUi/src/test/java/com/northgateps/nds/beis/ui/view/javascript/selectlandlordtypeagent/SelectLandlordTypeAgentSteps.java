package com.northgateps.nds.beis.ui.view.javascript.selectlandlordtypeagent;

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
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.serenitybdd.core.annotations.findby.By;
import net.thucydides.core.annotations.Managed;

public class SelectLandlordTypeAgentSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginAgentUsername = (String) testProperties.get("loginAgentUsername");
    private final String loginAgentPassword = (String) testProperties.get("loginAgentPassword");

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject PersonalisedDashboardPageObject;
    PersonalisedSelectLandlordTypeAgentPageHelper pageHelper;
    PersonalisedSelectLandlordTypeAgentPageObject pageObject;
    LoginPageHelper loginPageHelper;
    UsedServiceBeforePageHelper firstPageHelper;
    PersonalisedLandlordDetailsPageHelper personalisedLandlordDetailsPageHelper;
    PersonalisedLandlordDetailsPageObject personalisedLandlordDetailsPageObject;

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
    
    @Given("^I am on the 'personalised-select-landlord-type-agent' page$")
    public void i_am_on_the_personalised_select_landlord_type_agent_page() throws Throwable {
        // Register a custom form filler for login page
        LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

        PageHelperFactory.registerFormFiller("login-form",
                loginPageHelper.createFormFiller(loginAgentUsername, loginAgentPassword));
        
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedSelectLandlordTypeAgentPageHelper.class);            
            checkOnPage(pageHelper, "personalised-select-landlord-type-agent");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^I will be taken to the 'personalised-dashboard' page$")
    public void i_will_be_taken_to_the_personalised_dashboard_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-dashboard");
    }

    @Given("^I have not entered any data$")
    public void i_have_not_entered_any_data() throws Throwable {
        //not entered anything
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
       pageObject.clickNext();
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String validationMessage) throws Throwable {
        assertEquals("Check validation message", validationMessage, pageHelper.getFirstSummaryFaultMessage());
    }

    @Then("^I will remain on the personalised-select-landlord-type-agent page$")
    public void i_will_remain_on_the_personalised_select_landlord_type_agent_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-select-landlord-type-agent");
    }

    @Given("^I have selected person as landlord type$")
    public void i_have_selected_person_as_landlord_type() throws Throwable {
        pageObject.clickNdsRadiobuttonAccountType_PERSON();
    }    
    
    @Then("^I will be taken to the 'personalised-landloard-details' page$")
    public void i_will_be_taken_to_the_personalised_landloard_details_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-landlord-details");
    }
    
    @Then("^First name and Last name will be available for input$")
    public void first_name_and_Last_name_will_be_available_for_input() throws Throwable {
        personalisedLandlordDetailsPageHelper = new PersonalisedLandlordDetailsPageHelper(testHelper.getScenarioWebDriver());
        personalisedLandlordDetailsPageObject = personalisedLandlordDetailsPageHelper.getNewPageObject();
        assertTrue("visibility of First name", personalisedLandlordDetailsPageObject.getWebElementNdsInputFirstname().isDisplayed());
        assertTrue("visibility of Last name", personalisedLandlordDetailsPageObject.getWebElementNdsInputSurname().isDisplayed());
    }

    @Given("^I have selected organisation as landlord type$")
    public void i_have_selected_organisation_as_landlord_type() throws Throwable {
        pageObject.clickNdsRadiobuttonAccountType_ORGANISATION();
    }

    @Then("^Organisation name will be available for input$")
    public void organisation_name_will_be_available_for_input() throws Throwable {
        personalisedLandlordDetailsPageHelper = new PersonalisedLandlordDetailsPageHelper(testHelper.getScenarioWebDriver());
        personalisedLandlordDetailsPageObject = personalisedLandlordDetailsPageHelper.getNewPageObject();
        assertTrue("visibility of Organisation name",
                personalisedLandlordDetailsPageObject.getWebElementNdsInputOrgName().isDisplayed());
    }
    

    @Then("^heading will be 'Landlord’s details'$")
    public void heading_will_be_Landlord_s_details() throws Throwable {
        assertEquals("Checking heading",webDriver.findElement(By.xpath("//div[contains(@class, 'column-full')]/h1")).getText(),"Landlord's details");
    }
}
