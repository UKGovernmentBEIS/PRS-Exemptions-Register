package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.ExpiredPasswordConfirmationPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = ExpiredPasswordConfirmationPageObject.class)
public class ExpiredPasswordConfirmationPageHelper extends BasePageHelper<ExpiredPasswordConfirmationPageObject> implements PageHelper {

    

    public ExpiredPasswordConfirmationPageHelper(WebDriver driver) {
        super(driver);
    }

    public ExpiredPasswordConfirmationPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        // TODO Auto-generated method stub
        return null;
    }

  
}