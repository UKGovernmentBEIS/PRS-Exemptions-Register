package com.northgateps.nds.beis.ui.selenium.pagehelper;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.openqa.selenium.WebDriver;

import com.northgateps.nds.platform.ui.selenium.core.BasePageHelper;
import com.northgateps.nds.platform.ui.selenium.core.FormFiller;
import com.northgateps.nds.platform.ui.selenium.core.PageHelper;
import com.northgateps.nds.platform.ui.selenium.core.PageHelperException;
import com.northgateps.nds.platform.ui.selenium.core.PlatformPageHelperFactory;
import com.northgateps.nds.platform.ui.selenium.core.Steering;


/**
 * Factory class to build Page Helpers based on the dcterms.identifier value.
 * Usually a skipPage method will know what sort of PageHelper to return. Use
 * this helper factory when the result cannot be known at compile time.
 */
public class PageHelperFactory {

    static PlatformPageHelperFactory platformPageHelperFactory = new PlatformPageHelperFactory() {

    	/** 
    	 * Provide page name to PageHelper name exceptions for when they don't match up for some reason
    	 * (they should normally be the same).
    	 */
        @Override
        protected String mapDcIdToName(String dcId) {
        	if ("login-form".equals(dcId)) {
            	return "login";
            }
        	
        	return dcId;
        }

        // add exceptions to mapDcIdTo name if possible before adding to this method
        @Override
        protected PageHelper createHelper(String dcId, WebDriver driver, Locale locale) {
            
            if (Objects.equals("report-forgotten-username", dcId)) {
                return new ReportForgottenUsernamePageHelper(driver, locale);
            } else if ("personalised-select-property-type".equals(dcId)) {
                return new PersonalisedSelectPropertyTypePageHelper(driver, locale);
            } else if ("exemption-start-date".equals(dcId)) {
        		return new PersonalisedExemptionStartDatePageHelper(driver, locale);
        	} else if ("personalised-exemption-document-upload".equals(dcId)) {
        		return new PersonalisedExemptionDocumentUploadPageHelper(driver, locale);
        	}

            return super.createHelper(dcId, driver, locale);
        }

        @Override
        public Map<Class<? extends PageHelper>, Steering> getSteers(Class<? extends PageHelper> pageHelperClass, WebDriver driver, Locale locale) {
            if(PersonalisedEndExemptionPageHelper.class.equals(pageHelperClass))
                return getEndExemptionTask(driver,locale);
            else if(PersonalisedAccountSummaryPageHelper.class.equals(pageHelperClass))
                return getAccountSummaryTask(driver,locale);
            else if(PersonalisedChangeAccountAddressPageHelper.class.equals(pageHelperClass))
                return getChangeAccountAddressTask(driver,locale);
            else if(PersonalisedChangeEmailAddressPageHelper.class.equals(pageHelperClass))
                return getChangeEmailAddressTask(driver,locale);
            else if(PersonalisedChangePasswordPageHelper.class.equals(pageHelperClass))
                return getChangePasswordTask(driver,locale);
            else if(SelectLandlordOrAgentPageHelper.class.equals(pageHelperClass))
                return getPartiallyRegisteredUserTask(driver,locale);
            else
                return null;
        }
        
        private Map<Class<? extends PageHelper>, Steering> getPartiallyRegisteredUserTask(WebDriver driver, Locale locale) {
            Map<Class<? extends PageHelper>, Steering> steers = new HashMap<Class<? extends PageHelper>, Steering>();
            steers.put(LoginPageHelper.class, new Steering() {
               @Override
               public PageHelper steer() {
                   LoginPageHelper pageHelper = (LoginPageHelper)build("login-form", driver, locale);
                   return pageHelper.skipToSelectLandlordOrAgentPage();
               }
            });
            return steers;
        }
        
        private Map<Class<? extends PageHelper>, Steering> getEndExemptionTask(WebDriver driver, Locale locale) {
            Map<Class<? extends PageHelper>, Steering> steers = new HashMap<Class<? extends PageHelper>, Steering>();
            steers.put(PersonalisedDashboardPageHelper.class, new Steering() {
               @Override
               public PageHelper steer() {
                   PersonalisedDashboardPageHelper pageHelper = (PersonalisedDashboardPageHelper)build("personalised-dashboard", driver, locale);
                   return pageHelper.skipPageToEndExemption();
               }
            });
            return steers;
        }
        
        private Map<Class<? extends PageHelper>, Steering> getAccountSummaryTask(WebDriver driver, Locale locale) {
            Map<Class<? extends PageHelper>, Steering> steers = new HashMap<Class<? extends PageHelper>, Steering>();
            steers.put(PersonalisedDashboardPageHelper.class, new Steering() {
               @Override
               public PageHelper steer() {
                   PersonalisedDashboardPageHelper pageHelper = (PersonalisedDashboardPageHelper)build("personalised-dashboard", driver, locale);
                   return pageHelper.skipToAccountSummaryPage();
               }
            });
            return steers;
        }
        
        private Map<Class<? extends PageHelper>, Steering> getChangeAccountAddressTask(WebDriver driver, Locale locale) {
           Map<Class<? extends PageHelper>, Steering> steers = new HashMap<Class<? extends PageHelper>, Steering>();
            steers.put(PersonalisedDashboardPageHelper.class, new Steering() {
               @Override
               public PageHelper steer() {
                   PersonalisedDashboardPageHelper pageHelper = (PersonalisedDashboardPageHelper)build("personalised-dashboard", driver, locale);
                   return pageHelper.skipToAccountSummaryPage();
               }
            });
           steers.put(PersonalisedAccountSummaryPageHelper.class, new Steering() {
                @Override
                public PageHelper steer() {
                    PersonalisedAccountSummaryPageHelper pageHelper = (PersonalisedAccountSummaryPageHelper)build("personalised-account-summary", driver, locale);
                    return pageHelper.skipToChangeAccountAddress(); 
                }
            });
            return steers;
        }
        
        private Map<Class<? extends PageHelper>, Steering> getChangeEmailAddressTask(WebDriver driver, Locale locale) {
            Map<Class<? extends PageHelper>, Steering> steers = new HashMap<Class<? extends PageHelper>, Steering>();
             steers.put(PersonalisedDashboardPageHelper.class, new Steering() {
                @Override
                public PageHelper steer() {
                    PersonalisedDashboardPageHelper pageHelper = (PersonalisedDashboardPageHelper)build("personalised-dashboard", driver, locale);
                    return pageHelper.skipToAccountSummaryPage();
                }
             });
            steers.put(PersonalisedAccountSummaryPageHelper.class, new Steering() {
                 @Override
                 public PageHelper steer() {
                     PersonalisedAccountSummaryPageHelper pageHelper = (PersonalisedAccountSummaryPageHelper)build("personalised-account-summary", driver, locale);
                     return pageHelper.skipToChangeEmailAddress(); 
                 }
             });
             return steers;
         }
        
        private Map<Class<? extends PageHelper>, Steering> getChangePasswordTask(WebDriver driver, Locale locale) {
            Map<Class<? extends PageHelper>, Steering> steers = new HashMap<Class<? extends PageHelper>, Steering>();
             steers.put(PersonalisedDashboardPageHelper.class, new Steering() {
                @Override
                public PageHelper steer() {
                    PersonalisedDashboardPageHelper pageHelper = (PersonalisedDashboardPageHelper)build("personalised-dashboard", driver, locale);
                    return pageHelper.skipToAccountSummaryPage();
                }
             });
            steers.put(PersonalisedAccountSummaryPageHelper.class, new Steering() {
                 @Override
                 public PageHelper steer() {
                     PersonalisedAccountSummaryPageHelper pageHelper = (PersonalisedAccountSummaryPageHelper)build("personalised-account-summary", driver, locale);
                     return pageHelper.skipToChangePassword(); 
                 }
             });
             return steers;
         }
        
     };
    
    
    public static PageHelper build(String dcId, WebDriver driver, Locale locale) {
        return platformPageHelperFactory.build(dcId, driver, locale);
    }

