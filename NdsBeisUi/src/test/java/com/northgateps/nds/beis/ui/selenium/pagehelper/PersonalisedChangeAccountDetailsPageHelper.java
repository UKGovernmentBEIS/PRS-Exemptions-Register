package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedChangeAccountDetailsPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Selenium test helper class for personalised-change-account-details page
 */
@PageObject(pageObjectClass = PersonalisedChangeAccountDetailsPageObject.class)
public class PersonalisedChangeAccountDetailsPageHelper extends BasePageHelper<PersonalisedChangeAccountDetailsPageObject> implements PageHelper {    

    // set default FormFiller
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                getPageObject().setTextNdsInputTelNumber("02085822870");
            }
        });
    }
    
    public PersonalisedChangeAccountDetailsPageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedChangeAccountDetailsPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        final PersonalisedChangeAccountDetailsPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }     
        

    public void clearAllFields() {
      getPageObject().getWebElementNdsInputOrgName().clear();
      getPageObject().getWebElementNdsInputTelNumber().clear();      
    }

    public void clearAllFieldsForPerson() {
        getPageObject().getWebElementNdsInputTelNumber().clear();
        getPageObject().getWebElementNdsInputFirstname().clear();
        getPageObject().getWebElementNdsInputSurname().clear();
        
    }

}
