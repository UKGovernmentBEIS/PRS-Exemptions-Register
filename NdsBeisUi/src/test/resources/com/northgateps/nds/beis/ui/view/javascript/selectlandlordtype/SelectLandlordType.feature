Feature: Registration - Select Landlord Type

Scenario: Navigate back to used service before
	Given I am on the select-landlord-type page
	When I select Back
	Then I will be taken to the 'select-landlord-or-agent' page
	And details previously entered will be displayed
	
Scenario:Select person landlord type
	Given I am on the select-landlord-type page	
	And I have not entered any data
	When I select Next
	Then I will receive the message "The type of account must be selected"
	And I will remain on the select-landlord-type page
	
	Given I have selected person as landlord type
	When I select Next
	Then I will be taken to the account-details page
	And First name and Last name will be available for input	

Scenario: Select organisation landlord type
	Given I am on the select-landlord-type page
	And I have selected organisation as landlord type
	When I select Next
	Then I will be taken to the account-details page
	And Organisation name will be available for input

	
	