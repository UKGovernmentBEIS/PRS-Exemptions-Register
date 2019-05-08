package com.northgateps.nds.beis.ui.view.javascript.selectexemptiontype;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedSelectExemptionTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedSelectExemptionTypePageObject;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

/** Test steps for the SelectExemptionType.feature BDD file. */
public class SelectExemptionTypeSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    UsedServiceBeforePageHelper firstPageHelper;
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject PersonalisedDashboardPageObject;
    PersonalisedSelectExemptionTypePageHelper pageHelper;
    PersonalisedSelectExemptionTypePageObject pageObject;
    LoginPageHelper loginPageHelper;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        testHelper.openUrl();
        firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
        dashboardPageHelper = new PersonalisedDashboardPageHelper(testHelper.getScenarioWebDriver());
        PersonalisedDashboardPageObject = dashboardPageHelper.getPageObject();
        testHelper.openUrl();
    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }

    @Given("^I am on the 'personalised-select-exemption-type' page$")
    public void i_am_on_the_select_exemption_type_page() throws Throwable {
        
		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller(loginUsername, loginPassword));
		
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedSelectExemptionTypePageHelper.class);
            checkOnPage(pageHelper, "personalised-select-exemption-type");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^I will be taken to the 'personalised-select-property-type' page$")
    public void i_will_be_taken_to_the_select_property_type_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-select-property-type");
    }

    @Then("^details previously entered will be displayed$")
    public void details_previously_entered_will_be_displayed() throws Throwable {
        assertEquals(true,
                testHelper.getScenarioWebDriver().findElement(By.id("exemptionDetails.propertyType2")).isSelected());
    }

    @Then("^I have not entered any data and prompt is present$")
    public void i_have_not_entered_any_data_and_prompt_is_present() throws Throwable {
    	assertTrue("You must select the one that is most relevant is not present ", pageObject.getTextMainContent().contains(
                "You must select the one that is most relevant"));
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.clickButtonNext_NEXT();
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String validationMessage) throws Throwable {
        assertEquals("Check validation message", validationMessage, pageHelper.getFirstSummaryFaultMessage());
    }

    @Then("^I will remain on the 'personalised-select-exemption-type' page$")
    public void i_will_remain_on_the_select_exemption_type_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-select-exemption-type");
    }

    @Given("^I have selected an exemption type$")
    public void i_have_selected_an_exemption_type() throws Throwable {
        testHelper.getScenarioWebDriver().findElement(By.id("exemptionDetails.exemptionType1")).click();
    }

    @Then("^I will be taken to the 'personalised-exemption-requirements' page$")
    public void i_will_be_taken_to_the_exemption_requirements_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-exemption-requirements");
    }
}
