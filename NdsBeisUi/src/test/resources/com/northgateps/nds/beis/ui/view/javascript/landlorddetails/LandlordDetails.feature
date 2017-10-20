Feature: Exemption Registration - landlord's Details

Scenario: Navigate back to personalised select personalised-landlord type agent
	Given I am on the personalised-landlord-details to enter Person details
	When I select Back
	Then I will be taken to the personalised-select-landlord-type-agent page
	And details previously entered will be displayed

Scenario: Process personalised-landlord details for Person

#No data selected for person

	Given I am on the personalised-landlord-details to enter Person details
	And I have not entered any data
	When I select Next
	Then I will receive the message "First name must be entered"
	And I will receive the message "Last name must be entered"
	And I will receive the message "Email address must be entered"
	And I will receive the message "Confirm email address must be entered"
	And I will receive the message "Telephone number must be entered"
	And I will remain on the personalised-landlord-details page
	
#Invalid data supplied
	
	Given I have supplied an invalid email address as "dummy.co"
	And  I have supplied an invalid confirm email address as "dummy.co"
	And  I have supplied an invalid phone number that does not consist of numeric or space characters with an optional leading + as "abcs"
	When I select Next
	Then I will receive the message "Email address must be a valid email address"
	And I will receive the message "Confirm email address must be a valid email address"
	And I will receive the message "Telephone number must be a valid phone number"
	And I will remain on the personalised-landlord-details page
	
#Unconfirmed email address
	When I have supplied an email address as "nds-dummyOne@northgateps.com"
	And I have supplied a different confirm email address as "dumy@northgateps.com"
	And I select Next
	Then I will receive the message "Confirm email address must match email address"
	And I will remain on the personalised-landlord-details page
	
#Happy path, move to personalised-landlord address page
	Given I have supplied valid data
	When I select Next
	Then I will be taken to the personalised-landlord-address page


Scenario: Process personalised-landlord details for Organisation

	Given I am on the personalised-landlord-details to enter Organisation name
	And I have not entered any data
	When I select Next
	Then I will receive the message "Organisation name must be entered"
	And I will receive the message "Email address must be entered"
	And I will receive the message "Confirm email address must be entered"
	And I will receive the message "Telephone number must be entered"
	And I will remain on the personalised-landlord-details page
	
	