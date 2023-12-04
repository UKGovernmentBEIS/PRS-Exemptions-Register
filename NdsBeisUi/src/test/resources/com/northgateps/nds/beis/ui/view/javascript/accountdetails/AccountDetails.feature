Feature: Registration - Account Details

Scenario: Navigate back to select landlord type
	Given I am on the account-details to enter Person details
	When I select Back
	Then I will be taken to the select-landlord-type page
	And details previously entered will be displayed

Scenario: Process account details for Person

#No data selected for person

	Given I am on the account-details to enter Person details
	And I have not entered any data
	When I select Next
	Then I will receive the message "First name must be entered"
	And I will receive the message "Last name must be entered"
	And I will receive the message "Email address must be entered"
	And I will receive the message "Confirm email address must be entered"
	And I will receive the message "Telephone number must be entered"
	And I will remain on the account-details page
	
#Invalid data supplied
	
	Given I have supplied an invalid email address as "dummy.co"
	And  I have supplied an invalid confirm email address as "dummy.co"
	And  I have supplied an invalid phone number that does not consist of numeric or space characters with an optional leading + as "abcs"
	When I select Next
	Then I will receive the message "You must enter a valid email address in Email address"
	And I will receive the message "You must enter a valid email address in Confirm email address"
	And I will receive the message "Telephone number must be a valid phone number"
	And I will remain on the account-details page
	
#Unconfirmed email address
	When I have supplied an email address as "nds-dummyOne@necsws.com"
	And I have supplied a different confirm email address as "dumy@necsws.com"
	And I select Next
	Then I will receive the message "You must make the email address and confirm email address the same"
	And I will remain on the account-details page
	
#Happy path, move to account address page
	Given I have supplied valid data
	When I select Next
	Then I will be taken to the account-address page


Scenario: Process account details for Organisation

	Given I am on the account-details to enter Organisation name
	And I have not entered any data
	When I click Next
	Then I will receive the message "Organisation name must be entered"
	And I will receive the message "Email address must be entered"
	And I will receive the message "Confirm email address must be entered"
	And I will receive the message "Telephone number must be entered"
	And I will remain on the account-details page
	
Scenario: Agent name mandatory

	Given I have registered as an agent
	And I am on the 'account-details' page
	And I have not supplied Agent name
	When I submit Next
	Then I will receive the message "Agent name must be entered"
	And I will remain on the account-details page
	
# Back from agent
	When I select Back
	Then I will be taken to the 'select-landlord-or-agent' page
	And details previously entered on select-landlord-or-agent will be displayed
	
	
# move to Agent address
	When I select Next
	And I am on the 'account-details' page
	And I have entered valid details
	When I select Next
	Then I will be taken to the account-address page
	And heading will be 'Agent address'
	
	