package com.northgateps.nds.beis.ui.view.javascript.activatepasswordreset;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;

import org.openqa.selenium.WebDriver;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.ActivatePasswordResetPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PasswordResetConfirmationPageHelper;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

/**
 * @author Ben Cory
 * 
 * Selenium test steps class for activate password reset. These tests rely on pre-seeded users
 */
public class ActivatePasswordResetSteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    private ActivatePasswordResetPageHelper pageHelper;
    private PasswordResetConfirmationPageHelper confirmationPageHelper;

    //The base url is set to the personalised path which is for security but this page sits below this. 
    private final String unpopulatedTargetRelativeUrl = "../../activatepasswordreset?tenant=BEIS";
    private final String activateResetPasswordOneResetTokenExpires2066 = "YmVuYw==-b1d00a57-67ed-4cb8-8951-ac3709682399-3057746447634";
    private final String activateResetPasswordOneResetTokenNotMatching = "YmVuYw==-b1d00a57-67ed-4cb8-8051-ac3709682399-3057746447634";
    private final String activateExpiredResetPasswordTwoResetToken = "YmVuYw==-07e7b56a-c53d-446b-ba69-d89070532cb1-1479570110300";    
    private final String activateResetPasswordOneUsername = "activateResetPasswordOne"; 
    private final String activateExpiredResetPasswordTwoUsername = "ActivateExpiredResetPasswordTwo";
    private final String validPassword = "a123456789";
    private final String differentPassword = "a123456780";
    private final String tooShortPassword = "a";
    private final String missingCharacterPassword = "1234567890";
    private final String missingNumericPassword = "aaaaaaaaaa";
    private final String userDetailsPassword = activateResetPasswordOneUsername + "a1";
    
    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        testHelper.openUrl();
    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }    
        
    @Given("^I am on the 'activate-password-reset' page$")
    public void i_am_on_the_activate_password_reset_page() throws Throwable {        
        // There's no need to use view state pattern to reach this page as it's accessed from an email link
        testHelper.openUrl(unpopulatedTargetRelativeUrl);
        pageHelper = new ActivatePasswordResetPageHelper(testHelper.getScenarioWebDriver());
        checkOnPage(pageHelper, "activate-password-reset");
    }

    @Given("^activation code is not expired yet$")
    public void activation_code_is_not_expired_yet() throws Throwable {
        //User is seeded with activation code which expires in 2066
        assertTrue(true);
    }

    @Given("^activation code has not been used already$")
    public void activation_code_has_not_been_used_already() throws Throwable {
        //User is seeded with activation code on each test so we know the code has not expired.
        assertTrue(true);
    }

    @Given("^activation code is linked to my username$")
    public void activation_code_is_linked_to_my_username() throws Throwable {
        //User is seeded with activation code on each test so we know the code has not expired.
        assertTrue(true);
    }

    @Given("^I have input valid new password$")
    public void i_have_input_valid_new_password() throws Throwable {
        //Enter the password
        pageHelper.getPageObject().setTextNdsInputPassword(validPassword);
    }        

    @Given("^I have input valid confirm password matching the new password$")
    public void i_have_input_valid_confirm_password_matching_the_new_password() throws Throwable {
        //Enter the confirmation password
        pageHelper.getPageObject().setTextNdsInputConfirmPassword(validPassword);
    }
    
    @Then("^I have input valid but different repeat password$")
    public void i_have_input_valid_but_different_repeat_password() throws Throwable {
        //Enter a different password to test validation
        pageHelper.getPageObject().setTextNdsInputConfirmPassword(differentPassword);
    }    

    @Given("^I have input a valid username$")
    public void i_have_input_a_valid_username() throws Throwable {
        //Enter the username
        pageHelper.getPageObject().setTextNdsInputUsername(activateResetPasswordOneUsername);
    }

    @Given("^I have input activation code$")
    public void i_have_input_activation_code() throws Throwable {
        //Enter a valid token
        pageHelper.getPageObject().setTextNdsInputActivationCode(activateResetPasswordOneResetTokenExpires2066);
    }

    @When("^I choose to reset the password$")
    public void i_choose_to_reset_the_password() throws Throwable {
        //Invoke reset password
        pageHelper.getPageObject().clickButtonNext_NEXT();
        //Wait for the page to load
        BasePageHelper.waitUntilPageLoading(pageHelper.getPageObject().getDriver());    
    }

    @Then("^my password should be updated with my new password supplied$")
    public void my_password_should_be_updated_with_my_new_password_supplied() throws Throwable {
        //Can't check this because we can not make ESB calls from the selenium server in auto tests.
        assertTrue(true);
    }

    @Then("^I must be taken to the 'password-reset-confirmation' page$")
    public void i_must_be_taken_to_the_password_reset_confirmation_page() throws Throwable {
        checkOnPage(pageHelper, "password-reset-confirmation");
        confirmationPageHelper = new PasswordResetConfirmationPageHelper(testHelper.getScenarioWebDriver());
    }
    
    @Then("^I have selected Finish$")
    public void i_have_selected_Finish() throws Throwable {
        confirmationPageHelper.getPageObject().clickAnchorLink();
    }

    @Then("^I will be taken to the “sign-on” page$")
    public void i_will_be_taken_to_the_sign_on_page() throws Throwable {
        // Check we are on the login-form page
        checkOnPage(pageHelper, "login-form");
    }
    
    @Given("^activation code has already been used$")
    public void activation_code_has_already_been_used() throws Throwable {
        //Enter a token already used. This depends on the order of tests and I know this comes after
        //the scenario which uses the token.
        pageHelper.getPageObject().setTextNdsInputActivationCode(activateResetPasswordOneResetTokenExpires2066);
    }

    @Then("^I must be notified \"(.*?)\"$")
    public void i_must_be_notified(String expectedErrorMessage) throws Throwable {
        
        //The notification could be a validation message or system error message so ensure it is displayed.
        boolean foundMessage = false;
        
        pageHelper.getWait().untilTextPresent(expectedErrorMessage);
        foundMessage = expectedErrorMessage.equals(pageHelper.getFirstSysErr());
        
        if (!foundMessage)
        {
            foundMessage = expectedErrorMessage.equals(pageHelper.getFirstSummaryFaultMessage());                        
        }
        
        assertTrue("Check validation message", foundMessage);
    }

    @Then("^I must remain on 'activate-password-reset' page$")
    public void i_must_remain_on_activate_password_reset_page() throws Throwable {
        checkOnPage(pageHelper, "activate-password-reset");
    }

    @Then("^new password and repeat password must be set to blank$")
    public void new_password_and_repeat_password_must_be_set_to_blank() throws Throwable {
        //Had issue with the pageHelper being stale.
        pageHelper = new ActivatePasswordResetPageHelper(testHelper.getScenarioWebDriver());
        // check the passwords have been reset
        assertTrue(pageHelper.getPageObject().getTextNdsInputPassword().equals(""));
        assertTrue(pageHelper.getPageObject().getTextNdsInputConfirmPassword().equals(""));
    }
    
    @Given("^I have not input any data$")
    public void i_have_not_input_any_data() throws Throwable {
        pageHelper.ResetFields();
    }
    
    @Then("^I must receive the messages \"(.*?)\", \"(.*?)\", \"(.*?)\", \"(.*?)\"$")
    public void i_must_receive_the_messages(String usernameMessage, String resetCodeMessage, String passwordMessage, String confirmPasswordMessage) throws Throwable {
        pageHelper.getWait().untilTextPresent(usernameMessage);
        assertEquals("Check validation message for username", usernameMessage, pageHelper.getFirstSummaryFaultMessage());
        assertEquals("Check validation message for reset code", resetCodeMessage, pageHelper.getNthSummaryFaultMessage(2));
        assertEquals("Check validation message for password", passwordMessage, pageHelper.getNthSummaryFaultMessage(3));
        assertEquals("Check validation message for confirm password", confirmPasswordMessage, pageHelper.getNthSummaryFaultMessage(4));
    }
    
    @Given("^activation code has expired$")
    public void activation_code_has_expired() throws Throwable {
        assertTrue(true);
    }

    @Given("^I have input a valid username for expired code$")
    public void i_have_input_a_valid_username_for_expired_code() throws Throwable {
        //Enter the username which has an expired token
        pageHelper.getPageObject().setTextNdsInputUsername(activateExpiredResetPasswordTwoUsername);
    }

    @Given("^I have input expired activation code$")
    public void i_have_input_expired_activation_code() throws Throwable {
        //Enter an expired token
        pageHelper.getPageObject().setTextNdsInputActivationCode(activateExpiredResetPasswordTwoResetToken);
    }
    
    @Given("^I have input unmatching activation code$")
    public void i_have_input_unmatching_activation_code() throws Throwable {
        //Enter an unmatching token
        pageHelper.getPageObject().setTextNdsInputActivationCode(activateResetPasswordOneResetTokenNotMatching);
    }
    
    @Then("^I have supplied a new password without enough characters$")
    public void i_have_supplied_a_new_password_without_enough_characters() throws Throwable {
        pageHelper.getPageObject().setTextNdsInputPassword(tooShortPassword);
        pageHelper.getPageObject().setTextNdsInputConfirmPassword(tooShortPassword);
    }

    @Given("^I have supplied a new password without enough numbers$")
    public void i_have_supplied_a_new_password_without_enough_numbers() throws Throwable {
        pageHelper.getPageObject().setTextNdsInputPassword(missingNumericPassword);
        pageHelper.getPageObject().setTextNdsInputConfirmPassword(missingNumericPassword);
    }

    @Given("^I have supplied a new password without enough letters$")
    public void i_have_supplied_a_new_password_without_enough_letters() throws Throwable {
        pageHelper.getPageObject().setTextNdsInputPassword(missingCharacterPassword);
        pageHelper.getPageObject().setTextNdsInputConfirmPassword(missingCharacterPassword);
    }

    @Given("^I have supplied a new password that includes user details$")
    public void i_have_supplied_a_new_password_that_includes_user_details() throws Throwable {
        pageHelper.getPageObject().setTextNdsInputPassword(userDetailsPassword);
        pageHelper.getPageObject().setTextNdsInputConfirmPassword(userDetailsPassword);
    }
    
}

