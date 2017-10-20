package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedChangePasswordPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Page helper for the Change Password screen
 */
@PageObject(pageObjectClass = PersonalisedChangePasswordPageObject.class)
public class PersonalisedChangePasswordPageHelper extends BasePageHelper<PersonalisedChangePasswordPageObject> implements PageHelper {

	WebDriver driver;
	
	public PersonalisedChangePasswordPageHelper(WebDriver driver) {
		super(driver);
		
		this.driver = driver;
	}

	public PersonalisedChangePasswordPageHelper(WebDriver driver, Locale locale) {
		super(driver, locale);
	}

	@Override
	public PageHelper skipPage() {
		final PersonalisedChangePasswordPageObject pageObject = getPageObject();
		fillInForm();
		pageObject.invalidateDcId();
		pageObject.clickNext();
		BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
		return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
	}
	
}
