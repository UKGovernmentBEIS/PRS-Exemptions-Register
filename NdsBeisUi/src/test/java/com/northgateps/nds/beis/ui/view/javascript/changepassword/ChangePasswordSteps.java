package com.northgateps.nds.beis.ui.view.javascript.changepassword;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedAccountSummaryPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedChangePasswordConfirmationPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedChangePasswordPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedChangePasswordPageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

/**
 * 
 * @author DavidL
 * Note that passwordw!11bechangedbytest is the initial password and red999streets the password after this test.
 * That means this test is LIMITED to only run ONCE!
 * To reset it, run the following LDAP ldif script
 * 
 *    dn: uid=newpasswordtest,ou=Users,ou=BEIS,ou=Tenants,ou=NDS,dc=northgateps,dc=com
 *    changetype: modify
 *    replace: userPassword
 *    userPassword: passwordw!11bechangedbytest
 *    
 * Do not change the initial password of passwordw!11bechangedbytest until we change this hacky way of testing
 * because it will break the auto tests which always run on fresh LDAP data. 
 */
public class ChangePasswordSteps {

	private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
	PersonalisedAccountSummaryPageHelper summaryHelper;
	PersonalisedChangePasswordPageHelper pageHelper;
	PersonalisedChangePasswordPageObject pageObject;
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
	
	@Given("^I am on the personalised-account-summary page$")
	public void i_am_on_the_account_summary_page() throws Throwable {
	    
		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller("newpasswordtest", "passwordw!11bechangedbytest"));
        
