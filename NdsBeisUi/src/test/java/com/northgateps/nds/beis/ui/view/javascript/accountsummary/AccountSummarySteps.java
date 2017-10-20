package com.northgateps.nds.beis.ui.view.javascript.accountsummary;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedAccountSummaryPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedAccountSummaryPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

/** Test steps for the AccountSummary.feature BDD file. */
public class AccountSummarySteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    
    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");
    
    PersonalisedAccountSummaryPageObject pageObject;
    PersonalisedAccountSummaryPageHelper pageHelper;
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject PersonalisedDashboardPageObject;
    UsedServiceBeforePageHelper firstPageHelper;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);       
        firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
        pageHelper = new PersonalisedAccountSummaryPageHelper(testHelper.getScenarioWebDriver());
        pageObject = pageHelper.getPageObject();
        testHelper.openUrl();
    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }

    @Given("^I am on the personalised-account-summary page$")
    public void i_am_on_the_account_summary_page() throws Throwable {
    	
        // Register a custom form filler for login page
    	LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);
    	
        PageHelperFactory.registerFormFiller("login-form", 
        		loginPageHelper.createFormFiller(loginUsername, loginPassword));
        
        try {
            pageHelper = PageHelperFactory.visit(firstPageHelper, PersonalisedAccountSummaryPageHelper.class);   
            pageObject = pageHelper.getPageObject();
            checkOnPage(pageHelper, "personalised-account-summary");    
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @Then("^I will be taken to the personalised-dashboard page$")
    public void i_will_be_taken_to_the_personalised_dashboard_page() throws Throwable {
    	checkOnPage(pageHelper, "personalised-dashboard");
    }

    @Then("^details for the account will be displayed$")
    public void details_for_the_account_will_be_displayed() throws Throwable {

    }
   
    @When("^I select 'View my exemptions'$")
    public void i_select_View_my_exemptions() throws Throwable {
        pageObject.clickButtonNext_NEXTHome();
     }

}
