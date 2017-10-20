package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedChangeAccountAddressPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Page helper for the Change Account Address screen
 */
@PageObject(pageObjectClass = PersonalisedChangeAccountAddressPageObject.class)
public class PersonalisedChangeAccountAddressPageHelper extends BasePageHelper<PersonalisedChangeAccountAddressPageObject> implements PageHelper {

	WebDriver driver;
	
	//set default Form Filler
	{
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                getPageObject().clickAnchorEnterAddressManually();
                fillInForm("Flat 1, Projection West", "Merchants Place", "READING", "", "RG1 1ET");
            }
        });
    }
	
	public PersonalisedChangeAccountAddressPageHelper(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	public PersonalisedChangeAccountAddressPageHelper(WebDriver driver, Locale locale) {
		super(driver, locale);
	}
	
	public void fillInForm(String line0, String line1, String town, String county, String postcode) {
        final PersonalisedChangeAccountAddressPageObject pageObject = getPageObject();
        pageObject.setTextNdsInputLine0(line0);
        pageObject.setTextNdsInputLine1(line1);
        pageObject.setTextNdsInputTown(town);
        pageObject.setTextNdsInputCounty(county);
        pageObject.setTextNdsInputPostcode(postcode);
    }

	@Override
	public PageHelper skipPage() {
		final PersonalisedChangeAccountAddressPageObject pageObject = getPageObject();
		fillInForm();
		pageObject.invalidateDcId();
		pageObject.clickNext();
		BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
		return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
	}
		
	public WebElement getCurrentAddressSelectList() {

        return getPageObject().getDriver().findElement(
                By.id("uiData.addressFieldsmodifiedContactAddress.selectedAddressRef"));
    }
	
	public WebElement getCurrentAddressInput(String className) {
        return getPageObject().getWebElementDivDetail().findElement(
                By.cssSelector("input." + cssSelectorEscape(className)));
    }
		
    public Select getAddressCountryElement() {
        Select dropdown = new Select(
                getPageObject().getDriver().findElement(By.id("modifiedContactAddress.country")));
        return dropdown;
    }
}
