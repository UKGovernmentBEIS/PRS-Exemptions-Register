package com.northgateps.nds.beis.ui.view.javascript.endexemptionconfirmation;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedEndExemptionConfirmationPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedEndExemptionPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionConfirmationPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedEndExemptionConfirmationPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedEndExemptionPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionConfirmationPageObject;
import com.northgateps.nds.platform.ui.selenium.core.PageHelperException;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;

/** Test steps for the EndExemptionConfirmation.feature BDD file. */
public class EndExemptionConfirmationSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");
    

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject dashboardpageObject;
    PersonalisedEndExemptionConfirmationPageHelper pageHelper;
    PersonalisedEndExemptionConfirmationPageObject pageObject;
    UsedServiceBeforePageHelper firstPageHelper;
    PersonalisedExemptionConfirmationPageHelper exemptionConfirmationPageHelper;
    PersonalisedExemptionConfirmationPageObject exemptionConfirmationPageObject;
    PersonalisedEndExemptionPageHelper endExemptionPageHelper;
    PersonalisedEndExemptionPageObject endExemptionPageObject;

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
    
    @Given("^I am on the end-exemption-confirmation page$")
    public void i_am_on_the_end_exemption_confirmation_page() throws Throwable {
        dashboardPageHelper = createExemptionToEnd();
        endExemptionPageHelper = PageHelperFactory.visitNew(dashboardPageHelper, PersonalisedEndExemptionPageHelper.class);
        endExemptionPageObject = endExemptionPageHelper.getPageObject();
        endExemptionPageObject.getDriver().findElement(By.id("button.next")).click();
        pageHelper = new PersonalisedEndExemptionConfirmationPageHelper(testHelper.getScenarioWebDriver());
        pageObject = pageHelper.getPageObject();
        checkOnPage(pageHelper, "personalised-end-exemption-confirmation");
        
    }
    
    private PersonalisedDashboardPageHelper createExemptionToEnd() throws PageHelperException {
        
		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller(loginUsername, loginPassword));
		
        try {
            exemptionConfirmationPageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedExemptionConfirmationPageHelper.class);
            exemptionConfirmationPageObject = exemptionConfirmationPageHelper.getPageObject();
            exemptionConfirmationPageObject.clickAnchorFinish();
            dashboardPageHelper = new PersonalisedDashboardPageHelper(testHelper.getScenarioWebDriver());
            return dashboardPageHelper;
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @When("^I select 'View my exemptions'$")
    public void i_select_View_my_exemptions() throws Throwable {
        pageObject.clickNext();
    }

    @Then("^I will be taken to the personalised-dashboard page$")
    public void i_will_be_taken_to_the_personalised_dashboard_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-dashboard");
    }

}
