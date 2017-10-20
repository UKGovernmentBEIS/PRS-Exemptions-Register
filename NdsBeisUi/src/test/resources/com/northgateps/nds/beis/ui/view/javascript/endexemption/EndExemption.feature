Feature: End Exemption

Scenario: End exemption
	Given I am on the end-exemption page
	When I select End exemption
	Then I will be taken to the end-exemption-confirmation page
	
Scenario: Move back to dashboard
	Given I am on the end-exemption page
	When I select Back
	Then I will be taken to personalised-dashboard page
	
	