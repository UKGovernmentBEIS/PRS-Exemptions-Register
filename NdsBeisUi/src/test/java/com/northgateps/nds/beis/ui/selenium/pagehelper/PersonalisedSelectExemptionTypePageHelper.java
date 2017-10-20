package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedSelectExemptionTypePageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Selenium test helper class for the personalised-select-exemption-type page
 */
@PageObject(pageObjectClass = PersonalisedSelectExemptionTypePageObject.class)
public class PersonalisedSelectExemptionTypePageHelper extends BasePageHelper<PersonalisedSelectExemptionTypePageObject> implements PageHelper {

    // set default FormFiller
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                fillInForm("NEW");
            }
        });
    }
    
    public PersonalisedSelectExemptionTypePageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedSelectExemptionTypePageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }
    
    public void fillInForm(String code) {
        WebDriver driver = getPageObject().getDriver();
        driver.findElement(By.xpath("//input[@value='"+code+"']")).click();   
    }
    
    @Override
    public PageHelper skipPage() {
        fillInForm();
        getPageObject().clickButtonNext_NEXT();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }
}
