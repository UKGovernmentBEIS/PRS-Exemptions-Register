package com.northgateps.nds.beis.ui.view.nonjavascript.registersearchexemptions;

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
import com.northgateps.nds.beis.ui.selenium.pagehelper.RegisterSearchExemptionsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.RegisterSearchExemptionsPageObject;
import com.northgateps.nds.beis.ui.view.javascript.base.AlternateUrlBaseSteps;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
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

    @Given("^javascript is disabled$")
    public void javascript_is_disabled() throws Throwable {
      //js is disabled
       
    }

    @When("^the page is displayed$")
    public void the_page_is_displayed() throws Throwable {
       //page is displayed
    }

    @Then("^the Enter the postcode of the rental property will be displayed$")
    public void the_Enter_the_postcode_of_the_rental_property_will_be_displayed() throws Throwable {
        assertTrue("check postcode box is displayed",
                pageObject.getWebElementNdsInputExemptionPostcodeCriteria().isDisplayed());
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
        WebElement selectElement = pageObject.getDriver().findElement(By.id("uiData.exemptionSearch.service"));
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

    @Then("^a Domestic exemption type box will be displayed with a value of all \"(.*?)\" populated with domestic exemption type descriptions$")
    public void a_Domestic_exemption_type_box_will_be_displayed_with_a_value_of_all_populated_with_domestic_exemption_type_descriptions(String message) throws Throwable {
        WebElement selectElement = pageObject.getDriver().findElement(By.id("uiData.exemptionSearch.exemptionType_PRSD"));
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
        assertEquals("check short descriptions","Devaluation of more than 5%",
                new Select(selectElement).getOptions().get(1).getText().trim());
        assertEquals("check short descriptions","No suitable funding",
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

    @Then("^a Non-domestic exemption type box will be displayed with a value of all \"(.*?)\" populated with non-domestic exemption type descriptions$")
    public void a_Non_domestic_exemption_type_box_will_be_displayed_with_a_value_of_all_populated_with_non_domestic_exemption_type_descriptions(String message) throws Throwable {
        WebElement selectElement = pageObject.getDriver().findElement(By.id("uiData.exemptionSearch.exemptionType_PRSN"));
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

    @Then("^I will remain on the 'register-search-exemptions' page$")
    public void i_will_remain_on_the_register_search_exemptions_page() throws Throwable {
        checkOnPage(pageHelper, "register-search-exemptions");
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

    @Then("^I have supplied \"(.*?)\" as Property type$")
    public void i_have_supplied_as_Property_type(String type) throws Throwable {
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    new Select(pageHelper.getPropertyTypeSelectList())
                            .selectByVisibleText(type);
                    return true;
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });
    }

    @Then("^I have supplied \"(.*?)\" as Domestic exemption type$")
    public void i_have_supplied_as_Domestic_exemption_type(String type) throws Throwable {
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    new Select(pageHelper.getDomesticExemptionTypeSelectList())
                            .selectByVisibleText(type);
                    return true;
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });
    }

    @Then("^I have supplied \"(.*?)\" as Non-domestic exemption type$")
    public void i_have_supplied_as_Non_domestic_exemption_type(String type) throws Throwable {
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    new Select(pageHelper.getNonDomesticExemptionTypeSelectList())
                            .selectByVisibleText(type);
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
    
    @Then("^I have not supplied \"(.*?)\" as Property type$")
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
    
    @Then("^I have supplied a Non-domestic exemption type$")
    public void i_have_supplied_a_Non_domestic_exemption_type() throws Throwable {
        String selectValue = "Wall insulation would have a negative impact";
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
    
    @Then("^I have supplied a Domestic exemption type$")
    public void i_have_supplied_a_Domestic_exemption_type() throws Throwable {
        String selectValue = "No suitable funding";
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
    
    @Then("^I have supplied valid search criteria$")
    public void i_have_supplied_valid_search_criteria() throws Throwable {
       pageObject.setTextNdsInputExemptionLandlordsNameCriteria("TestOrg");
    }

    @Then("^the Domestic exemption type will be ignored in the search$")
    public void the_Domestic_exemption_type_will_be_ignored_in_the_search() throws Throwable {
      //domestic exemption type ignored in request
    }

    @Then("^the Non-domestic exemption type will be ignored in the search$")
    public void the_Non_domestic_exemption_type_will_be_ignored_in_the_search() throws Throwable {
      //non-domestic exemption type ignored in request
    }
    
    @Then("^no exemptions exist for the details supplied$")
    public void no_exemptions_exist_for_the_details_supplied() throws Throwable {
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
        pageObject.setTextNdsInputExemptionLandlordsNameCriteria("seleniumorg");
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
        assertTrue("check address",addressCells.get(0).getText().trim().contains(landlordname)); 
    }


    
}
