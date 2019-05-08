package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionStartDatePageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Selenium test helper class for the exemption start date page helper
 */
@PageObject(pageObjectClass = PersonalisedExemptionStartDatePageObject.class)
public class PersonalisedExemptionStartDatePageHelper extends BasePageHelper<PersonalisedExemptionStartDatePageObject>
        implements PageHelper {
    
   // set default FormFiller
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                LocalDate currentDate = LocalDate.now();
                setStartDate(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
            }
        });
    }

    public PersonalisedExemptionStartDatePageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedExemptionStartDatePageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        final PersonalisedExemptionStartDatePageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }

    public void setStartDate(String keys) {
        WebElement element = getWebElementById("uiData.dateTimeFieldsexemptionDetails.exemptionStartDate.calendar");
        element.clear();
        element.sendKeys(keys);
        focusBody();
    }

    public WebElement getWebElementById(String id) {
        return getPageObject().getWebElementNdsFormPageForm().findElement(By.id(id));
    }

    public String getFutureDate() {       
        LocalDate currentDate = LocalDate.now().plusYears(2);
        return currentDate.format(DateTimeFormatter.ofPattern("dd/MM/YYYY"));
    }

}
