package com.northgateps.nds.beis.ui.view.javascript.deleteuseraccount;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedAccountSummaryPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDeleteAccountPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedAccountSummaryPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDeleteAccountPageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;


public class DeleteUserAccountSteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedDeleteAccountPageHelper pageHelper;
    PersonalisedDeleteAccountPageObject pageObject;
    PersonalisedAccountSummaryPageHelper accountSummaryPageHelper;
    PersonalisedAccountSummaryPageObject personalisedAccountSummaryPageObject;
    UsedServiceBeforePageHelper firstPageHelper;
    String emailAddress;

	@Managed
	private WebDriver webDriver;

	   @Before
	    public void beforeScenario() {
	        testHelper.beforeScenario();
	        testHelper.setScenarioWebDriver(webDriver);
	        firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
	        pageHelper = new PersonalisedDeleteAccountPageHelper(testHelper.getScenarioWebDriver());
	        pageObject = pageHelper.getPageObject();
	        testHelper.openUrl();
	    }

	    @After
	    public void afterScenario() {
	        testHelper.afterScenario();
	    }    
	  
	
	@Given("^I am on the personalised-account-summary page$")
	public void i_am_on_the_account_summary_page() throws Throwable {
        //visit target page
        accountSummaryPageHelper = PageHelperFactory.visit(firstPageHelper, PersonalisedAccountSummaryPageHelper.class);
        checkOnPage(accountSummaryPageHelper, "personalised-account-summary");
        personalisedAccountSummaryPageObject = accountSummaryPageHelper.getPageObject();
       
	}
	
	@When("^I select 'Delete your account' link$")
	public void i_select_Delete_your_account_link() throws Throwable {
		accountSummaryPageHelper.getPageObject().clickButtonDeleteMyAccount_NEXTDeleteMyAccount();
		BasePageHelper.waitUntilPageLoading(accountSummaryPageHelper.getPageObject().getDriver());
	}

	@Then("^I will be taken to the personalised-delete-account page$")
	public void i_will_be_taken_to_the_personalised_delete_account_page() throws Throwable {
		checkOnPage(pageHelper, "personalised-delete-account");
	}

	@When("^I select Back$")
	public void i_select_Back() throws Throwable {
	    pageHelper.getPageObject().clickBack();
	}

	@Then("^I can see the delete guidance text and button$")
	public void i_can_see_the_delete_guidance_text_and_button() {
		assertTrue(
				"Once your account has been deleted you will not be able to access it again, is not present",
				pageObject.getTextMainContent().contains(
						"Once your account has been deleted you will not be able to access it again"));
		assertTrue(
				"Deleting your account can not be reversed, is not present",
				pageObject.getTextMainContent().contains(
						"Deleting your account can not be reversed"));
		assertTrue(
				"Any exemptions you have already registered will not be deleted, is not present",
				pageObject.getTextMainContent().contains(
						"Any exemptions you have already registered will not be deleted"));
		assertNotNull("Delete account button missing", pageHelper.getPageObject().getWebElementButtonNext_NEXTDeleteUserAccount());
	}

	@Then("^I will be taken to the 'personalised-account-summary' page$")
	public void i_will_be_taken_to_the_personalised_account_summary_page() {
		checkOnPage(accountSummaryPageHelper, "personalised-account-summary");
	   
	}


}