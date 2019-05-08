package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionDeclarationPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = PersonalisedExemptionDeclarationPageObject.class)
public class PersonalisedExemptionDeclarationPageHelper
        extends BasePageHelper<PersonalisedExemptionDeclarationPageObject> implements PageHelper {
    
    //set default Form Filler
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                getPageObject().clickInputIsAgreed_Agree();
            }
        });
    }

    public PersonalisedExemptionDeclarationPageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedExemptionDeclarationPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        final PersonalisedExemptionDeclarationPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }

}
