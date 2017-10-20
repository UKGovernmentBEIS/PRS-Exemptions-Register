Feature: Register Exemption - Select Exemption Type

Scenario: Navigate back select property type
	Given I am on the 'personalised-select-exemption-type' page
	When I select Back
	Then I will be taken to the 'personalised-select-property-type' page
	And details previously entered will be displayed
	
Scenario: Select exemption type
	Given I am on the 'personalised-select-exemption-type' page
	
	Given I have not entered any data
	When I select Next
	Then I will receive the message "What type of exemption are you registering must be selected"
	And I will remain on the 'personalised-select-exemption-type' page
	
	Given I have selected an exemption type
	When I select Next
	Then I will be taken to the 'personalised-exemption-requirements' page
	