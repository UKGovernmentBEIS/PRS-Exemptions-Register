package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.Locale;
import java.util.Random;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.beis.ui.selenium.pageobject.PersonalisedChangeEmailAddressPageObject;
import com.northgateps.nds.platform.ui.selenium.PageObject;
import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;

/**
 * Selenium test helper class for personalised-change-email-address page
 */
@PageObject(pageObjectClass = PersonalisedChangeEmailAddressPageObject.class)
public class PersonalisedChangeEmailAddressPageHelper extends BasePageHelper<PersonalisedChangeEmailAddressPageObject> implements PageHelper {

    //set default Form Filler
    {
        setFormFiller(new FormFiller() {
            @Override
            public void fill(BasePageHelper<?> pageHelper) { 
                String emailAddress = generateRandomEmailAddress();
                fillInForm(emailAddress,emailAddress,"password01");
            }
        });
    }
    
    public PersonalisedChangeEmailAddressPageHelper(WebDriver driver) {
        super(driver);
    }

    public PersonalisedChangeEmailAddressPageHelper(WebDriver driver, Locale locale) {
        super(driver, locale);
    }

    
    public void fillInForm(String emailAddress,String confirmEmailAddress,String password) {
        PersonalisedChangeEmailAddressPageObject changeEmailAddressPageObject = getNewPageObject();
        changeEmailAddressPageObject.setTextNdsInputEmail(emailAddress);
        changeEmailAddressPageObject.setTextNdsInputConfirmEmail(confirmEmailAddress);
        changeEmailAddressPageObject.setTextNdsInputPassword(password);
    }

    @Override
    public PageHelper skipPage() {
        final PersonalisedChangeEmailAddressPageObject pageObject = getPageObject();
        fillInForm();
        pageObject.invalidateDcId();
        pageObject.clickNext();
        BasePageHelper.waitUntilPageLoading(getPageObject().getDriver());
        return PageHelperFactory.build(getPageObject().getDcId(), getPageObject().getDriver(), getLocale());
    }

   
    private static String generateRandomEmailId(Random rng, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    private String generateRandomEmailAddress() {
        final String testMail = generateRandomEmailId(new Random(), "test", 10);
        return testMail + "@necsws.com";
    }
}
