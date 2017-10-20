Feature: Register Exemption - Exemption Requirements

Scenario: Navigate back select exemption type
	Given I am on the 'personalised-exemption-requirements' page
	When I select Back
	Then I will be taken to the 'personalised-select-exemption-type' page
	And details previously entered will be displayed
	
Scenario: Move to property address page
	Given I am on the 'personalised-exemption-requirements' page
	And requirements for the exemption type selected is displayed
	When I select Next
	Then I will be taken to the 'property address' page