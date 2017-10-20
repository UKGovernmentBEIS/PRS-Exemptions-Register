package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PasswordResetConfirmationPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * @author Ben Cory
 * This is a helper class for report Password reset conformation to help write the steps file
 * for Activate password reset page helper selenium tests.
 */
@PageObject(pageObjectClass = PasswordResetConfirmationPageObject.class)    
public class PasswordResetConfirmationPageHelper extends BasePageHelper<PasswordResetConfirmationPageObject> implements PageHelper{

    public PasswordResetConfirmationPageHelper(WebDriver driver) {
        super(driver);
    }

    public PasswordResetConfirmationPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }    
    
    @Override
    public PageHelper skipPage() {
        
        final PasswordResetConfirmationPageObject pageObject = getPageObject();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());

    }
    
}
