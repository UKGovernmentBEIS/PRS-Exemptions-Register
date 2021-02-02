package com.northgateps.nds.beis.ui.view.javascript.registersearchexemptions;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.google.common.base.Function;
import com.northgateps.nds.beis.ui.selenium.pagehelper.RegisterExemptionsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.RegisterSearchExemptionsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.RegisterExemptionsPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.RegisterSearchExemptionsPageObject;
import com.northgateps.nds.beis.ui.view.javascript.base.AlternateUrlBaseSteps;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.NdsUiWait;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class RegisterSearchExemptionsSteps extends AlternateUrlBaseSteps{

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    RegisterSearchExemptionsPageHelper pageHelper;
    RegisterSearchExemptionsPageObject pageObject;
    RegisterExemptionsPageHelper registerExemptionsPageHelper;
    RegisterExemptionsPageObject registerExemptionsPageObject;
    String parentHandle;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        testHelper.openUrl(GetUrl("register-search-exemptions"));
        pageHelper = new RegisterSearchExemptionsPageHelper(testHelper.getScenarioWebDriver());
        pageObject = pageHelper.getPageObject();

    }
    
    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }
    
    @Given("^I am on the 'register-search-exemptions' page$")
    public void i_am_on_the_register_search_exemptions_page() throws Throwable {
        pageHelper = new RegisterSearchExemptionsPageHelper(pageObject.getDriver());
        checkOnPage(pageHelper, "register-search-exemptions");
        pageObject=pageHelper.getPageObject();
    }
    
    @When("^I select 'minimum standard of energy efficiency'$")
    public void i_select_minimum_standard_of_energy_efficiency() throws Throwable {
        parentHandle = webDriver.getWindowHandle();
        pageObject.clickAnchorEfficiency();
    }

    @Then("^I will be taken to the 'Minimum standard of energy efficiency information' page$")
    public void i_will_be_taken_to_the_Minimum_standard_of_energy_efficiency_information_page() throws Throwable {
        for (String winHandle : webDriver.getWindowHandles()) {
            webDriver.switchTo().window(winHandle); // switch focus of WebDriver to the next found window handle (that's
                                                    // your newly opened window)
        }
        String guidancePage = "https://www.gov.uk/guidance/domestic-private-rented-property-minimum-energy-efficiency-standard-landlord-guidance";
        assertEquals("Checking the current url", guidancePage, webDriver.getCurrentUrl());
        webDriver.close(); // close newly opened window when done with it
        webDriver.switchTo().window(parentHandle);
    }
    
    @When("^I select 'Other ways to search for exemptions'$")
    public void i_select_Other_ways_to_search_for_exemptions() throws Throwable {
        pageObject.clickSummaryExemptions();
    }

    @Then("^the Landlord’s name box will be displayed$")
    public void the_Landlord_s_name_box_will_be_displayed() throws Throwable {
        assertTrue("check landlord's name box is displayed",
                pageObject.getWebElementNdsInputExemptionLandlordsNameCriteria().isDisplayed());
    }

    @Then("^the Exempt property details box will be displayed$")
    public void the_Exempt_property_details_box_will_be_displayed() throws Throwable {
        assertTrue("check property details box is displayed",
                pageObject.getWebElementNdsInputExemptPropertyDetails().isDisplayed());
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

    @Then("^I will remain on the 'register-search-exemptions' page$")
    public void i_will_remain_on_the_register_search_exemptions_page() throws Throwable {
        checkOnPage(pageHelper, "register-search-exemptions");
    }

    @When("^I select 'Non-domestic' as a value for Property type$")
    public void i_select_Non_domestic_as_a_value_for_Property_type() throws Throwable {
        String selectValue = "Non-domestic";
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

    @Then("^the Exemption type box will be populated with the short descriptions of non-domestic exemption types$")
    public void the_Exemption_type_box_will_be_populated_with_the_short_descriptions_of_non_domestic_exemption_types() throws Throwable {
        WebElement selectElement = pageHelper.getNonDomesticExemptionTypeSelectList();
        assertEquals("check short descriptions","Wall insulation would have a negative impact",
                new Select(selectElement).getOptions().get(1).getText().trim());
        assertEquals("check short descriptions","All relevant improvements have been made",
                new Select(selectElement).getOptions().get(2).getText().trim());
        assertEquals("check short descriptions","No improvements can be made",
                new Select(selectElement).getOptions().get(3).getText().trim());
        assertEquals("check short descriptions","7 year payback rule not met",
                new Select(selectElement).getOptions().get(4).getText().trim());
        assertEquals("check short descriptions","Consent denied or subject to unreasonable conditions",
                new Select(selectElement).getOptions().get(5).getText().trim());
        assertEquals("check short descriptions","Devaluation of more than 5%",
                new Select(selectElement).getOptions().get(6).getText().trim());
        assertEquals("check short descriptions","New landlord under qualifying circumstances",
                new Select(selectElement).getOptions().get(7).getText().trim());
    }
    
    @When("^I select 'Domestic' as a value for Property type$")
    public void i_select_Domestic_as_a_value_for_Property_type() throws Throwable {
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

    @Then("^the Exemption type box will be populated with the short descriptions of domestic exemption types$")
    public void the_Exemption_type_box_will_be_populated_with_the_short_descriptions_of_domestic_exemption_types() throws Throwable {
        WebElement selectElement = pageHelper.getDomesticExemptionTypeSelectList();
        assertEquals("check short descriptions","Devaluation of more than 5%",
                new Select(selectElement).getOptions().get(1).getText().trim());
        assertEquals("check short descriptions","Cost to landlord exceeds cap",
                new Select(selectElement).getOptions().get(2).getText().trim());
        assertEquals("check short descriptions","All relevant improvements have been made",
                new Select(selectElement).getOptions().get(3).getText().trim());
        assertEquals("check short descriptions","New landlord under qualifying circumstances",
                new Select(selectElement).getOptions().get(4).getText().trim());
        assertEquals("check short descriptions","Consent denied or subject to unreasonable conditions",
                new Select(selectElement).getOptions().get(5).getText().trim());
        assertEquals("check short descriptions","Wall insulation would have a negative impact",
                new Select(selectElement).getOptions().get(6).getText().trim());
    }
    
    @Then("^the Non-Domestic Exemption type box will be displayed with a value of \"(.*?)\"$")
    public void the_Non_Domestic_Exemption_type_box_will_be_displayed_with_a_value_of(String message) throws Throwable {
        WebElement selectElement = pageHelper.getNonDomesticExemptionTypeSelectList();
        assertTrue("check exemption type box is displayed",
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

    @Then("^the Domestic Exemption type box will be displayed with a value of \"(.*?)\"$")
    public void the_Domestic_Exemption_type_box_will_be_displayed_with_a_value_of(String message) throws Throwable {
        WebElement selectElement = pageHelper.getDomesticExemptionTypeSelectList();
        assertTrue("check exemption type box is displayed",
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

    @When("^I select 'All properties' as a value for Property type$")
    public void i_select_All_properties_as_a_value_for_Property_type() throws Throwable {
        String selectValue = "All properties";
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

    @Then("^the Exemption type box will be hidden$")
    public void the_Exemption_type_box_will_be_hidden() throws Throwable {
        assertTrue("check exemption type box for domestic is hidden",
                pageHelper.getDomesticExemptionTypeSelectList().isDisplayed()==false);
        assertTrue("check exemption type box for non-domestic is hidden",
                pageHelper.getNonDomesticExemptionTypeSelectList().isDisplayed()==false);
    }


    @Then("^I have not supplied Enter the postcode of the rental property$")
    public void i_have_not_supplied_Enter_the_postcode_of_the_rental_property() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.setTextNdsInputExemptionPostcodeCriteria("");
    }

    @Then("^I have not supplied Landlord’s name$")
    public void i_have_not_supplied_Landlord_s_name() throws Throwable {
        pageObject.setTextNdsInputExemptionLandlordsNameCriteria("");
    }

    @Then("^I have not supplied Exempt property details$")
    public void i_have_not_supplied_Exempt_property_details() throws Throwable {
        pageObject.setTextNdsInputExemptPropertyDetails("");
    }

    @Then("^I have not supplied Town$")
    public void i_have_not_supplied_Town() throws Throwable {
      pageObject.setTextNdsInputTown("");
    }

    @Then("^I have supplied 'All properties' as Property type$")
    public void i_have_supplied_All_properties_as_Property_type() throws Throwable {
        String selectValue = "All properties";
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

    @When("^I select 'Find exemptions'$")
    public void i_select_Find_exemptions() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.clickButtonNext_FindExemptions();
    }

    @Then("^I will receive the validation message \"(.*?)\"$")
    public void i_will_receive_the_validation_message(String validationMessage) throws Throwable {
       assertEquals("check validation message",validationMessage,pageHelper.findFaultMessage(validationMessage));
    }
    
    @When("^I have supplied 'Non-domestic' as Property type$")
    public void i_have_supplied_Non_domestic_as_Property_type() throws Throwable {
        String selectValue = "Non-domestic";
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

    @When("^I have supplied 'All types' as Exemption type$")
    public void i_have_supplied_All_types_as_Exemption_type() throws Throwable {
        String selectValue = "All types";
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    new Select(pageHelper.getNonDomesticExemptionTypeSelectList())
                            .selectByVisibleText(selectValue);
                    return true;
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });
    }


    @When("^I have supplied 'Domestic' as Property type$")
    public void i_have_supplied_Domestic_as_Property_type() throws Throwable {
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

    @When("^I have supplied 'All types' as Domestic Exemption type$")
    public void i_have_supplied_All_types_as_Domestic_Exemption_type() throws Throwable {
        String selectValue = "All types";
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    new Select(pageHelper.getDomesticExemptionTypeSelectList())
                            .selectByVisibleText(selectValue);
                    return true;
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });
    }
    
    @When("^I have entered a wild card in Landlord’s name and entered less than (\\d+) other characters in Landlord’s name$")
    public void i_have_entered_a_wild_card_in_Landlord_s_name_and_entered_less_than_other_characters_in_Landlord_s_name(int arg1) throws Throwable {
        BasePageHelper.waitUntilPageLoading(webDriver);
        pageObject = pageHelper.getNewPageObject();
        pageObject.setTextNdsInputExemptionLandlordsNameCriteria("*T*");
    }

    @When("^I have entered a wild card in Exempt property details and entered less than (\\d+) other characters in Exempt property details$")
    public void i_have_entered_a_wild_card_in_Exempt_property_details_and_entered_less_than_other_characters_in_Exempt_property_details(int arg1) throws Throwable {
        pageObject.setTextNdsInputExemptPropertyDetails("*B*");
    }

    @When("^I have entered a wild card in Town and entered less than (\\d+) other characters in Town$")
    public void i_have_entered_a_wild_card_in_Town_and_entered_less_than_other_characters_in_Town(int arg1) throws Throwable {
        pageObject.setTextNdsInputTown("*B*");
    }

    @Then("^I will receive the validation message \"(.*?)\" above the Landlord's name field$")
    public void i_will_receive_the_validation_message_above_the_Landlord_s_name_field(String message) throws Throwable {
        assertEquals("check message above field",message,pageObject.getTextNdsInvalidExemptionLandlordsNameCriteria());
    }

    @Then("^I will receive the validation message \"(.*?)\" above the Exempt property details field$")
    public void i_will_receive_the_validation_message_above_the_Exempt_property_details_field(String message) throws Throwable {
        assertEquals("check message above field",message,pageObject.getTextNdsInvalidExemptPropertyDetails());
    }

    @Then("^I will receive the validation message \"(.*?)\" above the Town field$")
    public void i_will_receive_the_validation_message_above_the_Town_field(String message) throws Throwable {
        assertEquals("check message above field",message,pageObject.getTextNdsInvalidTown());
    }
    
    @When("^I have not entered a wild card in Landlord’s name and entered less than (\\d+) characters in Landlord’s name$")
    public void i_have_not_entered_a_wild_card_in_Landlord_s_name_and_entered_less_than_characters_in_Landlord_s_name(int arg1) throws Throwable {
        pageObject = pageHelper.getNewPageObject();
      pageObject.setTextNdsInputExemptionLandlordsNameCriteria("T");
    }

    @When("^I have not entered a wild card in Exempt property details and entered less than (\\d+) characters in Exempt property details$")
    public void i_have_not_entered_a_wild_card_in_Exempt_property_details_and_entered_less_than_characters_in_Exempt_property_details(int arg1) throws Throwable {
        pageObject.setTextNdsInputExemptPropertyDetails("B");
    }

    @When("^I have not entered a wild card in Town and entered less than (\\d+) characters in Town$")
    public void i_have_not_entered_a_wild_card_in_Town_and_entered_less_than_characters_in_Town(int arg1) throws Throwable {
       pageObject.setTextNdsInputTown("B");
    }

    @Then("^I have entered less than (\\d+) characters in Enter the postcode of the rental property$")
    public void i_have_entered_less_than_characters_in_Enter_the_postcode_of_the_rental_property(int arg1) throws Throwable {
        pageObject.setTextNdsInputExemptionPostcodeCriteria("R");
    }

    @Then("^I will receive the validation message \"(.*?)\" above the Enter the postcode of the rental property field$")
    public void i_will_receive_the_validation_message_above_the_Enter_the_postcode_of_the_rental_property_field(String message) throws Throwable {
       assertEquals("check message above field",message,pageObject.getTextNdsInvalidExemptionPostcodeCriteria());
    }
    
    @Then("^I have supplied valid search criteria$")
    public void i_have_supplied_valid_search_criteria() throws Throwable {
        pageHelper.ClearForm();
        
        
    }

    @Then("^no exemptions exist for the details supplied$")
    public void no_exemptions_exist_for_the_details_supplied() throws Throwable {
       //no exemptions exist
        pageObject.setTextNdsInputExemptionPostcodeCriteria("xl3 8da");
        pageObject.setTextNdsInputExemptionLandlordsNameCriteria("LIZ DOM ORG1");
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String message) throws Throwable {
        assertEquals("check message",message,pageObject.getTextPCustomlede());
    }

    @Then("^exemptions exist$")
    public void exemptions_exist() throws Throwable {
        pageHelper.ClearForm();
        pageObject.setTextNdsInputExemptionPostcodeCriteria("RG12 7BW");
        pageObject.setTextNdsInputExemptionLandlordsNameCriteria("TestOrg");
    }

    @Then("^the property address will be displayed for each exemption$")
    public void the_property_address_will_be_displayed_for_each_exemption() throws Throwable {
        String address1 = "Unit 12/13, Bracknell Beeches, Bracknell, Berkshire, RG12 7BW";
        String address2 = "Unit 6, Bracknell Beeches, Bracknell, Berkshire, RG12 7BW";
        String address3 = "Unit 1, Bracknell Beeches, Bracknell, Berkshire, RG12 7BW";
        List<WebElement> addressCells = pageObject.getDriver().findElements(By.className("dgstandard"));
        assertTrue("check address",addressCells.get(0).getText().trim().contains(address1));
        assertTrue("check address",addressCells.get(0).getText().trim().contains(address2));
        assertTrue("check address",addressCells.get(0).getText().trim().contains(address3));
       
    }

    @Then("^the organisation name will be displayed for each exemption$")
    public void the_organisation_name_will_be_displayed_for_each_exemption() throws Throwable {
        String landlordname = "TestOrg";
        List<WebElement> addressCells = pageObject.getDriver().findElements(By.className("dgstandard"));
        assertTrue("check landlordname is displayed",addressCells.get(0).getText().trim().contains(landlordname));
    }
    
    @When("^I select 'Other ways to search for exemptions' again$")
    public void i_select_Other_ways_to_search_for_exemptions_again() throws Throwable {
    	((JavascriptExecutor)pageObject.getDriver()).executeScript("arguments[0].scrollIntoView(true);", pageObject.getWebElementSummaryExemptions());
        new NdsUiWait(pageObject.getDriver()).untilElementClickedOK(pageObject.getBySummaryExemptions(), pageObject.getDriver());
        if(pageObject.getWebElementNdsInputExemptionLandlordsNameCriteria().isDisplayed())
        {
        	pageObject.clickSummaryExemptions();
        }       
    }

    @Then("^the Landlord’s name box will be hidden$")
    public void the_Landlord_s_name_box_will_be_hidden() throws Throwable {
        assertTrue("check landlord name box is hidden",
                pageObject.getWebElementNdsInputExemptionLandlordsNameCriteria().isDisplayed()==false);
    }

    @Then("^the Exempt property details box will be hidden$")
    public void the_Exempt_property_details_box_will_be_hidden() throws Throwable {
        assertTrue("check Exempt property details box is hidden",
                pageObject.getWebElementNdsInputExemptPropertyDetails().isDisplayed()==false);
    }

    @Then("^the Town box will be hidden$")
    public void the_Town_box_will_be_hidden() throws Throwable {
        assertTrue("check town box is hidden",
                pageObject.getWebElementNdsInputTown().isDisplayed()==false);
    }

    @Then("^the Property type box will be hidden$")
    public void the_Property_type_box_will_be_hidden() throws Throwable {
        assertTrue("check property type box is hidden",
                pageHelper.getPropertyTypeSelectList().isDisplayed());
    }
    
    @When("^I select Finish$")
    public void i_select_Finish() throws Throwable {
        pageObject.clickAnchorFinish();
    }

    @Then("^I will be taken to the finish page$")
    public void i_will_be_taken_to_the_finish_page() throws Throwable {
        checkOnPage(pageHelper,"failover-landing");
    }
   
}
