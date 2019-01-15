package com.northgateps.nds.beis.ui.view.javascript.accountdetails;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.AccountDetailsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.SelectLandlordOrAgentPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.SelectLandlordTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.AccountDetailsPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.SelectLandlordOrAgentPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.SelectLandlordTypePageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.serenitybdd.core.annotations.findby.By;
import net.thucydides.core.annotations.Managed;

public class AccountDetailsSteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    UsedServiceBeforePageHelper firstPageHelper;
    AccountDetailsPageHelper pageHelper;
    AccountDetailsPageObject pageObject;
    SelectLandlordTypePageObject selectLandlordTypePageObject;
    SelectLandlordTypePageHelper selectLandlordTypePageHelper;
    SelectLandlordOrAgentPageHelper selectLandlordOrAgentPageHelper;
    SelectLandlordOrAgentPageObject selectLandlordOrAgentPageObject;   
    
    
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

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^I will be taken to the select-landlord-type page$")
    public void i_will_be_taken_to_the_select_landlord_type_page() throws Throwable {
        checkOnPage(pageHelper, "select-landlord-type");
    }

    @Then("^details previously entered will be displayed$")
    public void details_previously_entered_will_be_displayed() throws Throwable {
        selectLandlordTypePageHelper = new SelectLandlordTypePageHelper(webDriver);
        selectLandlordTypePageObject = selectLandlordTypePageHelper.getPageObject();
        assertTrue("Check if Person selected",
                selectLandlordTypePageObject.getWebElementNdsRadiobuttonAccountType_PERSON().isSelected());
    }

    @Given("^I am on the account-details to enter Person details$")
    public void i_am_on_the_account_details_to_enter_Person_details() throws Throwable {
                
        //Set a custom form filler for used-service-before page
        firstPageHelper.setFormFiller(new FormFiller() {
            public void fill(BasePageHelper<?> pageHelper) {
                ((UsedServiceBeforePageHelper) pageHelper).fillInForm(false);
            }
       });
        pageHelper = PageHelperFactory.visit(firstPageHelper, AccountDetailsPageHelper.class);
        checkOnPage(pageHelper, "account-details");
        pageObject=pageHelper.getPageObject();
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        pageObject.clickNext();        
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String validationMessage) throws Throwable {  
        pageHelper.waitUntilValidationMessageSeen(validationMessage);
    }

    @Then("^I will remain on the account-details page$")
    public void i_will_remain_on_the_account_details_page() throws Throwable {
        checkOnPage(pageHelper, "account-details");       
    }
   
    @Given("^I have supplied an invalid email address as \"(.*?)\"$")
    public void i_have_supplied_an_invalid_email_address_as(String invalidEmail) throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.setTextNdsInputEmail(invalidEmail);
    }
    
    @Given("^I have supplied an invalid confirm email address as \"(.*?)\"$")
    public void i_have_supplied_an_invalid_confirm_email_address_as(String email) throws Throwable {
        pageObject.setTextNdsInputConfirmEmail(email);
    }
    
    @Given("^I have supplied an invalid phone number that does not consist of numeric or space characters with an optional leading \\+ as \"(.*?)\"$")
    public void i_have_supplied_an_invalid_phone_number_that_does_not_consist_of_numeric_or_space_characters_with_an_optional_leading_as(String phone) throws Throwable {
        pageObject.setTextNdsInputTelNumber(phone);
    }

    @When("^I have supplied an email address as \"(.*?)\"$")
    public void i_have_supplied_an_email_address_as(String email) throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.setTextNdsInputEmail(email);
    }

    @When("^I have supplied a different confirm email address as \"(.*?)\"$")
    public void i_have_supplied_a_different_confirm_email_address_as(String email) throws Throwable {
        pageObject.setTextNdsInputConfirmEmail(email);
    }
    
    @Given("^I have supplied valid data$")
    public void i_have_supplied_valid_data() throws Throwable {
        pageHelper.fillInForm();
    }

    @Then("^I will be taken to the account-address page$")
    public void i_will_be_taken_to_the_account_address_page() throws Throwable {
        checkOnPage(pageHelper, "account-address");        
    }
    
    @When("^I click Next$")
    public void i_click_Next() throws Throwable {
        pageObject.clickNext(); 
       
    }

    @Given("^I am on the account-details to enter Organisation name$")
    public void i_am_on_the_account_details_to_enter_Organisation_name() throws Throwable {
         
        //Set a custom form filler for used-service-before page
        firstPageHelper.setFormFiller(new FormFiller() {
            public void fill(BasePageHelper<?> pageHelper) {
                ((UsedServiceBeforePageHelper) pageHelper).fillInForm(false);
            }
       });
        //Set a custom form filler for select-landlord-type page
        PageHelperFactory.registerFormFiller("select-landlord-type", new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) {
                ((SelectLandlordTypePageHelper) pageHelper).fillInForm("ORGANISATION");
                
            }
        });

        // visit target page
        try {
            pageHelper = PageHelperFactory.visit(firstPageHelper,AccountDetailsPageHelper.class);
            checkOnPage(pageHelper, "account-details");
            pageObject=pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("select-landlord-type");
        }
      }



    @Given("^I have not entered any data$")
    public void i_have_not_entered_any_data() throws Throwable {
    }
        
    @Given("^I have registered as an agent$")
    public void i_have_registered_as_an_agent() throws Throwable {
                
        //Set a custom form filler for used-service-before page
        firstPageHelper.setFormFiller(new FormFiller() {
            public void fill(BasePageHelper<?> pageHelper) {
                ((UsedServiceBeforePageHelper) pageHelper).fillInForm(false);
            }
       });

       //Set a custom form filler for select-landlord-or-agent page
       PageHelperFactory.registerFormFiller("select-landlord-or-agent", new FormFiller() {
           @Override
           public void fill(BasePageHelper<?> pageHelper) {
               ((SelectLandlordOrAgentPageHelper) pageHelper).fillInForm("AGENT");
                
           }
       });

       // visit target page
       try {
           pageHelper = PageHelperFactory.visit(firstPageHelper,AccountDetailsPageHelper.class);
           checkOnPage(pageHelper, "account-details");
           pageObject=pageHelper.getPageObject();
       } finally {
           PageHelperFactory.unregisterFormFiller("select-landlord-or-agent");
       }
    }

    @Given("^I am on the 'account-details' page$")
    public void i_am_on_the_account_details_page() throws Throwable {
        checkOnPage(pageHelper, "account-details");
    }

    @Given("^I have not supplied Agent name$")
    public void i_have_not_supplied_Agent_name() throws Throwable {
        //not entered agent name
    }

    @Then("^I will be taken to the 'select-landlord-or-agent' page$")
    public void i_will_be_taken_to_the_select_landlord_or_agent_page() throws Throwable {
        checkOnPage(pageHelper, "select-landlord-or-agent");
    }
    
    @Then("^details previously entered on select-landlord-or-agent will be displayed$")
    public void details_previously_entered_on_select_landlord_or_agent_will_be_displayed() throws Throwable {
        selectLandlordOrAgentPageHelper = new SelectLandlordOrAgentPageHelper(webDriver);
        selectLandlordOrAgentPageObject = selectLandlordOrAgentPageHelper.getPageObject();
        assertTrue("Check if Agent selected",
                selectLandlordOrAgentPageObject.getWebElementNdsRadiobuttonUserType_AGENT().isSelected());
    }
    

    @When("^I have entered valid details$")
    public void i_have_entered_valid_details() throws Throwable {
       pageHelper.fillInForm("AGENT");
    }
    
    @Then("^heading will be 'Agent address'$")
    public void heading_will_be_Agent_address() throws Throwable {
        assertEquals("Checking heading",webDriver.findElement(By.xpath("//div[contains(@class, 'column-full')]/h2")).getText(),"Agent address");
    }
    
    @When("^I submit Next$")
    public void i_submit_Next() throws Throwable {
        pageObject.clickNext(); 
        
    }

}
