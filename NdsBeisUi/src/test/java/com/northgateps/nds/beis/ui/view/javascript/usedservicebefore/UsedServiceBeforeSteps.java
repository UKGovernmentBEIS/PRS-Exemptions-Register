package com.northgateps.nds.beis.ui.view.javascript.usedservicebefore;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.UsedServiceBeforePageObject;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

/** Test steps for the UsedServiceBefore.feature BDD file. */
public class UsedServiceBeforeSteps {
    
    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    
    UsedServiceBeforePageHelper pageHelper;
    UsedServiceBeforePageObject pageObject;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        testHelper.openUrl();
        pageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
        pageObject = pageHelper.getPageObject();
    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }
    
    @Given("^I am on the 'used service before' page$")
    public void i_am_on_the_used_service_before_page() throws Throwable {
    	checkOnPage(pageHelper, "used-service-before");
    }
    
    @Given("^I have not selected an option$")
    public void i_have_not_selected_an_option() throws Throwable {
    	//assume nothing is selected by default
    }

    @Then("^I will remain on the 'used service before'$")
    public void i_will_remain_on_the_used_service_before() throws Throwable {
    	checkOnPage(pageHelper, "used-service-before");
    }

    @When("^I select 'No, I need to register'$")
    public void i_select_No_I_need_to_register() throws Throwable {
        pageObject.clickNdsRadiobuttonelementUsedServiceBefore("false"); 
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
    	pageObject.clickButtonNext_NEXTUsedservicebefore();
    }

    @Then("^I will be taken to the 'select-landlord-or-agent' page$")
    public void i_will_be_taken_to_the_select_landlord_or_agent_page() throws Throwable {
    	checkOnPage(pageHelper, "select-landlord-or-agent");
    }
    
    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String validationMessage) throws Throwable {
    	assertEquals("Check validation message", validationMessage, pageHelper.getFirstSummaryFaultMessage());
    }
    
    @When("^I select 'Yes, I already have an account'$")
    public void i_select_Yes_I_already_have_an_account() throws Throwable {
        pageObject.clickNdsRadiobuttonelementUsedServiceBefore("true"); 
    }

    @Then("^I will be taken to the sign-on page$")
    public void i_will_be_taken_to_the_sign_on_page() throws Throwable {
    	checkOnPage(pageHelper, "login-form");
    }
    
    @Given("^I am on the 'sign-on' page$")
    public void i_am_on_the_sign_on_page() throws Throwable {
    	checkOnPage(pageHelper, "login-form");
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        LoginPageHelper helper = new LoginPageHelper(testHelper.getScenarioWebDriver());
        helper.getPageObject().clickBack();
    }

    @Then("^I will be taken to the 'used service before' page$")
    public void i_will_be_taken_to_the_used_service_before_page() throws Throwable {
    	checkOnPage(pageHelper, "used-service-before");
    }
}