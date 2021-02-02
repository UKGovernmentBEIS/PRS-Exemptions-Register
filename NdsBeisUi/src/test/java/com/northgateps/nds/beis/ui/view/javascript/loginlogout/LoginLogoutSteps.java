package com.northgateps.nds.beis.ui.view.javascript.loginlogout;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.fail;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.HelloUserPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.LoginPageObject;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class LoginLogoutSteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    
    UsedServiceBeforePageHelper usedServiceBeforePageHelper;
    LoginPageHelper loginPageHelper;
    LoginPageObject loginPageObject;
    
    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        usedServiceBeforePageHelper = new UsedServiceBeforePageHelper(webDriver);
        loginPageHelper = new LoginPageHelper(testHelper.getScenarioWebDriver());
        testHelper.openUrl();
     }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }

    @Given("^I'm on login page$")
    public void i_m_on_login_page() throws Throwable {
        usedServiceBeforePageHelper.skipPage();
        checkOnPage(loginPageHelper, "login-form");
        loginPageObject = loginPageHelper.getPageObject();
    }

    @When("^I log in$")
    public void i_log_in() throws Throwable {
        loginPageHelper.skipPage();
    }

    @Then("^I'm taken to the dashboard page$")
    public void i_m_taken_to_the_dashboard_page() throws Throwable {
        checkOnPage(loginPageHelper, "personalised-dashboard");
    }

    @When("^I log out$")
    public void i_log_out() throws Throwable {
        HelloUserPageHelper helloHelper = new HelloUserPageHelper(webDriver);
        helloHelper.openMenu();
        helloHelper.getPageObject().clickAnchorLogout();
    }

    /* Update this method when the CAS redirect on logout configuration is updated. */ 
    @Then("^I'm taken to the done page$")
    public void i_m_taken_to_the_done_page() throws Throwable {
        final String currentPage = webDriver.getCurrentUrl();
        
        // dev versions and final version, depending on config; any are valid
        String expectedPattern1 = "NdsBeisUi/failover-landing";
        String expectedPattern2 = "NdsBeisUi/";
        String expectedPattern3 = "NdsBeisUi/failover-landing.htm";
        String expectedPattern4 = "https://docs.google.com/forms";
        
        if (! ((currentPage.endsWith(expectedPattern1)) || (currentPage.endsWith(expectedPattern2)) || (currentPage.endsWith(expectedPattern3)) ||
        		(currentPage.startsWith(expectedPattern4)))) {
            fail("Current page " + currentPage + " does not ends with " + expectedPattern1 + " or " + expectedPattern2);
        }
    }

    @When("^I return to the site$")
    public void i_return_to_the_site() throws Throwable {
         testHelper.openUrl();         
    }

    @Then("^I am not logged in$")
    public void i_am_not_logged_in() throws Throwable {
        usedServiceBeforePageHelper = new UsedServiceBeforePageHelper(webDriver);
        usedServiceBeforePageHelper.skipPage();
        checkOnPage(usedServiceBeforePageHelper, "login-form");
    }
}
