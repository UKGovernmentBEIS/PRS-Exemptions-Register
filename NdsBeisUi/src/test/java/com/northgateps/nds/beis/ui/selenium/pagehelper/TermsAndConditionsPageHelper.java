package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.TermsAndConditionsPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = TermsAndConditionsPageObject.class)
public class TermsAndConditionsPageHelper extends BasePageHelper<TermsAndConditionsPageObject> implements PageHelper {

    public TermsAndConditionsPageHelper(WebDriver driver) {
        super(driver);
    }

    public TermsAndConditionsPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public void fillInForm() {

    }

    @Override
    public PageHelper skipPage() {
        return null;

    }
}
