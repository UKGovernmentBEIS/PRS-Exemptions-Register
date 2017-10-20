package com.northgateps.nds.beis.ui.view.nonjavascript.registersearchpenalties;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.google.common.base.Function;
import com.northgateps.nds.beis.ui.selenium.pagehelper.RegisterSearchPenaltiesPageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.FailoverLandingPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.RegisterSearchPenaltiesPageObject;
import com.northgateps.nds.beis.ui.view.javascript.base.AlternateUrlBaseSteps;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.thucydides.core.annotations.Managed;

public class RegisterSearchPenaltiesSteps extends AlternateUrlBaseSteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    RegisterSearchPenaltiesPageHelper pageHelper;
    RegisterSearchPenaltiesPageObject pageObject;
    FailoverLandingPageObject failoverLandlingPageObject;
    String parentHandle;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        testHelper.openUrl(GetUrl("register-search-penalties"));
        pageHelper = new RegisterSearchPenaltiesPageHelper(testHelper.getScenarioWebDriver());
        pageObject = pageHelper.getPageObject();

    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }

    @Given("^I am on the 'register-search-penalties' page$")
    public void i_am_on_the_register_search_penalties_page() throws Throwable {
        pageHelper = new RegisterSearchPenaltiesPageHelper(pageObject.getDriver());
        checkOnPage(pageHelper, "register-search-penalties");
    }

    @Given("^javascript is disabled$")
    public void javascript_is_disabled() throws Throwable {
        // js is disabled
    }

    @When("^the page is displayed$")
    public void the_page_is_displayed() throws Throwable {
        // page is displayed
    }

    @Then("^the I have not supplied Enter the postcode of the rental property will be displayed$")
    public void the_I_have_not_supplied_Enter_the_postcode_of_the_rental_property_will_be_displayed() throws Throwable {
        assertTrue("check postcode box is displayed",
                pageObject.getWebElementNdsInputPenaltyPostcodesCriteria().isDisplayed());
    }

    @Then("^the Landlord’s name box will be displayed$")
    public void the_Landlord_s_name_box_will_be_displayed() throws Throwable {
        assertTrue("check landlord name box is displayed",
                pageObject.getWebElementNdsInputPenaltyLandlordsNameCriteria().isDisplayed());
    }

    @Then("^the Rental property details box will be displayed$")
    public void the_Rental_property_details_box_will_be_displayed() throws Throwable {
        assertTrue("check rental property details box is displayed",
                pageObject.getWebElementNdsInputRentalPropertyDetails().isDisplayed());
    }

    @Then("^the Town box will be displayed$")
    public void the_Town_box_will_be_displayed() throws Throwable {
        assertTrue("check town box is displayed", pageObject.getWebElementNdsInputTown().isDisplayed());
    }

    @Then("^the Property type box will be displayed with a value of \"(.*?)\"$")
    public void the_Property_type_box_will_be_displayed_with_a_value_of(String message) throws Throwable {
        WebElement selectElement = pageHelper.getPropertyTypeSelectList();
        assertTrue("check property type box is displayed",
                selectElement.isDisplayed());
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    String text = new Select(
                            selectElement).getFirstSelectedOption().getText();
                    return message.equals(text);
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });
        assertEquals("First Row of the list ", message,
                new Select(selectElement).getFirstSelectedOption().getText().trim());
    }

    @Then("^a Domestic penalty type box will be displayed with a value of all \"(.*?)\" populated with domestic penalty type descriptions$")
    public void a_Domestic_penalty_type_box_will_be_displayed_with_a_value_of_all_populated_with_domestic_penalty_type_descriptions(
            String type) throws Throwable {
        WebElement selectElement = pageHelper.getDomesticPenaltyTypeSelectList();
        assertEquals("check selected text", type, new Select(selectElement).getOptions().get(0).getText().trim());
        assertEquals("check short descriptions of domestic penalty types", "Let a property in breach for less than 3 months",
                new Select(selectElement).getOptions().get(1).getText().trim());
        assertEquals("check short descriptions of domestic penalty types", "Failed to comply with a compliance notice",
                new Select(selectElement).getOptions().get(2).getText().trim());
        assertEquals("check short descriptions of domestic penalty types", "Registered false or misleading information",
                new Select(selectElement).getOptions().get(3).getText().trim());
        assertEquals("check short descriptions of domestic penalty types", "Let a property in breach for more than 3 months",
                new Select(selectElement).getOptions().get(4).getText().trim());
    }

    @Then("^a Non-domestic penalty type box will be displayed with a value of all \"(.*?)\" populated with non-domestic penalty type descriptions$")
    public void a_Non_domestic_penalty_type_box_will_be_displayed_with_a_value_of_all_populated_with_non_domestic_penalty_type_descriptions(
            String type) throws Throwable {
        WebElement selectElement = pageHelper.getNonDomesticPenaltyTypeSelectList();
        assertEquals("check selected text", type, new Select(selectElement).getOptions().get(0).getText().trim());
        assertEquals("check short descriptions of non-domestic penalty types", "Let a property in breach for less than 3 months",
                new Select(selectElement).getOptions().get(1).getText().trim());
        assertEquals("check short descriptions of non-domestic penalty types", "Failed to comply with a compliance notice",
                new Select(selectElement).getOptions().get(2).getText().trim());
        assertEquals("check short descriptions of non-domestic penalty types", "Registered false or misleading information",
                new Select(selectElement).getOptions().get(3).getText().trim());
        assertEquals("check short descriptions of non-domestic penalty types", "Let a property in breach for more than 3 months",
                new Select(selectElement).getOptions().get(4).getText().trim());
    }

    @Then("^I will remain on the 'register-search-penalties' page$")
    public void i_will_remain_on_the_register_search_penalties_page() throws Throwable {
        checkOnPage(pageHelper, "register-search-penalties");
    }

    @Given("^I have not supplied Enter the postcode of the rental property$")
    public void i_have_not_supplied_Enter_the_postcode_of_the_rental_property() throws Throwable {
        pageObject.setTextNdsInputPenaltyPostcodesCriteria("");
    }

    @Given("^I have not supplied Landlord’s name$")
    public void i_have_not_supplied_Landlord_s_name() throws Throwable {
        pageObject.setTextNdsInputPenaltyLandlordsNameCriteria("");
    }

    @Given("^I have not supplied Rental property details$")
    public void i_have_not_supplied_Rental_property_details() throws Throwable {
        pageObject.setTextNdsInputRentalPropertyDetails("");
    }

    @Given("^I have not supplied Town$")
    public void i_have_not_supplied_Town() throws Throwable {
        pageObject.setTextNdsInputTown("");
    }

    @Given("^I have supplied \"(.*?)\" as Property type$")
    public void i_have_supplied_as_Property_type(String selectValue) throws Throwable {
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    new Select(pageHelper.getPropertyTypeSelectList())
                            .selectByVisibleText(selectValue);
                    return true;
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });
    }

    @Given("^I have supplied \"(.*?)\" as Domestic penalty type$")
    public void i_have_supplied_as_Domestic_penalty_type(String type) throws Throwable {
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    new Select(pageHelper.getDomesticPenaltyTypeSelectList())
                            .selectByVisibleText(type);
                    return true;
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });
    }

    @Given("^I have supplied \"(.*?)\" as Non-domestic penalty type$")
    public void i_have_supplied_as_Non_domestic_penalty_type(String type) throws Throwable {
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    new Select(pageHelper.getNonDomesticPenaltyTypeSelectList())
                            .selectByVisibleText(type);
                    return true;
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });
    }

    @When("^I select 'Find penalty notices'$")
    public void i_select_Find_penalty_notices() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.clickButtonNext_FindPenalties();
    }

    @Then("^I will receive the validation message \"(.*?)\"$")
    public void i_will_receive_the_validation_message(String validationMessage) throws Throwable {
        assertEquals("check valdiation message", validationMessage, pageHelper.findFaultMessage(validationMessage));
    }

    @Given("^I have not supplied \"(.*?)\" as Property type$")
    public void i_have_not_supplied_as_Property_type(String arg1) throws Throwable {
        String selectValue = "Domestic";
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    new Select(pageHelper.getPropertyTypeSelectList())
                            .selectByVisibleText(selectValue);
                    return true;
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });
    }

    @Given("^I have supplied a Non-domestic penalty type$")
    public void i_have_supplied_a_Non_domestic_penalty_type() throws Throwable {
        String penaltyType = "Let a property in breach for less than 3 months";
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    new Select(pageHelper.getNonDomesticPenaltyTypeSelectList())
                            .selectByVisibleText(penaltyType);
                    return true;
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });
    }

    @Given("^I have supplied a Domestic penalty type$")
    public void i_have_supplied_a_Domestic_penalty_type() throws Throwable {
        String penaltyType = "Failed to comply with a compliance notice";
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    new Select(pageHelper.getDomesticPenaltyTypeSelectList())
                            .selectByVisibleText(penaltyType);
                    return true;
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });
    }

    @Given("^I have supplied valid search criteria$")
    public void i_have_supplied_valid_search_criteria() throws Throwable {
        pageObject.setTextNdsInputPenaltyLandlordsNameCriteria("John Smith");
    }

    @Then("^the Domestic penalty type will be ignored in the search$")
    public void the_Domestic_penalty_type_will_be_ignored_in_the_search() throws Throwable {
        // penalty type is ignored in search
    }

    @Then("^the Non-domestic penalty type will be ignored in the search$")
    public void the_Non_domestic_penalty_type_will_be_ignored_in_the_search() throws Throwable {
        // penalty type is ignored in search
    }

    @Given("^no penalty notices exist for the details supplied$")
    public void no_penalty_notices_exist_for_the_details_supplied() throws Throwable {
        pageObject.setTextNdsInputPenaltyPostcodesCriteria("xl38da");
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String message) throws Throwable {
        assertEquals("check message", message, pageObject.getTextPCustomlede());
    }

    @Given("^penalty notices exist$")
    public void penalty_notices_exist() throws Throwable {
        pageObject.setTextNdsInputPenaltyPostcodesCriteria("");
    }

    @Given("^the property addresses can be published$")
    public void the_property_addresses_can_be_published() throws Throwable {
        // address is published
    }

    @Then("^the property address will be displayed for each penalty notice$")
    public void the_property_address_will_be_displayed_for_each_penalty_notice() throws Throwable {
        String address = "1 Acacia Avenue, READING, Berkshire, RG1 1PB";
        List<WebElement> addressCells = pageObject.getDriver().findElements(By.className("dgstandard"));
        assertTrue("check address", addressCells.get(0).getText().trim().contains(address));
    }
    
    @Given("^the property addresses cannot be published$")
    public void the_property_addresses_cannot_be_published() throws Throwable {
       //address not published
    }

    @Then("^the message \"(.*?)\" will be displayed for each penalty$")
    public void the_message_will_be_displayed_for_each_penalty(String arg1) throws Throwable {
        String address = "Address not published";
        List<WebElement> addressCells = pageObject.getDriver().findElements(By.className("dgstandard"));
        assertTrue("check address", addressCells.get(0).getText().trim().contains(address));
    }


}
