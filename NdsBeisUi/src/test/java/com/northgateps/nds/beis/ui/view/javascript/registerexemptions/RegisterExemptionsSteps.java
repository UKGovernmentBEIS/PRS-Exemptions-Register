package com.northgateps.nds.beis.ui.view.javascript.registerexemptions;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.RegisterExemptionsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.RegisterSearchExemptionsPageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.RegisterExemptionsPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.RegisterSearchExemptionsPageObject;
import com.northgateps.nds.beis.ui.view.javascript.base.AlternateUrlBaseSteps;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.cukes.SeleniumCucumberTestHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Managed;

public class RegisterExemptionsSteps extends AlternateUrlBaseSteps {

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    RegisterSearchExemptionsPageHelper registerSearchExemptionsPageHelper;
    RegisterSearchExemptionsPageObject registerSearchExemptionsPageObject;
    RegisterExemptionsPageHelper pageHelper;
    RegisterExemptionsPageObject pageObject;

    @Managed
    private WebDriver webDriver;

    @Before
    public void beforeScenario() {
        testHelper.beforeScenario();
        testHelper.setScenarioWebDriver(webDriver);
        testHelper.openUrl(GetUrl("register-search-exemptions"));
        registerSearchExemptionsPageHelper = new RegisterSearchExemptionsPageHelper(testHelper.getScenarioWebDriver()); 
        registerSearchExemptionsPageObject = registerSearchExemptionsPageHelper.getPageObject();
   }
    
    @After
    public void afterScenario() {
        testHelper.afterScenario();
    }

    @Given("^I am on the register-search-exemptions page$")
    public void i_am_on_the_register_search_exemptions_page() throws Throwable {       
        checkOnPage(registerSearchExemptionsPageHelper, "register-search-exemptions");
    }

    @When("^I select the property address$")
    public void i_select_the_property_address() throws Throwable {
        registerSearchExemptionsPageHelper.skipPage();   
     }

    @When("^I select the landlord name$")
    public void i_select_the_landlord_name() throws Throwable {
        registerSearchExemptionsPageHelper.skipPageClickingLandlordName();
    }

    @Then("^I will be taken to the register-exemptions page$")
    public void i_will_be_taken_to_the_register_exemption_page() throws Throwable {
        checkOnPage(registerSearchExemptionsPageHelper, "register-exemptions");
    }

    @Given("^I am on the register-exemptions page$")
    public void i_am_on_the_register_exemptions_page() throws Throwable {
        //visit target page
        pageHelper = PageHelperFactory.visitNew(registerSearchExemptionsPageHelper, RegisterExemptionsPageHelper.class);
        checkOnPage(pageHelper, "register-exemptions");
        pageObject=pageHelper.getPageObject();

    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();
    }

    @Then("^I will be taken to the register-search-exemptions page$")
    public void i_will_be_taken_to_the_register_search_exemptions_page() throws Throwable {
        checkOnPage(pageHelper, "register-search-exemptions");
    }

    @Then("^the search results will be displayed$")
    public void the_search_results_will_be_displayed() throws Throwable {
        String address1 = "Unit 1, Bracknell Beeches, Bracknell, Berkshire, RG12 7BW";
        String address2 = "Unit 12/13, Bracknell Beeches, Bracknell, Berkshire, RG12 7BW";
        String landlordname = "TestOrg";
        List<WebElement> addressCells = pageObject.getDriver().findElements(By.className("dgstandard"));
        assertTrue("check address1 is displayed",addressCells.get(0).getText().trim().contains(address1));
        assertTrue("check address2 is displayed",addressCells.get(0).getText().trim().contains(address2));
        assertTrue("check landlordname is displayed",addressCells.get(0).getText().trim().contains(landlordname));
        
    }
 
    @When("^I select 'New search'$")
    public void i_select_New_search() throws Throwable {
        registerSearchExemptionsPageHelper.click_addresslink();
        pageObject.getDriver().findElement(By.id("button.next")).click();
    }   
    
    
    @Given("^the exemption has a suitable EPC$")
    public void the_exemption_has_a_suitable_EPC() throws Throwable {
        //has suitable epc
    }

    @When("^the page is displayed$")
    public void the_page_is_displayed() throws Throwable {
       checkOnPage(pageHelper,"register-exemptions");
    }

    @Then("^I will have the option to download the EPC$")
    public void i_will_have_the_option_to_download_the_EPC() throws Throwable {
        assertTrue("check epc option",pageObject.getWebElementAnchorLink().isDisplayed());
    }

    @Then("^I will have the option to report the content$")
    public void i_will_have_the_option_to_report_the_content() throws Throwable {
        assertTrue("check epc option",pageObject.getWebElementAnchorContent().isDisplayed());
    }

    @Then("^I will remain on the 'register-exemptions' page$")
    public void i_will_remain_on_the_register_exemptions_page() throws Throwable {
        checkOnPage(pageHelper,"register-exemptions");
    }

    @When("^I will have selected the option to download the EPC$")
    public void i_will_have_selected_the_option_to_download_the_EPC() throws Throwable {
       pageObject.clickAnchorLink();
    }

    @Then("^the EPC will be downloaded$")
    public void the_EPC_will_be_downloaded() throws Throwable {
        //epc will be downloaded
    }
    
    @When("^I will have selected the option to report the content$")
    public void i_will_have_selected_the_option_to_report_the_content() throws Throwable {
        pageObject.clickAnchorContent();
    }

    @Then("^I will be taken to the 'report content email link page'$")
    public void i_will_be_taken_to_the_report_content_email_link_page() throws Throwable {
        assertTrue("check link contains mailto:",
                pageObject.getWebElementAnchorContent().getAttribute("href").contains("mailto:"));
    }
    
    @Given("^I am on the register-exemptions page with EPC available$")
    public void i_am_on_the_register_exemptions_page_with_EPC_available() throws Throwable {           
        registerSearchExemptionsPageHelper = new RegisterSearchExemptionsPageHelper(webDriver);
        
       //set a custom form filler for register-search-exemptions page
       registerSearchExemptionsPageHelper.setFormFiller(new FormFiller() {
            public void fill(BasePageHelper<?> pageHelper) {
                ((RegisterSearchExemptionsPageHelper) pageHelper).fillInForm("RG1 1ET");
            }
       });
        
        try {
            pageHelper = PageHelperFactory.visitNew(registerSearchExemptionsPageHelper, RegisterExemptionsPageHelper.class);
            checkOnPage(pageHelper, "register-exemptions");
            pageObject = pageHelper.getPageObject();

        } finally {
            PageHelperFactory.unregisterFormFiller("register-search-exemptions");
        }
    }

}
