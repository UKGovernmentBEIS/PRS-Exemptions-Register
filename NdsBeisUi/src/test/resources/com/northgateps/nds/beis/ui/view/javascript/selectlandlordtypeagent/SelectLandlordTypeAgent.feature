Feature: Exemption Registration - Select Landlord Type Agent

Scenario: Navigate back to dashboard
	Given I am on the 'personalised-select-landlord-type-agent' page
	When I select Back
	Then I will be taken to the 'personalised-dashboard' page	
	
Scenario:Select person landlord type
	Given I am on the 'personalised-select-landlord-type-agent' page	
	And I have not entered any data
	When I select Next
	Then I will receive the message "You must choose an option for The type of account"
	And I will remain on the personalised-select-landlord-type-agent page
	
	Given I have selected person as landlord type
	When I select Next
	Then I will be taken to the 'personalised-landloard-details' page
	And First name and Last name will be available for input
	And heading will be 'Landlord’s details'	

Scenario: Select organisation landlord type
	Given I am on the 'personalised-select-landlord-type-agent' page
	And I have selected organisation as landlord type
	When I select Next
	Then I will be taken to the 'personalised-landloard-details' page
	And Organisation name will be available for input
	And heading will be 'Landlord’s details'
	
	