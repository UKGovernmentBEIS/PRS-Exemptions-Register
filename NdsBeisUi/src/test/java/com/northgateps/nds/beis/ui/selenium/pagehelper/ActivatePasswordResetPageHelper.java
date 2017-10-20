package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;
import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.ActivatePasswordResetPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * @author Ben Cory
 * This is a helper class for report forgotten password to help write the steps file
 * for selenium tests.
 */
@PageObject(pageObjectClass = ActivatePasswordResetPageObject.class)
public class ActivatePasswordResetPageHelper extends BasePageHelper<ActivatePasswordResetPageObject> implements PageHelper {

    public ActivatePasswordResetPageHelper(WebDriver driver) {
        super(driver);
    }

    public ActivatePasswordResetPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }    
    
    @Override
    public PageHelper skipPage() {
        //This won't work because Activate password reset due to dependencies on a valid LDAP user with a current unused reset code
        return null;    
    }
    
    public void ResetFields()
    {
        final ActivatePasswordResetPageObject pageObject = getPageObject();
        pageObject.setTextNdsInputActivationCode("");
        pageObject.setTextNdsInputPassword("");
        pageObject.setTextNdsInputConfirmPassword("");
        pageObject.setTextNdsInputUsername("");        
    }
}
