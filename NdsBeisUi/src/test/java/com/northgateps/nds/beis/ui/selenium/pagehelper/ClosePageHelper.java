package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.ClosePageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Page helper for the Close page screen
 */
@PageObject(pageObjectClass = ClosePageObject.class)
public class ClosePageHelper extends BasePageHelper<ClosePageObject> implements PageHelper {

	WebDriver driver;
	
	public ClosePageHelper(WebDriver driver) {
		super(driver);
		
		this.driver = driver;
	}

	public ClosePageHelper(WebDriver driver, Locale locale) {
		super(driver, locale);
	}

	@Override
	public PageHelper skipPage() {
		final ClosePageObject pageObject = getPageObject();
		fillInForm();
		pageObject.invalidateDcId();
		pageObject.clickNext();
		BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
		return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
	}
	
}
