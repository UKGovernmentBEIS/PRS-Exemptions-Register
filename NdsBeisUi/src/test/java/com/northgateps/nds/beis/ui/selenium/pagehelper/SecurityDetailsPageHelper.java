package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import com.google.common.base.Function;
import com.northgateps.nds.beis.ui.selenium.pageobject.SecurityDetailsPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

@PageObject(pageObjectClass = SecurityDetailsPageObject.class)
public class SecurityDetailsPageHelper extends BasePageHelper<SecurityDetailsPageObject> implements PageHelper {

    private static final String FIRST_NAME = "John";
    private static final String SURNAME = "Smith";
    
    // set default FormFiller
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                getPageObject().setTextNdsInputUsername("PeSelTest-" + System.currentTimeMillis());
                getPageObject().setTextNdsInputPassword("Randompassword11");
                getPageObject().setTextNdsInputConfirmPassword("Randompassword11");
                if (!getPageObject().getWebElementNdsCheckboxIsAgreeRegistrationTermsConditions_AgreeTerms().isSelected()) {
                    getPageObject().clickNdsCheckboxIsAgreeRegistrationTermsConditions_AgreeTerms();
                }
            }
        });
    }
    
    public SecurityDetailsPageHelper(WebDriver driver) {
        super(driver);
    }

    public SecurityDetailsPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }
    
    @Override
    public PageHelper skipPage() {
        final SecurityDetailsPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }
    
    public void clearForm() {
        final SecurityDetailsPageObject pageObject = getPageObject();
        pageObject.setTextNdsInputUsername("");
        pageObject.setTextNdsInputPassword("");
        pageObject.setTextNdsInputConfirmPassword("");

    }
    
    public void fillInPassword(String missingType) {
        if ("characters".equals(missingType)) {

            // password that is too short
            getPageObject().setTextNdsInputPassword("shrt");
            getPageObject().setTextNdsInputConfirmPassword("shrt");
        } else if ("numbers".equals(missingType)) {

            // password that doesn't contain any numbers
            getPageObject().setTextNdsInputPassword("qwertyuionetwothree");
            getPageObject().setTextNdsInputConfirmPassword("qwertyuionetwothree");
        } else if ("letters".equals(missingType)) {

            // password that doesn't contain any letters
            getPageObject().setTextNdsInputPassword("123456789123456789");
            getPageObject().setTextNdsInputConfirmPassword("123456789123456789");
        }

    }
    
    public void fillInPasswordBasedOffUsername() {
        final String password = FIRST_NAME + SURNAME + "01!";
        getPageObject().setTextNdsInputPassword(password);
        getPageObject().setTextNdsInputConfirmPassword(password);
    }
    
    public void fillInSamePasswordAsUsername() {
        final String password = "randomusername";
        getPageObject().setTextNdsInputPassword(password);
        getPageObject().setTextNdsInputConfirmPassword(password);
    }
    
    public void waitUntilValidationMessageSeen(final String validationMessage) {
        getWait().until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                     return validationMessage.equals(findFaultMessage(validationMessage));
                } catch (StaleElementReferenceException e) {
                    getNewPageObject();
                    return false; // try again
                }
            }
        });
    }

}
