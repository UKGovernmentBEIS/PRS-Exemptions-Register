package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedSelectPropertyTypePageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Selenium test helper class for the personalised-select-property-type page
 */
@PageObject(pageObjectClass = PersonalisedSelectPropertyTypePageObject.class)
public class PersonalisedSelectPropertyTypePageHelper extends BasePageHelper<PersonalisedSelectPropertyTypePageObject> implements PageHelper {

    // set default FormFiller
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                fillInForm("NONDOMESTIC");
            }
        });
    }
    public PersonalisedSelectPropertyTypePageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedSelectPropertyTypePageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

  
    @Override
    public PersonalisedSelectExemptionTypePageHelper skipPage(){ 
        final PersonalisedSelectPropertyTypePageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return (PersonalisedSelectExemptionTypePageHelper)PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }

    
    public void fillInForm(String type) {
        if (type.equals("DOMESTIC")) {
            getPageObject().clickNdsRadiobuttonelementPropertyType("PRSD");
        } else {
            getPageObject().clickNdsRadiobuttonelementPropertyType("PRSN");            
        }
    }

  }
