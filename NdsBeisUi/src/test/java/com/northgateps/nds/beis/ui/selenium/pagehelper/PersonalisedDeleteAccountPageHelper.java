package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDeleteAccountPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Page helper for the Delete user account screen
 */
@PageObject(pageObjectClass = PersonalisedDeleteAccountPageObject.class)
public class PersonalisedDeleteAccountPageHelper extends BasePageHelper<PersonalisedDeleteAccountPageObject> implements PageHelper {

	WebDriver driver;
	
	public PersonalisedDeleteAccountPageHelper(WebDriver driver) {
		super(driver);
		
		this.driver = driver;
	}

	public PersonalisedDeleteAccountPageHelper(WebDriver driver, Locale locale) {
		super(driver, locale);
	}

	@Override
	public PageHelper skipPage() {
		final PersonalisedDeleteAccountPageObject pageObject = getPageObject();
		fillInForm();
		pageObject.invalidateDcId();
		pageObject.clickNext();
		BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
		return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
	}
	
}
