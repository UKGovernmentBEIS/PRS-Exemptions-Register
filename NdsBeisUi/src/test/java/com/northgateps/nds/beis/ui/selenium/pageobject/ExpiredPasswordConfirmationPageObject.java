package com.northgateps.nds.beis.ui.selenium.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

public class ExpiredPasswordConfirmationPageObject extends BeisPageObject {

    @CacheLookup
    @FindBy(id = "pageForm")
    private WebElement ndsFormPageForm;
    @CacheLookup
    @FindBy(id = "content")
    private WebElement mainContent;
    @CacheLookup
    @FindBy(id = "successmessage")
    private WebElement successMessage;
    @CacheLookup
    @FindBy(id = "continue")
    private WebElement buttonContinue;

    public ExpiredPasswordConfirmationPageObject(WebDriver driver) {
        super(driver);
    }

    public String getTextNdsFormPageForm() {
        return ndsFormPageForm.getText();
    }

    public void setTextNdsFormPageForm(String param) {
        ndsFormPageForm.clear();
        ndsFormPageForm.sendKeys(param);
    }

    public void clickNdsFormPageForm() {
        ndsFormPageForm.click();
    }

    public WebElement getWebElementNdsFormPageForm() {
        return ndsFormPageForm;
    }

    public By getByNdsFormPageForm() {
        return By.id("pageForm");
    }

    public String getTextMainContent() {
        return mainContent.getText();
    }

    public void setTextMainContent(String param) {
        mainContent.clear();
        mainContent.sendKeys(param);
    }

    public void clickMainContent() {
        mainContent.click();
    }

    public WebElement getWebElementMainContent() {
        return mainContent;
    }

    public By getByMainContent() {
        return By.id("content");
    }

    public String getTextSuccessMessage() {
        return successMessage.getText();
    }

    public void setTextSuccessMessage(String param) {
        successMessage.clear();
        successMessage.sendKeys(param);
    }

    public void clickSuccessMessage() {
        successMessage.click();
    }

    public WebElement getWebElementSuccessMessage() {
        return successMessage;
    }

    public By getBySuccessMessage() {
        return By.id("successMessage");
    }

    public String getTextButtonContinue() {
        return buttonContinue.getAttribute("value");
    }

    public void clickButtonContinue() {
        buttonContinue.click();
    }

    public WebElement getWebElementButtonContinue() {
        return buttonContinue;
    }

    public By getByButtonContinue() {
        return By.id("continue");
    }

}