    public static PlatformPageHelperFactory factory() {
        return platformPageHelperFactory;
    }
    
    /**
     * Visit a target page based on its PageHelper class.
     * This is just a wrapper for calling :
     *    PageHelperFactory.factory().new PageVisitor<TargetedPageHelper>(){}.visit(firstPageHelper);
     * 
     * @param startPageHelper - the current page helper
     * @param targetPageHelperClass - class of the page helper you want to get to
     * @return <T> targetPageHelper instance
     * @throws PageHelperException - CyclicPageVisitException if the page has been visited before, DeadEndPageVisitException if 
     *                                  there is no NEXT button or Steering. 
     */
    public static <T extends BasePageHelper<?>> T visit(BasePageHelper<?> startPageHelper, Class<T> targetPageHelperClass) throws PageHelperException {
         return PageHelperFactory.factory().new PageVisitor<T>(){}.visit(startPageHelper, targetPageHelperClass);
    }
    
    /**

     * Visit a target page based on its PageHelper class. Does not use the cache of any previous visit to the page.
     * This is just a wrapper for calling :
     *    PageHelperFactory.factory().new PageVisitor<TargetedPageHelper>(){}.noCache().visit(firstPageHelper);
     * 
     * @param startPageHelper - the current page helper
     * @param targetPageHelperClass - class of the page helper you want to get to
     * @return <T> targetPageHelper instance
     * @throws PageHelperException - CyclicPageVisitException if the page has been visited before, DeadEndPageVisitException if 
     *                                  there is no NEXT button or Steering. 
     */
    public static <T extends BasePageHelper<?>> T visitNew(BasePageHelper<?> startPageHelper, Class<T> targetPageHelperClass) throws PageHelperException {
         return PageHelperFactory.factory().new PageVisitor<T>(){}.noCache().visit(startPageHelper, targetPageHelperClass);
    }


    /** 
     * Convenience method to register a form filler.
     * NB when calling PageHelperFactory.visit() this form filler only take effect after the 
     * first page has been skipped.  If you need to set a filler on your starting PageHelper,
     * set it on the PageHelper directly instead.
     */
    public static void registerFormFiller(String dcId, FormFiller formFiller) {
        platformPageHelperFactory.getFormFillerRegistry().put(dcId, formFiller);
    }
    
    /** 
     * Convenience method to unregister a form filler
     */
    public static void unregisterFormFiller(String dcId) {
        platformPageHelperFactory.getFormFillerRegistry().remove(dcId);
    }

    /** 
     * Convenience method to get the currently registered form filler
     */
    public static FormFiller getFormFiller(String dcId) {
        return platformPageHelperFactory.getFormFillerRegistry().get(dcId);
    }
    
    /** 
     * Remove the registry entry for the given page. 
     * This is just a wrapper for calling :
     *     PageHelperFactory.factory().getFormFillerRegistry().remove(dcId);
     */
    public static void remove(String dcId) {
        PageHelperFactory.factory().getFormFillerRegistry().remove(dcId);
    }
    
}
