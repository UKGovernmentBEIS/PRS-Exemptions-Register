package com.northgateps.nds.beis.ui.view.javascript.exemptiontext;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionDocumentUploadPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionTextPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedSelectExemptionTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionDocumentUploadPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionTextPageObject;
import com.northgateps.nds.beis.ui.view.helper.BeisTestUtilities;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class ExemptionTextSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());
    BeisTestUtilities beisUtils= new BeisTestUtilities();
    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");
    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedExemptionTextPageHelper pageHelper;
    PersonalisedExemptionTextPageObject pageObject;
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject PersonalisedDashboardPageObject;
    UsedServiceBeforePageHelper firstPageHelper;
    PersonalisedExemptionDocumentUploadPageHelper exemptionDocumentUploadPageHelper;
    PersonalisedExemptionDocumentUploadPageObject PersonalisedExemptionDocumentUploadPageObject;
    
    WebElement element;
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

    @Given("^I am on the personalised-exemption-text page$")
    public void i_am_on_the_exemption_text_page() throws Throwable {
        
		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller(loginUsername, loginPassword));
        
        /*Register a custom form filler for select-exemption-type page*/
        PageHelperFactory.registerFormFiller("personalised-select-exemption-type", new FormFiller(){
            @Override
            public void fill(BasePageHelper<?> pageHelper) {
                ((PersonalisedSelectExemptionTypePageHelper)pageHelper).fillInForm("ALLIMP");
            }
        });
        
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedExemptionTextPageHelper.class);
            checkOnPage(pageHelper, "personalised-exemption-text");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
            PageHelperFactory.unregisterFormFiller("personalised-select-exemption-type");
        }
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^I will be taken to the personalised-exemption-document-upload page$")
    public void i_will_be_taken_to_the_exemption_document_upload_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-exemption-document-upload");
    }

    @Then("^the details previously entered will be displayed$")
    public void the_details_previously_entered_will_be_displayed() throws Throwable {
        exemptionDocumentUploadPageHelper = new PersonalisedExemptionDocumentUploadPageHelper(webDriver);
        PersonalisedExemptionDocumentUploadPageObject = exemptionDocumentUploadPageHelper.getPageObject();
        assertEquals("file uploaded", "test.docx",
                PersonalisedExemptionDocumentUploadPageObject.getDriver().findElement(
                        By.className("filename")).getText());
    }

    @Given("^I have not entered any text$")
    public void i_have_not_entered_any_text() throws Throwable {
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
        Thread.sleep(2000);
    	BasePageHelper.waitUntilPageLoading(pageObject.getDriver());
        JavascriptExecutor jse = (JavascriptExecutor)pageObject.getDriver();
        jse.executeScript("scroll(0, 250)");       
        pageObject.clickNext();
    }

    @Then("^I will receive \"(.*?)\" as validation message$")
    public void i_will_receive_as_validation_message(String validationMessage) throws Throwable {
    	// There's a redirect from this page to this page that we need to wait for to get the validation message
    	Thread.sleep(500);
        assertEquals("check validation message", validationMessage, pageHelper.findFaultMessage(validationMessage));
    }

    @Then("^I will remain on the personalised-exemption-text page$")
    public void i_will_remain_on_the_exemption_text_page() throws Throwable {
        Thread.sleep(1000);
        checkOnPage(pageHelper, "personalised-exemption-text");
        pageObject = pageHelper.getPageObject();
    }

    @Then("^the file \"(.*?)\" will be uploaded$")
    public void the_file_will_be_uploaded(String fileName) throws Throwable {
        assertTrue(pageHelper.isFileFoundInGrid(fileName));
    }

    @Then("^I will have an option to remove the file$")
    public void i_will_have_an_option_to_remove_the_file() throws Throwable {
        WebElement element = pageObject.getDriver().findElement(By.xpath("//*[contains(@id,'DeleteResource')]"));
        assertTrue("check remove option", element.isDisplayed());
    }

    @Then("^I will be able to add a description of the file$")
    public void i_will_be_able_to_add_a_description_of_the_file() throws Throwable {
        pageObject.getDriver().findElement(By.id("exemptionDetails.exemptionTextFile.resources0.description")).sendKeys(
                "Exemption text and file");

    }

    @Then("^I will be taken to the personalised-further-information page$")
    public void i_will_be_taken_to_the_personalised_further_information_page() throws Throwable {    	
        checkOnPage(pageHelper, "personalised-further-information");
    }

    @When("^I select Remove file$")
    public void i_select_Remove_file() throws Throwable {
        pageHelper.ClearForm();
    }

    @Given("^I select a file \"(.*?)\" with an incorrect file type$")
    public void i_select_a_file_with_an_incorrect_file_type(String fileName) throws Throwable {
        selectFile(fileName, "personalised-exemption-text");        
    }

    @Given("^I select a file \"(.*?)\" that is larger than the maximum size$")
    public void i_select_a_file_that_is_larger_than_the_maximum_size(String fileName) throws Throwable {
        selectFile(fileName, "personalised-exemption-text");
    }

    @Given("^I select a file \"(.*?)\" that is of the correct type and size$")
    public void i_select_a_file_that_is_of_the_correct_type_and_size(String fileName) throws Throwable {
        selectFile(fileName, "personalised-exemption-text");
    }

    @Then("^the document \"(.*?)\" is not listed as uploaded$")
    public void the_document_is_not_listed_as_uploaded(String fileName) throws Throwable {
        assertFalse("Uploaded file is still listed", pageHelper.isFileFoundInGrid(fileName));
    }

    @Then("^I will be displayed the name \"(.*?)\" of the file I have loaded$")
    public void i_will_be_displayed_the_name_of_the_file_I_have_loaded(String fileName) throws Throwable {
        assertEquals("file uploaded", fileName, pageObject.getDriver().findElement(By.className("filename")).getText());
    }

    @Given("^I have not loaded a document$")
    public void i_have_not_loaded_a_document() throws Throwable {
    }
    
    private void selectFile(String filename, String page) {
        checkOnPage(pageHelper, page);
        pageObject = pageHelper.getPageObject();
        beisUtils.selectFile(pageObject.getDriver(), filename);
    }
    
}
