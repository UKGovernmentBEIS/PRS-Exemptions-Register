package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.northgateps.nds.beis.ui.selenium.pageobject.ExpiredPasswordPageObject;
import com.northgateps.nds.beis.ui.selenium.pageobject.LoginPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = ExpiredPasswordPageObject.class)
public class ExpiredPasswordPageHelper extends BasePageHelper<ExpiredPasswordPageObject> implements PageHelper {

    /** A possible password to use.  If using this as the old password then PASSWORD_2 will be the new password. */
    private static final String PASSWORD_1 = "Password123$%^";
    
    /** A possible password to use.  If using this as the old password then PASSWORD_1 will be the new password. */
    private static final String PASSWORD_2 = "pAssword123|||";

    /** Set when we know which password was used on the login page (either PASSWORD_1 or PASSWORD_2). */
    private static String oldPassword;
    
    /** Set when we know which password was used on the login page (either PASSWORD_1 or PASSWORD_2). */
    private static String newPassword;
    
    private ExpiredPasswordPageObject pageObject;

    public ExpiredPasswordPageHelper(WebDriver driver) {
        super(driver);
    }

    public ExpiredPasswordPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    /** 
     * Usually accessing this page will be either a mistake to be reported or to specially
     * test this page, so don't want a standard "skipPage()" method.
     * @throws NotImplementedException - always.
     */
    @Override
    public PageHelper skipPage() {
        throw new NotImplementedException();
    }
    
    public PageHelper skipPage(String oldPassword, String newPassword, String confirmNewPassword) {
        pageObject = getNewPageObject();
        fillInForm(oldPassword, newPassword, confirmNewPassword);
        
        pageObject.clicksaveChanges();
        
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }

    public void fillInForm(String oldPassword, String newPassword, String confirmNewPassword) {
        pageObject.setTextCurrentPassword(oldPassword);
        pageObject.setTextNewPassword(newPassword);
        pageObject.setTextConfirmPassword(confirmNewPassword);
    }

    /** Skip the login page using either PASSWORD_1 or PASSWORD_2 for user ExpiredPasswordTest. */ 
    public void login(LoginPageHelper loginPageHelper) {
        
        // try to log in (without any retries)
        login(loginPageHelper, PASSWORD_1);
        
        String currentPage = getNewPageObject().getDcId();
        
        // check if that worked or not
        if ("login-form".equals(currentPage)) {
            
            // login has failed try again with the other password
            oldPassword = PASSWORD_2;
            newPassword = PASSWORD_1;
            
            login(loginPageHelper, oldPassword);
            
        } else {
            
            // login succeeded
            oldPassword = PASSWORD_1;
            newPassword = PASSWORD_2;
        }
    }

    /** Fill in the username and password and press the login button. */
    private void login(LoginPageHelper loginPageHelper, String password) {
		loginPageHelper.setFormFiller(loginPageHelper.createFormFiller("ExpiredPasswordTest", password));
		   
		LoginPageObject loginPageObject = loginPageHelper.getNewPageObject();
		loginPageHelper.fillInForm();
		loginPageObject.clickButton_LOGIN();
		   
		BasePageHelper.waitUntilPageLoading(loginPageObject.getDriver());
    }

    public static String getOldPassword() {
        return oldPassword;
    }
    
    public static String getNewPassword() {
        return newPassword;
    }

    /**
     * @return a collection of the displayed summary validation message using the css class "fault".
     */
    public List<String> getSummaryFaultMessages() {
        WebDriver driver = getPageObject().getDriver();
        WebElement summaryFault = null;
        
        // protect against being on an error page that doesn't have a fault section
        try {
            summaryFault = driver.findElement(By.className("error-summary-list"));
        } catch (NoSuchElementException e) {
            return null;
        }
        
        List<String> messages = new ArrayList<String>();
        if (summaryFault != null) {
            List<WebElement> elements = summaryFault.findElements(By.xpath(".//li"));
            for (WebElement item : elements) {
                if (item.isDisplayed()) {
                    List<WebElement> linkElements = item.findElements(By.xpath(".//a"));
                    for (WebElement linkElement : linkElements) {
                        messages.add(linkElement.getAttribute("innerHTML"));
                    }
                }
            }            
        }

        return messages;
    }
    
}