package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedSelectLandlordTypeAgentPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = PersonalisedSelectLandlordTypeAgentPageObject.class)
public class PersonalisedSelectLandlordTypeAgentPageHelper extends BasePageHelper<PersonalisedSelectLandlordTypeAgentPageObject> implements PageHelper {
    
    // set default FormFiller
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                fillInForm("PERSON");
            }
        });
    }
    
    public PersonalisedSelectLandlordTypeAgentPageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedSelectLandlordTypeAgentPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }
    
    public void fillInForm(String type) {
        PersonalisedSelectLandlordTypeAgentPageObject pageObject = getNewPageObject();   
        
        if (type.equals("PERSON")) {
            pageObject.clickNdsRadiobuttonelementAccountType("PERSON");
        } else {
            pageObject.clickNdsRadiobuttonelementAccountType("ORGANISATION");            
        }            
    }
    
    @Override
    public PageHelper skipPage() {
        final PersonalisedSelectLandlordTypeAgentPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }
}
