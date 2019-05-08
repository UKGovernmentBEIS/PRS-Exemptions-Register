Feature: Activate Password Reset

# Test uses pre-seeded data which means this test is LIMITED to only run ONCE!
# To reset it, run the following LDAP ldif script
#
#dn: userid=activateResetPasswordOne,ou=Users,ou=BEIS,ou=Tenants,ou=NDS,dc=northgateps,dc=com
#changetype: modify
#replace: ndsPasswordResetCode
#ndsPasswordResetCode: YmVuYw==-b1d00a57-67ed-4cb8-8951-ac3709682399-3057746447634
#
#dn: userid=activateExpiredResetPasswordTwo,ou=Users,ou=BEIS,ou=Tenants,ou=NDS,dc=northgateps,dc=com
#changetype: modify
#replace: ndsPasswordResetCode
#ndsPasswordResetCode: YmVuYw==-07e7b56a-c53d-446b-ba69-d89070532cb1-1479570110300
#

Scenario: Error if activation code has expired by time

Given I am on the 'activate-password-reset' page
And activation code has expired
And activation code has not been used already
And activation code is linked to my username
And I have input valid new password
And I have input valid confirm password matching the new password
And I have input a valid username for expired code
And I have input expired activation code
When I choose to reset the password
Then I must be notified "Your password reset activation link has expired. You will need to request for a new password again."
And new password and repeat password must be set to blank
And I must remain on 'activate-password-reset' page

Scenario: Successfully reset the password after testing validation errors

Given I am on the 'activate-password-reset' page
And I have not input any data
When I choose to reset the password
Then I must receive the messages "Username must be entered", "Reset Code must be entered", "Password must be entered", "Confirm Password must be entered"
And I must remain on 'activate-password-reset' page
And activation code is not expired yet
And activation code has not been used already
And activation code is linked to my username
And I have input a valid username
And I have input activation code

And  I have supplied a new password without enough characters
When I choose to reset the password
Then I must be notified "Your password must be a minimum of 10 characters long with at least 1 lower case letter and 1 number"
And I must remain on 'activate-password-reset' page
Given I have supplied a new password without enough numbers
When I choose to reset the password
Then I must be notified "Your password must be a minimum of 10 characters long with at least 1 lower case letter and 1 number"
And I must remain on 'activate-password-reset' page
Given I have supplied a new password without enough letters
When I choose to reset the password
Then I must be notified "Your password must be a minimum of 10 characters long with at least 1 lower case letter and 1 number"
And I must remain on 'activate-password-reset' page
#REMOVED THIS TEST AS OPEN LDAP DOES NOT SUPPORT THIS
#Given I have supplied a new password that includes user details
#When I choose to reset the password
#Then I must be notified "Password must not contain user details."
#And I must remain on 'activate-password-reset' page

And I have input valid new password
And I have input valid but different repeat password
When I choose to reset the password
Then I must be notified "You must make the passwords the same"
And I must remain on 'activate-password-reset' page
And new password and repeat password must be set to blank
And I have input valid new password
And I have input valid confirm password matching the new password
When I choose to reset the password
Then my password should be updated with my new password supplied
And I must be taken to the 'password-reset-confirmation' page
And I have selected Finish
Then I will be taken to the “sign-on” page

Scenario: Error if activation code has expired by previous usage

Given I am on the 'activate-password-reset' page
 And activation code is not expired yet
 And activation code has already been used
 And activation code is linked to my username
 And I have input valid new password
 And I have input valid confirm password matching the new password
 And I have input a valid username
 And I have input activation code
 When I choose to reset the password
 Then I must be notified "Username or activation code is invalid.Use the forgotten password option to try again."
 And I must remain on 'activate-password-reset' page
 And new password and repeat password must be set to blank
 
Scenario: Username and activation code are not linked

Given I am on the 'activate-password-reset' page
And activation code is not expired yet
And activation code has not been used already
And activation code is linked to my username
And I have input valid new password
And I have input valid confirm password matching the new password
And I have input a valid username
And I have input unmatching activation code
When I choose to reset the password
Then I must be notified "Username or activation code is invalid.Use the forgotten password option to try again."
And I must remain on 'activate-password-reset' page
And new password and repeat password must be set to blank