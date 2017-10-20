package com.northgateps.nds.beis.ui.view.javascript.contentsecurity;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.*;

import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.WebDriver;
import com.northgateps.nds.beis.ui.selenium.pagehelper.LoginPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PageHelperFactory;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionDeclarationPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionListOfValuesPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedExemptionTextPageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.PersonalisedSelectExemptionTypePageHelper;
import com.northgateps.nds.beis.ui.selenium.pagehelper.UsedServiceBeforePageHelper;
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

public class ContentSecuritySteps {

    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json",
            this.getClass().getClassLoader());

    private final String loginUsername = (String) testProperties.get("loginUsername");
    private final String loginPassword = (String) testProperties.get("loginPassword");

    private SeleniumCucumberTestHelper testHelper = new SeleniumCucumberTestHelper();
    UsedServiceBeforePageHelper firstPageHelper;

    @Managed
    private WebDriver webDriver;

	private PersonalisedExemptionListOfValuesPageHelper personalisedExemptionListOfValuesPageHelper;
	private PersonalisedExemptionTextPageHelper personalisedExemptionTextPageHelper;

	private BasePageHelper<?> endPageHelper;

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

    @Given("^I am on the personalised-exemption-list-of-values page with option \"(.*?)\"$")
    public void i_am_on_the_personalised_exemption_declaration_page_with_option(String code) throws Throwable {
        
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
        // skip to the appropriate page
        try {
            if("NEW".equals(code))
            {
                personalisedExemptionListOfValuesPageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedExemptionListOfValuesPageHelper.class);
                checkOnPage(personalisedExemptionListOfValuesPageHelper, "personalised-exemption-list-of-values");
            }
            else if ("ALLIMP".equals(code)) {
                personalisedExemptionTextPageHelper = PageHelperFactory.visitNew(firstPageHelper, PersonalisedExemptionTextPageHelper.class);
                checkOnPage(personalisedExemptionTextPageHelper, "personalised-exemption-text");
            }
            else {
                throw new NotImplementedException(code);
            }
        } finally {
            PageHelperFactory.unregisterFormFiller("login-form");
            PageHelperFactory.unregisterFormFiller("personalised-select-exemption-type");
        }
    }

    @When("^I enter the test text for \"(.*?)\"")
    public void i_enter_the_test_text_for(String code) throws Throwable {
    	StringBuilder sb = new StringBuilder();
    	
    	if ("NEW".equals(code)) {
	    	sb.append("_I got to know on 01-Jan-17 but it happened ~ 01/12/2016. I'm not sure what is meant by '(for the avoidance of doubt, a \\\"guarantor\\\" who exercises");
	    	sb.append("this right under the 1995 Act is the guarantor of a former tenant)'Could someone explain?_");
	
	    	personalisedExemptionListOfValuesPageHelper.skipPage(sb.toString());
    	} else if ("ALLIMP".equals(code)) {
	    	sb.append("_They installed a pipe that was 35cms [1' 2''] and another that was 70.5 cms {2' 4\\\"}. It cost £924.76 + £100.00 materials");
	    	sb.append("My share was a half, £1,024.76/2 = £512.38 If its > 600 I get a 10% discount If its < 600 I pay in full The chap's email address");
	    	sb.append("is bob@pipes.co.uk. Website is HTTP://WWW.BOBSPIPES.CO.UK Phone +44 (0)7879 697778 They also installed the following:");
	    	sb.append("#1 Window 3.5 * 1.2 m's - in the rear wall #2 Insulation in roof & walls");
	    	
	    	personalisedExemptionTextPageHelper.skipPage(sb.toString());
    	} else {
    		throw new NotImplementedException(code);
    	}
    }

    @Given("^I select mandatory field and submit$")
    public void i_select_mandatory_field_and_submit() throws Throwable {
    	endPageHelper = (BasePageHelper<?>) new PersonalisedExemptionDeclarationPageHelper(webDriver);
    	
    	// check if the previous step triggered a mod-security "Security check" page
    	checkNotOnPage(endPageHelper, "406");
    	
    	endPageHelper.skipPage();
    }

    /** NB this will fail saying it's on the 406 page if a mod-security error happens */
    @Then("^I will be taken to personalised-exemption-confirmation page$")
    public void i_will_be_taken_to_personalised_exemption_confirmation_page() throws Throwable {
        checkOnPage(endPageHelper, "personalised-exemption-confirmation");
    }   
}