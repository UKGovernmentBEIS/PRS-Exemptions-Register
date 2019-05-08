package com.northgateps.nds.beis.ui.view.javascript.exemptiondeclaration;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedDashboardPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionDeclarationPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionListOfValuesPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedSelectExemptionTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedSelectPropertyTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionDeclarationPageObject;
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

public class ExemptionDeclarationSteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");
    private final String loginAgentUsername = (String) testProperties.get("loginAgentUsername");
    private final String loginAgentPassword = (String) testProperties.get("loginAgentPassword");
    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    PersonalisedExemptionDeclarationPageHelper pageHelper;
    PersonalisedExemptionDeclarationPageObject pageObject;
    PersonalisedDashboardPageHelper dashboardPageHelper;
    PersonalisedDashboardPageObject PersonalisedDashboardPageObject;
    UsedServiceBeforePageHelper firstPageHelper;

    String parentHandle;

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

    @Given("^I am on the personalised-exemption-declaration page$")
    public void i_am_on_the_personalised_exemption_declaration_page() throws Throwable {
        
		// Register a custom form filler for login page
		LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

		PageHelperFactory.registerFormFiller("login-form",
				loginPageHelper.createFormFiller(loginUsername, loginPassword));
		
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedExemptionDeclarationPageHelper.class);
            checkOnPage(pageHelper, "personalised-exemption-declaration");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }

    }

    @When("^I select Back$")
    public void i_select_Back() throws Throwable {
        pageObject.clickBack();

    }

    @Then("^I will be taken to the personalised-exemption-list-of-values page$")
    public void i_will_be_taken_to_the_personalised_exemption_list_of_values_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-exemption-list-of-values");
    }

    @Then("^the details previously entered will be displayed$")
    public void the_details_previously_entered_will_be_displayed() throws Throwable {

        PersonalisedExemptionListOfValuesPageHelper listOfValuesHelper = new PersonalisedExemptionListOfValuesPageHelper(
                webDriver);
        listOfValuesHelper.getPageObject().getDriver().findElement(By.id("exemptionDetails.exemptionReason1")).isSelected();

    }

    @Given("^I have not ticked the declaration box$")
    public void i_have_not_ticked_the_declaration_box() throws Throwable {

    }

    @When("^I select Submit exemption registration$")
    public void i_select_Submit_exemption_registration() throws Throwable {
        pageObject.clickNext();

    }

    @Then("^I will receive \"(.*?)\" as validation message$")
    public void i_will_receive_as_validation_message(String validationMessage) throws Throwable {
        assertEquals("check validation message", validationMessage, pageHelper.findFaultMessage(validationMessage));

    }

    @Then("^I will remain on the personalised-exemption-declaration page$")
    public void i_will_remain_on_the_exemption_declaration_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-exemption-declaration");

    }

    @Given("^I have ticked the declaration box$")
    public void i_have_ticked_the_declaration_box() throws Throwable {
        pageObject.clickInputIsAgreed_Agree();
    }

    @Then("^I will be taken to personalised-exemption-confirmation page$")
    public void i_will_be_taken_to_exemption_confirmation_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-exemption-confirmation");
    }

    @Given("^I select mandatory field$")
    public void i_select_mandatory_field() throws Throwable {
        pageHelper.fillInForm();
    }
    
    @Given("^I am registering a domestic exemption$")
    public void i_am_registering_a_domestic_exemption() throws Throwable {
    	
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
        
        // Register a custom form filler for select-exemption-type page
        PageHelperFactory.registerFormFiller("personalised-select-exemption-type", new FormFiller(){
            @Override
            public void fill(BasePageHelper<?> pageHelper) {
                ((PersonalisedSelectExemptionTypePageHelper)pageHelper).fillInForm("WALL");
            }
        });
        
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedExemptionDeclarationPageHelper.class);
            checkOnPage(pageHelper, "personalised-exemption-declaration");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
            PageHelperFactory.unregisterFormFiller("personalised-select-property-type");
            PageHelperFactory.unregisterFormFiller("personalised-select-exemption-type");
        }

    }

    @Given("^I am on the 'exemption-declaration' page$")
    public void i_am_on_the_exemption_declaration_page() throws Throwable {
        checkOnPage(pageHelper, "personalised-exemption-declaration");
    }

    @When("^the page is displayed$")
    public void the_page_is_displayed() throws Throwable {
        checkOnPage(pageHelper, "personalised-exemption-declaration");
    }

    @Then("^I will see text \"(.*?)\"$")
    public void i_will_see_text(String testString) throws Throwable {
       pageObject.getWebElementUlRequirementList().getText().contains(testString);
    }
    
    @Given("^I am on the personalised-exemption-declaration page with registered as agent$")
    public void i_am_on_the_personalised_exemption_declaration_page_with_registered_as_agent() throws Throwable {
        // Register a custom form filler for login page
        LoginPageHelper loginPageHelper = new LoginPageHelper(webDriver);

        PageHelperFactory.registerFormFiller("login-form",
                loginPageHelper.createFormFiller(loginAgentUsername, loginAgentPassword));
        
        try {
            pageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedExemptionDeclarationPageHelper.class);            
            checkOnPage(pageHelper, "personalised-exemption-declaration");
            pageObject = pageHelper.getPageObject();
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
        }
    }

    @Then("^I will see Landlord name$")
    public void i_will_see_Landlord_name() throws Throwable {
        webDriver.findElement(By.xpath("//table/tbody/tr/td")).getText().contains("Landlord Name");
    }

    @Then("^I will see Landlord email address$")
    public void i_will_see_Landlord_email_address() throws Throwable {
        webDriver.findElement(By.xpath("//table/tbody/tr[1]/td")).getText().contains("Landlord Email Address");
    }

    @Then("^I will see Landlord telephone number$")
    public void i_will_see_Landlord_telephone_number() throws Throwable {
        webDriver.findElement(By.xpath("//table/tbody/tr[2]/td")).getText().contains("Landlord Telephone Number");
    }




}
