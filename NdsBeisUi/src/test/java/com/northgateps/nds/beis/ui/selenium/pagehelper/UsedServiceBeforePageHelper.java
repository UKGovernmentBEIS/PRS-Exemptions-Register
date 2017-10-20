package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;
import com.northgateps.nds.beis.ui.selenium.pageobject.UsedServiceBeforePageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Page Helper for the Used Service Before page
 */
@PageObject(pageObjectClass = UsedServiceBeforePageObject.class)
public class UsedServiceBeforePageHelper extends BasePageHelper<UsedServiceBeforePageObject> implements PageHelper {
    
    //set default Form Filler
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                fillInForm(true);
            }
        });
    }

    public UsedServiceBeforePageHelper(WebDriver driver) {
        super(driver);
    }

    public UsedServiceBeforePageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    public void fillInForm(boolean loginOrRegister) {
        UsedServiceBeforePageObject pageObject = getNewPageObject();   
        
        if (loginOrRegister) {
            pageObject.clickNdsRadiobuttonUsedServiceBefore_True();
            pageObject.clickNdsRadiobuttonUsedServiceBefore_True();  /*not clear why first click doesn't always work,
                                                                     Richard is upgrading Serenity which may fix this sort of quirk*/
        } else {
            pageObject.clickNdsRadiobuttonUsedServiceBefore_False();
            pageObject.clickNdsRadiobuttonUsedServiceBefore_False();
        }   
    }
    
    public PageHelper skipPage() {
        UsedServiceBeforePageObject pageObject = getPageObject();
        fillInForm();
        pageObject.clickButtonNext_NEXTUsedservicebefore();
        BasePageHelper.waitUntilPageLoading(pageObject.getDriver());
        String newPage = getPageObject().getDcId();
        PageHelper output = PageHelperFactory.build(newPage, getPageObject().getDriver(), getLocale());
        return output;
    }
    
}