package com.northgateps.nds.beis.ui.view.javascript.activatepasswordreset;

import org.junit.runner.RunWith;
import net.serenitybdd.cucumber.CucumberWithSerenity;

/**
 * @author Ben Cory
 * 
 * JUnit test class for serenity to run steps.
 * 
 * Test uses pre-seeded data which means this test is LIMITED to only run ONCE!
 * To reset it, run the following LDAP ldif script :
 * 
dn: userid=activateResetPasswordOne,ou=Users,ou=BEIS,ou=Tenants,ou=NDS,dc=northgateps,dc=com
changetype: modify
replace: ndsPasswordResetCode
ndsPasswordResetCode: YmVuYw==-b1d00a57-67ed-4cb8-8951-ac3709682399-3057746447634

dn: userid=activateExpiredResetPasswordTwo,ou=Users,ou=BEIS,ou=Tenants,ou=NDS,dc=northgateps,dc=com
changetype: modify
replace: ndsPasswordResetCode
ndsPasswordResetCode: YmVuYw==-07e7b56a-c53d-446b-ba69-d89070532cb1-1479570110300
 */
@RunWith(CucumberWithSerenity.class)
public class ActivatePasswordResetTest {

}