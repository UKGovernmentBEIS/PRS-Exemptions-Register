package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import com.google.common.base.Function;

import com.northgateps.nds.beis.ui.selenium.pageobject.AccountActivationPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = AccountActivationPageObject.class)
public class AccountActivationPageHelper extends BasePageHelper<AccountActivationPageObject> implements PageHelper {
    public AccountActivationPageHelper(WebDriver driver) {
        super(driver);
    }

    public AccountActivationPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        final AccountActivationPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }    
    
    public void waitUntilValidationMessageSeen(final String validationMessage) {
        getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                     return validationMessage.equals(findFaultMessage(validationMessage));
                } catch (StaleElementReferenceException e) {
                    getNewPageObject();
                    return false; // try again
                }
            }
        });
    }
}
