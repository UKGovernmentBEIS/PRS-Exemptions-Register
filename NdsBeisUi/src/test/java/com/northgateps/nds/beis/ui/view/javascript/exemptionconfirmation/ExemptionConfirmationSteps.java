package com.northgateps.nds.beis.ui.view.javascript.exemptionconfirmation;

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
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionConfirmationPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionRequirementsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedSelectExemptionTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionConfirmationPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionRequirementsPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedSelectExemptionTypePageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;
import static org.junit.Assert.assertTrue;
import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;

/** Test steps for the ExemptionConfirmation.feature BDD file. */
public class ExemptionConfirmationSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");
    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject PersonalisedDashboardPageObject;
    PersonalisedExemptionConfirmationPageHelper pageHelper;
    PersonalisedExemptionConfirmationPageObject pageObject;
    PersonalisedSelectExemptionTypePageHelper selectExemptionTypePageHelper;
    PersonalisedSelectExemptionTypePageObject selectExemptionTypePageObject;
    PersonalisedExemptionRequirementsPageHelper exemptionsRequirementsPageHelper;
    PersonalisedExemptionRequirementsPageObject exemptionsRequirementsPageObject;
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

    @Given("^I am on the personalised-exemption-confirmation page$")
    public void i_am_on_the_exemption_confirmation_page() throws Throwable {
    	
		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller(loginUsername, loginPassword));
		
		try {
            pageHelper = PageHelperFactory.visit(firstPageHelper, PersonalisedExemptionConfirmationPageHelper.class);
            checkOnPage(pageHelper, "personalised-exemption-confirmation");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @When("^I select 'View my exemptions'$")
    public void i_select_View_my_exemptions() throws Throwable {
        pageObject.clickAnchorFinish();
    }

    @Then("^I will be taken to the personalised-dashboard page$")
    public void i_will_be_taken_to_the_dashboard_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-dashboard");

    }

    @Given("^I am on personalised-select-exemption-type page$")
    public void i_am_on_personalised_select_exemption_type_page() throws Throwable {
        
        // Register a custom form filler for login page
        LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

        PageHelperFactory.registerFormFiller("login-form",
                loginPageHelper.createFormFiller(loginUsername, loginPassword));
        
        try {
            selectExemptionTypePageHelper = PageHelperFactory.visit(firstPageHelper, PersonalisedSelectExemptionTypePageHelper.class);
            checkOnPage(selectExemptionTypePageHelper, "personalised-select-exemption-type");
            selectExemptionTypePageObject = selectExemptionTypePageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @Given("^I select 'WALL' as the exemption type$")
    public void i_select_WALL_as_the_exemption_type() throws Throwable {
        selectExemptionTypePageObject.getDriver().findElement(By.xpath("//input[@value='"+"WALL"+"']")).click();
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        selectExemptionTypePageObject.clickButtonNext_NEXT();
    }

    @Then("^I must move to personalised-exemption-requirements page$")
    public void i_must_move_to_personalised_exemption_requirements_page() throws Throwable {
        checkOnPage(selectExemptionTypePageHelper, "personalised-exemption-requirements");
    }

    @When("^I select back and move to exemption type page$")
    public void i_select_back_and_move_to_exemption_type_page() throws Throwable {
        exemptionsRequirementsPageHelper = new PersonalisedExemptionRequirementsPageHelper(webDriver);
        exemptionsRequirementsPageObject = exemptionsRequirementsPageHelper.getPageObject();
        exemptionsRequirementsPageObject.clickBack();
        checkOnPage(exemptionsRequirementsPageHelper, "personalised-select-exemption-type");
    }

    @When("^I select a different exemption type 'ALLIMP' and try submitting the exemption$")
    public void i_select_a_different_exemption_type_ALLIMP_and_try_submitting_the_exemption() throws Throwable {
        selectExemptionTypePageHelper = new PersonalisedSelectExemptionTypePageHelper(webDriver);

        // set form filler for personalised-select-exemption-type page
        selectExemptionTypePageHelper.setFormFiller(new FormFiller() {

            public void fill(BasePageHelper<?> pageHelper) {
                ((PersonalisedSelectExemptionTypePageHelper) pageHelper).fillInForm("ALLIMP");
            }
        });

        try {
            pageHelper = PageHelperFactory.visit(selectExemptionTypePageHelper, PersonalisedExemptionConfirmationPageHelper.class);
            checkOnPage(pageHelper, "personalised-exemption-confirmation");
            pageObject = pageHelper.getPageObject();

        } finally {
            PageHelperFactory.unregisterFormFiller("personalised-select-exemption-type");
        }
    }

    @Then("^I must be taken to personalised-exemption-confirmation page$")
    public void i_must_be_taken_to_personalised_exemption_confirmation_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-exemption-confirmation");
    }
    
	@Given("^I can see the correct guidance text$")
	public void i_can_see_the_correct_guidance_text() {
		pageObject = pageHelper.getPageObject();
		assertTrue(
				"As this is a self-certification register, you do not need to do anything else. The Enforcing Authority will only, is not present ",
				pageObject.getTextMainContent().contains(
						"As this is a self-certification register, you do not need to do anything else. The Enforcing Authority will only"));
	}
    
    @Then("^the exemptions region will be expanded$")
    public void the_exemptions_region_will_be_expanded() throws Throwable {
        dashboardPageHelper = new PersonalisedDashboardPageHelper(webDriver);
        PersonalisedDashboardPageObject = dashboardPageHelper.getPageObject();
        assertTrue("check examptions region is expanded", 
                PersonalisedDashboardPageObject.getWebElementSectionContent().isDisplayed());
    }
}
