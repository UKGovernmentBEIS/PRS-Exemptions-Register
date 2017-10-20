package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionListOfValuesPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Selenium test helper class for the exemption list of values page helper
 */
@PageObject(pageObjectClass = PersonalisedExemptionListOfValuesPageObject.class)
public class PersonalisedExemptionListOfValuesPageHelper extends BasePageHelper<PersonalisedExemptionListOfValuesPageObject>
        implements PageHelper {
    
    //set a default Form Filler
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                getPageObject().getDriver().findElement(By.id("exemptionDetails.exemptionReason1")).click();
            }
        });
    }

    public PersonalisedExemptionListOfValuesPageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedExemptionListOfValuesPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        final PersonalisedExemptionListOfValuesPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }
    
    public PageHelper skipPage(String optionalText) {
        final PersonalisedExemptionListOfValuesPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.setTextNdsTextareaExemptionReasonAdditionalText(optionalText);
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }

}
