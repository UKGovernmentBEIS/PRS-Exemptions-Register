package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.SelectLandlordOrAgentPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = SelectLandlordOrAgentPageObject.class)
public class SelectLandlordOrAgentPageHelper extends BasePageHelper<SelectLandlordOrAgentPageObject> implements PageHelper {
    
    // set default FormFiller
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                fillInForm("LANDLORD");
            }
        });
    }
    
    public SelectLandlordOrAgentPageHelper(WebDriver driver) {
        super(driver);
    }

    public SelectLandlordOrAgentPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }
    
    public void fillInForm(String type) {
        SelectLandlordOrAgentPageObject pageObject = getNewPageObject();   
        
        if (type.equals("LANDLORD")) {           
            pageObject.clickNdsRadiobuttonelementUserType("LANDLORD");
        } else {
            pageObject.clickNdsRadiobuttonelementUserType("AGENT");            
        }            
    }
    
    @Override
    public PageHelper skipPage() {
        final SelectLandlordOrAgentPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }
}
