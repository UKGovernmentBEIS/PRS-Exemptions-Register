package com.northgateps.nds.beis.ui.view.javascript.navigationtests;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.northgateps.nds.beis.ui.selenium.pagehelper.ClosePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.NavigationPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedEpcDetailsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionDocumentUploadPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionListOfValuesPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionRequirementsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionStartDatePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionTextPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedFurtherInformationPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedPropertyAddressPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedSelectExemptionTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedSelectPropertyTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.ClosePageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedEpcDetailsPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionDocumentUploadPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionListOfValuesPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionStartDatePageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionTextPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedFurtherInformationPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedSelectExemptionTypePageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedSelectPropertyTypePageObject;
import com.northgateps.nds.beis.ui.view.helper.BeisTestUtilities;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.NdsUiWait;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class NavigationSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");
    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    NavigationPageHelper pageHelper;
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject dashboardPageObject;
    PersonalisedEpcDetailsPageHelper epcDetailsPageHelper;
    PersonalisedEpcDetailsPageObject epcDetailsPageObject;
    PersonalisedSelectExemptionTypePageHelper personalisedSelectExemptionTypePageHelper;
    PersonalisedSelectExemptionTypePageObject personalisedSelectExemptionTypePageObject;
    PersonalisedSelectExemptionTypePageHelper selectExemptionTypePageHelper;
    PersonalisedSelectExemptionTypePageObject selectExemptionTypePageObject;
    PersonalisedExemptionRequirementsPageHelper personalisedExemptionRequirementsPageHelper;
    PersonalisedPropertyAddressPageHelper personalisedPropertyAddressPageHelper;
    PersonalisedSelectPropertyTypePageHelper selectPropertyTypePageHelper;
    PersonalisedSelectPropertyTypePageObject selectPropertyTypePageObject;
    PersonalisedExemptionDocumentUploadPageHelper documentUploadPageHelper;
    PersonalisedExemptionDocumentUploadPageObject documentUploadPageObject;
    PersonalisedFurtherInformationPageHelper personalisedFurtherInformationPageHelper;
    PersonalisedFurtherInformationPageObject personalisedFurtherInformationPageObject;
    PersonalisedExemptionTextPageHelper exemptionTextPageHelper;
    PersonalisedExemptionTextPageObject exemptionTextPageObject;
    PersonalisedExemptionStartDatePageHelper exemptionStartDatePageHelper;
    PersonalisedExemptionStartDatePageObject exemptionStartDatePageObject;
    PersonalisedExemptionListOfValuesPageHelper exemptionListOfValuesPageHelper;
    PersonalisedExemptionListOfValuesPageObject exemptionListOfValuesPageObject;
    UsedServiceBeforePageHelper firstPageHelper;
    ClosePageHelper closePageHelper;
    ClosePageObject closePageObject;
    
    BeisTestUtilities beisUtils= new BeisTestUtilities();
    
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
    
    @Given("^I have selected a non-domestic exemption of \"(.*?)\"$")
    public void i_have_selected_a_non_domestic_exemption_of(String code) throws Throwable {

		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller(loginUsername, loginPassword));
        
        /*Register a custom form filler for personalised-select-exemption-type page*/
        PageHelperFactory.registerFormFiller("personalised-select-exemption-type", new FormFiller(){
            @Override
            public void fill(BasePageHelper<?> pageHelper) {
                ((PersonalisedSelectExemptionTypePageHelper)pageHelper).fillInForm(code);
            }
        });
        
        try {
            epcDetailsPageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedEpcDetailsPageHelper.class);
            checkOnPage(epcDetailsPageHelper, "personalised-epc-details");
            epcDetailsPageObject = epcDetailsPageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
            PageHelperFactory.unregisterFormFiller("personalised-select-exemption-type");
        }
    }

    @Given("^I have selected a domestic exemption code of \"(.*?)\"$")
    public void i_have_selected_a_domestic_exemption_code_of(String code) throws Throwable {
    	
        // Register a custom form filler for login page
    	LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);
    	
        PageHelperFactory.registerFormFiller("login-form", loginPageHelper.createFormFiller(loginUsername,loginPassword));
        
        // Register a custom form filler for personalised-select-property-type page
        PageHelperFactory.registerFormFiller("personalised-select-property-type", new FormFiller(){
            @Override
            public void fill(BasePageHelper<?> pageHelper) {
                ((PersonalisedSelectPropertyTypePageHelper)pageHelper).fillInForm("DOMESTIC");
            }
        });        
        
        // Register a custom form filler for personalised-select-exemption-type page
        PageHelperFactory.registerFormFiller("personalised-select-exemption-type", new FormFiller(){
            @Override
            public void fill(BasePageHelper<?> pageHelper) {
                ((PersonalisedSelectExemptionTypePageHelper)pageHelper).fillInForm(code);
            }
        });
        
        try {
            epcDetailsPageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedEpcDetailsPageHelper.class);
            checkOnPage(epcDetailsPageHelper, "personalised-epc-details");
            epcDetailsPageObject = epcDetailsPageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
            PageHelperFactory.unregisterFormFiller("personalised-select-property-type");
            PageHelperFactory.unregisterFormFiller("personalised-select-exemption-type");
        }
       
    }

    @Given("^I am on the personalised-epc-details page$")
    public void i_am_on_the_epc_details_page() throws Throwable {
        checkOnPage(epcDetailsPageHelper, "personalised-epc-details");
    }
    
	@Then("^I can see the digital assist guidance text$")
	public void i_can_see_the_digital_assist_guidance_text() {
		closePageHelper = new ClosePageHelper(webDriver);
		closePageObject = closePageHelper.getPageObject();

		assertTrue("help to register your exemption, get in touch by email, is not present", closePageObject
				.getTextFooterFooter().contains("help to register your exemption, get in touch by email"));
		assertTrue("call the digital helpline on",
				closePageObject.getTextFooterFooter().contains("call the digital helpline on"));
	}

    @Given("^I have uploaded \"(.*?)\" as EPC$")
    public void i_have_uploaded_as_EPC(String fileName) throws Throwable {
        beisUtils.selectFile(epcDetailsPageObject.getDriver(), fileName);  
        checkOnPage(epcDetailsPageHelper, "personalised-epc-details");
    }

    @When("^I select Next$")
    public void i_select_Next() throws Throwable {
    	WebElement nextButton = webDriver.findElement(By.cssSelector("button[value=NEXT]"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", nextButton);   	
        epcDetailsPageObject.clickNext();
    }

    @Then("^I will be taken to the personalised-exemption-document-upload page$")
    public void i_will_be_taken_to_the_exemption_document_upload_page() throws Throwable {
        checkOnPage(epcDetailsPageHelper, "personalised-exemption-document-upload");
    }

    @Given("^I have loaded greater than or equal to Min Documents$")
    public void i_have_loaded_greater_than_or_equal_to_Min_Documents() throws Throwable {
        documentUploadPageHelper = new PersonalisedExemptionDocumentUploadPageHelper(webDriver);
        documentUploadPageObject = documentUploadPageHelper.getPageObject();
        documentUploadPageHelper.fillInForm();

    }

    @Then("^I will be taken to the personalised-further-information page$")
    public void i_will_be_taken_to_the_personalised_further_information_page() throws Throwable {
        checkOnPage(documentUploadPageHelper, "personalised-further-information");
        personalisedFurtherInformationPageHelper = new PersonalisedFurtherInformationPageHelper(webDriver);
        personalisedFurtherInformationPageObject = personalisedFurtherInformationPageHelper.getPageObject();
    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {    	
    	JavascriptExecutor js = (JavascriptExecutor)documentUploadPageObject.getDriver();
    	 // if the element is on top.
    	js.executeScript("scroll(250, 0)");
    	WebElement backButton = documentUploadPageObject.getDriver().findElement(By.xpath("//button[@id='button.back']"));    	
    	Actions actions = new Actions(documentUploadPageObject.getDriver());
    	actions.moveToElement(backButton).click().perform();       
    }
    
    @When("^I click Back$")
    public void i_click_Back() throws Throwable {    	
    	personalisedFurtherInformationPageObject.clickBack();
    }


    @Then("^previous exemption document upload details will be displayed$")
    public void previous_exemption_document_upload_details_will_be_displayed() throws Throwable {
        documentUploadPageObject = documentUploadPageHelper.getNewPageObject();
        assertEquals("file uploaded", "test.docx",
                documentUploadPageObject.getDriver().findElement(By.className("filename")).getText());
    }

    @Then("^previous epc details will be displayed$")
    public void previous_epc_details_will_be_displayed() throws Throwable {
        epcDetailsPageObject = epcDetailsPageHelper.getNewPageObject();
        assertEquals("file uploaded", "test.docx",
                epcDetailsPageObject.getDriver().findElement(By.className("filename")).getText());

    }

    @Then("^I will be taken to the personalised-epc-details page$")
    public void i_will_be_taken_to_the_personalised_epc_details_page() throws Throwable {
        checkOnPage(documentUploadPageHelper, "personalised-epc-details");
    }
    
    @Then("^I will be navigated to the personalised-epc-details page$")
    public void i_will_be_navigated_to_the_personalised_epc_details_page() throws Throwable {
    	documentUploadPageHelper.getPageObject().clickBack();
        checkOnPage(documentUploadPageHelper, "personalised-epc-details");
    }

    @Then("^I will be taken to the personalised-exemption-text page$")
    public void i_will_be_taken_to_the_personalised_exemption_text_page() throws Throwable {    	
        checkOnPage(documentUploadPageHelper, "personalised-exemption-text");
        exemptionTextPageHelper = new PersonalisedExemptionTextPageHelper(webDriver);
        exemptionTextPageObject = exemptionTextPageHelper.getPageObject();
    }
    
    @Then("^I will be taken to personalised-exemption-text page$")
    public void i_will_be_taken_to_personalised_exemption_text_page() throws Throwable {
    	documentUploadPageHelper.getNewPageObject().clickButtonNext_NEXT();
        checkOnPage(documentUploadPageHelper, "personalised-exemption-text");
        exemptionTextPageHelper = new PersonalisedExemptionTextPageHelper(webDriver);
        exemptionTextPageObject = exemptionTextPageHelper.getPageObject();
    }


    @Given("^I supplied valid data$")
    public void i_supplied_valid_data() throws Throwable {
        exemptionTextPageHelper.fillInForm();
    }

    @Then("^previous exemption text details will be displayed$")
    public void previous_exemption_text_details_will_be_displayed() throws Throwable {
        exemptionTextPageObject = exemptionTextPageHelper.getNewPageObject();
        assertEquals("check previous details", "Exemption text",
                exemptionTextPageObject.getTextNdsTextareaExemptionText());
    }

    @Then("^I will be taken to the personalised-exemption-start-date page$")
    public void i_will_be_taken_to_the_personalised_exemption_start_date_page() throws Throwable {
        checkOnPage(epcDetailsPageHelper, "personalised-exemption-start-date");
        exemptionStartDatePageHelper = new PersonalisedExemptionStartDatePageHelper(webDriver);
        exemptionStartDatePageObject = exemptionStartDatePageHelper.getPageObject();
    }

    @Given("^I have entered a valid date$")
    public void i_have_entered_a_valid_date() throws Throwable {
        exemptionStartDatePageHelper.fillInForm();
        exemptionStartDatePageObject = exemptionStartDatePageHelper.getPageObject();
    }

    @When("^I select Next option$")
    public void i_select_Next_option() throws Throwable {
        exemptionStartDatePageObject.clickNext();

    }

    @Then("^I will be taken to the personalised-exemption-list-of-values page$")
    public void i_will_be_taken_to_the_personalised_exemption_list_of_values_page() throws Throwable {
        checkOnPage(exemptionStartDatePageHelper, "personalised-exemption-list-of-values");
        exemptionListOfValuesPageHelper = new PersonalisedExemptionListOfValuesPageHelper(webDriver);
        exemptionListOfValuesPageObject = exemptionListOfValuesPageHelper.getPageObject();
    }

    @Given("^I have selected an option$")
    public void i_have_selected_an_option() throws Throwable {
        exemptionListOfValuesPageHelper.fillInForm();
    }

    @Then("^I will be taken to personalised-further-information page$")
    public void i_will_be_taken_to_personalised_further_information_page() throws Throwable {
        checkOnPage(exemptionListOfValuesPageHelper, "personalised-further-information");
        personalisedFurtherInformationPageHelper = new PersonalisedFurtherInformationPageHelper(webDriver);
        personalisedFurtherInformationPageObject = personalisedFurtherInformationPageHelper.getPageObject();
    }

    @When("^I select Back option$")
    public void i_select_Back_option() throws Throwable {
        personalisedFurtherInformationPageObject = personalisedFurtherInformationPageHelper.getNewPageObject();
        personalisedFurtherInformationPageObject.clickBack();

    }

    @Then("^previous exemption list of values details will be displayed$")
    public void previous_exemption_list_of_values_details_will_be_displayed() throws Throwable {
        exemptionListOfValuesPageObject = exemptionListOfValuesPageHelper.getNewPageObject();
        assertTrue("check previous details", exemptionListOfValuesPageObject.getDriver().findElement(
                By.id("exemptionDetails.exemptionReason1")).isSelected());
    }

    @When("^I select back option$")
    public void i_select_back_option() throws Throwable {
        exemptionListOfValuesPageObject = exemptionListOfValuesPageHelper.getNewPageObject();
        exemptionListOfValuesPageObject.clickBack();
    }

    @Then("^previous exemption start date details will be displayed$")
    public void previous_exemption_start_date_details_will_be_displayed() throws Throwable {
        exemptionStartDatePageObject = exemptionStartDatePageHelper.getNewPageObject();
        LocalDate currentDate = LocalDate.now();
        assertEquals("check previous details", currentDate.format(DateTimeFormatter.ofPattern("dd/MM/YYYY")),
                exemptionStartDatePageObject.getDriver().findElement(
                        By.id("uiData.dateTimeFieldsexemptionDetails.exemptionStartDate.calendar")).getAttribute(
                                "value"));

    }

    @When("^I select the Back option$")
    public void i_select_the_Back_option() throws Throwable {
        exemptionStartDatePageObject.clickBack();
    }

    @Then("^I will be taken to personalised-epc-details page$")
    public void i_will_be_taken_to_personalised_epc_details_page() throws Throwable {
        checkOnPage(exemptionStartDatePageHelper, "personalised-epc-details");
    }
    
    @Then("^previous exemption document details will be displayed\\.$")
    public void previous_exemption_document_details_will_be_displayed() throws Throwable {
        documentUploadPageHelper = new PersonalisedExemptionDocumentUploadPageHelper(webDriver);
        documentUploadPageObject = documentUploadPageHelper.getPageObject();
        assertEquals("file uploaded", "test.docx",
                documentUploadPageObject.getDriver().findElement(
                        By.className("filename")).getText());
    }
    
    @When("^Pressing Back I will be navigated to the personalised-epc-details page$")
    public void pressing_Back_I_will_be_navigated_to_the_personalised_epc_details_page() {
        // if the element is on top.
        JavascriptExecutor js = (JavascriptExecutor)documentUploadPageObject.getDriver();         
        js.executeScript("scroll(document.body.scrollHeight,0)");
        NdsUiWait wait = documentUploadPageHelper.getWait();        
        wait.untilElementClickedOK(By.className("back"), documentUploadPageObject.getDriver());    	
    	try {
    		checkOnPage(documentUploadPageHelper, "personalised-epc-details");
    	} catch (AssertionError ae) {
    		pressing_Back_I_will_be_navigated_to_the_personalised_epc_details_page();
    	}
    }
    
    @When("^pressing Next button I will be taken to the personalised-further-information page$")
    public void pressing_Next_button_I_will_be_taken_to_the_personalised_further_information_page() {    	
		documentUploadPageObject.clickNext();	    	
    	try {
    		checkOnPage(documentUploadPageHelper, "personalised-further-information");
    	} catch (AssertionError ae) {
    		pressing_Next_button_I_will_be_taken_to_the_personalised_further_information_page();
    	}
    }
    
    @When("^I select Next button$")
    public void i_select_Next_button() throws Throwable {
    	WebElement nextButton = webDriver.findElement(By.cssSelector("button[value=NEXT]"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", nextButton);
        documentUploadPageObject.clickNext();
    }

    @When("^I select Next on list of values page$")
    public void i_select_Next_on_list_of_values_page() throws Throwable {
        exemptionListOfValuesPageObject.clickNext();
    }
    
    @When("^I select next button$")
    public void i_select_next_button() throws Throwable {
    	JavascriptExecutor js = (JavascriptExecutor)documentUploadPageObject.getDriver();
    	// if the element is on bottom.
    	js.executeScript("scroll(0, 250)");
	   	WebElement nextButton = documentUploadPageObject.getDriver().findElement(By.cssSelector("button[value=NEXT]"));    	
	   	Actions actions = new Actions(documentUploadPageObject.getDriver());
	   	actions.moveToElement(nextButton).click().perform(); 
	   	BasePageHelper.waitUntilPageLoading(documentUploadPageObject.getDriver());
    }
}
