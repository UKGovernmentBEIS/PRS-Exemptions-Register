package com.northgateps.nds.beis.ui.view.javascript.exemptiondocumentupload;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedEpcDetailsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionDocumentUploadPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedSelectExemptionTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedEpcDetailsPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionDocumentUploadPageObject;
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

public class ExemptionDocumentUploadSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");
    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedExemptionDocumentUploadPageHelper pageHelper;
    PersonalisedExemptionDocumentUploadPageObject pageObject;
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject PersonalisedDashboardPageObject;
    PersonalisedEpcDetailsPageHelper epcDetailsPageHelper;
    PersonalisedEpcDetailsPageObject PersonalisedEpcDetailsPageObject;
    UsedServiceBeforePageHelper firstPageHelper;
    PersonalisedSelectExemptionTypePageHelper selectExemptionTypePageHelper;

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

    @Given("^I am on the personalised-exemption-document-upload page$")
    public void i_am_on_the_exemption_document_upload_page() throws Throwable {
    	
		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller(loginUsername, loginPassword));
		
        // Register a custom form filler for select-exemption-type page
        PageHelperFactory.registerFormFiller("personalised-select-exemption-type", new FormFiller(){
            @Override
            public void fill(BasePageHelper<?> pageHelper) {
                ((PersonalisedSelectExemptionTypePageHelper) pageHelper).fillInForm("CONSENT");
            }
        });
        
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedExemptionDocumentUploadPageHelper.class);
            checkOnPage(pageHelper, "personalised-exemption-document-upload");
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

    @Then("^I will be taken to the personalised-epc-details page$")
    public void i_will_be_taken_to_the_epc_details_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-epc-details");

    }

    @Then("^the details previously entered will be displayed$")
    public void the_details_previously_entered_will_be_displayed() throws Throwable {
        epcDetailsPageHelper = new PersonalisedEpcDetailsPageHelper(webDriver);
        PersonalisedEpcDetailsPageObject = epcDetailsPageHelper.getPageObject();
        assertEquals("file uploaded", "test.docx",
                PersonalisedEpcDetailsPageObject.getDriver().findElement(By.className("filename")).getText());

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

    @Then("^I will remain on the personalised-exemption-document-upload page$")
    public void i_will_remain_on_the_exemption_document_upload_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-exemption-document-upload");

    }

    @Given("^I select a file \"(.*?)\" with an incorrect file type$")
    public void i_select_a_file_with_an_incorrect_file_type(String fileName) throws Throwable {
        pageObject.getDriver().findElement(By.id("resource")).sendKeys(pageHelper.getFilePath(fileName));
    }

    @Then("^the file will not be uploaded$")
    public void the_file_will_not_be_uploaded() throws Throwable {
        assertFalse(pageHelper.isFileFoundInGrid());
    }

    @Given("^I select a file \"(.*?)\" that is larger than the maximum size$")
    public void i_select_a_file_that_is_larger_than_the_maximum_size(String fileName) throws Throwable {
        pageHelper.ClearForm();
        pageObject.getDriver().findElement(By.id("resource")).sendKeys(pageHelper.getFilePath(fileName));
    }

    @Given("^I select a file \"(.*?)\" that is of the correct type and size$")
    public void i_select_a_file_that_is_of_the_correct_type_and_size(String fileName) throws Throwable {
        pageHelper.ClearForm();
        pageObject = pageHelper.getNewPageObject();
        pageObject.getDriver().findElement(By.id("resource")).sendKeys(pageHelper.getFilePath(fileName));
    }

    @Then("^the file will be uploaded$")
    public void the_file_will_be_uploaded() throws Throwable {

    }

    @Then("^I will be displayed the name of the file I have loaded$")
    public void i_will_be_displayed_the_name_of_the_file_I_have_loaded() throws Throwable {
        assertEquals("file uploaded", "test.docx",
                pageObject.getDriver().findElement(By.className("filename")).getText());

    }

    @Then("^I will have an option to remove the file$")
    public void i_will_have_an_option_to_remove_the_file() throws Throwable {
        WebElement element = pageObject.getDriver().findElement(By.xpath("//*[contains(@id,'DeleteResource')]"));
        assertTrue("check remove option", element.isDisplayed());

    }

    @Then("^I will be able to add a description of the file$")
    public void i_will_be_able_to_add_a_description_of_the_file() throws Throwable {
        pageObject.getDriver().findElement(By.id("exemptionDetails.epcEvidenceFiles.resources0.description")).sendKeys(
                "Exemption Proof");

    }

    @When("^I have loaded \"(.*?)\" less than Max Documents$")
    public void i_have_loaded_less_than_Max_Documents(String fileName) throws Throwable {
        pageHelper.ClearForm();
        pageObject.getDriver().findElement(By.id("resource")).sendKeys(pageHelper.getFilePath(fileName));
    }

    @Then("^the Upload button will be enabled$")
    public void the_Upload_button_will_be_enabled() throws Throwable {
        // assertTrue("check upload button enabled",
        // pageObject.getDriver().findElement(By.id("button.uploadresource")).isEnabled());

    }

    @Given("^I have loaded a file \"(.*?)\" with a correct file type$")
    public void i_have_loaded_a_file_with_a_correct_file_type(String fileName) throws Throwable {
        pageHelper.ClearForm();
        pageObject.getDriver().findElement(By.id("resource")).sendKeys(pageHelper.getFilePath(fileName));

    }

    @When("^I select Remove file$")
    public void i_select_Remove_file() throws Throwable {
        pageHelper.ClearForm();

    }

    @Then("^the document \"(.*?)\" is not listed as uploaded$")
    public void the_document_is_not_listed_as_uploaded(String fileName) throws Throwable {
        List<WebElement> fileNameCells = pageObject.getDriver().findElements(By.className("filename"));
        boolean found = false;
        for (WebElement fileNameCell : fileNameCells) {
            if (fileName.equals(fileNameCell.getText())) {
                found = true;
                break;
            }
        }
        assertTrue("Uploaded file is still listed", !found);

    }

    @Given("^I have loaded \"(.*?)\" with Max Documents$")
    public void i_have_loaded_with_Max_Documents(String fileName) throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        pageObject.getDriver().findElement(By.id("resource")).sendKeys(pageHelper.getFilePath(fileName));
    }

    @When("^I upload \"(.*?)\" as one more document$")
    public void i_upload_as_one_more_document(String fileName) throws Throwable {
        pageObject.getDriver().findElement(By.id("resource")).sendKeys(pageHelper.getFilePath(fileName));
    }

    @Then("^the document \"(.*?)\" is not listed in uploaded files$")
    public void the_document_is_not_listed_in_uploaded_files(String fileName) throws Throwable {
        List<WebElement> fileNameCells = pageObject.getDriver().findElements(By.className("filename"));
        boolean found = false;
        for (WebElement fileNameCell : fileNameCells) {
            if (fileName.equals(fileNameCell.getText())) {
                found = true;
                break;
            }
        }
        assertTrue("Uploaded file is still listed", !found);
    }

    @Then("^I will be taken to the personalised-exemption-declaration page$")
    public void i_will_be_taken_to_the_exemption_declaration_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-exemption-declaration");
    }

    @Then("^the Upload button will be shown$")
    public void the_Upload_button_will_be_shown() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        assertTrue("Uploaded file is shown", pageObject.getDriver().findElement(By.id("resource")).isDisplayed());
    }

    @Then("^the Upload button will be hidden$")
    public void the_Upload_button_will_be_hidden() throws Throwable {
        pageObject = pageHelper.getNewPageObject();
        boolean showFileUpload = true;
        try {
            @SuppressWarnings("unused")
            WebElement fileUpload = pageObject.getDriver().findElement(By.id("resource"));
        } catch (org.openqa.selenium.NoSuchElementException ex) {
            showFileUpload = false;
        }
        assertFalse("Uploaded file is not shown shown", showFileUpload);
    }

}
