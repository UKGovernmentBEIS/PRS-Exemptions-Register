package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedPropertyAddressPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Selenium test helper class for the property address page helper
 */
@PageObject(pageObjectClass = PersonalisedPropertyAddressPageObject.class)
public class PersonalisedPropertyAddressPageHelper extends BasePageHelper<PersonalisedPropertyAddressPageObject> implements PageHelper {

    //set default Form Filler
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                clickAnchorEnterAddressManually();
                fillInForm("Flat 1, Projection West", "Merchants Place", "READING", "", "RG1 1ET");
            }
        });
    }
    
    public PersonalisedPropertyAddressPageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedPropertyAddressPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    public void fillInForm(String line0, String line1, String town, String county, String postcode) {
        final PersonalisedPropertyAddressPageObject pageObject = getPageObject();
        pageObject.setTextNdsInputLine0(line0);
        pageObject.setTextNdsInputLine1(line1);
        pageObject.setTextNdsInputTown(town);
        pageObject.setTextNdsInputCounty(county);
        pageObject.setTextNdsInputPostcode(postcode);
     }

    public void ClearForm() {
        final PersonalisedPropertyAddressPageObject pageObject = getPageObject();
        pageObject.setTextNdsInputLine0("");
        pageObject.setTextNdsInputLine1("");
        pageObject.setTextNdsInputTown("");
        pageObject.setTextNdsInputCounty("");
        pageObject.setTextNdsInputPostcode("");
    }

    public WebElement getCurrentAddressSelectList() {

        return getPageObject().getDriver().findElement(
                By.id("uiData.addressFieldsexemptionDetails.epc.propertyAddress.selectedAddressRef"));
    }

    public WebElement getCurrentAddressInput(String className) {
        return getPageObject().getWebElementDivDetail().findElement(
                By.cssSelector("input." + cssSelectorEscape(className)));
    }

    @Override
    public PageHelper skipPage() {
        final PersonalisedPropertyAddressPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }
    
    public void clickAnchorEnterAddressManually() {
        getPageObject().clickAnchorEnterAddressManually();
    }

}
