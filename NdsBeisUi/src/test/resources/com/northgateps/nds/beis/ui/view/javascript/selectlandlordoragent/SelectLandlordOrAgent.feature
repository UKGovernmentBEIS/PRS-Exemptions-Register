Feature: Registration - Select Landlord or Agent

Scenario: Navigate back to used service before
	Given I am on the select-landlord-or-agent page
	When I select Back
	Then I will be taken to the 'used-service-before' page
	
Scenario: Select landlord

	#No data entered
	Given I am on the select-landlord-or-agent page
	And I have not selected landlord or agent
	When I select Next
	Then I will receive the message "You must select whether the account is for a landlord or an agent" 
	And I will remain on the 'select-landlord-or-agent' page
	
	#Happy path, landlord selected
	Given I have selected landlord
	When I select Next
	Then I will be taken to the 'select-landlord-type' page
	
Scenario: Select agent

	Given I am on the select-landlord-or-agent page
	And I have selected agent
	When I select Next
	Then I will be taken to the 'account-details' page
	And Agent name will be available for input
	And heading will be 'Agent details'




	
	