Feature: Registration - Security Details

Scenario: Navigate back to account address

	Given I am on the security-details page
	When I select Back
	Then I will be taken to the account-address page
	And details previously entered will be displayed
	
Scenario: View TsnCs
	Given I am on the security-details page
	And  I have supplied valid data
	When I select the terms and conditions link
	Then I will be taken to the terms-and-conditions page

Scenario: Process security details

#No data selected 
	Given I am on the security-details page

	Given I have not entered any data
	When I select Next
	Then I will receive the message "Create a username must be entered"
	And I will receive the message "Create a password must be entered"
	And I will receive the message "Confirm password must be entered"
 	And I will receive the message "You must confirm you have read and understood the terms and conditions"
	And I will remain on the security-details page
	
	
	#Invalid user name supplied
	Given I have supplied an invalid user name
	When I select Next
	Then I will receive the message "Create a username must be a valid user name"
	And I will remain on the security-details page
	
	#Username supplied outside lengtn restrictions 3 and 100
	Given I have supplied a username which is outside of length restrictions
	When I select Next
	Then I will receive the message "The username must be between 3 and 100 characters long"
	And I will remain on the security-details page
	
	#Invalid new password supplied
	
	Given I have supplied a password without enough characters
	When I select Next
	Then I will receive the validation message "Your password must be a minimum of 10 characters long with at least 1 lower case letter and 1 number"
	And I will remain on the security-details page

    Given I have supplied a password without enough numbers
	When I select Next
	Then I will receive the validation message "Your password must be a minimum of 10 characters long with at least 1 lower case letter and 1 number"
	And I will remain on the security-details page
	
	Given I have supplied a password without enough letters
	When I select Next
	Then I will receive the validation message "Your password must be a minimum of 10 characters long with at least 1 lower case letter and 1 number"
	And I will remain on the security-details page
	
	Given I have supplied a password that includes user details
	When I select Next
	Then I will receive the message "You must make the password different from the username"
	And I will remain on the security-details page
		
	
#Unconfirmed password
	Given I have supplied a password
	And I have supplied a different confirm password
	When I select Next
	Then I will receive the message "You must make the passwords the same"
	And I will remain on the security-details page
	
	#Happy path, move to account confirmation page
	Given I have supplied valid data
	When I select Next
	Then I will be taken to the account-confirmation page
