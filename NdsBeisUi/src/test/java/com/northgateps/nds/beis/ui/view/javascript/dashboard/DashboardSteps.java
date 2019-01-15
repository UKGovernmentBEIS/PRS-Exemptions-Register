package com.northgateps.nds.beis.ui.view.javascript.dashboard;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.northgateps.nds.beis.ui.selenium.pagehelper.HelloUserPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedAccountSummaryPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.LoginPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedAccountSummaryPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.serenitybdd.core.annotations.findby.By;
import net.thucydides.core.annotations.Managed;

/** Test steps for the ChangeEmailAddressConfirmation.feature BDD file. */
public class DashboardSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());
    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");
    private final String loginDashboardUsername = (String) testProperties.get("loginDashboardUsername");
    private final String loginDashboardPassword = (String) testProperties.get("loginDashboardPassword");
    private final String loginAgentUsername = (String) testProperties.get("loginAgentUsername");
    private final String loginAgentPassword = (String) testProperties.get("loginAgentPassword");
    String parentHandle;

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedDashboardPageHelper pageHelper;
    UsedServiceBeforePageHelper firstPageHelper; 
    PersonalisedDashboardPageObject pageObject;
    LoginPageHelper loginPageHelper;
    LoginPageObject loginPageObject;
    PersonalisedAccountSummaryPageObject accountSummaryPageObject;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        pageHelper = new PersonalisedDashboardPageHelper(webDriver);
        testHelper.openUrl();
        firstPageHelper = new UsedServiceBeforePageHelper(webDriver);
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
				loginPageHelper.createFormFiller(loginDashboardUsername, loginDashboardPassword));
        
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedDashboardPageHelper.class);
            checkOnPage(pageHelper, "personalised-dashboard");
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
     
    }

    @Given("^I am on the personalised-dashboard page for a person$")
    public void i_am_on_the_dashboard_page_for_a_person() throws Throwable {

		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller(loginUsername, loginPassword));
		
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedDashboardPageHelper.class);
            checkOnPage(pageHelper, "personalised-dashboard");
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @When("^I select Account details$")
    public void i_select_Account_details() throws Throwable {
        HelloUserPageHelper helloHelper = new HelloUserPageHelper(webDriver);
        helloHelper.clickMyAccountDetailsButton();
    }

    @Then("^I will be taken to the personalised-account-summary page$")
    public void i_will_be_taken_to_the_account_summary_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-account-summary");
    }

    @Then("^details will be displayed with First name and Last name$")
    public void details_will_be_displayed_with_First_name_and_Last_name() throws Throwable {
        PersonalisedAccountSummaryPageHelper accountSummaryPageHelper = new PersonalisedAccountSummaryPageHelper(webDriver);
        assertTrue(accountSummaryPageHelper.checkFirstNameAndLastName());
    }

    @Given("^I am on the personalised-dashboard page for an organisation$")
    public void i_am_on_the_dashboard_page_for_an_organisation() throws Throwable {
        //Visit target page for organisation
        pageHelper = PageHelperFactory.visit(firstPageHelper, PersonalisedDashboardPageHelper.class);
        pageObject = pageHelper.getPageObject();
        checkOnPage(pageHelper, "personalised-dashboard");
    }

    @Then("^details will be displayed with Organisation name$")
    public void details_will_be_displayed_with_Organisation_name() throws Throwable {
        PersonalisedAccountSummaryPageHelper accountSummaryPageHelper = new PersonalisedAccountSummaryPageHelper(webDriver);
        assertTrue(accountSummaryPageHelper.checkOrgName());
    }

    @When("^the page is displayed$")
    public void the_page_is_displayed() throws Throwable {
        checkOnPage(pageHelper, "personalised-dashboard");
    }

    @Then("^I will be see the number of current exemptions as (\\d+)$")
    public void i_will_be_see_the_number_of_current_exemptions(int count) throws Throwable {
        assertEquals("validate current exemption count", Integer.toString(count),
                pageHelper.getPageObject().getTextPCurrentExemptionCount());
    }

    @Then("^I will see the number of expired exemptions as (\\d+)$")
    public void i_will_see_the_number_of_expired_exemptions(int count) throws Throwable {
        assertEquals("validate expired exemption count", Integer.toString(count),
                pageHelper.getPageObject().getTextPExpiredExemptionCount());
    }

    @Then("^I will have an option to view my exemptions$")
    public void i_will_have_an_option_to_view_my_exemptions() throws Throwable {
        assertTrue(pageHelper.getPageObject().getWebElementSummaryLink().isDisplayed());
    }

    @Then("^I will remain on the 'personalised-dashboard' page$")
    public void i_will_remain_on_the_dashboard_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-dashboard");
    }

    @Given("^no exemptions are displayed$")
    public void no_exemptions_are_displayed() throws Throwable {
        if (pageHelper.getPageObject().getWebElementSectionContent().isDisplayed()) {
            pageHelper.getPageObject().clickSummaryLink();
        }

        assertFalse(pageHelper.getPageObject().getWebElementSectionContent().isDisplayed());
    }

    @When("^I select 'View exemptions'$")
    public void i_select_View_exemptions() throws Throwable {
        pageHelper.getPageObject().clickSummaryLink();
    }

    @When("^I select 'View exemptions' again$")
    public void i_select_View_exemptions_again() throws Throwable {
        pageHelper.getPageObject().clickSummaryLink();
    }

    @Then("^I will see a list of my current exemptions$")
    public void i_will_see_a_list_of_my_current_exemptions() throws Throwable {
        assertTrue(pageHelper.getPageObject().getWebElementDivCurrentExemptions().isDisplayed());
        assertFalse(pageHelper.getPageObject().getWebElementDivExpiredExemptions().isDisplayed());
    }

    @Then("^I will have an option to end each exemption$")
    public void i_will_have_an_option_to_end_each_exemption() throws Throwable {

        List<WebElement> divs = pageHelper.getPageObject().getWebElementDivCurrentExemptions().findElements(
                By.className("exemption-table"));

        for (int i = 0; i < divs.size(); i++) {

            WebElement endLink = divs.get(i).findElement(By.id("button.endExemption"));
            assertEquals("Check link text", "End this exemption now", endLink.getText());
        }

    }

    @When("^I select 'Current exemptions'$")
    public void i_select_Current_exemptions() throws Throwable {
        pageHelper.getPageObject().clickAnchorCurrentExemptions();
    }

    @When("^I select 'Expired exemptions'$")
    public void i_select_Expired_exemptions() throws Throwable {
        pageHelper.getPageObject().clickAnchorExpiredExemptions();
    }

    @Then("^I will see a list of my expired exemptions$")
    public void i_will_see_a_list_of_my_expired_exemptions() throws Throwable {
        assertFalse(pageHelper.getPageObject().getWebElementDivCurrentExemptions().isDisplayed());
        assertTrue(pageHelper.getPageObject().getWebElementDivExpiredExemptions().isDisplayed());
    }

    @Given("^current exemptions are displayed$")
    public void current_exemptions_are_displayed() throws Throwable {

        if (!pageHelper.getPageObject().getWebElementSectionContent().isDisplayed()) {
            pageHelper.getPageObject().clickSummaryLink();
        }

        pageHelper.getPageObject().clickAnchorCurrentExemptions();
    }

    @Given("^expired exemptions are displayed$")
    public void expired_exemptions_are_displayed() throws Throwable {

        if (!pageHelper.getPageObject().getWebElementSectionContent().isDisplayed()) {
            pageHelper.getPageObject().clickSummaryLink();
        }

        pageHelper.getPageObject().clickAnchorExpiredExemptions();
    }

    @Given("^exemptions are displayed$")
    public void exemptions_are_displayed() throws Throwable {

        if (!pageHelper.getPageObject().getWebElementSectionContent().isDisplayed()) {
            pageHelper.getPageObject().clickSummaryLink();
        }

        assertTrue(pageHelper.getPageObject().getWebElementSectionContent().isDisplayed());
    }

    @Then("^the exemptions will be hidden$")
    public void the_exemptions_will_be_hidden() throws Throwable {
        assertFalse(pageHelper.getPageObject().getWebElementSectionContent().isDisplayed());
    }

    @When("^I click on 'Find out more about exemptions'$")
    public void i_click_on_Find_out_more_about_exemptions() throws Throwable {
        parentHandle = webDriver.getWindowHandle();
        pageHelper.getPageObject().clickAnchorMore();
        BasePageHelper.waitUntilPageLoading(pageHelper.getPageObject().getDriver()); 
    }

    @Then("^I will be taken to the page for that link$")
    public void i_will_be_taken_to_the_page_for_that_link() throws Throwable {
        for (String winHandle : webDriver.getWindowHandles()) {
            webDriver.switchTo().window(winHandle); // switch focus of WebDriver to the next found window handle (that's
                                                    // your newly opened window)
        }
        assertEquals("Checking the current url", "https://www.gov.uk/government/publications/private-rented-sector-minimum-energy-efficiency-standard-exemptions/guidance-on-prs-exemptions-and-exemptions-register-evidence-requirements", webDriver.getCurrentUrl());
        webDriver.close(); // close newly opened window when done with it
        webDriver.switchTo().window(parentHandle);
    }

    @When("^I select 'End exemption'$")
    public void i_select_End_exemption() throws Throwable {
        List<WebElement> divs = pageHelper.getPageObject().getWebElementDivCurrentExemptions().findElements(
                By.className("exemption-table"));
        WebElement endLink = divs.get(0).findElement(By.id("button.endExemption"));
        endLink.click();
    }

    @Then("^I will be taken to the 'personalised-end-exemption' page$")
    public void i_will_be_taken_to_the_personalised_end_exemption_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-end-exemption");
    }
    
    @Given("^I am on the personalised-dashboard page with signed as agent$")
    public void i_am_on_the_personalised_dashboard_page_with_signed_as_agent() throws Throwable {
     // Register a custom form filler for login page
        LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

        PageHelperFactory.registerFormFiller("login-form",
                loginPageHelper.createFormFiller(loginAgentUsername, loginAgentPassword));
        
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedDashboardPageHelper.class);            
            checkOnPage(pageHelper, "personalised-dashboard");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @When("^I select 'Register a new exemption'$")
    public void i_select_Register_a_new_exemption() throws Throwable {
        pageObject.clickNext();
    }

    @Then("^I will be taken to the 'select-landlord-type-agent' page$")
    public void i_will_be_taken_to_the_select_landlord_type_agent_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-select-landlord-type-agent");
    }

    @Then("^heading will be 'Is the landlord an organisation or a person\\?'$")
    public void heading_will_be_Is_the_landlord_an_organisation_or_a_person() throws Throwable {
        assertEquals("Checking heading",webDriver.findElement(By.xpath("//div[contains(@class, 'column-full')]/h1")).getText(),"Is the landlord an organisation or a person?");
    }
    
    @Then("^Agent name will be displayed$")
    public void agent_name_will_be_displayed() throws Throwable {
        PersonalisedAccountSummaryPageHelper accountSummaryPageHelper = new PersonalisedAccountSummaryPageHelper(webDriver);
        accountSummaryPageObject = accountSummaryPageHelper.getPageObject();
        List<WebElement> addressCells = accountSummaryPageObject.getDriver().findElements(By.className("dgstandard"));
        assertTrue("check agent name displayed",addressCells.get(0).getText().trim().contains("Test Agent"));
      
    }

    @Then("^there will be a 'Change your agent details' link$")
    public void there_will_be_a_Change_your_agent_details_link() throws Throwable {
        assertTrue("check change agent details link is displayed",accountSummaryPageObject.getWebElementButtonChangeAgentDetails_NEXTEditPersonalDetails().isDisplayed());
    }
    
    @Then("^I will see a summary of exemptions for the agent$")
    public void i_will_see_a_summary_of_exemptions_for_the_agent() throws Throwable {
        pageHelper.getPageObject().clickSummaryLink();
        assertTrue(pageHelper.getPageObject().getWebElementDivCurrentExemptions().isDisplayed());
        pageHelper.getPageObject().clickAnchorExpiredExemptions();
        assertTrue(pageHelper.getPageObject().getWebElementDivExpiredExemptions().isDisplayed());
    }
    
    @Given("^I have signed in as a landlord$")
    public void i_have_signed_in_as_a_landlord() throws Throwable {
        //signed in as landlord
    }

    @Then("^I will see a summary of exemptions for the landlord$")
    public void i_will_see_a_summary_of_exemptions_for_the_landlord() throws Throwable {
        pageHelper.getPageObject().clickSummaryLink();
        assertTrue(pageHelper.getPageObject().getWebElementDivCurrentExemptions().isDisplayed());
        pageHelper.getPageObject().clickAnchorExpiredExemptions();
        assertTrue(pageHelper.getPageObject().getWebElementDivExpiredExemptions().isDisplayed());
    }


}
