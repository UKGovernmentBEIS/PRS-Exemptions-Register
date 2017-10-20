package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedChangePasswordConfirmationPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;
/**
 * Selenium test helper class for personalised-change-password-confirmation page
 * */
@PageObject(pageObjectClass = PersonalisedChangePasswordConfirmationPageObject.class)
public class PersonalisedChangePasswordConfirmationPageHelper extends BasePageHelper<PersonalisedChangePasswordConfirmationPageObject> implements PageHelper {

	WebDriver driver;
	
	public PersonalisedChangePasswordConfirmationPageHelper(WebDriver driver) {
		super(driver);
		
		this.driver = driver;
	}

	public PersonalisedChangePasswordConfirmationPageHelper(WebDriver driver, Locale locale) {
		super(driver, locale);
	}
	
	@Override
	public PageHelper skipPage() {
		final PersonalisedChangePasswordConfirmationPageObject pageObject = getPageObject();		
		pageObject.clickNext();
		BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
		return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
	}
}