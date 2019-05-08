package com.northgateps.nds.beis.ui.view.javascript.accountactivation;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.AccountActivationPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.AccountActivationPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.LoginPageObject;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class AccountActivationSteps {
    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    LoginPageHelper loginPageHelper;
    LoginPageObject firstPageObject;
    AccountActivationPageHelper pageHelper;
    AccountActivationPageObject pageObject;
    UsedServiceBeforePageHelper firstPageHelper;
    

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
        loginPageHelper = new LoginPageHelper(testHelper.getScenarioWebDriver());
        testHelper.openUrl();
    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }
    
    @Given("^I am on the login page$")
    public void i_am_on_the_sign_on_page() throws Throwable {
        //visit target page
        loginPageHelper = PageHelperFactory.visit(firstPageHelper,LoginPageHelper.class);
        firstPageObject=loginPageHelper.getPageObject();
        checkOnPage(loginPageHelper, "login-form");
        
    }

    @When("^I select activate your account$")
    public void i_select_activate_your_account() throws Throwable {
        firstPageObject.clickAnchorActivateRegistration();
    }

    @Then("^I will be taken to the account-activation page$")
    public void i_will_be_taken_to_the_account_activation_page() throws Throwable {
        checkOnPage(firstPageHelper,"account-activation");
        pageHelper = new AccountActivationPageHelper(webDriver);
        pageObject = pageHelper.getPageObject();
    }
    
    @Given("^I am on the account-activation page$")
    public void i_am_on_the_account_activation_page() throws Throwable {
        checkOnPage(pageHelper,"account-activation");
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
        
    }

    @Then("^I will be taken to the login page$")
    public void i_will_be_taken_to_the_sign_on_page() throws Throwable {
        checkOnPage(pageHelper,"login-form");
     
    }
    
    @Given("^I have not entered a valid code$")
    public void i_have_not_entered_a_valid_code() throws Throwable {
    	pageObject.setTextNdsInputActivationCode("xxx");
    }
    
    @Given("^I have not entered any data$")
    public void i_have_not_entered_any_data() throws Throwable {
       
    }

    @When("^I select activate registration$")
    public void i_select_activate_registration() throws Throwable {
        pageObject.clickNext();
        pageObject = pageHelper.getNewPageObject();       
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String message) throws Throwable {
        pageHelper.waitUntilValidationMessageSeen(message);
    }

    @Then("^I will remain on the account activation page$")
    public void i_will_remain_on_the_account_activation_page() throws Throwable {
        checkOnPage(pageHelper,"account-activation");
       
    }
}
