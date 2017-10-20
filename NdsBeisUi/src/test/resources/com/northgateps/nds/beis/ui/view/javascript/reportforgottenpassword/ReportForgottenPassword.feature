Feature: Report Forgotten Password

Scenario: Navigate to reset password and test all scenarios

Given I am on the 'login' page
When I select 'Forgotten your password?'
Then I will be taken to the 'report-forgotten-password' page
When I select Back
Then I will be taken to the 'login' page
When I select 'Forgotten your password?'
Then I will be taken to the 'report-forgotten-password' page
And I have not entered any data
When I select 'Reset password'
And I will receive the message "Username must be entered"
And I will remain on the 'report-forgotten-password' page

Given I have supplied a user name
When I select 'Reset password'
Then I will be taken to the 'forgotten-password-confirmation' page

Scenario: Navigate to reset password and test for invalid user

# Invalid user
Given I am on the 'login' page
When I select 'Forgotten your password?'
Then I will be taken to the 'report-forgotten-password' page
And I have supplied a user name that doesn't exist
And I select 'Reset password'
Then I will be taken to the 'forgotten-password-confirmation' page


