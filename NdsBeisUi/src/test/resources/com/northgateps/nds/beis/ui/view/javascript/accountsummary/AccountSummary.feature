Feature: Account Summary

Scenario: Navigate back to personalised-dashboard with View my exemptions button
	Given I am on the personalised-account-summary page
	When I select 'View my exemptions'
    Then I will be taken to the personalised-dashboard page
	And details for the account will be displayed 