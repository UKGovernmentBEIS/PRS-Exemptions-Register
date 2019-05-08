package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDeleteAccountPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedFurtherInformationPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Page helper for the further information screen
 */
@PageObject(pageObjectClass = PersonalisedFurtherInformationPageObject.class)
public class PersonalisedFurtherInformationPageHelper extends BasePageHelper<PersonalisedFurtherInformationPageObject> implements PageHelper {
	
	
    //set default Form Filler
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                getPageObject().clickInputExemptionConfirmationIndicator_Y();
            }
        });
    }

	
	public PersonalisedFurtherInformationPageHelper(WebDriver driver) {
		super(driver);
	}

	public PersonalisedFurtherInformationPageHelper(WebDriver driver, Locale locale) {
		super(driver, locale);
	}

	@Override
	public PageHelper skipPage() {
		final PersonalisedFurtherInformationPageObject pageObject = getPageObject();
		fillInForm();
		pageObject.invalidateDcId();
		pageObject.clickNext();
		BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
		return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
	}
	
}
