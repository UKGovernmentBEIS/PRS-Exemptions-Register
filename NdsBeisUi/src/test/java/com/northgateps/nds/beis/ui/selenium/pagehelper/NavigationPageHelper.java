package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;
import org.openqa.selenium.WebDriver;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedSelectPropertyTypePageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = PersonalisedSelectPropertyTypePageObject.class)
public class NavigationPageHelper extends BasePageHelper<PersonalisedSelectPropertyTypePageObject>
        implements PageHelper {

    public NavigationPageHelper(WebDriver driver) {
        super(driver);
    }

    public NavigationPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        //Auto-generated method stub
        return null;
    }

}
