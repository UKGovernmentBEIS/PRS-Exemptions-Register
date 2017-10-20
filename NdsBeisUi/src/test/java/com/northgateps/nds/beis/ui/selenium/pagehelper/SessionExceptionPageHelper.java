package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.northgateps.nds.beis.ui.selenium.pageobject.SessionExceptionPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = SessionExceptionPageObject.class)
public class SessionExceptionPageHelper extends BasePageHelper<SessionExceptionPageObject> implements PageHelper {

    public SessionExceptionPageHelper(WebDriver driver) {
        super(driver);
    }

    public SessionExceptionPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        final SessionExceptionPageObject pageObject = getPageObject();
        
        // does nothing but allows a FormFiller to be set
        fillInForm();
        
        // find the link
        WebElement link = pageObject.getDriver().findElement(By.id("start_again"));
        link.click();
        
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale()); 
    }
}
