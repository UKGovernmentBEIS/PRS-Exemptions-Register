package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionRequirementsPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Selenium test helper class for the exemption requirements page helper
 */
@PageObject(pageObjectClass = PersonalisedExemptionRequirementsPageObject.class)
public class PersonalisedExemptionRequirementsPageHelper extends BasePageHelper<PersonalisedExemptionRequirementsPageObject>
        implements PageHelper {

    public PersonalisedExemptionRequirementsPageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedExemptionRequirementsPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        final PersonalisedExemptionRequirementsPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }

}
