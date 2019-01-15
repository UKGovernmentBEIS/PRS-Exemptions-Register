package com.northgateps.nds.beis.ui.view.javascript.registersearchpenalties;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

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
import net.thucydides.core.annotations.Managed;

public class RegisterSearchPenaltiesSteps extends AlternateUrlBaseSteps{

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

    @When("^the page is first displayed$")
    public void the_page_is_first_displayed() throws Throwable {
      //page is displayed
    }

    @Then("^I will only have the option to search by postcode$")
    public void i_will_only_have_the_option_to_search_by_postcode() throws Throwable {
       assertTrue("check postcode box is displayed",
               pageObject.getWebElementNdsInputPenaltyPostcodesCriteria().isDisplayed());
       assertTrue("check landlord name box is hidden",
               pageObject.getWebElementNdsInputPenaltyLandlordsNameCriteria().isDisplayed()==false);
       assertTrue("check rental property details box is hidden",
               pageObject.getWebElementNdsInputRentalPropertyDetails().isDisplayed()==false);
       assertTrue("check town box is hidden",
               pageObject.getWebElementNdsInputTown().isDisplayed()==false);
       assertTrue("check property type dropdown is hidden",
               pageHelper.getPropertyTypeSelectList().isDisplayed()==false);
       assertTrue("check domestic penalty type drop down is hidden",
               pageHelper.getDomesticPenaltyTypeSelectList().isDisplayed()==false);
       assertTrue("check non-domestic penalty type drop down is hidden",
               pageHelper.getNonDomesticPenaltyTypeSelectList().isDisplayed()==false);
    }

    @Then("^I will remain on the 'register-search-penalties' page$")
    public void i_will_remain_on_the_register_search_penalties_page() throws Throwable {
        checkOnPage(pageHelper, "register-search-penalties");
    }
    
    @When("^I select Finish$")
    public void i_select_Finish() throws Throwable {
        pageObject.clickAnchorFinish();
    }

    @Then("^I will be taken to the finish page$")
    public void i_will_be_taken_to_the_finish_page() throws Throwable {
        checkOnPage(pageHelper,"failover-landing");
    }
    
    @When("^I select 'What is a penalty notice'$")
    public void i_select_What_is_a_penalty_notice() throws Throwable {
      pageObject.clickSummaryNotice();
      parentHandle = webDriver.getWindowHandle();
      pageObject.clickAnchorUrl();
    }

    @Then("^I will be taken to the 'What is a penalty notice information page'$")
    public void i_will_be_taken_to_the_What_is_a_penalty_notice_information_page() throws Throwable {
        for (String winHandle : webDriver.getWindowHandles()) {
            webDriver.switchTo().window(winHandle); // switch focus of WebDriver to the next found window handle (that's
                                                    // your newly opened window)
        }
        assertEquals("Checking the current url", "https://www.gov.uk/government/publications/the-private-rented-property-minimum-standard-landlord-guidance-documents", webDriver.getCurrentUrl());
        webDriver.close(); // close newly opened window when done with it
        webDriver.switchTo().window(parentHandle);
    }
    
