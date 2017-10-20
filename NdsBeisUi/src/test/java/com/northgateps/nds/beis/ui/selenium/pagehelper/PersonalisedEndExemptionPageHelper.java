package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedEndExemptionPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Selenium test helper class for personalised-end-exemption page
 */
@PageObject(pageObjectClass = PersonalisedEndExemptionPageObject.class)
public class PersonalisedEndExemptionPageHelper extends BasePageHelper<PersonalisedEndExemptionPageObject>
        implements PageHelper {

    public PersonalisedEndExemptionPageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedEndExemptionPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        final PersonalisedEndExemptionPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        getPageObject().getDriver().findElement(By.id("button.next")).click();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }

}