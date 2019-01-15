package com.northgateps.nds.beis.ui.view.javascript.exemptionrequirements;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionRequirementsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedSelectPropertyTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.LoginPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionRequirementsPageObject;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

/** Test steps for the ExemptionRequirements.feature BDD file. */
public class ExemptionRequirementsSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject PersonalisedDashboardPageObject;
    LoginPageObject loginPageObject;
    PersonalisedExemptionRequirementsPageHelper pageHelper;
    PersonalisedExemptionRequirementsPageObject pageObject;
    PersonalisedSelectPropertyTypePageHelper selectPropertyTypePageHelper;
    UsedServiceBeforePageHelper firstPageHelper;

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

    @Given("^I am on the 'personalised-exemption-requirements' page$")
    public void i_am_on_the_exemption_requirements_page() throws Throwable {

 		// Register a custom form filler for login page
 		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

 		PageHelperFactory.registerFormFiller("login-form",
 				loginPageHelper.createFormFiller(loginUsername, loginPassword));
        
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedExemptionRequirementsPageHelper.class);
            checkOnPage(pageHelper, "personalised-exemption-requirements");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
   }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^I will be taken to the 'personalised-select-exemption-type' page$")
    public void i_will_be_taken_to_the_select_exemption_type_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-select-exemption-type");
    }

    @Then("^details previously entered will be displayed$")
    public void details_previously_entered_will_be_displayed() throws Throwable {
        assertEquals(true,
                testHelper.getScenarioWebDriver().findElement(By.xpath("//input[@value='NEW']")).isSelected());
    }

    @Given("^requirements for the exemption type selected is displayed$")
    public void requirements_for_the_exemption_type_selected_is_displayed() throws Throwable {
        assertEquals(true, testHelper.getScenarioWebDriver().findElement(By.cssSelector(".list-bullet")).isDisplayed());
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        pageObject.clickButtonNext_NEXT();
    }

    @Then("^I will be taken to the 'property address' page$")
    public void i_will_be_taken_to_the_property_address_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-property-address");
    }

}
