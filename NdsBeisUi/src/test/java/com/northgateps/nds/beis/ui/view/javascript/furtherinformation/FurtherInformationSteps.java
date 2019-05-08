package com.northgateps.nds.beis.ui.view.javascript.furtherinformation;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedFurtherInformationPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedFurtherInformationPageObject;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class FurtherInformationSteps {

	private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
			this.getClass().getClassLoader());

	private final String loginUsername = (String) testProperties.get("loginUsername");
	private final String loginPassword = (String) testProperties.get("loginPassword");
	private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
	PersonalisedFurtherInformationPageHelper pageHelper;
	PersonalisedFurtherInformationPageObject pageObject;
	UsedServiceBeforePageHelper firstPageHelper;

	String parentHandle;

	@Managed
	private WebDriver webDriver;

	@Before
	public void beforeScenario() {
		testHelper.beforeScenario();
		testHelper.setScenarioWebDriver(webDriver);
		firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
		pageHelper = new PersonalisedFurtherInformationPageHelper(testHelper.getScenarioWebDriver());
		pageObject = pageHelper.getPageObject();
		testHelper.openUrl();
	}

	@After
	public void afterScenario() {
		testHelper.afterScenario();
	}

	@Given("^I am on the personalised-further-information page$")
	public void i_am_on_the_personalised_further_information_page() throws Throwable {

		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller(loginUsername, loginPassword));

		try {
			pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedFurtherInformationPageHelper.class);
			checkOnPage(pageHelper, "personalised-further-information");
			pageObject = pageHelper.getPageObject();
		} finally {
			PageHelperFactory.unregisterFormFiller("login-form");
		}

	}

	@Given("^I enter text in the confirmation text area$")
	public void i_enter_text_in_the_confirmation_text_area() {
		pageObject.setTextNdsTextareaExemptionConfirmationText("Exemption confirmation text");
	}

	@Given("^I click next$")
	public void i_click_next() {
		pageObject.clickNext();
	}

	@Then("^I will receive the message \"([^\"]*)\"$")
	public void i_will_receive_the_message(String arg1) {
	}

	@Then("^I will remain on the personalised-further-information page$")
	public void i_will_remain_on_the_personalised_further_information_page() throws Throwable {
		checkOnPage(pageHelper, "personalised-further-information");

	}

	@When("^I select the confirm checkbox$")
	public void i_select_the_confirm_checkbox() {
		pageObject.clickInputExemptionConfirmationIndicator_Y();
	}

	@Then("^I will be taken to the personalised-exemption-declaration page$")
	public void i_will_be_taken_to_the_personalised_exemption_declaration_page() {
		checkOnPage(pageHelper, "personalised-exemption-declaration");
	}

}