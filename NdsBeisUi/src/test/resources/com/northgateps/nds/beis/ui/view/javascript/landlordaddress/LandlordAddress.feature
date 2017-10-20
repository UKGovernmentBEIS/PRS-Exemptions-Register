Feature: Exemption Registration - Landlord Address 

Scenario: Navigate back to landlord details
	Given I am on the personalised-landlord-address page
	When I select Back
	Then I will be taken to the personalised-landlord-details page
	And details previously entered will be displayed

Scenario: Process landlord address details
	Given I am on the personalised-landlord-address page	
	And I have selected or entered a valid address
	When I select Next
	Then I must move to the personalised-select-property-type page
	
