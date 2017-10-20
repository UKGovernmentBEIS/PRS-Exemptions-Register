package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.google.common.base.Function;
import com.northgateps.nds.beis.ui.selenium.pageobject.RegisterSearchExemptionsPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = RegisterSearchExemptionsPageObject.class)
public class RegisterSearchExemptionsPageHelper extends BasePageHelper<RegisterSearchExemptionsPageObject>
        implements PageHelper {
    
    
    //set default Form Filler
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                getPageObject().setTextNdsInputExemptionPostcodeCriteria("RG1 1ET");
                getPageObject().clickButtonNext_FindExemptions();
             }
        });
    }

    public RegisterSearchExemptionsPageHelper(WebDriver driver) {
        super(driver);
    }

    public RegisterSearchExemptionsPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    public void click_addresslink()
    {
        final RegisterSearchExemptionsPageObject pageObject = getPageObject();
        pageObject.getDriver().findElement(By.id("button.authority.address")).click();
    }
    
    public void click_landlordname()
    {
        final RegisterSearchExemptionsPageObject pageObject = getPageObject();
        pageObject.getDriver().findElement(By.id("button.authority.landlordname")).click();
    }

    @Override
    public PageHelper skipPage() {
        final RegisterSearchExemptionsPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        click_addresslink();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }

    public WebElement getPropertyAddressSelectList() {

        return getPageObject().getDriver().findElement(By.id("button.authority.address"));
    }
    
    public void ClearForm() {
        final RegisterSearchExemptionsPageObject pageObject = getPageObject();
        pageObject.setTextNdsInputExemptionPostcodeCriteria("");
        pageObject.setTextNdsInputExemptionLandlordsNameCriteria("");
        pageObject.setTextNdsInputExemptPropertyDetails("");
        pageObject.setTextNdsInputTown("");
        String selectValue = "All properties";
        RegisterSearchExemptionsPageHelper pageHelper = new RegisterSearchExemptionsPageHelper(
                getPageObject().getDriver());
        pageHelper.getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    new Select(pageHelper.getPropertyTypeSelectList())
                            .selectByVisibleText(selectValue);
                    return true;
                } catch (NoSuchElementException e) {
                    return false; // try again
                }
            }
        });
        
     }
    
    public WebElement getPropertyTypeSelectList()
    {
        return getPageObject().getDriver().findElement(By.id("uiData.exemptionSearch.service"));
    }
    
    public WebElement getNonDomesticExemptionTypeSelectList()
    {
        return getPageObject().getDriver().findElement(By.id("uiData.exemptionSearch.exemptionType_PRSN"));
    }
    
    public WebElement getDomesticExemptionTypeSelectList()
    {
        return getPageObject().getDriver().findElement(By.id("uiData.exemptionSearch.exemptionType_PRSD"));
    }

}
