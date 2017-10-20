package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.HelloUserPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Selenium test helper class for hello-user page.
 * 
 * Note that the logout link is hidden and a menu one added if the screen size is reduced enough
 * (as it may be on the autotest server).  @see {@link #openMenu()}
 */
@PageObject(pageObjectClass = HelloUserPageObject.class)
public class HelloUserPageHelper extends BasePageHelper<HelloUserPageObject> implements PageHelper {
    
    public HelloUserPageHelper(WebDriver driver) {
        super(driver);        
    }

    public HelloUserPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);        
    }

    @Override
    public void fillInForm() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PageHelper skipPage() {
        throw new UnsupportedOperationException();
    }

    public void clickMyAccountDetailsButton() {
        openMenu();
        getPageObject().clickButtonMyAccountDetails_NEXTMyAccountDetails();
    }

    /** Opens the menu that is shown when the screen is small, if available. */
    public void openMenu() {
    	if (getPageObject().getWebElementAnchorTogglemenu().isDisplayed()) {
            getPageObject().clickAnchorTogglemenu();
        }
    }
}