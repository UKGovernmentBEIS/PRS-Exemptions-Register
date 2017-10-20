package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionConfirmationPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Selenium test helper class for the exemption confirmation page helper
 */
@PageObject(pageObjectClass = PersonalisedExemptionConfirmationPageObject.class)
public class PersonalisedExemptionConfirmationPageHelper
        extends BasePageHelper<PersonalisedExemptionConfirmationPageObject> implements PageHelper {

    public PersonalisedExemptionConfirmationPageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedExemptionConfirmationPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        final PersonalisedExemptionConfirmationPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickAnchorFinish();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }
}
