package com.northgateps.nds.beis.ui.view.javascript.selectlandlordtype;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.openqa.selenium.By;
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
import net.thucydides.core.annotations.Managed;

public class SelectLandlordTypeSteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    UsedServiceBeforePageHelper firstPageHelper;
    SelectLandlordTypePageHelper pageHelper;
    SelectLandlordTypePageObject pageObject;
    AccountDetailsPageObject accountDetailsPageObject;
    AccountDetailsPageHelper accountDetailsPageHelper;
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
    
    @Given("^I am on the select-landlord-type page$")
    public void i_am_on_the_select_landlord_type_page() throws Throwable {
        firstPageHelper = new UsedServiceBeforePageHelper(webDriver);
        /*Set a custom form filler for used-service-before page*/
        firstPageHelper.setFormFiller(new FormFiller() {
            public void fill(BasePageHelper<?> pageHelper) {
                ((UsedServiceBeforePageHelper) pageHelper).fillInForm(false);
            }
       });
        pageHelper = PageHelperFactory.visit(firstPageHelper, SelectLandlordTypePageHelper.class);
        checkOnPage(pageHelper, "select-landlord-type");
        pageObject=pageHelper.getPageObject();
      }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Given("^I have not entered any data$")
    public void i_have_not_entered_any_data() throws Throwable {
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        pageObject.clickNext();
    }

    @Given("^I have selected organisation as landlord type$")
    public void i_have_selected_organisation_as_landlord_type() throws Throwable {
        pageObject.clickNdsRadiobuttonAccountType_ORGANISATION();
    }

    @Then("^I will be taken to the account-details page$")
    public void i_will_be_taken_to_the_account_details_page() throws Throwable {
        checkOnPage(pageHelper, "account-details");
    }

    @Then("^Organisation name will be available for input$")
    public void organisation_name_will_be_available_for_input() throws Throwable {
        accountDetailsPageHelper = new AccountDetailsPageHelper(testHelper.getScenarioWebDriver());
        accountDetailsPageObject = accountDetailsPageHelper.getNewPageObject();
        assertTrue("visibility of Organisation name",
                accountDetailsPageObject.getWebElementNdsInputOrgName().isDisplayed());

    }

    @Given("^I have selected person as landlord type$")
    public void i_have_selected_person_as_landlord_type() throws Throwable {
        pageObject.clickNdsRadiobuttonAccountType_PERSON();
    }

    @Then("^First name and Last name will be available for input$")
    public void first_name_and_Last_name_will_be_available_for_input() throws Throwable {
        accountDetailsPageHelper = new AccountDetailsPageHelper(testHelper.getScenarioWebDriver());
        accountDetailsPageObject = accountDetailsPageHelper.getNewPageObject();
        assertTrue("visibility of First name", accountDetailsPageObject.getWebElementNdsInputFirstname().isDisplayed());
        assertTrue("visibility of Last name", accountDetailsPageObject.getWebElementNdsInputSurname().isDisplayed());
    }

    @Then("^I will be taken to the select-landlord-type page$")
    public void i_will_be_taken_to_the_select_landlord_type_page() throws Throwable {
        checkOnPage(firstPageHelper, "select-landlord-type");
    }

    @Then("^I will be taken to the 'select-landlord-or-agent' page$")
    public void i_will_be_taken_to_the_select_landlord_or_agent_page() throws Throwable {
        checkOnPage(firstPageHelper, "select-landlord-or-agent");
    }
    
    @Then("^details previously entered will be displayed$")
    public void details_previously_entered_will_be_displayed() throws Throwable {
        selectLandlordOrAgentPageHelper = new SelectLandlordOrAgentPageHelper(webDriver);
        selectLandlordOrAgentPageObject = selectLandlordOrAgentPageHelper.getPageObject();
        assertTrue("Check if lanlord selected",
                selectLandlordOrAgentPageObject.getWebElementNdsRadiobuttonUserType_LANDLORD().isSelected());
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String validationMessage) throws Throwable {
        assertEquals("Check validation message", validationMessage, pageHelper.getFirstSummaryFaultMessage());
    }

    @Then("^I will remain on the select-landlord-type page$")
    public void i_will_remain_on_the_select_landlord_type_page() throws Throwable {
        checkOnPage(pageHelper, "select-landlord-type");
    }  
    
}
