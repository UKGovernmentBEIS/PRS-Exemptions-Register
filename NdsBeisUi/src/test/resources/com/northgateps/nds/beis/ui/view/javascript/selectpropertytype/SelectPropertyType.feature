Feature: Register Exemption - Select Property Type

Scenario: Navigate to select property type
	Given I am on the personalised-dashboard page
	When I select 'Register a new exemption'
	Then I will be taken to the personalised-select-property-type page
	
Scenario: Navigate back to personalised-dashboard
	Given I am on the 'personalised-select-property-type' page
	When I select Back
	Then I will be taken to the 'personalised-dashboard' page
	
Scenario: Select property type
	Given I am on the 'personalised-select-property-type' page
	
	Given I have not entered any data
	When I select Next
	Then I will receive the message "What type of property are you registering must be selected"
	And I will remain on the 'personalised-select-property-type' page
	
	Given I select 'More about property types'
	Then help text will be displayed
	And I will remain on the 'personalised-select-property-type' page
	
	Given I select 'More about property types'
	Then help text will be hidden
	And I will remain on the 'personalised-select-property-type' page

	#Select non-domestic
	
	When I select the "PRSN" option
	Then the Next button will be shown
	And I will remain on the 'personalised-select-property-type' page
	
	
	When I select Next
	Then I will be taken to the 'personalised-select-exemption-type' page
	And exemptions for the type of property selected will be displayed
	
	
Scenario: Select property type as domestic

	Given I am on the 'personalised-select-property-type' page
	And I select the "PRSD" option
	When I select Next
	Then I will be taken to the 'personalised-select-exemption-type' page
	And exemptions for the type of property selected will be displayed
	
Scenario: Back from select property type
	
	Given I have signed in as an agent
	And I am on the personalised-select-property-type page
	When I select Back
	Then I will be taken to the 'personalised-landlord-address' page
	And details previously entered will be displayed
	