        try {
            summaryHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedAccountSummaryPageHelper.class);
            
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
        
	}
	
	@When("^I select 'Change password' link$")
	public void i_select_Change_password_link() throws Throwable {
		summaryHelper.getPageObject().clickButtonChangePassword_NEXTChangePassword();
		BasePageHelper.waitUntilPageLoading(summaryHelper.getPageObject().getDriver());
	}

	@Then("^I will be taken to the personalised-change-password page$")
	public void i_will_be_taken_to_the_change_password_page() throws Throwable {
		pageHelper = new PersonalisedChangePasswordPageHelper(testHelper.getScenarioWebDriver());
		checkOnPage(pageHelper, "personalised-change-password");
	}

	@When("^I select Back$")
	public void i_select_Back() throws Throwable {
	    pageHelper.getPageObject().clickBack();
	}

	@Then("^details of the account will be displayed$")
	public void details_of_the_account_will_be_displayed() throws Throwable {
	    
		//TODO
	}

	
	@Given("^I am on the personalised-change-password page$")
	public void i_am_on_the_personalised_change_password_page() throws Throwable {

		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller("newpasswordtest", "passwordw!11bechangedbytest"));
        
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedChangePasswordPageHelper.class);
            pageObject = pageHelper.getPageObject();
            
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
        
	}

	@Given("^I have not entered any data$")
	public void i_have_not_entered_any_data() throws Throwable {
	    //do nothing
	}

	@When("^I select 'Change password'$")
	public void i_select_Change_password() throws Throwable {
	    pageObject.clickButtonNext_NEXT();
	    pageHelper.getNewPageObject();
        pageObject = pageHelper.getPageObject();
	}

	@Then("^I will receive the error message \"(.*?)\"$")
	public void i_will_receive_the_error_message(String validationMessage) throws Throwable {
		
		assertEquals(validationMessage, pageHelper.findFaultMessage(validationMessage));
	}
	
	@Then("^I will receive the message \"(.*?)\"$")
	public void i_will_receive_the_message(String validationMessage) throws Throwable {
		
		pageHelper.getNewPageObject();
		
		boolean found = false;
		
		for( int i=1; i <= 3; i++){
			final String message = pageHelper.getNthSummaryFaultMessage(i);
			if( message.compareTo(validationMessage) == 0 )
			{
				found = true;
				break;
			}
		}
		
		assertTrue(found);
	}
	
	@Then("^I will receive the custom message \"(.*?)\"$")
    public void i_will_receive_the_custom_message(String message) throws Throwable {
        assertEquals("check custom message", message, pageHelper.getFirstSysErr());
    }

	@Then("^I will remain on the personalised-change-password page$")
	public void i_will_remain_on_the_personalised_change_password_page() throws Throwable {
	    checkOnPage(pageHelper, "personalised-change-password");
	}
	
	@Given("^I have supplied an invalid current password$")
	public void i_have_supplied_an_invalid_current_password() throws Throwable {
		pageObject.setTextNdsInputOldPassword("incorrect");
		
	}
	
	@Given("^I have supplied 'New password'$")
	public void i_have_supplied_New_password() throws Throwable {
		pageObject.setTextNdsInputNewPassword("test");
		
	}
	
	@Given("^I have supplied 'Confirm new password'$")
	public void i_have_supplied_Confirm_new_password() throws Throwable {
		pageObject.setTextNdsInputConfirmPassword("test");
		
	}
	
	@Given("^I have supplied a valid new password$")
	public void i_have_supplied_a_valid_new_password() throws Throwable {
		pageObject.setTextNdsInputNewPassword("red999streets");
		pageObject.setTextNdsInputConfirmPassword("red999streets");
	}
	
	@Given("^I have supplied a different confirm password$")
	public void i_have_supplied_a_different_confirm_password() throws Throwable {
		pageObject.setTextNdsInputConfirmPassword("test");		
	}
	
	@Given("^I have supplied a valid old password$")
	public void i_have_supplied_a_valid_old_password() throws Throwable {
		pageObject.setTextNdsInputOldPassword("passwordw!11bechangedbytest");
	}

	@Given("^I have supplied a new password without enough characters$")
	public void i_have_supplied_a_new_password_without_enough_characters() throws Throwable {
		pageObject.setTextNdsInputNewPassword("red1");
		pageObject.setTextNdsInputConfirmPassword("red1");
	}

	@Given("^I have supplied a new password without enough numbers$")
	public void i_have_supplied_a_new_password_without_enough_numbers() throws Throwable {
		pageObject.setTextNdsInputNewPassword("redstreets");
		pageObject.setTextNdsInputConfirmPassword("redstreets");
	}

	@Given("^I have supplied a new password without enough letters$")
	public void i_have_supplied_a_new_password_without_enough_letters() throws Throwable {
		pageObject.setTextNdsInputNewPassword("1234567890");
		pageObject.setTextNdsInputConfirmPassword("1234567890");
	}

	@Given("^I have supplied a new password that includes user details$")
	public void i_have_supplied_a_new_password_that_includes_user_details() throws Throwable {
		pageObject.setTextNdsInputNewPassword("newpasswordtest");
		pageObject.setTextNdsInputConfirmPassword("newpasswordtest");
	}	
	@Given("^I am on the 'personalised-change-password-confirmation' page$")
	public void i_am_on_the_change_personalised_change_password_confirmation_page() throws Throwable {
		checkOnPage(pageHelper, "personalised-change-password-confirmation");
	}
	
	@When("^I select Finish$")
    public void i_select_Finish() throws Throwable {
	    PersonalisedChangePasswordConfirmationPageHelper helper = new PersonalisedChangePasswordConfirmationPageHelper(webDriver);
        helper.getPageObject().clickButtonNext_NEXT();      
    }
	
	@Then("^I will be taken to the 'personalised-change-password-confirmation' page$")
	public void i_will_be_taken_to_the_personalised_change_password_confirmation_page() throws Throwable {
		checkOnPage(pageHelper, "personalised-change-password-confirmation");
	}
	
	@Then("^I will be taken to the 'personalised-account-summary' page$")
    public void i_will_be_taken_to_the_personalised_account_summary() throws Throwable {
        checkOnPage(pageHelper, "personalised-account-summary");
    }
}