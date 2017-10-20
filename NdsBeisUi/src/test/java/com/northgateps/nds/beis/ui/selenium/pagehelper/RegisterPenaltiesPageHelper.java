package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;
import org.openqa.selenium.WebDriver;
import com.northgateps.nds.beis.ui.selenium.pageobject.RegisterPenaltiesPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = RegisterPenaltiesPageObject.class)
public class RegisterPenaltiesPageHelper extends BasePageHelper<RegisterPenaltiesPageObject>
        implements PageHelper {

    public RegisterPenaltiesPageHelper(WebDriver driver) {
        super(driver);
    }

    public RegisterPenaltiesPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        final RegisterPenaltiesPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }

   

}
