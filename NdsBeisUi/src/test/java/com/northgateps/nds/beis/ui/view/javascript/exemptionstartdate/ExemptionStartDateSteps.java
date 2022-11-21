package com.northgateps.nds.beis.ui.view.javascript.exemptionstartdate;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionStartDatePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionStartDatePageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

/** Test steps for the ExemptionStartDate.feature BDD file. */
public class ExemptionStartDateSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject PersonalisedDashboardPageObject;
    PersonalisedExemptionStartDatePageHelper pageHelper;
    PersonalisedExemptionStartDatePageObject pageObject;
    UsedServiceBeforePageHelper firstPageHelper;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        testHelper.openUrl();
        firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
        testHelper.openUrl();
     }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }

    @Given("^I am on the 'personalised-exemption-start-date' page$")
	public void i_am_on_the_exemption_start_date_page() throws Throwable {

		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller(loginUsername, loginPassword));

		try {
			pageHelper = PageHelperFactory.visit(firstPageHelper, PersonalisedExemptionStartDatePageHelper.class);
			checkOnPage(pageHelper, "personalised-exemption-start-date");
			pageObject = pageHelper.getPageObject();
		} finally {
			PageHelperFactory.unregisterFormFiller("login-form");
		}
	}

    @Given("^I have not entered a date$")
    public void i_have_not_entered_a_date() throws Throwable {
        // Do Nothing
    }

    @Then("^I will receive the error message \"(.*?)\" as validation message$")
    public void i_will_receive_the_error_message_as_validation_message(String validationMessage) throws Throwable {
        assertEquals("Check validation message", validationMessage, pageHelper.getFirstSummaryFaultMessage());
    }

    @Given("^I have supplied a date in the future$")
    public void i_have_supplied_a_date_in_the_future() throws Throwable {
        pageHelper.setStartDate(pageHelper.getFutureDate());
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        BasePageHelper.waitUntilPageLoading(pageObject.getDriver());
        pageObject = pageHelper.getNewPageObject();       
        pageObject.clickNext();
    }

    @Then("^I must receive \"(.*?)\" as validation message$")
    public void i_must_receive_as_validation_message(String validationMessage) throws Throwable {
        Thread.sleep(1000);
        assertTrue("Check validation message", pageHelper.getFirstSummaryFaultMessage().contains(validationMessage));
    }

    @Then("^I will remain on the 'personalised-exemption-start-date' page$")
    public void i_will_remain_on_the_exemption_start_date_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-exemption-start-date");
    }

    @Given("^I have supplied a start date that means the exemption has already ended\\.$")
    public void i_have_supplied_a_start_date_that_means_the_exemption_has_already_ended() throws Throwable {
        LocalDate currentDate = LocalDate.now().minusYears(2);
        pageHelper.setStartDate(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
    }

    @Then("^I will receive validation message \"(.*?)\"$")
    public void i_will_receive_validation_message(String message) throws Throwable {
        if (!pageHelper.getFirstSummaryFaultMessage().contains(message)){
            pageObject.clickNext();
            BasePageHelper.waitUntilPageLoading(pageObject.getDriver());
        }
        assertTrue("Check validation message", pageHelper.getFirstSummaryFaultMessage().contains(message));
    }

}
