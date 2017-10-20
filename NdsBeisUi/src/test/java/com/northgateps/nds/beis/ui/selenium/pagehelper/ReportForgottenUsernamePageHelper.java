package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.ReportForgottenUsernamePageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * This is a helper class for report forgotten username to help write the steps file for selenium tests.
 */
@PageObject(pageObjectClass = ReportForgottenUsernamePageObject.class)
public class ReportForgottenUsernamePageHelper extends BasePageHelper<ReportForgottenUsernamePageObject> {
    public ReportForgottenUsernamePageHelper(WebDriver driver) {
        super(driver);
    }

    public ReportForgottenUsernamePageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        return null;
    }
}
