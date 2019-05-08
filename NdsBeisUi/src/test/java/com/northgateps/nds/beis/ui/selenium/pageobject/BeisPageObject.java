package com.northgateps.nds.beis.ui.selenium.pageobject;

import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.northgateps.nds.platform.ui.selenium.core.NdsUiWait;
import com.northgateps.nds.platform.ui.selenium.pageobject.NdsSeleniumBasePage;

public class BeisPageObject extends NdsSeleniumBasePage {

    public BeisPageObject(WebDriver driver) {
        super(driver);
    }
    @FindBy(how = How.CSS, using = "button[value=NEXT]")
    private WebElement nextButton;

    @FindBy(how = How.CSS, using = ".languageButton[lang=cy]")
    private WebElement welshLanguageButton;

    @FindBy(how = How.CLASS_NAME, using = "back")
    private WebElement backLink;

    @FindBy(how = How.CSS, using = ".languageButton[lang=en]")
    private WebElement englishLanguageButton;
    
    @FindBy(how = How.CLASS_NAME, using="fault")
    private WebElement faultMessage;

    /**
     * @return The title of the current page, with leading and trailing whitespace stripped, or null if one is not
     *         already set.
     */
    public String getTitle() {
        return getDriver().getTitle();
    }

    public String getNextButtonText() {
        return nextButton.getText();
    }

    /**
     * Set the locale.
     * 
     * @param locale - the local to set
     * @throws RuntimeException if the locale language doesn't match any of the hard coded values
     */
    @Override
    public void setLocale(Locale locale) {
        switch (locale.getLanguage()) {
        case "en":
            if (englishLanguageButton.isDisplayed())
                englishLanguageButton.click();
            break;
        case "cy":
            if (welshLanguageButton.isDisplayed())
                welshLanguageButton.click();
            break;
        default:
            throw new RuntimeException("Unrecognised language");
        }
    }

    /**
     * Click the next button to submit the page.
     **/
    public void clickNext() {
    	((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", nextButton);
        new NdsUiWait(getDriver()).untilElementClickedOK(By.cssSelector("button[value=NEXT]"), getDriver());
    }

    /**
     * Click the back button to move previous page.
     **/
    public void clickBack() {
    	((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", backLink);
        backLink.sendKeys("");
        backLink.click();
    }
    
    /**
     * Get the summary of faults message if any
     * @return the fault message
     */
    public String getFaultMessage(){
        return faultMessage.isDisplayed() ? faultMessage.getText() : "";
    } 

}
