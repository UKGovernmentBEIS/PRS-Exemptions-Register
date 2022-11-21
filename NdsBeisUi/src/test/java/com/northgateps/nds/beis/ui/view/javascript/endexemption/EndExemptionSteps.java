package com.northgateps.nds.beis.ui.view.javascript.endexemption;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedEndExemptionPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionConfirmationPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedEndExemptionPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionConfirmationPageObject;
import com.northgateps.nds.platform.ui.selenium.core.NdsUiWait;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

/** Test steps for the EndExemption.feature BDD file. */
public class EndExemptionSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());
  
    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");
   
    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject dashboardpageObject;
    PersonalisedEndExemptionPageHelper pageHelper;
    PersonalisedEndExemptionPageObject pageObject;
    UsedServiceBeforePageHelper firstPageHelper;
    PersonalisedExemptionConfirmationPageHelper exemptionConfirmationPageHelper;
    PersonalisedExemptionConfirmationPageObject exemptionConfirmationPageObject;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
        pageHelper = new PersonalisedEndExemptionPageHelper(testHelper.getScenarioWebDriver());        
        testHelper.openUrl();
    }
    
    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }

    @Given("^I am on the end-exemption page$")
    public void i_am_on_the_end_exemption_page() throws Throwable {       
        
        // Register a custom form filler for login page
    	LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);
    	
        PageHelperFactory.registerFormFiller("login-form", 
        		loginPageHelper.createFormFiller(loginUsername, loginPassword));

        try {
        	checkOnPage(loginPageHelper, "used-service-before");
        	pageHelper = PageHelperFactory.visit(firstPageHelper, PersonalisedEndExemptionPageHelper.class);
            checkOnPage(pageHelper, "personalised-end-exemption");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }    
    
    @When("^I select End exemption$")
    public void i_select_End_exemption() throws Throwable {        
        webDriver.findElement(By.id("button.next")).click();
    }

    @Then("^I will be taken to the end-exemption-confirmation page$")
    public void i_will_be_taken_to_the_end_exemption_confirmation_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-end-exemption-confirmation");
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^I will be taken to personalised-dashboard page$")
    public void i_will_be_taken_to_personalised_dashboard_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-dashboard");
    }
    
    @When("^I click on end exemption link$")
    public void i_click_on_end_exemption_link() {
    	dashboardPageHelper = new PersonalisedDashboardPageHelper(testHelper.getScenarioWebDriver());    	
    	dashboardpageObject = dashboardPageHelper.getPageObject();
    	dashboardpageObject.clickSummaryLink();
    	new NdsUiWait(dashboardpageObject.getDriver()).untilElementClickedOK(By.xpath("//div[@id='current-exemptions']//table[1]//tbody[1]//tr[4]//td[3]//button[1]"), dashboardpageObject.getDriver());    	
    }

    @Then("^I will be taken to end-exemption page$")
    public void i_will_be_taken_to_end_exemption_page() {
    	checkOnPage(pageHelper, "personalised-end-exemption");
    }
    
    @When("^I select 'View my exemptions'$")
    public void i_select_View_my_exemptions() throws Throwable {
    	exemptionConfirmationPageHelper = new PersonalisedExemptionConfirmationPageHelper(testHelper.getScenarioWebDriver());    	
    	exemptionConfirmationPageObject = exemptionConfirmationPageHelper.getPageObject();
        pageObject.clickNext();
    }
   
}
