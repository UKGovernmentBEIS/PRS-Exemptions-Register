package com.northgateps.nds.beis.ui.view.javascript.selectlandlordoragent;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.AccountDetailsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.SelectLandlordOrAgentPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.AccountDetailsPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.SelectLandlordOrAgentPageObject;
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

public class SelectLandlordOrAgentSteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    UsedServiceBeforePageHelper firstPageHelper;
    SelectLandlordOrAgentPageHelper pageHelper;
    SelectLandlordOrAgentPageObject pageObject;
    AccountDetailsPageObject accountDetailsPageObject;
    AccountDetailsPageHelper accountDetailsPageHelper;
    
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
    
    @Given("^I am on the select-landlord-or-agent page$")
    public void i_am_on_the_select_landlord_or_agent_page() throws Throwable {       
       
        /*Set a custom form filler for used-service-before page*/
        firstPageHelper.setFormFiller(new FormFiller() {
            public void fill(BasePageHelper<?> pageHelper) {
                ((UsedServiceBeforePageHelper) pageHelper).fillInForm(false);
            }
       });
        pageHelper = PageHelperFactory.visit(firstPageHelper, SelectLandlordOrAgentPageHelper.class);
        checkOnPage(pageHelper, "select-landlord-or-agent");
        pageObject=pageHelper.getPageObject();
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^I will be taken to the 'used-service-before' page$")
    public void i_will_be_taken_to_the_used_service_before_page() throws Throwable {
        checkOnPage(pageHelper, "used-service-before");
    }
    
    @Given("^I have not selected landlord or agent$")
    public void i_have_not_selected_landlord_or_agent() throws Throwable {
        // nothing selected
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        pageObject.clickNext();
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String validationMessage) throws Throwable {
        assertEquals("Check validation message", validationMessage, pageHelper.getFirstSummaryFaultMessage());
    }

    @Then("^I will remain on the 'select-landlord-or-agent' page$")
    public void i_will_remain_on_the_select_landlord_or_agent_page() throws Throwable {
        checkOnPage(pageHelper, "select-landlord-or-agent");
    }

    @Given("^I have selected landlord$")
    public void i_have_selected_landlord() throws Throwable {
       pageObject.clickNdsRadiobuttonelementUserType("LANDLORD");
    }

    @Then("^I will be taken to the 'select-landlord-type' page$")
    public void i_will_be_taken_to_the_select_landlord_type_page() throws Throwable {
        checkOnPage(pageHelper, "select-landlord-type");
    }
    
    @Given("^I have selected agent$")
    public void i_have_selected_agent() throws Throwable {
        pageObject.clickNdsRadiobuttonelementUserType("AGENT");
    }

    @Then("^I will be taken to the 'account-details' page$")
    public void i_will_be_taken_to_the_account_details_page() throws Throwable {
        checkOnPage(pageHelper, "account-details");
    }

    @Then("^Agent name will be available for input$")
    public void agent_name_will_be_available_for_input() throws Throwable {
        accountDetailsPageHelper = new AccountDetailsPageHelper(testHelper.getScenarioWebDriver());
        accountDetailsPageObject = accountDetailsPageHelper.getNewPageObject();
        assertTrue("visibility of Agent name", accountDetailsPageObject.getWebElementNdsInputAgentName().isDisplayed());
    }
    
    @Then("^heading will be 'Agent details'$")
    public void heading_will_be_Agent_details() throws Throwable {
        assertEquals("Checking heading",webDriver.findElement(By.xpath("//div[contains(@class, 'column-full')]/h1")).getText(),"Agent details");
    }

 }
