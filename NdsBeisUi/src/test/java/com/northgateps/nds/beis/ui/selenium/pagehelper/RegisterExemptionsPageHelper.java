package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;
import org.openqa.selenium.WebDriver;
import com.northgateps.nds.beis.ui.selenium.pageobject.RegisterExemptionsPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = RegisterExemptionsPageObject.class)
public class RegisterExemptionsPageHelper extends BasePageHelper<RegisterExemptionsPageObject>
        implements PageHelper {

    public RegisterExemptionsPageHelper(WebDriver driver) {
        super(driver);
    }

    public RegisterExemptionsPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        final RegisterExemptionsPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }

   

}