    @When("^I select 'Other ways to search for penalties'$")
    public void i_select_Other_ways_to_search_for_penalties() throws Throwable {
        pageObject.clickSummaryPenalties();
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
        assertTrue("check town box is displayed",
                pageObject.getWebElementNdsInputTown().isDisplayed());
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
    
    @When("^I select \"(.*?)\" as a value for Property type$")
    public void i_select_as_a_value_for_Property_type(String selectValue) throws Throwable {
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

    @Then("^the Non-domestic Penalty type box will be displayed with a value of \"(.*?)\"$")
    public void the_Non_domestic_Penalty_type_box_will_be_displayed_with_a_value_of(String type) throws Throwable {
        WebElement selectElement = pageHelper.getNonDomesticPenaltyTypeSelectList();
        assertEquals("check selected text",type,
                new Select(selectElement).getOptions().get(0).getText().trim());
    }

    @Then("^the Domestic Penalty type box will be displayed with a value of \"(.*?)\"$")
    public void the_Domestic_Penalty_type_box_will_be_displayed_with_a_value_of(String type) throws Throwable {
        WebElement selectElement = pageHelper.getDomesticPenaltyTypeSelectList();
        assertEquals("check selected text",type,
                new Select(selectElement).getOptions().get(0).getText().trim());
    }

    @Then("^the Penalty type box will be populated with the short descriptions of non-domestic penalty types$")
    public void the_Penalty_type_box_will_be_populated_with_the_short_descriptions_of_non_domestic_penalty_types() throws Throwable {
        WebElement selectElement = pageHelper.getNonDomesticPenaltyTypeSelectList();
        assertEquals("check short descriptions of non-domestic penalty type","Let a property in breach for less than 3 months",
                new Select(selectElement).getOptions().get(1).getText().trim());
        assertEquals("check short descriptions of non-domestic penalty type","Failed to comply with a compliance notice",
                new Select(selectElement).getOptions().get(2).getText().trim());
        assertEquals("check short descriptions of non-domestic penalty type","Registered false or misleading information",
                new Select(selectElement).getOptions().get(3).getText().trim());
        assertEquals("check short descriptions of non-domestic penalty type","Let a property in breach for more than 3 months",
                new Select(selectElement).getOptions().get(4).getText().trim());
    }


    @Then("^the Penalty type box will be populated with the short descriptions of domestic penalty types$")
    public void the_Penalty_type_box_will_be_populated_with_the_short_descriptions_of_domestic_penalty_types() throws Throwable {
        WebElement selectElement = pageHelper.getDomesticPenaltyTypeSelectList();
        assertEquals("check short descriptions of domestic penalty types","Let a property in breach for less than 3 months",
                new Select(selectElement).getOptions().get(1).getText().trim());
        assertEquals("check short descriptions of domestic penalty types","Failed to comply with a compliance notice",
                new Select(selectElement).getOptions().get(2).getText().trim());
        assertEquals("check short descriptions of domestic penalty types","Registered false or misleading information",
                new Select(selectElement).getOptions().get(3).getText().trim());
        assertEquals("check short descriptions of domestic penalty types","Let a property in breach for more than 3 months",
                new Select(selectElement).getOptions().get(4).getText().trim());
    }

    @Then("^the Penalty type box will be hidden$")
    public void the_Penalty_type_box_will_be_hidden() throws Throwable {
        assertTrue("check domestic penalty type drop down is hidden",
                pageHelper.getDomesticPenaltyTypeSelectList().isDisplayed()==false);
        assertTrue("check non-domestic penalty type drop down is hidden",
                pageHelper.getNonDomesticPenaltyTypeSelectList().isDisplayed()==false);
    }
    
    @Given("^I have not supplied Enter the postcode of the rental property$")
    public void i_have_not_supplied_Enter_the_postcode_of_the_rental_property() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
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

    @When("^I select 'Find penalty notices'$")
    public void i_select_Find_penalty_notices() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.clickButtonNext_FindPenalties();
    }

    @Then("^I will receive the validation message \"(.*?)\"$")
    public void i_will_receive_the_validation_message(String validationMessage) throws Throwable {
        assertEquals("check validation message",validationMessage,pageHelper.findFaultMessage(validationMessage));
    }
    
    @When("^I have supplied \"(.*?)\" as Non-domestic Penalty type$")
    public void i_have_supplied_as_Non_domestic_Penalty_type(String type) throws Throwable {
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


    @When("^I have supplied \"(.*?)\" as Domestic Penalty type$")
    public void i_have_supplied_as_Domestic_Penalty_type(String type) throws Throwable {
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
    
    @Then("^I have entered a wild card in Landlord’s name and entered less than (\\d+) other characters in Landlord’s name$")
    public void i_have_entered_a_wild_card_in_Landlord_s_name_and_entered_less_than_other_characters_in_Landlord_s_name(int arg1) throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.setTextNdsInputPenaltyLandlordsNameCriteria("*Y*");
    }

    @Then("^I have entered a wild card in rental property details and entered less than (\\d+) other characters in rental property details$")
    public void i_have_entered_a_wild_card_in_rental_property_details_and_entered_less_than_other_characters_in_rental_property_details(int arg1) throws Throwable {
       pageObject.setTextNdsInputRentalPropertyDetails("*G*");
    }

    @Then("^I have entered a wild card in Town and entered less than (\\d+) characters in Town$")
    public void i_have_entered_a_wild_card_in_Town_and_entered_less_than_characters_in_Town(int arg1) throws Throwable {
        pageObject.setTextNdsInputTown("*Y*");
    }

    @Then("^I will receive the validation message \"(.*?)\" above the Landlord's name field$")
    public void i_will_receive_the_validation_message_above_the_Landlord_s_name_field(String message) throws Throwable {
        assertEquals("check validation message",message,pageObject.getTextNdsInvalidPenaltyLandlordsNameCriteria());
    }

    @Then("^I will receive the validation message \"(.*?)\" above the Rental property details field$")
    public void i_will_receive_the_validation_message_above_the_Rental_property_details_field(String message) throws Throwable {
        assertEquals("check validation message",message,pageObject.getTextNdsInvalidRentalPropertyDetails());
    }

    @Then("^I will receive the validation message \"(.*?)\" above the Town field$")
    public void i_will_receive_the_validation_message_above_the_Town_field(String message) throws Throwable {
        assertEquals("check validation message",message,pageObject.getTextNdsInvalidTown());
    }
    
    @Then("^I have not entered a wild card in Landlord’s name and entered less than (\\d+) other characters in Landlord’s name$")
    public void i_have_not_entered_a_wild_card_in_Landlord_s_name_and_entered_less_than_other_characters_in_Landlord_s_name(int arg1) throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.setTextNdsInputPenaltyLandlordsNameCriteria("T");
    }

    @Then("^I have not entered a wild card in rental property details and entered less than (\\d+) other characters in rental property details$")
    public void i_have_not_entered_a_wild_card_in_rental_property_details_and_entered_less_than_other_characters_in_rental_property_details(int arg1) throws Throwable {
        pageObject.setTextNdsInputRentalPropertyDetails("B");
    }

    @Then("^I have not entered a wild card in Town and entered less than (\\d+) characters in Town$")
    public void i_have_not_entered_a_wild_card_in_Town_and_entered_less_than_characters_in_Town(int arg1) throws Throwable {
        pageObject.setTextNdsInputTown("B");
    }

    @Given("^I have entered less than (\\d+) characters in Enter the postcode of the rental property$")
    public void i_have_entered_less_than_characters_in_Enter_the_postcode_of_the_rental_property(int arg1) throws Throwable {
        pageObject.setTextNdsInputPenaltyPostcodesCriteria("R");
    }

    @Then("^I will receive the validation message \"(.*?)\" above the Enter the postcode of the rental property field$")
    public void i_will_receive_the_validation_message_above_the_Enter_the_postcode_of_the_rental_property_field(String message) throws Throwable {
       assertEquals("check validation message",message,pageObject.getTextNdsInvalidPenaltyPostcodesCriteria());
    }


    @Given("^I have supplied valid search criteria$")
    public void i_have_supplied_valid_search_criteria() throws Throwable {
        pageHelper.ClearForm();
    }

    @Given("^no penalty notices exist for the details supplied$")
    public void no_penalty_notices_exist_for_the_details_supplied() throws Throwable {
        pageObject.setTextNdsInputPenaltyPostcodesCriteria("xl3 8da");
        pageObject.setTextNdsInputPenaltyLandlordsNameCriteria("LIZ DOM ORG1");
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String message) throws Throwable {
        assertEquals("check message",message,pageObject.getTextPCustomlede());
    }

    @Given("^penalty notices exist$")
    public void penalty_notices_exist() throws Throwable {
        pageHelper.ClearForm();
        pageObject.setTextNdsInputPenaltyLandlordsNameCriteria("John Smith");
    }

    @Given("^the property addresses can be published$")
    public void the_property_addresses_can_be_published() throws Throwable {
        //address can be published
    }

    @Then("^the property address will be displayed for each penalty notice$")
    public void the_property_address_will_be_displayed_for_each_penalty_notice() throws Throwable {
        String address1 = "1 Acacia Avenue, READING, Berkshire, RG1 1PB";
        List<WebElement> addressCells = pageObject.getDriver().findElements(By.className("dgstandard"));
        assertTrue("check address",addressCells.get(0).getText().trim().contains(address1));
    }

    @Given("^the property addresses cannot be published$")
    public void the_property_addresses_cannot_be_published() throws Throwable {
        pageObject.setTextNdsInputPenaltyLandlordsNameCriteria("Jane Smith");
    }

    @Then("^the message \"(.*?)\" will be displayed for each penalty$")
    public void the_message_will_be_displayed_for_each_penalty(String addressNotPublished) throws Throwable {
        List<WebElement> addressCells = pageObject.getDriver().findElements(By.className("dgstandard"));
        assertTrue("check address",addressCells.get(0).getText().trim().contains(addressNotPublished));
    }
    
    @When("^I select 'Other ways to search for penalties' again$")
    public void i_select_Other_ways_to_search_for_penalties_again() throws Throwable {
        pageObject.clickSummaryPenalties();
    }

    @Then("^the Landlord’s name box will be hidden$")
    public void the_Landlord_s_name_box_will_be_hidden() throws Throwable {
        assertTrue("check landlord name box is hidden",
                pageObject.getWebElementNdsInputPenaltyLandlordsNameCriteria().isDisplayed()==false);
    }

    @Then("^the Rental property details box will be hidden$")
    public void the_Rental_property_details_box_will_be_hidden() throws Throwable {
       assertTrue("check rental property details box is hidden",
               pageObject.getWebElementNdsInputRentalPropertyDetails().isDisplayed()==false);
    }

    @Then("^the Town box will be hidden$")
    public void the_Town_box_will_be_hidden() throws Throwable {
       assertTrue("check town box will be hidden",pageObject.getWebElementNdsInputTown().isDisplayed()==false);
    }

    @Then("^the Property type box will be hidden$")
    public void the_Property_type_box_will_be_hidden() throws Throwable {
        assertTrue("check property type box is hidden",
                pageHelper.getPropertyTypeSelectList().isDisplayed()==false);
    }
}
