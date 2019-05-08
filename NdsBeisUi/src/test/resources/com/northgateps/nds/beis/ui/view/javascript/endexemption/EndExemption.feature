Feature: End Exemption

Scenario: To End current registered exemption
	Given I am on the end-exemption page
	When I select Back
	Then I will be taken to personalised-dashboard page
	When I click on end exemption link
	Then I will be taken to end-exemption page
	When I select End exemption
	Then I will be taken to the end-exemption-confirmation page
	When I select 'View my exemptions'
	Then I will be taken to personalised-dashboard page
	