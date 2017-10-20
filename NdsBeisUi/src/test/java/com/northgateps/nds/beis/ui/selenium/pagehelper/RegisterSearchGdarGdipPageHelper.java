package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.RegisterSearchGdarGdipPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = RegisterSearchGdarGdipPageObject.class)
public class RegisterSearchGdarGdipPageHelper extends BasePageHelper<RegisterSearchGdarGdipPageObject>
        implements PageHelper {

    public RegisterSearchGdarGdipPageHelper(WebDriver driver) {
        super(driver);
    }

    public RegisterSearchGdarGdipPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

	@Override
	public PageHelper skipPage() {
		
		//no need to skip as this is an end page
		return null;
	}
	
}