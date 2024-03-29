package com.northgateps.nds.beis.ui.view.javascript.accountaddress;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import com.google.common.base.Function;
import com.northgateps.nds.beis.ui.selenium.pagehelper.AccountAddressPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.AccountDetailsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.SelectLandlordTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.AccountAddressPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.AccountDetailsPageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class AccountAddressSteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    UsedServiceBeforePageHelper firstPageHelper;
    AccountAddressPageHelper pageHelper;
    AccountAddressPageObject pageObject;
    AccountDetailsPageHelper accountDetailsPageHelper;
    AccountDetailsPageObject accountDetailsPageObject;
    SelectLandlordTypePageHelper selectLandlordTypePageHelper;
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

    @Given("^I am on the account-address page$")
    public void i_am_on_the_account_address_page() throws Throwable {
        /*Set a custom form filler for used-service-before page*/
        firstPageHelper.setFormFiller(new FormFiller() {
            public void fill(BasePageHelper<?> pageHelper) {
                ((UsedServiceBeforePageHelper) pageHelper).fillInForm(false);
            }
       });
        
         // visit target page
        pageHelper = PageHelperFactory.visitNew(firstPageHelper,AccountAddressPageHelper.class);
        pageObject=pageHelper.getPageObject();
        checkOnPage(pageHelper, "account-address");
        // check on page gets the new page object, therefore page object needs to be updated
        pageObject = pageHelper.getPageObject();
    }

    @Then("^I will be taken to the account-details page$")
    public void i_will_be_taken_to_the_account_details_page() throws Throwable {
        checkOnPage(pageHelper, "account-details");
        // check on page gets the new page object, therefore page object needs to be updated
        pageObject = pageHelper.getPageObject();
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^details previously entered will be displayed$")
    public void details_previously_entered_will_be_displayed() throws Throwable {
        accountDetailsPageHelper = new AccountDetailsPageHelper(pageObject.getDriver());
        accountDetailsPageObject = accountDetailsPageHelper.getPageObject();
        assertEquals("First name", "John", accountDetailsPageObject.getTextNdsInputFirstname());
        assertEquals("Surname", "Smith", accountDetailsPageObject.getTextNdsInputSurname());
        assertEquals("Email", "nds-dummyOne@necsws.com", accountDetailsPageObject.getTextNdsInputEmail());
        assertEquals("Confirm Email", "nds-dummyOne@necsws.com",
                accountDetailsPageObject.getTextNdsInputConfirmEmail());
        assertEquals("Telephone Number", "0115 921 0200", accountDetailsPageObject.getTextNdsInputTelNumber());

    }

    @Given("^I have not selected an address from the address search$")
    public void i_have_not_selected_an_address_from_the_address_search() throws Throwable {

    }

    @Given("^I have not supplied any manual address details$")
    public void i_have_not_supplied_any_manual_address_details() throws Throwable {
        pageHelper.ClearForm();
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        pageObject.clickNext();
    }

    @Then("^I must receive \"(.*?)\" as validation message$")
    public void i_must_receive_as_validation_message(String validationMessage) throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        assertEquals("Check validation message", validationMessage, pageHelper.findFaultMessage(validationMessage));
    }

    @Then("^I will remain on the account-address page$")
    public void i_will_remain_on_the_account_address_page() throws Throwable {
        checkOnPage(pageHelper, "account-address");
        // check on page gets the new page object, therefore page object needs to be updated
        pageObject = pageHelper.getPageObject();
    }

    @Given("^I have not supplied a postcode$")
    public void i_have_not_supplied_a_postcode() throws Throwable {

    }

    @When("^I select Find address$")
    public void i_select_Find_address() throws Throwable {
        pageObject.clickButtonFindPostcode();
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String message) throws Throwable {
    	BasePageHelper.waitUntilPageLoading(pageObject.getDriver());
        assertEquals("check message", message, pageHelper.findFaultMessage(message));
    }
    
    @Then("^I will receive the validation message \"(.*?)\"$")
    public void i_will_receive_the_validation_message(String message) throws Throwable {
        Thread.sleep(1000);
    	String validationMessage =pageObject.getDriver().findElement(By.xpath("//span[@class='error-message sticky-error']")).getText();
    	 assertEquals("check message", message, validationMessage);
    }

    @Given("^I have supplied an invalid postcode as \"(.*?)\"$")
    public void i_have_supplied_an_invalid_postcode_as(String postcode) throws Throwable {
        pageObject.setTextNdsInputPostcodeCriterion(postcode);
    }

    @Given("^I have supplied a valid postcode as \"(.*?)\"$")
    public void i_have_supplied_a_valid_postcode_as(String postcode) throws Throwable {
        pageObject.setTextNdsInputPostcodeCriterion(postcode);
    }

    @Given("^addresses exist for the postcode$")
    public void addresses_exist_for_the_postcode() throws Throwable {
    }

    @Then("^a pick an address selection box will be displayed$")
    public void a_pick_an_address_selection_box_will_be_displayed() throws Throwable {
        while (true) {
            try {
                assertTrue("Check validation message", pageHelper.getCurrentAddressSelectList().isDisplayed());
                assertTrue("Check validation message",
                        pageHelper.getCurrentAddressSelectList().findElements(By.tagName("option")).size() >= 2);
                return;
            } catch (StaleElementReferenceException e) {
                // retry
                pageHelper.getNewPageObject();
            }
        }

    }

    @Then("^the number of addresses will be displayed in the box as \"(.*?)\"$")
    public void the_number_of_addresses_will_be_displayed_in_the_box_as(String message) throws Throwable {
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    String text = new Select(
                            pageHelper.getCurrentAddressSelectList()).getFirstSelectedOption().getText();
                    return message.equals(text);
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });
        assertEquals("First Row of the list ", message,
                new Select(pageHelper.getCurrentAddressSelectList()).getFirstSelectedOption().getText().trim());

    }

    @Given("^address details are already displayed$")
    public void address_details_are_already_displayed() throws Throwable {

    }

    @Then("^the address fields will be hidden$")
    public void the_address_fields_will_be_hidden() throws Throwable {
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return (!pageHelper.getCurrentAddressInput("line[0]").isDisplayed());
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });

        assertTrue("Check if Address Line 0 is hiddden",
                pageHelper.getCurrentAddressInput("line[0]").isDisplayed() == false);
        assertTrue("Check if Address Line 1 is hiddden",
                pageHelper.getCurrentAddressInput("line[1]").isDisplayed() == false);
        assertTrue("Check if Address Line town is hiddden",
                pageHelper.getCurrentAddressInput("town").isDisplayed() == false);
        assertTrue("Check if Address Line country is hiddden",
                pageHelper.getCurrentAddressInput("county").isDisplayed() == false);
        assertTrue("Check if Address Line postcode is hiddden",
                pageHelper.getCurrentAddressInput("postcode").isDisplayed() == false);

    }

    @Given("^I have selected Find address$")
    public void i_have_selected_Find_address() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.clickButtonFindPostcode();

    }

    @When("^I select an address$")
    public void i_select_an_address() throws Throwable {
        String selectValue = "Flat 1, Projection West, Merchants Place, READING, RG1 1ET";
        pageHelper.waitUntilElementFound(pageObject.getByNdsSelectSelectedAddressRef());
        pageObject.selectOptionNdsSelectSelectedAddressRef(selectValue);

    }

    @Then("^the address will be displayed$")
    public void the_address_will_be_displayed() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        assertEquals("Check Address Line 0", "Flat 1, Projection West",
                pageHelper.getCurrentAddressInput("line[0]").getAttribute("value"));
        assertEquals("Check Address Line 1", "Merchants Place",
                pageHelper.getCurrentAddressInput("line[1]").getAttribute("value"));
        assertEquals("Check Address Town", "READING", pageHelper.getCurrentAddressInput("town").getAttribute("value"));
        assertEquals("Check Address County", "", pageHelper.getCurrentAddressInput("county").getAttribute("value"));
        assertEquals("Check Address Postcode", "RG1 1ET",
                pageHelper.getCurrentAddressInput("postcode").getAttribute("value"));

    }

    @Then("^the address fields will be display only$")
    public void the_address_fields_will_be_display_only() throws Throwable {
        assertTrue("Check if line 0 is not editable",
                pageHelper.getCurrentAddressInput("line[0]").getAttribute("readonly") != null);
        assertTrue("Check if Line 1 is not editable",
                pageHelper.getCurrentAddressInput("line[1]").getAttribute("readonly") != null);
        assertTrue("Check if County is not editable",
                pageHelper.getCurrentAddressInput("county").getAttribute("readonly") != null);
        assertTrue("Check if Town is not editable",
                pageHelper.getCurrentAddressInput("town").getAttribute("readonly") != null);
        assertTrue("Check if Postcode is not editable",
                pageHelper.getCurrentAddressInput("postcode").getAttribute("readonly") != null);

    }

    @Given("^I have selected an address$")
    public void i_have_selected_an_address() throws Throwable {
        String selectValue = "Flat 1, Projection West, Merchants Place, READING, RG1 1ET";
        pageHelper.waitUntilElementFound(pageObject.getByNdsSelectSelectedAddressRef());
        pageObject.selectOptionNdsSelectSelectedAddressRef(selectValue);

    }

    
    @Given("^the address is displayed$")
    public void the_address_is_displayed() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        assertEquals("Check Address Line 0", "Flat 1, Projection West",
                pageHelper.getCurrentAddressInput("line[0]").getAttribute("value"));
        assertEquals("Check Address Line 1", "Merchants Place",
                pageHelper.getCurrentAddressInput("line[1]").getAttribute("value"));
        assertEquals("Check Address Town", "READING", pageHelper.getCurrentAddressInput("town").getAttribute("value"));
        assertEquals("Check Address County", "", pageHelper.getCurrentAddressInput("county").getAttribute("value"));
        assertEquals("Check Address Postcode", "RG1 1ET",
                pageHelper.getCurrentAddressInput("postcode").getAttribute("value"));

    }


    @When("^I select Enter an address manually$")
    public void i_select_Enter_an_address_manually() throws Throwable {
        pageObject.clickAnchorEnterAddressManually();
    }

    @Then("^the address fields will become updatable$")
    public void the_address_fields_will_become_updatable() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        assertTrue("Check if Line 0 is updatable",
                pageHelper.getCurrentAddressInput("line[0]").getAttribute("readonly") == null);
        assertTrue("Check if Line 1 is updatable",
                pageHelper.getCurrentAddressInput("line[1]").getAttribute("readonly") == null);
        assertTrue("Check if Country is updatable",
                pageHelper.getCurrentAddressInput("county").getAttribute("readonly") == null);
        assertTrue("Check if Town is updatable",
                pageHelper.getCurrentAddressInput("town").getAttribute("readonly") == null);
        assertTrue("Check if Postcode is updatable",
                pageHelper.getCurrentAddressInput("postcode").getAttribute("readonly") == null);

    }

    @Given("^I have not selected an address$")
    public void i_have_not_selected_an_address() throws Throwable {
    }

    @Then("^the address fields will be displayed$")
    public void the_address_fields_will_be_displayed() throws Throwable {
        assertTrue("Check if line 0 will be displayed",
                webDriver.findElement(By.id("beisRegistrationDetails.contactAddress.line0")).isDisplayed());
        assertTrue("Check if line 1 will be displayed",
                webDriver.findElement(By.id("beisRegistrationDetails.contactAddress.line1")).isDisplayed());
        assertTrue("Check if Country will be displayed",
                webDriver.findElement(By.id("beisRegistrationDetails.contactAddress.town")).isDisplayed());
        assertTrue("Check if Town will be displayed",
                webDriver.findElement(By.id("beisRegistrationDetails.contactAddress.county")).isDisplayed());
        assertTrue("Check if Town will be displayed",
                webDriver.findElement(By.id("beisRegistrationDetails.contactAddress.postcode")).isDisplayed());

    }

    @Then("^the address fields will be updatable$")
    public void the_address_fields_will_be_updatable() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        assertTrue("Check if Line 0 is editable",
                pageHelper.getCurrentAddressInput("line[0]").getAttribute("readonly") == null);
        assertTrue("Check if Line 1 is editable",
                pageHelper.getCurrentAddressInput("line[1]").getAttribute("readonly") == null);
        assertTrue("Check if Country is editable",
                pageHelper.getCurrentAddressInput("county").getAttribute("readonly") == null);
        assertTrue("Check if Town is editable",
                pageHelper.getCurrentAddressInput("town").getAttribute("readonly") == null);
        assertTrue("Check if Postcode is editable",
                pageHelper.getCurrentAddressInput("postcode").getAttribute("readonly") == null);

    }

    @Given("^I have selected or entered a valid address$")
    public void i_have_selected_or_entered_a_valid_address() throws Throwable {
        pageObject.clickAnchorEnterAddressManually();
        pageHelper.fillInForm();

    }

    @Then("^I will receive the custom message \"(.*?)\"$")
    public void i_will_receive_the_custom_message(String message) throws Throwable {
        final String customMessage = pageObject.getTextDivCustommessage();
        assertEquals("Check custom validation message", message, customMessage);
    }

    @Then("^I must move to the security-details page$")
    public void i_must_move_to_the_security_details_page() throws Throwable {
        checkOnPage(pageHelper, "security-details");
        // check on page gets the new page object, therefore page object needs to be updated
        pageObject = pageHelper.getPageObject();
    }

    @Given("^I have supplied a valid postcode with no Address exists$")
    public void i_have_supplied_a_valid_postcode_with_no_Address_exists() throws Throwable {
        pageObject.setTextNdsInputPostcodeCriterion("nl3 8da");
        //
    }

    @Given("^I have selected Enter an address manually$")
    public void i_have_selected_Enter_an_address_manually() throws Throwable {
        pageObject.clickAnchorEnterAddressManually();
    }

    @Given("^I have only supplied Postcode$")
    public void i_have_only_supplied_Postcode() throws Throwable {
        pageHelper.ClearForm();
        pageObject.setTextNdsInputPostcode("RG11ET");
    }

    @Given("^I have only supplied Town$")
    public void i_have_only_supplied_Town() throws Throwable {
        pageHelper.ClearForm();
        pageObject.setTextNdsInputTown("Reading");
    }

    @Given("^I have selected Country as \"(.*?)\"$")
    public void i_have_selected_Country_as(String country) throws Throwable {
        pageHelper.getAddressCountryElement().selectByValue("GB");
    }

    @Given("^I have supplied Postcode not in UK format$")
    public void i_have_supplied_Postcode_not_in_UK_format() throws Throwable {
        pageObject.setTextNdsInputPostcode("XXX");
    }

    @Given("^I have selected Country as not United Kingdom$")
    public void i_have_selected_Country_as_not_United_Kingdom() throws Throwable {
        pageHelper.getAddressCountryElement().selectByValue("AF");
    }

    @Given("^I have not supplied Postcode$")
    public void i_have_not_supplied_Postcode() throws Throwable {
        pageObject.setTextNdsInputPostcode("");
    }

    @Given("^I have supplied Postcode \"(.*?)\"$")
    public void i_have_supplied_Postcode(String postcode) throws Throwable {
        pageObject.setTextNdsInputPostcode(postcode);
    }
    
    @Given("^I have supplied other details$")
    public void i_have_supplied_other_details() throws Throwable {
        pageObject.setTextNdsInputLine0("Flat 1, Projection West");
        pageObject.setTextNdsInputTown("READING");
    }

}
