package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.google.common.base.Function;
import com.northgateps.nds.beis.ui.selenium.pageobject.RegisterSearchPenaltiesPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = RegisterSearchPenaltiesPageObject.class)
public class RegisterSearchPenaltiesPageHelper extends BasePageHelper<RegisterSearchPenaltiesPageObject>
        implements PageHelper {
    
    //set default Form Filler
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                getPageObject().setTextNdsInputPenaltyPostcodesCriteria("RG1 1PB");
                getPageObject().clickButtonNext_FindPenalties();
             }
        });
    }

    public RegisterSearchPenaltiesPageHelper(WebDriver driver) {
        super(driver);
    }

    public RegisterSearchPenaltiesPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    public void click_landlordname()
    {
        final RegisterSearchPenaltiesPageObject pageObject = getPageObject();
        pageObject.getDriver().findElement(By.id("button.authority.landlordname")).click();
    }
    
    public void click_addresslink()
    {
        final RegisterSearchPenaltiesPageObject pageObject = getPageObject();
        pageObject.getDriver().findElement(By.id("button.authority.address")).click();
    }

    @Override
    public PageHelper skipPage() {
        final RegisterSearchPenaltiesPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }

    public WebElement getPenaltyAddressSelectList() {
        return getPageObject().getDriver().findElement(By.id("local_authority_list"));
    }

    public void ClearForm() {
        final RegisterSearchPenaltiesPageObject pageObject = getPageObject();
        pageObject.setTextNdsInputPenaltyPostcodesCriteria("");
        pageObject.setTextNdsInputPenaltyLandlordsNameCriteria("");
        pageObject.setTextNdsInputRentalPropertyDetails("");
        pageObject.setTextNdsInputTown("");
        String selectValue = "All properties";
        RegisterSearchPenaltiesPageHelper pageHelper = new RegisterSearchPenaltiesPageHelper(
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
        return getPageObject().getDriver().findElement(By.id("uiData.penaltySearch.propertyType"));
    }
    
    public WebElement getNonDomesticPenaltyTypeSelectList()
    {
        return getPageObject().getDriver().findElement(By.id("uiData.penaltySearch.penaltyType_PRSN"));
    }
    
    public WebElement getDomesticPenaltyTypeSelectList()
    {
        return getPageObject().getDriver().findElement(By.id("uiData.penaltySearch.penaltyType_PRSD"));
    }
}
