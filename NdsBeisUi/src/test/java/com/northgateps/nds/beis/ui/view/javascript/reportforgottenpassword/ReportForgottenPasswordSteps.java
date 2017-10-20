/**
 * Steps to test Report Forgotten Password screen
 */
package com.northgateps.nds.beis.ui.view.javascript.reportforgottenpassword;


import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.ForgottenPasswordConfirmationPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.ReportForgottenPasswordPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

/**
 * Test class for Report Forgotten Password
 *
 */
public class ReportForgottenPasswordSteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    LoginPageHelper loginPageHelper;
    ReportForgottenPasswordPageHelper pageHelper;
    ForgottenPasswordConfirmationPageHelper confirmationPageHelper;
    UsedServiceBeforePageHelper firstPageHelper;
    
    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
        testHelper.openUrl();
    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }    
    
    @Given("^I am on the 'login' page$")
    public void i_am_on_the_login_page() throws Throwable {
        //visit target page
        loginPageHelper = PageHelperFactory.visitNew(firstPageHelper, LoginPageHelper.class);
        checkOnPage(loginPageHelper, "login-form");
    }

    @When("^I select 'Forgotten your password\\?'$")
    public void i_select_Forgotten_your_password() throws Throwable {
        loginPageHelper.getPageObject().clickAnchorForgotPassword();
    }

    @Then("^I will be taken to the 'report-forgotten-password' page$")
    public void i_will_be_taken_to_the_report_forgotten_password_page() throws Throwable {
        pageHelper = new ReportForgottenPasswordPageHelper(testHelper.getScenarioWebDriver());
        checkOnPage(pageHelper, "report-forgotten-password");
    }
    
    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageHelper.getPageObject().clickAnchorLink();
    }
    
    @Then("^I will be taken to the 'login' page$")
    public void i_will_be_taken_to_the_login_page() throws Throwable {
        checkOnPage(loginPageHelper, "login-form");
    }  

    @Given("^I have not entered any data$")
    public void i_have_not_entered_any_data() throws Throwable {
        assertTrue(true);
    }

    @When("^I select 'Reset password'$")
    public void i_select_Reset_password() throws Throwable {
        pageHelper.getPageObject().clickNext();
        BasePageHelper.waitUntilPageLoading(pageHelper.getPageObject().getDriver());        
    }

    @When("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String validationMessage) throws Throwable {
        pageHelper.getWait().untilTextPresent(validationMessage);
        assertEquals("Check validation message", validationMessage, pageHelper.getFirstSummaryFaultMessage());
    }

    @When("^I will remain on the 'report-forgotten-password' page$")
    public void i_will_remain_on_the_report_forgotten_password_page() throws Throwable {
        checkOnPage(pageHelper, "report-forgotten-password");
    }

    @Given("^I have supplied a user name$")
    public void i_have_supplied_a_user_name() throws Throwable {
        pageHelper.getPageObject().setTextNdsInputUsername("forgottenpassword");
    }

    @Then("^I will be taken to the 'forgotten-password-confirmation' page$")
    public void i_will_be_taken_to_the_forgotten_password_confirmation_page() throws Throwable {
        checkOnPage(pageHelper, "forgotten-password-confirmation");        
        confirmationPageHelper = new ForgottenPasswordConfirmationPageHelper(testHelper.getScenarioWebDriver());
    }
    
       
    @When("^I have supplied a user name that doesn't exist$")
    public void i_have_supplied_a_user_name_that_doesn_t_exist() throws Throwable {
        pageHelper.getPageObject().setTextNdsInputUsername("nonexistentusername");
    }

    
}
