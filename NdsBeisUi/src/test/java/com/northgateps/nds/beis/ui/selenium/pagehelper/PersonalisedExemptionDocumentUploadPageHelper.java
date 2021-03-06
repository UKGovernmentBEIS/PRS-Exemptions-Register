package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedExemptionDocumentUploadPageObject;
import com.northgateps.nds.beis.ui.view.helper.BeisTestUtilities;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;
import com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils;

@PageObject(pageObjectClass = PersonalisedExemptionDocumentUploadPageObject.class)
public class PersonalisedExemptionDocumentUploadPageHelper
        extends BasePageHelper<PersonalisedExemptionDocumentUploadPageObject> implements PageHelper {

    BeisTestUtilities beisUtils= new BeisTestUtilities();
    
    //set default Form Filler
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                beisUtils.selectFile(getPageObject().getDriver(), "test.docx");  
            }
        });
    }

    public PersonalisedExemptionDocumentUploadPageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedExemptionDocumentUploadPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }    

   public void ClearForm() {
        final PersonalisedExemptionDocumentUploadPageObject pageObject = getPageObject();
        StepsUtils.checkOnPage(this, "personalised-exemption-document-upload");
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

    public boolean isFileFoundInGrid() {
        final PersonalisedExemptionDocumentUploadPageObject pageObject = getPageObject();
        List<WebElement> fileNameCells = pageObject.getDriver().findElements(By.className("filename"));
        boolean found = false;
        if (fileNameCells.size() > 0) {
            found = true;
        }
        return found;
    }

    @Override
    public PageHelper skipPage() {
    	
        final PersonalisedExemptionDocumentUploadPageObject pageObject = getPageObject();
        fillInForm();
        StepsUtils.checkOnPage(this, "personalised-exemption-document-upload");
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }
}
