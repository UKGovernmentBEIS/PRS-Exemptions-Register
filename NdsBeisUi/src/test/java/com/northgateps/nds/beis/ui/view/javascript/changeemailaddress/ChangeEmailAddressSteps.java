package com.northgateps.nds.beis.ui.view.javascript.changeemailaddress;

/** Test steps for the ChangeEmailAddress.feature BDD file. */
import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedAccountSummaryPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedChangeEmailAddressPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedAccountSummaryPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedChangeEmailAddressPageObject;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.serenitybdd.core.annotations.findby.By;
import net.thucydides.core.annotations.Managed;

public class ChangeEmailAddressSteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedChangeEmailAddressPageHelper pageHelper;
    PersonalisedChangeEmailAddressPageObject pageObject;
    PersonalisedAccountSummaryPageHelper accountSummaryPageHelper;
    PersonalisedAccountSummaryPageObject PersonalisedAccountSummaryPageObject;
    UsedServiceBeforePageHelper firstPageHelper;
    String emailAddress;
   

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
        pageHelper = new PersonalisedChangeEmailAddressPageHelper(testHelper.getScenarioWebDriver());
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
        PersonalisedAccountSummaryPageObject = accountSummaryPageHelper.getPageObject();
    }

    @When("^I select Change email address$")
    public void i_select_Change_email_address() throws Throwable {
       PersonalisedAccountSummaryPageObject.clickButtonChangeEmailID_NEXTChangeEmail();
    }

   
    @Then("^I will be taken to the personalised-change-email-address page$")
    public void i_will_be_taken_to_the_change_email_address_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-change-email-address");
    }


    @Given("^I am on the change-email address page$")
    public void i_am_on_the_change_email_address_page() throws Throwable {
        //visit target page
        pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedChangeEmailAddressPageHelper.class);
        checkOnPage(pageHelper, "personalised-change-email-address");
        pageObject = pageHelper.getPageObject();
    
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^I will be taken to the personalised-account-summary page$")
    public void i_will_be_taken_to_the_account_summary_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-account-summary");
    }

   
    @Given("^I have not entered any data$")
    public void i_have_not_entered_any_data() throws Throwable {
        //nothing to entered
    }

    @When("^I select Submit email address change$")
    public void i_select_Submit_email_address_change() throws Throwable {
        pageObject.clickButtonNext_NEXT();;
    }   

    @When("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String validationMessage) throws Throwable {
        assertEquals("Check validation message", validationMessage, pageHelper.findFaultMessage(validationMessage));
    }

    @When("^I will remain on the personalised-change-email-address page$")
    public void i_will_remain_on_the_change_email_address_page() throws Throwable {
        checkOnPage(firstPageHelper, "personalised-change-email-address");
    }

    @Given("^I have supplied an invalid email address$")
    public void i_have_supplied_an_invalid_email_address() throws Throwable {
        pageObject.setTextNdsInputEmail("invalidemail");
    }

    @Given("^I have supplied an invalid confirm email address$")
    public void i_have_supplied_an_invalid_confirm_email_address() throws Throwable {
        pageObject.setTextNdsInputConfirmEmail("invalidemail");
    }

    @Given("^I have supplied an invalid password$")
    public void i_have_supplied_an_invalid_password() throws Throwable {
       pageObject.setTextNdsInputPassword("invlidpassword");
    }  

    @Given("^I have supplied a new email address$")
    public void i_have_supplied_a_new_email_address() throws Throwable {
       pageObject.setTextNdsInputEmail("abc@gmail.com");
    }

    @Given("^I have supplied a different confirm email address$")
    public void i_have_supplied_a_different_confirm_email_address() throws Throwable {
        pageObject.setTextNdsInputConfirmEmail("pqr@gmail.com");
    }


    @Then("^I have supplied a confirm email address$")
    public void i_have_supplied_a_confirm_email_address() throws Throwable {
        pageObject.setTextNdsInputConfirmEmail("abc@gmail.com");
    }   

    @Given("^I have supplied valid data$")
    public void i_have_supplied_valid_data() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageHelper.fillInForm();
        emailAddress = pageObject.getTextNdsInputEmail();
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
       pageObject.clickNext();
    }
    
    @Then("^details for the account will be displayed$")
    public void details_for_the_account_will_be_displayed() throws Throwable {
        PersonalisedAccountSummaryPageHelper accountSummaryPageHelper = new PersonalisedAccountSummaryPageHelper(webDriver);
        PersonalisedAccountSummaryPageObject = accountSummaryPageHelper.getPageObject();
        List<WebElement> detailsCells = PersonalisedAccountSummaryPageObject.getDriver().findElements(By.className("dgstandard"));
        assertTrue("check new email address",detailsCells.get(0).getText().trim().contains(emailAddress));
    }
}
