package com.northgateps.nds.beis.ui.view.javascript.epcdetails;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedEpcDetailsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedPropertyAddressPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedEpcDetailsPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedPropertyAddressPageObject;
import com.northgateps.nds.platform.ui.selenium.core.AbstractSeleniumTest;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.selenium.drivers.DriverFactory;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class EpcDetailsSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");
    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedEpcDetailsPageHelper pageHelper;
    PersonalisedEpcDetailsPageObject pageObject;
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject PersonalisedDashboardPageObject;
    PersonalisedPropertyAddressPageHelper propertyAddressPageHelper;
    PersonalisedPropertyAddressPageObject PersonalisedPropertyAddressPageObject;
    private static final String fileGridLocation = "file:/C:/grid/beis/uploadfiles/";
    UsedServiceBeforePageHelper firstPageHelper;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        String useFilelocation;

        DriverFactory driverFactory = AbstractSeleniumTest.createDriverFactory();
        useFilelocation = fileGridLocation;

        System.out.println(
                "During EvidenceUpload, the Driverfactory was of type " + driverFactory.getClass().getSimpleName()
                        + ", " + "which means that files will be uploaded from " + useFilelocation + ".");
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        firstPageHelper = new UsedServiceBeforePageHelper(testHelper.getScenarioWebDriver());
        testHelper.openUrl();
     }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }

    private String getFilePath(String fileName) {
        return fileGridLocation + fileName;
    }

    @Given("^I am on the personalised-epc-details page$")
    public void i_am_on_the_epc_details_page() throws Throwable {
    	
		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller(loginUsername, loginPassword));
        
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedEpcDetailsPageHelper.class);
            checkOnPage(pageHelper, "personalised-epc-details");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();

    }

    @Then("^I will be taken to the personalised-property-address page$")
    public void i_will_be_taken_to_the_property_address_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-property-address");

    }

    @Then("^the details previously entered will be displayed$")
    public void the_details_previously_entered_will_be_displayed() throws Throwable {
        propertyAddressPageHelper = new PersonalisedPropertyAddressPageHelper(pageObject.getDriver());
        PersonalisedPropertyAddressPageObject = propertyAddressPageHelper.getPageObject();
        assertEquals("Building and street line 0", "Flat 1, Projection West",
                PersonalisedPropertyAddressPageObject.getTextNdsInputLine0());
        assertEquals("Building and street line 1", "Merchants Place",
                PersonalisedPropertyAddressPageObject.getTextNdsInputLine1());
        assertEquals("town", "READING", PersonalisedPropertyAddressPageObject.getTextNdsInputTown());
        assertEquals("County", "", PersonalisedPropertyAddressPageObject.getTextNdsInputCounty());
        assertEquals("Postcode", "RG1 1ET", PersonalisedPropertyAddressPageObject.getTextNdsInputPostcode());

    }

    @Given("^I have not uploaded any file$")
    public void i_have_not_uploaded_any_file() throws Throwable {
        assertFalse(pageHelper.isFileFoundInGrid());
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        pageObject.clickNext();

    }

    @Then("^I must receive \"(.*?)\" as validation message$")
    public void i_must_receive_as_validation_message(String validationMessage) throws Throwable {
        assertEquals("check validation message", validationMessage, pageHelper.findFaultMessage(validationMessage));

    }

    @Then("^I will remain on the personalised-epc-details page$")
    public void i_will_remain_on_the_epc_details_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-epc-details");

    }

    @Given("^I have select a file with a correct file type$")
    public void i_have_select_a_file_with_a_correct_file_type() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.getDriver().findElement(By.id("resource")).sendKeys(getFilePath("test.docx"));
    }

    @Given("^I select a file with an incorrect file type$")
    public void i_select_a_file_with_an_incorrect_file_type() throws Throwable {
        pageHelper.ClearForm();
        pageObject.getDriver().findElement(By.id("resource")).sendKeys(getFilePath("testProperties.json"));
    }

    @Then("^the file will not be uploaded$")
    public void the_file_will_not_be_uploaded() throws Throwable {
        assertFalse(pageHelper.isFileFoundInGrid());
    }

    @Then("^I must receive the custom message \"(.*?)\" as validation message$")
    public void i_must_receive_the_custom_message_as_validation_message(String message) throws Throwable {
        assertEquals("Check custom validation message", message,
        		 pageHelper.getFirstSummaryFaultMessage());       
    }

    @Given("^I select a file that is larger than the maximum size$")
    public void i_select_a_file_that_is_larger_than_the_maximum_size() throws Throwable {
        pageHelper.ClearForm();
        pageObject.getDriver().findElement(By.id("resource")).sendKeys(
                getFilePath("Spring in Action, 4th Edition.pdf"));
    }

    @When("^I click on 'Remove File' link for the uploaded file$")
    public void i_click_on_Remove_File_link_for_the_uploaded_file() throws Throwable {
        List<WebElement> rows = pageObject.getDriver().findElements(By.cssSelector("tr"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.cssSelector("td"));
            if (cells.size() > 0) { // No td cells in the header row
                if ("test.docx".equals(cells.get(0).getText())) {
                    List<WebElement> removeButtons = cells.get(2).findElements(By.cssSelector("button"));
                    if (removeButtons.size() > 0) {
                        removeButtons.get(0).click();
                        return;
                    }
                }
            }
        }

    }

    @Then("^The file is not listed as uploaded$")
    public void the_file_is_not_listed_as_uploaded() throws Throwable {
        List<WebElement> fileNameCells = pageObject.getDriver().findElements(By.className("filename"));
        boolean found = false;
        for (WebElement fileNameCell : fileNameCells) {
            if ("test.docx".equals(fileNameCell.getText())) {
                found = true;
                break;
            }
        }
        assertFalse("Uploaded file is still listed", found);
    }

    @Given("^I enter the description for the file$")
    public void i_enter_the_description_for_the_file() throws Throwable {
        pageObject.getDriver().findElement(By.id("exemptionDetails.epc.files.resources0.description")).sendKeys(
                "Java Programming");
        ;
    }

    @Then("^I must move to personalised-exemption-start-date page$")
    public void i_must_move_to_personalised_exemption_start_date_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-exemption-start-date");
    }

}
