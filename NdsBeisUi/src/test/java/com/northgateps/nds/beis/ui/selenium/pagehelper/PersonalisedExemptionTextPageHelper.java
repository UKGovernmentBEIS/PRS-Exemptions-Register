package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionTextPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = PersonalisedExemptionTextPageObject.class)
public class PersonalisedExemptionTextPageHelper extends BasePageHelper<PersonalisedExemptionTextPageObject>
        implements PageHelper {
    
   //set default Form Filler
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                getPageObject().setTextNdsTextareaExemptionText("Exemption text");
            }
        });
    }


    public PersonalisedExemptionTextPageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedExemptionTextPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    public void ClearForm() {
        final PersonalisedExemptionTextPageObject pageObject = getPageObject();
        List<WebElement> rows = pageObject.getDriver().findElements(By.cssSelector("tr"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.cssSelector("td"));
            if (cells.size() > 0) { // No td cells in the header row
                if (cells.get(0).getText().isEmpty() == false) {
                    List<WebElement> removeButtons = cells.get(2).findElements(By.cssSelector("button"));
                    if (removeButtons.size() > 0) {
                        removeButtons.get(0).click();
                        return;
                    }
                }
            }
        }
    }

    public boolean isFileFoundInGrid(String fileName) {
        final PersonalisedExemptionTextPageObject pageObject = getPageObject();
        List<WebElement> fileNameCells = pageObject.getDriver().findElements(By.className("filename"));
        boolean found = false;
        for (WebElement fileNameCell : fileNameCells) {
            if (fileName.equals(fileNameCell.getText())) {
                found = true;
                break;
            }
        }
        return found;
    }

    @Override
    public PageHelper skipPage() {
        final PersonalisedExemptionTextPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }
    
    public PageHelper skipPage(String optionalText) {
        final PersonalisedExemptionTextPageObject pageObject = getPageObject();
        pageObject.setTextNdsTextareaExemptionText(optionalText);
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }
}
