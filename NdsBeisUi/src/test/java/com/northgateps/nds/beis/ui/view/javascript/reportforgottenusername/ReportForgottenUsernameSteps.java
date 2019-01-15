package com.northgateps.nds.beis.ui.view.javascript.reportforgottenusername;

import org.openqa.selenium.WebDriver;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.ReportForgottenUsernamePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.ReportForgottenUsernamePageObject;
import com.northgateps.nds.platform.ui.selenium.core.PageHelperException;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;

/**
 * Test steps for the report forgotten username page.
 */
public class ReportForgottenUsernameSteps {
    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    private ReportForgottenUsernamePageHelper pageHelper;
    private ReportForgottenUsernamePageObject pageObject;
    private UsedServiceBeforePageHelper firstPageHelper;
    private LoginPageHelper loginPageHelper;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        pageHelper = new ReportForgottenUsernamePageHelper(testHelper.getScenarioWebDriver());
        firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());        
        testHelper.openUrl();
    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }

    @Given("^I am on the 'login-form' page$")
    public void i_am_on_the_login_form_page() throws PageHelperException {
        loginPageHelper = PageHelperFactory.visit(firstPageHelper, LoginPageHelper.class);
        checkOnPage(loginPageHelper, "login-form");
    }

    @Given("^I am on the 'report-forgotten-username' page$")
    public void i_am_on_the_report_forgotten_username_page() throws Throwable {
        //visit target page
        loginPageHelper = PageHelperFactory.visitNew(firstPageHelper, LoginPageHelper.class);
        checkOnPage(loginPageHelper, "login-form");
        i_select_forgotten_your_username();
        pageObject = pageHelper.getPageObject();
        checkOnPage(pageHelper, "report-forgotten-username");
    }

    @Given("^I am on the 'report-forgotten-username-confirmation' page$")
    public void i_am_on_the_report_forgotten_username_confirmation_page() throws Throwable {
        loginPageHelper = PageHelperFactory.visit(firstPageHelper, LoginPageHelper.class);
        checkOnPage(loginPageHelper, "login-form");
        i_select_forgotten_your_username();
        pageObject = pageHelper.getPageObject();
        checkOnPage(pageHelper, "report-forgotten-username");
        pageObject.setTextNdsInputEmailAddress("example@northgateps.com");
        pageObject.clickButtonNext_NEXT();
        checkOnPage(pageHelper, "report-forgotten-username-confirmation");
    }

    @Given("^I have supplied the email address \"(.*)\"$")
    public void i_have_supplied_the_email_address(String emailAddress) {
        pageObject.setTextNdsInputEmailAddress(emailAddress);
    }

    @When("^I select 'Forgotten your username\\?'$")
    public void i_select_forgotten_your_username() {
        loginPageHelper.getPageObject().clickAnchorForgotUsername();
    }

    @When("^I select 'Back'$")
    public void i_select_back() {
        pageObject.clickAnchorBack();
    }

    @When("^I select 'Next'$")
    public void i_select_next() {
        pageObject.clickButtonNext_NEXT();
    }

    @Then("^I will be taken to the '(.*)' page$")
    public void i_will_be_taken_to_the_page(String page) {
        checkOnPage(pageHelper, page);
    }

    @Then("^I will receive the message \"(.*)\"$")
    public void i_will_receive_the_message(String message) {
        pageHelper.getWait().untilTextPresent(message);
        
        assertEquals("Check validation message", message, pageHelper.findFaultMessage(message));
    }

    @Then("^I will remain on the '(report-forgotten-username.*)' page$")
    public void i_will_remain_on_the_page(String page) {
        checkOnPage(pageHelper, page);
    }
}
