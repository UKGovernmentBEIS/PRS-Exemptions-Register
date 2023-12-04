package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import com.google.common.base.Function;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedLandlordDetailsPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = PersonalisedLandlordDetailsPageObject.class)
public class PersonalisedLandlordDetailsPageHelper extends BasePageHelper<PersonalisedLandlordDetailsPageObject> implements PageHelper {
    
    // set default FormFiller
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                fillInForm("PERSON");
            }
        });
    }

    public PersonalisedLandlordDetailsPageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedLandlordDetailsPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }
    
    public void fillInForm(String type) {
        PersonalisedLandlordDetailsPageObject pageObject = getNewPageObject();   
        
        if (type.equals("PERSON")) {
            pageObject.setTextNdsInputFirstname("John");
            pageObject.setTextNdsInputSurname("Smith");
        }else{
            pageObject.setTextNdsInputOrgName("dummy");
        }    
        pageObject.setTextNdsInputEmailAddress("nds-dummyOne@necsws.com");
        pageObject.setTextNdsInputConfirmEmail("nds-dummyOne@necsws.com");
        pageObject.setTextNdsInputPhoneNumber("0115 921 0200");
    }
    
    @Override
    public PageHelper skipPage() {
        final PersonalisedLandlordDetailsPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }
    
    public void waitUntilValidationMessageSeen(final String validationMessage) {
        getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                     return validationMessage.equals(findFaultMessage(validationMessage));
                } catch (StaleElementReferenceException e) {
                    getNewPageObject();
                    return false; // try again
                }
            }
        });
    }
    
}
