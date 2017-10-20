package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.northgateps.nds.beis.ui.selenium.pageobject.AccountAddressPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = AccountAddressPageObject.class)
public class AccountAddressPageHelper extends BasePageHelper<AccountAddressPageObject> implements PageHelper {
 
    // set default FormFiller
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                clickAnchorEnterAddressManually();
                fillInForm("Flat 1, Projection West", "Merchants Place", "READING", "", "RG1 1ET");
            }
        });
    }

    public AccountAddressPageHelper(WebDriver driver) {
        super(driver);
    }

    public AccountAddressPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    
    public void fillInForm(String line0, String line1, String town, String county, String postcode) {
        final AccountAddressPageObject pageObject = getPageObject();
        pageObject.setTextNdsInputLine0(line0);
        pageObject.setTextNdsInputLine1(line1);
        pageObject.setTextNdsInputTown(town);
        pageObject.setTextNdsInputCounty(county);
        pageObject.setTextNdsInputPostcode(postcode);
        getAddressCountryElement().selectByValue("GB");

    }
    
    public void ClearForm() {
        final AccountAddressPageObject pageObject = getPageObject();
        pageObject.setTextNdsInputLine0("");
        pageObject.setTextNdsInputLine1("");
        pageObject.setTextNdsInputTown("");
        pageObject.setTextNdsInputCounty("");
        pageObject.setTextNdsInputPostcode("");
        getAddressCountryElement().selectByValue("");
    }

    @Override
    public PageHelper skipPage() {
        final AccountAddressPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }

    public void clickAnchorEnterAddressManually() {
        getPageObject().clickAnchorEnterAddressManually();
    }
    
    public WebElement getCurrentAddressSelectList() {

        return getPageObject().getDriver().findElement(
                By.id("uiData.addressFieldsbeisRegistrationDetails.contactAddress.selectedAddressRef"));
    }

    public WebElement getCurrentAddressInput(String className) {
        return getPageObject().getWebElementDivDetail().findElement(
                By.cssSelector("input." + cssSelectorEscape(className)));
    }

    public Select getAddressCountryElement() {
        Select dropdown = new Select(
                getPageObject().getDriver().findElement(By.id("beisRegistrationDetails.contactAddress.country")));
        return dropdown;
    }

}
