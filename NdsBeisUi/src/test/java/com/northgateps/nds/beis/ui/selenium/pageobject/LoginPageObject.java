package com.northgateps.nds.beis.ui.selenium.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

/**
 * Custom page object for the login page which is not a normal NDS JSP but instead shown
 * by the security system.
 */
public class LoginPageObject extends BeisPageObject {

    @CacheLookup
    @FindBy(id = "pageForm")
    private WebElement ndsFormPageForm;
    @CacheLookup
    @FindBy(id = "content")
    private WebElement mainContent;
    @CacheLookup
    @FindBy(id = "button.back")
    private WebElement buttonBack_PREVIOUS;
    @CacheLookup
    @FindBy(id = "forminfowrap")
    private WebElement sectionForminfowrap;
    @CacheLookup
    @FindBy(id = "formwrap")
    private WebElement sectionFormwrap;
    @CacheLookup
    @FindBy(id = "username")
    private WebElement inputUsername;
    @CacheLookup
    @FindBy(id = "password")
    private WebElement inputPassword;
    @CacheLookup
    @FindBy(id = "forgotpassword")
    private WebElement anchorForgotPassword;
    @FindBy(id = "forgotusername")
    private WebElement anchorForgotUsername;
    @CacheLookup
    @FindBy(id = "register")
    private WebElement anchorRegister;
    @CacheLookup
    @FindBy(id = "activateRegistration")
    private WebElement anchorActivateRegistration;
    @CacheLookup
    @FindBy(id = "nextpage")
    private WebElement inputNextpage_HomePage;
    @CacheLookup
    @FindBy(name = "submit")
    private WebElement button_LOGIN;

    public LoginPageObject(WebDriver driver) {
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

    public String getTextButtonBack_PREVIOUS() {
        return buttonBack_PREVIOUS.getAttribute("value");
    }

    public void clickButtonBack_PREVIOUS() {
        buttonBack_PREVIOUS.click();
    }

    public WebElement getWebElementButtonBack_PREVIOUS() {
        return buttonBack_PREVIOUS;
    }

    public String getTextSectionForminfowrap() {
        return sectionForminfowrap.getText();
    }

    public void setTextSectionForminfowrap(String param) {
        sectionForminfowrap.clear();
        sectionForminfowrap.sendKeys(param);
    }

    public void clickSectionForminfowrap() {
        sectionForminfowrap.click();
    }

    public WebElement getWebElementSectionForminfowrap() {
        return sectionForminfowrap;
    }

    public String getTextSectionFormwrap() {
        return sectionFormwrap.getText();
    }

    public void setTextSectionFormwrap(String param) {
        sectionFormwrap.clear();
        sectionFormwrap.sendKeys(param);
    }

    public void clickSectionFormwrap() {
        sectionFormwrap.click();
    }

    public WebElement getWebElementSectionFormwrap() {
        return sectionFormwrap;
    }

    public String getTextInputUsername() {
        return inputUsername.getText();
    }

    public void setTextInputUsername(String param) {
        inputUsername.clear();
        inputUsername.sendKeys(param);
    }

    public WebElement getWebElementInputUsername() {
        return inputUsername;
    }

    public String getTextInputPassword() {
        return inputPassword.getText();
    }

    public void setTextInputPassword(String param) {
        inputPassword.clear();
        inputPassword.sendKeys(param);
    }

    public void clickAnchorForgotPassword() {
        anchorForgotPassword.click();
    }
    
    public void clickAnchorForgotUsername() {
        anchorForgotUsername.click();
    }

    public WebElement getWebElementAnchorForgotpassword() {
        return anchorForgotPassword;
    }
    
    public void clickAnchorActivateRegistration() {
    	anchorActivateRegistration.click();
    }
    
    public String getTextAnchorRegister() {
        return anchorRegister.getAttribute("value");
    }

    public void clickAnchorRegister() {
        anchorRegister.click();
    }

    public WebElement getWebElementAnchorRegister() {
        return anchorRegister;
    }

    public String getTextInputNextpage_HomePage() {
        return inputNextpage_HomePage.getText();
    }

    public void setTextInputNextpage_HomePage(String param) {
        inputNextpage_HomePage.clear();
        inputNextpage_HomePage.sendKeys(param);
    }

    public void clickInputNextpage_HomePage() {
        inputNextpage_HomePage.click();
    }

    public WebElement getWebElementInputNextpage_HomePage() {
        return inputNextpage_HomePage;
    }

    public String getTextButton_LOGIN() {
        return button_LOGIN.getAttribute("value");
    }

    public void clickButton_LOGIN() {
        button_LOGIN.click();
    }

    public WebElement getWebElementButton_LOGIN() {
        return button_LOGIN;
    }
}
