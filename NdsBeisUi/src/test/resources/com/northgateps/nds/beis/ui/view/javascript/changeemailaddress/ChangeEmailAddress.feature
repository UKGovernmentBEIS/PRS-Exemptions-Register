Feature: Email Address - Change

Scenario: Navigate to change email address

Given I am on the personalised-account-summary page
When I select Change email address
Then I will be taken to the personalised-change-email-address page

Scenario: Navigate back to account details

Given I am on the change-email address page
When I select Back
Then I will be taken to the personalised-account-summary page


Scenario: Process email address
#No data enetered
	Given I am on the change-email address page
	And I have not entered any data
	When I select Submit email address change
	And I will receive the message "New email address must be entered"
	And I will receive the message "Confirm new email address must be entered"
	And I will receive the message "Password must be entered"
	And I will remain on the personalised-change-email-address page
#Invalid data supplied
	Given I have supplied an invalid email address
	And I have supplied an invalid confirm email address
	And I have supplied an invalid password
	When I select Submit email address change
	Then I will receive the message "New email address must be a valid email address"
	And I will receive the message "Confirm new email address must be a valid email address"
	And I will remain on the personalised-change-email-address page
#Unconfirmed email address
	Given I have supplied a new email address
	And I have supplied a different confirm email address
	When I select Submit email address change
	Then I will receive the message "Confirm email address must match new email address"
	And I will remain on the personalised-change-email-address page

#Happy path, move to account address page
	Given I have supplied valid data
	When I select Next
	Then I will be taken to the personalised-account-summary page
	And details for the account will be displayed