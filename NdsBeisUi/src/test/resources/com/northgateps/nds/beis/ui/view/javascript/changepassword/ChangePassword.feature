Feature: Changing the user's password

# Note that blue1skies is the initial password and red9streets the password after this test.
# That means this test is LIMITED to only run ONCE!
# To reset it, run the following LDAP ldif script
# 
#    dn: uid=newpasswordtest,ou=Users,ou=BEIS,ou=Tenants,ou=NDS,dc=northgateps,dc=com
#    changetype: modify
#    replace: userPassword
#    userPassword: passwordw!11bechangedbytest
#    
# Do not change the initial password of blue1skies until we change this hacky way of testing
# because it will break the auto tests which always run on fresh LDAP data. 

 
Scenario: Navigate to change password
Given I am on the personalised-account-summary page
When I select 'Change password' link
Then I will be taken to the personalised-change-password page
 
Scenario: Navigate back to account details
Given I am on the personalised-change-password page
When I select Back
Then I will be taken to the 'personalised-account-summary' page
And details of the account will be displayed

Scenario: Process passwords
Given I am on the personalised-change-password page

#No data selected
And I have not entered any data
When I select 'Change password'
Then I will receive the message "Current password must be entered"
And I will receive the message "New password must be entered"
And I will receive the message "Confirm new password must be entered"
And I will remain on the personalised-change-password page

#Invalid current password supplied
Given I have supplied an invalid current password
And I have supplied 'New password'
And I have supplied 'Confirm new password'
When I select 'Change password'
Then I will receive the error message "Current password invalid"
And I will remain on the personalised-change-password page

#Invalid new password supplied
Given I have supplied a valid old password
And  I have supplied a new password without enough characters
When I select 'Change password'
Then I will receive the error message "Your password must be a minimum of 10 characters long with at least 1 lower case letter and 1 number"
And I will remain on the personalised-change-password page

Given I have supplied a valid old password
And I have supplied a new password without enough numbers
When I select 'Change password'
Then I will receive the error message "Your password must be a minimum of 10 characters long with at least 1 lower case letter and 1 number"
And I will remain on the personalised-change-password page

Given I have supplied a valid old password
And I have supplied a new password without enough letters
When I select 'Change password'
Then I will receive the error message "Your password must be a minimum of 10 characters long with at least 1 lower case letter and 1 number"
And I will remain on the personalised-change-password page

#THIS IS NO LONGER SUPPORTED IN OPENLDAP TO BE UNCOMMENTED WHEN CUSTOM MOD ADDED##
Given I have supplied a valid old password
And I have supplied a new password that includes user details
When I select 'Change password'
Then I will receive the message "Username and password must not be the same"
And I will remain on the personalised-change-password page

#Unconfirmed password 
Given I have supplied a valid old password
Given I have supplied a valid new password 
And I have supplied a different confirm password 
When I select 'Change password' 
Then I will receive the message "Confirm password must match new password" 
And I will remain on the personalised-change-password page

#Happy path, move to account address page 
Given I have supplied a valid old password
Given I have supplied a valid new password 
When I select 'Change password' 
Then I will be taken to the 'personalised-change-password-confirmation' page

#Move to account details 
Given I am on the 'personalised-change-password-confirmation' page 
When I select Finish 
Then I will be taken to the 'personalised-account-summary' page