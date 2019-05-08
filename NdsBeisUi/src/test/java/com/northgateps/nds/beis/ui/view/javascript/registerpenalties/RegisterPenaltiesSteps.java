package com.northgateps.nds.beis.ui.view.javascript.registerpenalties;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertTrue;

import java.util.List;

import com.northgateps.nds.beis.ui.selenium.pagehelper.RegisterPenaltiesPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.RegisterSearchPenaltiesPageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.RegisterPenaltiesPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.RegisterSearchPenaltiesPageObject;
import com.northgateps.nds.beis.ui.view.javascript.base.AlternateUrlBaseSteps;
import com.northgateps.nds.platform.ui.selenium.core.NdsUiWait;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class RegisterPenaltiesSteps extends AlternateUrlBaseSteps{

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    RegisterPenaltiesPageHelper pageHelper;
    RegisterPenaltiesPageObject pageObject;
    RegisterSearchPenaltiesPageObject registerSearchPenaltiesPageObject;
    RegisterSearchPenaltiesPageHelper registerSearchPenaltiesPageHelper;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        testHelper.openUrl(GetUrl("register-search-penalties"));
        registerSearchPenaltiesPageHelper = new RegisterSearchPenaltiesPageHelper(testHelper.getScenarioWebDriver());
        registerSearchPenaltiesPageObject = registerSearchPenaltiesPageHelper.getPageObject();

    }

    @Given("^I am on the register-search-penalties page$")
    public void i_am_on_the_register_search_penalties_page() throws Throwable {
        registerSearchPenaltiesPageHelper = new RegisterSearchPenaltiesPageHelper(
                registerSearchPenaltiesPageObject.getDriver());
        checkOnPage(registerSearchPenaltiesPageHelper, "register-search-penalties");
    }

    @Given("^I search exemption using postcode$")
    public void i_search_exemption_using_postcode() throws Throwable {
        registerSearchPenaltiesPageHelper.fillInForm();
    }

    @When("^I select the property address$")
    public void i_select_the_property_address() throws Throwable {
    	WebElement addressLink = webDriver.findElement(By.id("button.authority.address"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", addressLink);
        new NdsUiWait(webDriver).untilElementClickedOK(By.id("button.authority.address"), webDriver);
    }

    @Then("^I will be taken to the register-penalties page$")
    public void i_will_be_taken_to_the_register_penalties_page() throws Throwable {
        checkOnPage(registerSearchPenaltiesPageHelper, "register-penalties");
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageHelper = new RegisterPenaltiesPageHelper(webDriver);
        pageObject = pageHelper.getNewPageObject();
        pageObject.clickBack();
        registerSearchPenaltiesPageHelper = new RegisterSearchPenaltiesPageHelper(webDriver);
        registerSearchPenaltiesPageObject = registerSearchPenaltiesPageHelper.getPageObject();
    }

    @Then("^I will be taken to the register-search-penalties page$")
    public void i_will_be_taken_to_the_register_search_penalties_page() throws Throwable {
        checkOnPage(pageHelper, "register-search-penalties");
    }

    @Then("^the search results will be displayed$")
    public void the_search_results_will_be_displayed() throws Throwable {
        String address1 = "1 Acacia Avenue, READING, Berkshire, RG1 1PB";
        String address2 = "Address not published";
        String landlordname1 = "John Smith";
        String landlordname2 = "Jane Smith";
        List<WebElement> addressCells = pageObject.getDriver().findElements(By.className("dgstandard"));
        
        assertTrue("check address1",addressCells.get(0).getText().trim().contains(address1));
        assertTrue("check address2",addressCells.get(0).getText().trim().contains(address2));
        assertTrue("check landlordname1",addressCells.get(0).getText().trim().contains(landlordname1));
        assertTrue("check landlordname2",addressCells.get(0).getText().trim().contains(landlordname2));
    }

    @When("^I select the landlord name$")
    public void i_select_the_landlord_name() throws Throwable {
        registerSearchPenaltiesPageHelper.click_landlordname();
    }

    
    @When("^I select 'New search'$")
    public void i_select_New_search() throws Throwable {
        pageObject.clickNext();
    }    
}
