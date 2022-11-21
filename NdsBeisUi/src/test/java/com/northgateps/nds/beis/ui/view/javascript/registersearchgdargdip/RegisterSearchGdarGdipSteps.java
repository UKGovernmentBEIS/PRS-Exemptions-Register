package com.northgateps.nds.beis.ui.view.javascript.registersearchgdargdip;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.RegisterSearchGdarGdipPageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.RegisterSearchGdarGdipPageObject;
import com.northgateps.nds.beis.ui.view.javascript.base.AlternateUrlBaseSteps;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class RegisterSearchGdarGdipSteps extends AlternateUrlBaseSteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    RegisterSearchGdarGdipPageHelper pageHelper;
    RegisterSearchGdarGdipPageObject pageObject;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        testHelper.openUrl(GetUrl("register-search-gdar-gdip"));
        pageHelper = new RegisterSearchGdarGdipPageHelper(testHelper.getScenarioWebDriver());
        pageObject = pageHelper.getPageObject();
    }

    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }

    @Given("^I am on the 'register-search-gdar-gdip' page$")
    public void i_am_on_the_register_search_gdar_gdip_page() throws Throwable {
        pageHelper = new RegisterSearchGdarGdipPageHelper(pageObject.getDriver());
        checkOnPage(pageHelper, "register-search-gdar-gdip");
        // check on page gets the new page object, therefore page object needs to be updated
        pageObject = pageHelper.getPageObject();
    }

    @Given("^I have not supplied a reference$")
    public void i_have_not_supplied_a_reference() throws Throwable {
        pageObject.setTextNdsInputSearchTerm("");
    }

    @Given("^I have supplied an invalid reference$")
    public void i_have_supplied_an_invalid_reference() throws Throwable {
        pageObject.setTextNdsInputSearchTerm("123-123");
    }

    @When("^I select 'Download'$")
    public void i_select_Download() throws Throwable {
        pageObject.clickButtonSearch_SearchGdarGdip();       
    }

    @Then("^I will receive the error \"(.*?)\"$")
    public void i_will_receive_the_error(String message) throws Throwable {
        assertEquals("check error", message, pageHelper.findFaultMessage(message));
    }

    @Then("^I will receive the message \"(.*?)\"$")
    public void i_will_receive_the_message(String message) throws Throwable {
        assertEquals("check message", message, pageObject.getTextDivContents());
    }

    @Then("^I will receive the message$")
    public void i_will_receive_the_multi_line_message(String message) throws Throwable {
        message = message.replace("\r", "");
        String htmlMessage = pageObject.getTextDivContents();
        assertEquals("check message", message, htmlMessage);
    }

    @Then("^I will remain on the 'register-search-gdar-gdip' page$")
    public void i_will_remain_on_the_register_search_gdar_gdip_page() throws Throwable {
        checkOnPage(pageHelper, "register-search-gdar-gdip");
        // check on page gets the new page object, therefore page object needs to be updated
        pageObject = pageHelper.getPageObject();
    }    

    @Given("^I have supplied a valid reference$")
    public void i_have_supplied_a_valid_reference() throws Throwable { 
        pageObject.setTextNdsInputSearchTerm("0000-1111-2222-3333-6666");
    }

    @Given("^no document exists for the reference$")
    public void no_document_exists_for_the_reference() throws Throwable {
        // ref with no document
        pageObject.setTextNdsInputSearchTerm("0000-1111-2222-3333-6655");
    }

    @Given("^document identified is not the latest for the property$")
    public void document_identified_is_not_the_latest_for_the_property() throws Throwable {
        // ref with superseded
        pageObject.setTextNdsInputSearchTerm("0000-1111-2222-3333-6666");
    }

    @Given("^a document cannot be returned due to an unexpected error$")
    public void a_document_cannot_be_returned_due_to_an_unexpected_error() throws Throwable {
        // TODO: work this one out
    }

    @Given("^a document exists for the reference$")
    public void a_document_exists_for_the_reference() throws Throwable {
        // document available ref
        pageObject.setTextNdsInputSearchTerm("0000-1111-2222-3333-7777");
    }

    @Then("^the document will be downloaded as a PDF file$")
    public void the_document_will_be_downloaded_as_a_PDF_file() throws Throwable {
        // TODO; need to check the file is actually downloaded
    }

    @Then("^I will see the name of document to download$")
    public void i_will_see_the_name_of_document_to_download() throws Throwable {
        assertTrue(pageObject.getWebElementAnchorDownloadLink().isDisplayed());
        assertEquals("checking file name", "GDAR_0000-1111-2222-3333-7777.PDF",
                pageObject.getWebElementAnchorDownloadLink().getText());
    }

    @When("^I click on the name of the document$")
    public void i_click_on_the_name_of_the_document() throws Throwable {
        pageObject.clickAnchorDownloadLink();
    }

}