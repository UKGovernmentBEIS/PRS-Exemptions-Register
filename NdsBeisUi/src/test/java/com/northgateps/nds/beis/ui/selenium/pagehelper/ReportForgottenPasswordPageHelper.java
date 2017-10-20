package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.ReportForgottenPasswordPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * @author Ben Cory
 * This is a helper class for report forgotten password to help write the steps file
 * for selenium tests.
 */
@PageObject(pageObjectClass = ReportForgottenPasswordPageObject.class)
public class ReportForgottenPasswordPageHelper extends BasePageHelper<ReportForgottenPasswordPageObject> implements PageHelper {

    //set default Form Filler
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                getPageObject().setTextNdsInputUsername("forgottenpassword");
            }
        });
    }
    
    public ReportForgottenPasswordPageHelper(WebDriver driver) {
        super(driver);
    }

    public ReportForgottenPasswordPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        final ReportForgottenPasswordPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }    

}
