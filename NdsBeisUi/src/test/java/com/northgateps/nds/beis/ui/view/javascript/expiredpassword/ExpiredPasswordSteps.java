package com.northgateps.nds.beis.ui.view.javascript.expiredpassword;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.ExpiredPasswordConfirmationPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.ExpiredPasswordPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.ExpiredPasswordPageObject;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

/**
 * Exercise the expired-password page.
 * 
 * This test uses the ExpiredPasswordTest user which has a password policy sub-entry
 * that points to a password policy (obviously) that expires the password after 1 second.
 * 
 * Since this test will be run many times on dev and since we don't want to re-set the password
 * each time, the test will toggle the password between two options.
 * @See ExpiredPasswordPageHelper
 */
public class ExpiredPasswordSteps {
	protected final NdsLogger logger = NdsLogger.getLogger(getClass());
	
    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    LoginPageHelper loginPageHelper;
    ExpiredPasswordPageObject pageObject;
    ExpiredPasswordPageHelper pageHelper;
    UsedServiceBeforePageHelper firstPageHelper;
    ExpiredPasswordConfirmationPageHelper expiredPasswordConfirmationPageHelper;
    
    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
        pageHelper = new ExpiredPasswordPageHelper(webDriver);
        expiredPasswordConfirmationPageHelper = new ExpiredPasswordConfirmationPageHelper(webDriver);
        testHelper.openUrl();
    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }
    
    @Given("^I am on the login page$")
    public void i_am_on_the_login_page() throws Throwable {
        //visit target page
        loginPageHelper = PageHelperFactory.visit(firstPageHelper,LoginPageHelper.class);
        checkOnPage(loginPageHelper, "login-form");
    }

    @Given("^I have logged in as an expired user$")
    public void i_have_entered_an_expired_user_s_details_on_the_login_page() throws Throwable {
        pageHelper.login(loginPageHelper);
    }

    @Then("^I must see the expired password page$")
    public void i_must_see_the_expired_password_page() throws Throwable {  
        checkOnPage(pageHelper, "expired-password");
        pageObject = pageHelper.getNewPageObject();       
        assertTrue("checking on expired password page", pageObject.getTextNdsFormTitleHeading().contains("Your password has expired"));
    }

    @When("^I press submit$")
    public void i_press_submit() throws Throwable {
        pageObject.clicksaveChanges();
    }

    @Then("^I should see a validation error containing \"(.*?)\"$")
    public void i_should_see_a_validation_error_containing(String message) throws Throwable {
       
        try {
        	assertTrue(pageHelper.getSummaryFaultMessages().contains(message));
        } catch (AssertionError ae) {
        	logger.error("Expected message : \"" + message + "\" did not match error message : \"");
        	throw ae;
        }
    }

    @When("^I enter the just the old password \\(incorrectly\\)$")
    public void i_enter_the_just_the_old_password_incorrectly() throws Throwable {
        pageHelper.skipPage("lksdajlk", "", "");
    }

    @When("^I enter mis-matching password and confirmation passwords$")
    public void i_enter_mis_matching_password_and_confirmation_passwords() throws Throwable {
        pageHelper.skipPage("lksdajlk", "asdfsd", "342534");
    }

    @When("^I enter matching password and confirmation passwords but wrong old password$")
    public void i_enter_matching_password_and_confirmation_passwords_but_wrong_old_password() throws Throwable {
        pageHelper.skipPage("lksdajlk", "psst", "psst");
    }

    @When("^I enter the old password correctly but the new password is too short$")
    public void i_enter_the_old_password_correctly_but_the_new_password_is_too_short() throws Throwable {
        pageHelper.skipPage(ExpiredPasswordPageHelper.getOldPassword(), "psst", "psst");
    }

    @When("^I enter a valid new password and confirmation password$")
    public void i_enter_a_valid_new_password_and_confirmation_password() throws Throwable {
        final String newPassword = ExpiredPasswordPageHelper.getNewPassword();
        pageHelper.skipPage(ExpiredPasswordPageHelper.getOldPassword(), newPassword, newPassword);
    }

    @Then("^I should see the password confirmation page$")
    public void i_should_see_the_password_confirmation_page() throws Throwable {        
        checkOnPage(pageHelper, "expired-password-confirmation");
    }

    @When("^I click the next button$")
    public void i_click_the_next_button() throws Throwable {
        expiredPasswordConfirmationPageHelper.getPageObject().clickButtonContinue();
    }
    
    @Then("^I should see the \"(.*?)\" page with the sign in link and definitely not be logged in\\.$")
    public void i_should_see_the_page_with_the_sign_in_link_and_definitely_not_be_logged_in(String arg1) throws Throwable {
    	checkOnPage(pageHelper, "login-form");
    }
}
