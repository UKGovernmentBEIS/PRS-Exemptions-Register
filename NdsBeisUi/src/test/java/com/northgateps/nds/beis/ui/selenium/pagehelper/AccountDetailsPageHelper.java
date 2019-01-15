package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import com.google.common.base.Function;
import com.northgateps.nds.beis.ui.selenium.pageobject.AccountDetailsPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = AccountDetailsPageObject.class)
public class AccountDetailsPageHelper extends BasePageHelper<AccountDetailsPageObject> implements PageHelper {
    
    // set default FormFiller
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                fillInForm("PERSON");
            }
        });
    }

    public AccountDetailsPageHelper(WebDriver driver) {
        super(driver);
    }

    public AccountDetailsPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }
    
    public void fillInForm(String type) {
        AccountDetailsPageObject pageObject = getNewPageObject();   
        
        if (type.equals("PERSON")) {
            pageObject.setTextNdsInputFirstname("John");
            pageObject.setTextNdsInputSurname("Smith");
        } else if(type.equals("AGENT")) {
            pageObject.setTextNdsInputAgentName("Test Agent");
        }else{
            pageObject.setTextNdsInputOrgName("dummy");
        }    
        pageObject.setTextNdsInputEmail("nds-dummyOne@northgateps.com");
        pageObject.setTextNdsInputConfirmEmail("nds-dummyOne@northgateps.com");
        pageObject.setTextNdsInputTelNumber("0115 921 0200");
    }
    
    @Override
    public PageHelper skipPage() {
        final AccountDetailsPageObject pageObject = getPageObject();
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
