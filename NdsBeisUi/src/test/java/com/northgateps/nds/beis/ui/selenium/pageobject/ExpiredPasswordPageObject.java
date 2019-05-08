package com.northgateps.nds.beis.ui.selenium.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

public class ExpiredPasswordPageObject extends BeisPageObject {

    @CacheLookup
    @FindBy(id = "pageForm")
    private WebElement ndsFormPageForm;
    @CacheLookup
    @FindBy(id = "content")
    private WebElement mainContent;
    @CacheLookup
    @FindBy(css = ".form-title")
    private WebElement ndsFormTitleHeading;
    @CacheLookup
    @FindBy(id = "guidance")
    private WebElement divGuidance;   
    @FindBy(id = "oldPassword")
    @CacheLookup
    private WebElement currentPassword;   
    @FindBy(id = "newPassword")
    @CacheLookup
    private WebElement newPassword; 
    @FindBy(id = "confirmPassword")
    @CacheLookup
    private WebElement confirmPassword;
    @CacheLookup
    @FindBy(id = "submitExpiredChangePwd")
    private WebElement saveChanges;

    public ExpiredPasswordPageObject(WebDriver driver) {
        super(driver);
    }

    public String getTextNdsFormTitleHeading() {
        return ndsFormTitleHeading.getText();
    }
    
    public WebElement getWebElementNdsFormTitleHeading() {
        return ndsFormTitleHeading;
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

    public String getTextDivGuidance() {
        return divGuidance.getText();
    }

    public void setTextDivGuidance(String param) {
        divGuidance.clear();
        divGuidance.sendKeys(param);
    }

    public void clickDivGuidance() {
        divGuidance.click();
    }

    public WebElement getWebElementDivGuidance() {
        return divGuidance;
    }

    public By getByDivGuidance() {
        return By.id("guidance");
    }


    public String getTextCurrentPassword() {
        return currentPassword.getAttribute("value");
    }

    public void setTextCurrentPassword(String param) {
        currentPassword.clear();
        currentPassword.sendKeys(param);
    }

    public void clickCurrentPassword() {
        currentPassword.click();
    }

    public WebElement getWebElementCurrentPassword() {
        return currentPassword;
    }

    public By getByCurrentPassword() {
        return By.id("oldPassword");
    }
   
    public String getTextNewPassword() {
        return newPassword.getAttribute("value");
    }

    public void setTextNewPassword(String param) {
        newPassword.clear();
        newPassword.sendKeys(param);
    }

    public void clickNewPassword() {
        newPassword.click();
    }

    public WebElement getWebElementNewPassword() {
        return newPassword;
    }

    public By getByNewPassword() {
        return By.id("newPassword");
    }
    
    public String getTextConfirmPassword() {
        return confirmPassword.getAttribute("value");
    }

    public void setTextConfirmPassword(String param) {
        confirmPassword.clear();
        confirmPassword.sendKeys(param);
    }

    public void clickConfirmPassword() {
        confirmPassword.click();
    }

    public WebElement getWebElementConfirmPassword() {
        return confirmPassword;
    }

    public By getByConfirmPassword() {
        return By.id("confirmPassword");
    }
   
    public void clicksaveChanges() {
        saveChanges.click();
    }
}