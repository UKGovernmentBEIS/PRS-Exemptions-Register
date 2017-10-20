package com.northgateps.nds.beis.ui.selenium.pagehelper;

import static com.northgateps.nds.platform.ui.selenium.cukes.StepsUtils.checkOnPage;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.northgateps.nds.beis.ui.selenium.pageobject.LoginPageObject;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.CredentialsFormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;
import com.northgateps.nds.platform.ui.utils.JsonPropertiesLoader;

/**
 * Selenium test helper class for cas login page
 */
@PageObject(pageObjectClass = LoginPageObject.class)
public class LoginPageHelper extends BasePageHelper<LoginPageObject> implements PageHelper {
    
    /** Load the default settings. */
    private final Map<String, ?> testProperties = JsonPropertiesLoader.load("testProperties.json", this.getClass().getClassLoader());
    
    final NdsLogger logger = NdsLogger.getLogger(LoginPageHelper.class);
    
    // set default FormFiller
    {
        setFormFiller(createFormFiller((String) testProperties.get("loginOrgUsername"), (String) testProperties.get("loginOrgPassword")));
    }

    public LoginPageHelper(WebDriver driver) {
        super(driver);
    }

    public LoginPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }
    
    abstract class LoginFormFiller extends CredentialsFormFiller {
        @Override
        public void fill(BasePageHelper<?> pageHelper) { 
            getPageObject().setTextInputUsername(getLoginUsername());
            getPageObject().setTextInputPassword(getLoginPassword());
        }
    };
    
    public LoginFormFiller createFormFiller(final String username, final String password) {
        return new LoginFormFiller() {
            @Override
            public String getLoginUsername() {
                return (String) username;
            }
            
            @Override
            public String getLoginPassword() {
                return (String) password;
            }
        };
    }
    
    @Override
	public PageHelper skipPage() {
		LoginPageObject pageObject = getPageObject();

		for (int i = 0; i < 5; i++) {
			checkOnPage(this, "login-form");

			logger.debug("Login attempt " + i);

			checkNotAccountLocked();

			fillInForm();
			pageObject.invalidateDcId();
			pageObject.clickButton_LOGIN();

			// handle firefox-on-dev-machine unexpected alert issue
			try {
				Alert alert = pageObject.getDriver().switchTo().alert();
				String alertText = alert.getText();
				logger.debug("Alert data: " + alertText);
				alert.accept();
			} catch (NoAlertPresentException e) {
				logger.debug("no unexpected alerts, good.");
			}

			BasePageHelper.waitUntilPageLoading(pageObject.getDriver());
			pageObject = getNewPageObject();

			if (!"login-form".equals(pageObject.getDcId())) {
				logger.info("Login attempt " + i + " succeeded");
				return PageHelperFactory.build(pageObject.getDcId(), pageObject.getDriver(), getLocale());
			}

			logger.info("Login attempt " + i + " failed " + ((CredentialsFormFiller) getFormFiller()).getLoginUsername()
					+ " : " + ((CredentialsFormFiller) getFormFiller()).getLoginPassword());
		}

		fail("Login failed after multiple attempts with "
				+ ((CredentialsFormFiller) getFormFiller()).getLoginUsername());
		return null;
	}
    
    /** 
     * The account locked page currently has to have the same dcid as the login page.
     * Fails if it detects we're on the account locked page.
     */
    private void checkNotAccountLocked() {
        List<WebElement> elements = getPageObject().getDriver().findElements(By.id("content"));
        if (elements.size() > 0) {
            final String content = elements.get(0).getText();
            
            if (content.contains("account has been locked")) {
                fail("The account has been locked");
            } else {
                logger.debug("Account locked page not detected");
            }
        }
    }
    
    //Steer method to skip to select-landlord-or-agent page for partially registered user
    public SelectLandlordOrAgentPageHelper skipToSelectLandlordOrAgentPage() {
        final LoginPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.clickButton_LOGIN();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return new SelectLandlordOrAgentPageHelper(getPageObject().getDriver(), getLocale());
    }

}
