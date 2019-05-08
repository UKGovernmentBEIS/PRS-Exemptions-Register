package com.northgateps.nds.beis.ui.selenium.pagehelper;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;

import java.util.Locale;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedDashboardPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.NdsUiWait;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

import net.serenitybdd.core.annotations.findby.By;

/** Test steps for the dashboard.feature BDD file. */
@PageObject(pageObjectClass = PersonalisedDashboardPageObject.class)
public class PersonalisedDashboardPageHelper extends BasePageHelper<PersonalisedDashboardPageObject>
        implements PageHelper {

    public PersonalisedDashboardPageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedDashboardPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    @Override
    public PageHelper skipPage() {
        final PersonalisedDashboardPageObject pageObject = getPageObject();
        checkOnPage(this, "personalised-dashboard");
        pageObject.clickButtonNext_NEXT();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }

    
    //Steer method to skip to account-summary page
    public PersonalisedAccountSummaryPageHelper skipToAccountSummaryPage() {
        final PersonalisedDashboardPageObject pageObject = getPageObject();
        WebDriver driver = pageObject.getDriver();
        checkOnPage(this, "personalised-dashboard");
        HelloUserPageHelper helloHelper = new HelloUserPageHelper(pageObject.getDriver());
        helloHelper.clickMyAccountDetailsButton();
        waitUntilPageLoading(driver);
        return new PersonalisedAccountSummaryPageHelper(getPageObject().getDriver(), getLocale());
    }

    
    //Steer method to skip to end-exemption page
    public PageHelper skipPageToEndExemption() {
        final PersonalisedDashboardPageObject pageObject = getPageObject();
        checkOnPage(this, "personalised-dashboard");
        pageObject.clickSummaryLink();
        new NdsUiWait(getPageObject().getDriver()).untilElementClickedOK(By.xpath("//div[@id='current-exemptions']//table[1]//tbody[1]//tr[4]//td[3]//button[1]"), getPageObject().getDriver());        
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }
    
}
