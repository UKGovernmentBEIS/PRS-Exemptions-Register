package com.northgateps.nds.beis.ui.view.javascript.changepassword;

import org.junit.runner.RunWith;

import net.serenitybdd.cucumber.CucumberWithSerenity;
/*
 * Note that blue1skies is the initial password and red9streets the password after this test.
 * That means this test is LIMITED to only run ONCE!
 * To reset it, run the following LDAP ldif script
 * 
 *    dn: uid=newpasswordtest,ou=Users,ou=BEIS,ou=Tenants,ou=NDS,dc=northgateps,dc=com
 *    changetype: modify
 *    replace: userPassword
 *    userPassword: passwordw!11bechangedbytest
 *    
 * Do not change the initial password of passwordw!11bechangedbytest until we change this hacky way of testing
 * because it will break the auto tests which always run on fresh LDAP data. 
 */
@RunWith(CucumberWithSerenity.class)
public class ChangePasswordTest {
}