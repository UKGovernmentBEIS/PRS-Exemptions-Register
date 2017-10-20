package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.ForgottenPasswordConfirmationPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * @author Ben Cory
 * This is a helper class for report forgotten password to help write the steps file
 * for selenium tests.
 */
@PageObject(pageObjectClass = ForgottenPasswordConfirmationPageObject.class)
public class ForgottenPasswordConfirmationPageHelper extends BasePageHelper<ForgottenPasswordConfirmationPageObject> implements PageHelper {

    public ForgottenPasswordConfirmationPageHelper(WebDriver driver) {
        super(driver);
    }

    public ForgottenPasswordConfirmationPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        final ForgottenPasswordConfirmationPageObject pageObject = getPageObject();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }    
}
