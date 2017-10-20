package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedEndExemptionConfirmationPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Selenium test helper class for personalised-end-exemption-confirmation page
 */
@PageObject(pageObjectClass = PersonalisedEndExemptionConfirmationPageObject.class)
public class PersonalisedEndExemptionConfirmationPageHelper extends BasePageHelper<PersonalisedEndExemptionConfirmationPageObject> implements PageHelper {

	public PersonalisedEndExemptionConfirmationPageHelper(WebDriver driver) {
		super(driver);
	}

	public PersonalisedEndExemptionConfirmationPageHelper(WebDriver driver, Locale locale) {
		super(driver, locale);
	}

	@Override
	public PageHelper skipPage() {
		final PersonalisedEndExemptionConfirmationPageObject pageObject = getPageObject();
		fillInForm();
		pageObject.invalidateDcId();
		pageObject.clickNext();
		BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
		return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
	}

}