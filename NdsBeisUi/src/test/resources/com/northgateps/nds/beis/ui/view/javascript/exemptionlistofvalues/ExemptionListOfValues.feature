Feature: Register Exemption - Exemption List Of Values

Scenario: No option selected
	Given I am on the 'personalised-exemption-list-of-values' page
	And I have not selected an option
	When I select Next
	Then I must receive "An option must be selected" as validation message
	And I will remain on the 'personalised-exemption-list-of-values' page
	