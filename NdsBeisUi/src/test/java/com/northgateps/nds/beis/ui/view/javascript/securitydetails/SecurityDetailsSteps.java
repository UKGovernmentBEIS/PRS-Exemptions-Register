package com.northgateps.nds.beis.ui.view.javascript.securitydetails;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.AccountAddressPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.AccountConfirmationPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.SecurityDetailsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.SelectLandlordTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.TermsAndConditionsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.AccountAddressPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.AccountConfirmationPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.SecurityDetailsPageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class SecurityDetailsSteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    UsedServiceBeforePageHelper firstPageHelper;
    SecurityDetailsPageHelper pageHelper;
    SecurityDetailsPageObject pageObject;
    AccountAddressPageHelper accountAddressPageHelper;
    AccountAddressPageObject accountAddressPageObject;
    AccountConfirmationPageObject accountConfirmationPageObject;
    AccountConfirmationPageHelper accountConfirmationPageHelper;
    SelectLandlordTypePageHelper selectLandlordTypePageHelper;
    String parentHandle;
    
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

    @Given("^I am on the security-details page$")
    public void i_am_on_the_security_details_page() throws Throwable {
        firstPageHelper = new UsedServiceBeforePageHelper(webDriver);
        /*Set a custom form filler for used-service-before page*/
        firstPageHelper.setFormFiller(new FormFiller() {
            public void fill(BasePageHelper<?> pageHelper) {
                ((UsedServiceBeforePageHelper) pageHelper).fillInForm(false);
            }
       });
        pageHelper = PageHelperFactory.visit(firstPageHelper, SecurityDetailsPageHelper.class);
        checkOnPage(pageHelper, "security-details");
        pageObject=pageHelper.getPageObject();
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^I will be taken to the account-address page$")
    public void i_will_be_taken_to_the_account_address_page() throws Throwable {
        checkOnPage(pageHelper, "account-address");
        pageObject = pageHelper.getPageObject();
    }

    @Then("^details previously entered will be displayed$")
    public void details_previously_entered_will_be_displayed() throws Throwable {
        accountAddressPageHelper = new AccountAddressPageHelper(webDriver);
        accountAddressPageObject = accountAddressPageHelper.getPageObject();
        assertEquals("building and street", "Flat 1, Projection West", accountAddressPageObject.getTextNdsInputLine0());
        assertEquals("building and street", "Merchants Place", accountAddressPageObject.getTextNdsInputLine1());
        assertEquals("town", "READING", accountAddressPageObject.getTextNdsInputTown());
        assertEquals("country", "", accountAddressPageObject.getTextNdsInputCounty());
        assertEquals("postcode", "RG1 1ET", accountAddressPageObject.getTextNdsInputPostcode());

    }

    @Given("^I have not entered any data$")
    public void i_have_not_entered_any_data() throws Throwable {
        
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        pageObject.clickNext();
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String validationMessage) throws Throwable {    
        pageHelper.waitUntilValidationMessageSeen(validationMessage);
    }

    @Then("^I will remain on the security-details page$")
    public void i_will_remain_on_the_security_details_page() throws Throwable {
        Thread.sleep(1000);
        checkOnPage(pageHelper, "security-details");
        Thread.sleep(1000);
        pageObject = pageHelper.getPageObject();
    }

    @Given("^I have supplied an invalid user name$")
    public void i_have_supplied_an_invalid_user_name() throws Throwable {
        pageObject.setTextNdsInputUsername("north$gate");
    }

    @Given("^I have supplied a password without enough (.*?)$")
    public void i_have_entered_an_invalid_password(String missingType) throws Throwable {
        pageHelper.clearForm();
        pageHelper.fillInForm();
        pageHelper.fillInPassword(missingType);
    }

    @Given("^I have supplied a password that includes user details$")
    public void i_have_supplied_a_password_that_includes_user_details() throws Throwable {
        pageHelper.clearForm();
        pageObject.setTextNdsInputUsername("randomusername");
        pageHelper.fillInSamePasswordAsUsername();
    }
    

    @Then("^I will receive the validation message \"(.*?)\"$")
    public void i_will_receive_the_validation_message(String validationMessage) throws Throwable {
        BasePageHelper.waitUntilPageLoading(pageObject.getDriver());        
        assertEquals("checking message",validationMessage, pageHelper.getFirstSummaryFaultMessage());
    }


    @Given("^I have supplied a password$")
    public void i_have_supplied_a_password() throws Throwable {
        pageHelper.clearForm();
        pageHelper.fillInForm();
        pageObject.setTextNdsInputUsername("testuser");
        pageObject.setTextNdsInputPassword("testuser123");
    }

    @Given("^I have supplied a different confirm password$")
    public void i_have_supplied_a_different_confirm_password() throws Throwable {
        pageObject.setTextNdsInputConfirmPassword("testuser345");
    }

    @Given("^I have supplied valid data$")
    public void i_have_supplied_valid_data() throws Throwable {
        pageHelper.clearForm();
        pageHelper.fillInForm();

    }

    @Then("^I will be taken to the account-confirmation page with the correct signposting$")
    public void i_will_be_taken_to_the_account_confirmation_page_with_the_correct_signposting() throws InterruptedException {
    	Thread.sleep(3000);
    	
        accountConfirmationPageHelper = new AccountConfirmationPageHelper(webDriver);
        accountConfirmationPageObject = accountConfirmationPageHelper.getPageObject();
        checkOnPage(accountConfirmationPageHelper, "account-confirmation");
        pageObject = pageHelper.getPageObject();
        assertTrue("It may take up to 12 hours for the email to arrive. is not present ", pageObject.getTextMainContent().contains(
                "It may take up to 12 hours for the email to arrive."));
        assertTrue("use the forgotten password link to request another one is not present ", pageObject.getTextMainContent().contains(
                "use the forgotten password link to request another one"));
    }

    @Then("^I will receive the custom message \"(.*?)\"$")
    public void i_will_receive_the_custom_message(String message) throws Throwable {
        assertEquals("check custom message", message, pageHelper.getFirstSysErr());
    }

    @When("^I select the terms and conditions link$")
    public void i_select_the_C_link() throws Throwable {
        parentHandle = webDriver.getWindowHandle();
        pageObject.clickAnchorLink();
        BasePageHelper.waitUntilPageLoading(pageHelper.getPageObject().getDriver()); 
     }

    @Then("^I will be taken to the terms-and-conditions page$")
    public void i_will_be_taken_to_the_terms_and_conditions_page() throws Throwable {
        for (String winHandle : webDriver.getWindowHandles()) {
            webDriver.switchTo().window(winHandle); // switch focus of WebDriver to the next found window handle (that's
                                                    // your newly opened window)
        }
        TermsAndConditionsPageHelper termsAndConditionsPageHelper = new TermsAndConditionsPageHelper(webDriver);
        BasePageHelper.waitUntilPageLoading(termsAndConditionsPageHelper.getPageObject().getDriver());    
        checkOnPage(termsAndConditionsPageHelper, "terms-and-conditions");

        webDriver.close(); // close newly opened window when done with it
        webDriver.switchTo().window(parentHandle);
    }
    
    @Given("^I have supplied a username which is outside of length restrictions$")
    public void i_have_supplied_a_username_which_is_outside_of_length_restrictions() throws Throwable {
    	pageObject.getTextNdsInputUsername();
    	pageObject.setTextNdsInputUsername("");
    	pageObject.setTextNdsInputUsername("a");
    	pageObject.setTextNdsInputUsername("a");
    }
}
