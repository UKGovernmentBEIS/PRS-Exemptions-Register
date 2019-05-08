package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.List;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedEpcDetailsPageObject;
import com.northgateps.nds.beis.ui.view.helper.BeisTestUtilities;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = PersonalisedEpcDetailsPageObject.class)
public class PersonalisedEpcDetailsPageHelper extends BasePageHelper<PersonalisedEpcDetailsPageObject>
        implements PageHelper {

    BeisTestUtilities beisUtils= new BeisTestUtilities();

    //set default FormFiller
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                beisUtils.selectFile(getPageObject().getDriver(), "test.docx");              
            }
        });
    }
    
    public PersonalisedEpcDetailsPageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedEpcDetailsPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }
    
    public boolean isFileFoundInGrid() {
        final PersonalisedEpcDetailsPageObject pageObject = getPageObject();
        List<WebElement> fileNameCells = pageObject.getDriver().findElements(By.className("filename"));
        boolean found = false;
        if (fileNameCells.size() > 0) {
            found = true;
        }
        return found;
    }

    public void ClearForm() {
        final PersonalisedEpcDetailsPageObject pageObject = getPageObject();

        List<WebElement> rows = pageObject.getDriver().findElements(By.cssSelector("tr"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.cssSelector("td"));
            if (cells.size() > 0) { // No td cells in the header row
                if ("lecture17.pdf".equals(cells.get(0).getText())) {
                    List<WebElement> removeButtons = cells.get(2).findElements(By.cssSelector("button"));
                    if (removeButtons.size() > 0) {
                        removeButtons.get(0).click();
                        return;
                    }
                }
            }
        }

    }

    @Override
    public PageHelper skipPage() {
        final PersonalisedEpcDetailsPageObject pageObject = getPageObject();
        fillInForm();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());        
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }
    
}
