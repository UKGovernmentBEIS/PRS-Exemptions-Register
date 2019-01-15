package com.northgateps.nds.beis.ui.view.javascript.changeaccountaddress;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import com.google.common.base.Function;
import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedAccountSummaryPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedChangeAccountAddressPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedAccountSummaryPageObject;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class ChangeAccountAddressSteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedAccountSummaryPageHelper summaryPageHelper;
    PersonalisedChangeAccountAddressPageHelper pageHelper;
    UsedServiceBeforePageHelper firstPageHelper;
    PersonalisedAccountSummaryPageObject summaryPageObject;

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

    @Given("^I am on the 'personalised-account-summary' page$")
	public void i_am_on_the_account_summary_page() throws Throwable {

		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller("changeaddresstest", "passwordw!11"));

		try {
			summaryPageHelper = PageHelperFactory.visit(firstPageHelper, PersonalisedAccountSummaryPageHelper.class);
			checkOnPage(summaryPageHelper, "personalised-account-summary");
		} finally {
			PageHelperFactory.unregisterFormFiller("login-form");
		}
	}

    @When("^I select Change address link$")
    public void i_select_Change_address_link() throws Throwable {
        summaryPageHelper.getPageObject().clickButtonChangeAddress_NEXTChangeAddress();
    }

    @Then("^I will be taken to the 'personalised-change-account-address' page$")
    public void i_will_be_taken_to_the_change_account_address_page() throws Throwable {
        pageHelper = new PersonalisedChangeAccountAddressPageHelper(summaryPageHelper.getPageObject().getDriver(),
                summaryPageHelper.getLocale());
        checkOnPage(pageHelper, "personalised-change-account-address");
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageHelper.getPageObject().clickBack();
    }

    @Then("^I will be taken to the 'personalised-account-summary' page$")
    public void i_will_be_taken_to_the_account_summary_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-account-summary");
    }

    @Then("^details will be displayed$")
    public void details_will_be_displayed() throws Throwable {
        // TODO
    }

    @Given("^I am on the 'personalised-change-account-address' page$")
	public void i_am_on_the_change_account_address_page() throws Throwable {

		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller("changeaddresstest", "passwordw!11"));

		try {
			pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedChangeAccountAddressPageHelper.class);
			checkOnPage(pageHelper, "personalised-change-account-address");

		} finally {
			PageHelperFactory.unregisterFormFiller("login-form");
		}
	}

    @Given("^have selected or entered a valid address$")
    public void have_selected_or_entered_a_valid_address() throws Throwable {
        pageHelper.fillInForm();
    }

    @When("^I select Change address$")
    public void i_select_Change_address() throws Throwable {
        pageHelper.getPageObject().clickButtonNext_NEXT();

    }

    @Then("^details for the account will be displayed$")
    public void details_for_the_account_will_be_displayed() throws Throwable {
        summaryPageHelper = new PersonalisedAccountSummaryPageHelper(testHelper.getScenarioWebDriver(),
                pageHelper.getLocale());

        final String newLine = "\n";
        assertEquals("Check address",
                "Flat 1, Projection West" + newLine + "Merchants Place" + newLine + "READING" + newLine + "RG1 1ET"
                        + newLine + "UNITED KINGDOM",
                summaryPageHelper.getPageObject().getWebElementTdAddressLines().getText());
    }

    @Then("^I will remain on the 'personalised-change-account-address' page$")
    public void i_will_remain_on_the_change_account_address_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-change-account-address");
    }

    @When("^I have not supplied a postcode$")
    public void i_have_not_supplied_a_postcode() throws Throwable {
        pageHelper.getPageObject().setTextNdsInputPostcodeCriterion("");
    }

    @When("^I select Find address$")
    public void i_select_Find_address() throws Throwable {
        pageHelper.getPageObject().clickButtonFindPostcode();
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String validationMessage) throws Throwable {

        assertEquals("Check error message", validationMessage, pageHelper.getFirstSummaryFaultMessage());
    }

    @Given("^I have supplied a valid postcode$")
    public void i_have_supplied_a_valid_postcode() throws Throwable {
        pageHelper.getPageObject().setTextNdsInputPostcodeCriterion("RG1 1ET");
    }

    @Given("^addresses exist for the postcode$")
    public void addresses_exist_for_the_postcode() throws Throwable {
        // nothing to check
    }

    @Then("^a pick an address selection box will be displayed$")
    public void a_pick_an_address_selection_box_will_be_displayed() throws Throwable {
        while (true) {
            try {
                assertTrue("Check validation message", pageHelper.getCurrentAddressSelectList().isDisplayed());
                assertTrue("Check validation message",
                        pageHelper.getCurrentAddressSelectList().findElements(By.tagName("option")).size() >= 2);
                return;
            } catch (StaleElementReferenceException e) {
                // retry
                pageHelper.getNewPageObject();
            }
        }
    }

    @Then("^the number of addresses will be displayed in the box as \"(.*?)\"$")
    public void the_number_of_addresses_will_be_displayed_in_the_box_as(String message) throws Throwable {
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    String text = new Select(
                            pageHelper.getCurrentAddressSelectList()).getFirstSelectedOption().getText();
                    return message.equals(text);
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });
        assertEquals("First Row of the list ", message,
                new Select(pageHelper.getCurrentAddressSelectList()).getFirstSelectedOption().getText().trim());
    }

    @When("^I select an address$")
    public void i_select_an_address() throws Throwable {
        String selectValue = "Flat 1, Projection West, Merchants Place, READING, RG1 1ET";
        pageHelper.waitUntilElementFound(pageHelper.getPageObject().getByNdsSelectSelectedAddressRef());
        pageHelper.getPageObject().selectOptionNdsSelectSelectedAddressRef(selectValue);
    }

    @Then("^the address will be displayed$")
    public void the_address_will_be_displayed() throws Throwable {
        pageHelper.waitUntilElementFound(pageHelper.getPageObject().getByNdsInputTown());

        pageHelper.getNewPageObject();
        assertEquals("Check Address Line 0", "Flat 1, Projection West",
                pageHelper.getCurrentAddressInput("line[0]").getAttribute("value"));
        assertEquals("Check Address Line 0", "Merchants Place",
                pageHelper.getCurrentAddressInput("line[1]").getAttribute("value"));
        assertEquals("Check Address Town", "READING", pageHelper.getCurrentAddressInput("town").getAttribute("value"));
        assertEquals("Check Address County", "", pageHelper.getCurrentAddressInput("county").getAttribute("value"));
        assertEquals("Check Address Postcode", "RG1 1ET",
                pageHelper.getCurrentAddressInput("postcode").getAttribute("value"));
    }

    @Then("^the address fields will be display only$")
    public void the_address_fields_will_be_display_only() throws Throwable {
        assertTrue("Check if line 0 is not editable",
                pageHelper.getCurrentAddressInput("line[0]").getAttribute("readonly") != null);
        assertTrue("Check if Line 1 is not editable",
                pageHelper.getCurrentAddressInput("line[1]").getAttribute("readonly") != null);
        assertTrue("Check if County is not editable",
                pageHelper.getCurrentAddressInput("county").getAttribute("readonly") != null);
        assertTrue("Check if Town is not editable",
                pageHelper.getCurrentAddressInput("town").getAttribute("readonly") != null);
        assertTrue("Check if Postcode is not editable",
                pageHelper.getCurrentAddressInput("postcode").getAttribute("readonly") != null);
    }

    @Given("^I have selected Enter an address manually$")
    public void i_have_selected_Enter_an_address_manually() throws Throwable {
        pageHelper.getPageObject().clickAnchorEnterAddressManually();
    }

    @Given("^I have selected Country as United Kingdom$")
    public void i_have_selected_Country_as_United_Kingdom() throws Throwable {
        pageHelper.getAddressCountryElement().selectByValue("GB");
        pageHelper.getPageObject().setTextNdsInputLine0("Flat 1");
        pageHelper.getPageObject().setTextNdsInputTown("READING");
    }

    @Given("^I have supplied Postcode not in UK format$")
    public void i_have_supplied_Postcode_not_in_UK_format() throws Throwable {
        pageHelper.getPageObject().setTextNdsInputPostcode("XXX");
    }

    @When("^I select Submit address detail changes$")
    public void i_select_Submit_address_detail_changes() throws Throwable {
        pageHelper.getPageObject().clickButtonNext_NEXT();
    }

    @Given("^I have not supplied Postcode$")
    public void i_have_not_supplied_Postcode() throws Throwable {
        pageHelper.getPageObject().setTextNdsInputPostcode("");
    }

    @Given("^I have selected Country as not United Kingdom$")
    public void i_have_selected_Country_as_not_United_Kingdom() throws Throwable {
        pageHelper.getAddressCountryElement().selectByValue("AF");
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        pageHelper.getPageObject().clickButtonNext_NEXT();
    }

}
