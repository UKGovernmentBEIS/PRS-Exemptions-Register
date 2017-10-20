package com.northgateps.nds.beis.ui.view.javascript.sessionbleed;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkNotOnPage;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.HelloUserPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedAccountSummaryPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.SessionExceptionPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.LoginPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

/**
 * Checks the viewstate information from one tab cannot be resurrected by logging in
 * using a different tab (which establishes a new, cookie based session).
 */
public class SessionBleedSteps {
    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedDashboardPageHelper pageHelper;
    LoginPageHelper loginPageHelper;
    LoginPageObject loginPageObject;
    PersonalisedDashboardPageObject pageObject;
    UsedServiceBeforePageHelper firstPageHelper;

    @Managed
    private WebDriver webDriver;

    /** We save the loginPageUrl when we first log in, to use in the test to attempt a session bleed attack. */
    private String loginPageUrl;
    
    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        pageHelper = new PersonalisedDashboardPageHelper(webDriver);
        loginPageHelper = new LoginPageHelper(webDriver);
        loginPageObject = loginPageHelper.getPageObject();
        firstPageHelper = new UsedServiceBeforePageHelper(webDriver);
        testHelper.openUrl();
    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }

    @Given("^I've logged in$")
    public void i_ve_logged_in() throws Throwable {
    	firstPageHelper.skipPage();
    	checkOnPage(loginPageHelper, "login-form");
    	
        // save the login url for use later
        loginPageUrl = webDriver.getCurrentUrl();
        
        // login
        pageHelper = (PersonalisedDashboardPageHelper) loginPageHelper.skipPage();
    }

    @When("^proceed further$")
    public void proceed_further() throws Throwable {
        loginPageObject.clickButton_LOGIN();
    }

    @Given("^I wait (\\d+) seconds for the CAS cookie to expire$")
    public void i_wait_seconds_for_the_CAS_cookie_to_expire(int seconds) throws Throwable {
        Thread.sleep(seconds * 1000);
    }

    // using windows rather than tabs as support in Selenium exists
    @Given("^I open a new tab directly to the login page$")
    public void i_open_a_new_tab_directly_to_the_login_page() throws Throwable {

        // open a new one
        webDriver.findElement(By.tagName("body")).sendKeys(Keys.CONTROL,"n");

        // switch to it
        List<String> handles = new ArrayList<String> (webDriver.getWindowHandles());
        webDriver.switchTo().window(handles.get(1));
        
        // go direct to the login page
        testHelper.openUrl(loginPageUrl);
    }

    @Given("^I log in using the sessionbleed user$")
    public void i_log_in_using_the_sessionbleed_user() throws Throwable {

    	// check we've got a fresh session (ie on the login page)
    	checkOnPage(loginPageHelper, "login-form");
    	
        loginPageHelper.setFormFiller(loginPageHelper.createFormFiller("sessionbleed", "test@123456789"));
        pageHelper = PageHelperFactory.visit(loginPageHelper, PersonalisedDashboardPageHelper.class);
    }
    
    @Then("^I'm taken to the dashboard page$")
    public void i_m_taken_to_the_dashboard_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-dashboard");
    }

    @Then("^I'm definitely logged in as the sessionbleed user$")
    public void i_m_definitely_logged_in_as_the_sessionbleed_user() throws Throwable {
        HelloUserPageHelper hello = new HelloUserPageHelper(webDriver);
        hello.clickMyAccountDetailsButton();
        BasePageHelper.waitUntilPageLoading(webDriver);
        
        PersonalisedAccountSummaryPageHelper account = new PersonalisedAccountSummaryPageHelper(webDriver);
        assertEquals("session bleed", account.getUsersFirstLastName());
    }

    // using windows rather than tabs as support in Selenium exists
    @When("^I switch to the first tab$")
    public void i_switch_to_the_first_tab() throws Throwable {
        List<String> handles = new ArrayList<String> (webDriver.getWindowHandles());
        webDriver.switchTo().window(handles.get(0));
    }

    @When("^click the My account link$")
    public void click_the_My_account_link() throws Throwable {
        HelloUserPageHelper helloHelper = new HelloUserPageHelper(webDriver);
        helloHelper.clickMyAccountDetailsButton();
        BasePageHelper.waitUntilPageLoading(webDriver);
    }

    @Then("^I should see the Session Exception page$")
    public void i_should_see_the_Session_Exception_page() throws Throwable {
        checkOnPage(pageHelper, "session-exception");
    }

    @Then("^clicking Start again link should take me back to the start page$")
    public void clicking_Start_again_link_should_take_me_back_to_the_start_page() throws Throwable {
        SessionExceptionPageHelper sessionExceptionPageHelper = new SessionExceptionPageHelper(webDriver);
        sessionExceptionPageHelper.skipPage();
        BasePageHelper.waitUntilPageLoading(webDriver);
        checkOnPage(sessionExceptionPageHelper, "personalised-dashboard");
    }

    // using windows rather than tabs as support in Selenium exists
    @When("^I switch to the second tab$")
    public void i_switch_to_the_second_tab() throws Throwable {
        List<String> handles = new ArrayList<String> (webDriver.getWindowHandles());
        webDriver.switchTo().window(handles.get(1));
    }
    
    @When("^I enter a url crafted to overwrite uiData.sessionId$")
    public void i_enter_a_url_crafted_to_overwrite_uiData_sessionId() throws Throwable {
    	testHelper.openUrl("?uiData.sessionId=willthiscauseasessionexceptionbyallowingsessionidtobeoverwritten?");
    }
    
    @Then("^I'm not taken to the session-exception page$")
    public void i_m_not_taken_to_the_session_exception_page() {
    	checkNotOnPage(pageHelper, "session-exception");
    }
}