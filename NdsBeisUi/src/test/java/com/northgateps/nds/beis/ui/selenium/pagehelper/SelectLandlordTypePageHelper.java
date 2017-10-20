package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;
import com.northgateps.nds.beis.ui.selenium.pageobject.SelectLandlordTypePageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = SelectLandlordTypePageObject.class)
public class SelectLandlordTypePageHelper extends BasePageHelper<SelectLandlordTypePageObject> implements PageHelper {
    
    // set default FormFiller
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                fillInForm("PERSON");
            }
        });
    }
    
    public SelectLandlordTypePageHelper(WebDriver driver) {
        super(driver);
    }

    public SelectLandlordTypePageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }
    
    public void fillInForm(String type) {
        SelectLandlordTypePageObject pageObject = getNewPageObject();   
        
        if (type.equals("PERSON")) {
            pageObject.clickNdsRadiobuttonAccountType_PERSON();
        } else {
            pageObject.clickNdsRadiobuttonAccountType_ORGANISATION();
        }            
    }
    
    @Override
    public PageHelper skipPage() {
        final SelectLandlordTypePageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }
}
