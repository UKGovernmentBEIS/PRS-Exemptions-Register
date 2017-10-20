package com.northgateps.nds.beis.ui.selenium.pagehelper;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;

import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedAccountSummaryPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Selenium test helper class for personalised-account-summary page
 */
@PageObject(pageObjectClass = PersonalisedAccountSummaryPageObject.class)
public class PersonalisedAccountSummaryPageHelper extends BasePageHelper<PersonalisedAccountSummaryPageObject> implements PageHelper {

	public PersonalisedAccountSummaryPageHelper(WebDriver driver) {
		super(driver);
	}

	public PersonalisedAccountSummaryPageHelper(WebDriver driver, Locale locale) {
		super(driver, locale);
	}
	
	
    @Override
	public PageHelper skipPage() {
		final PersonalisedAccountSummaryPageObject pageObject = getPageObject();
		fillInForm();
		pageObject.invalidateDcId();
		pageObject.clickNext();
		BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
		return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
	}

    
	public boolean checkFirstNameAndLastName() {
		return getPageObject().getWebElementButtonChangePersonalDetails_NEXTEditPersonalDetails().isDisplayed();
	}

    public boolean checkOrgName() {        
        return getPageObject().getWebElementButtonChangeOrganisationDetails_NEXTEditPersonalDetails().isDisplayed();        
    }
   
   
    /**
     * Helper method to skip to personalised-change-account-details page for org
     */
    public PersonalisedChangeAccountDetailsPageHelper skipToChangeAccountDetailsPage() {
        final PersonalisedAccountSummaryPageObject pageObject = getPageObject();
        checkOnPage(this, "personalised-account-summary");       
        pageObject.clickButtonChangeOrganisationDetails_NEXTEditPersonalDetails();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return new PersonalisedChangeAccountDetailsPageHelper(getPageObject().getDriver(), getLocale());
    }
    
    /**
     * Helper method to skip to personalised-change-account-details page for person
     */
    public PersonalisedChangeAccountDetailsPageHelper skipToChangeAccountDetailsPageForPerson() {
        final PersonalisedAccountSummaryPageObject pageObject = getPageObject();
        checkOnPage(this, "personalised-account-summary");       
        pageObject.clickButtonChangePersonalDetails_NEXTEditPersonalDetails();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return new PersonalisedChangeAccountDetailsPageHelper(getPageObject().getDriver(), getLocale());
    }
    
    
    /**
     * Helper method to skip to personalised-change-account-details page for agent
     */
    public PersonalisedChangeAccountDetailsPageHelper skipToChangeAccountDetailsPageForAgent() {
        final PersonalisedAccountSummaryPageObject pageObject = getPageObject();
        checkOnPage(this, "personalised-account-summary");       
        pageObject.clickButtonChangeAgentDetails_NEXTEditPersonalDetails();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return new PersonalisedChangeAccountDetailsPageHelper(getPageObject().getDriver(), getLocale());
    }
    
    /**
     * Steer method to skip to personalised-change-account-address page
     */
    public PersonalisedChangeAccountAddressPageHelper skipToChangeAccountAddress() {
        final PersonalisedAccountSummaryPageObject pageObject = getPageObject();
        checkOnPage(this, "personalised-account-summary");       
        pageObject.clickButtonChangeAddress_NEXTChangeAddress();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return new PersonalisedChangeAccountAddressPageHelper(getPageObject().getDriver(), getLocale());
    }
    
    /**
     * Steer method to skip to personalised-change-email-address page
     */
    public PersonalisedChangeEmailAddressPageHelper skipToChangeEmailAddress() {
        final PersonalisedAccountSummaryPageObject pageObject = getPageObject();
        checkOnPage(this, "personalised-account-summary");       
        pageObject.clickButtonChangeEmailID_NEXTChangeEmail();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return new PersonalisedChangeEmailAddressPageHelper(getPageObject().getDriver(), getLocale());
    }
    
    /**
     * Steer method to skip to personalised-change-password page
     */
    public PersonalisedChangePasswordPageHelper skipToChangePassword() {
        final PersonalisedAccountSummaryPageObject pageObject = getPageObject();
        checkOnPage(this, "personalised-account-summary");       
        pageObject.clickButtonChangePassword_NEXTChangePassword();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return new PersonalisedChangePasswordPageHelper(getPageObject().getDriver(), getLocale());
    }

    /** 
     * Users may be logged in as an individual or an organisation.  This method expects to be used
     * for a user but will still return the organisation name and whatever comes next (eg telephone number)
     * for an organisation.
     * 
     * @return first and last name (separated by a space)
     */
	public String getUsersFirstLastName() {
		PersonalisedAccountSummaryPageObject apo = getNewPageObject();
		WebElement mainSection = apo.getWebElementMainContent();
		
		WebElement firstNameElement = mainSection.findElement(By.xpath("//*[@id='formwrap']/div/table/tbody/tr[1]/td[2]"));
		WebElement lastNameElement = mainSection.findElement(By.xpath("//*[@id='formwrap']/div/table/tbody/tr[2]/td[2]"));
		
		String name = firstNameElement.getText() + " " + lastNameElement.getText();
		
		return name;
	}
    
  
}