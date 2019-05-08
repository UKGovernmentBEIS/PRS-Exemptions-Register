Feature: Dashboard test

Scenario: Landlord dashboard
    Given I am on the personalised-dashboard page 
    And I can see the correct guidance text   
    And I have signed in as a landlord
    When the page is displayed
    Then I will see a summary of exemptions for the landlord

Scenario: View exemption
	Given I am on the personalised-dashboard page
	When the page is displayed
	Then I will be see the number of current exemptions as 2
	And I will see the number of expired exemptions as 1
	And I will have an option to view my exemptions
	And I will remain on the 'personalised-dashboard' page

#View exemptions
	Given no exemptions are displayed
	When I select 'View exemptions'
	Then I will see a list of my current exemptions
	And I will have an option to end each exemption
	And I will remain on the 'personalised-dashboard' page

#View current exemptions	
	And I will see a list of my current exemptions
    And I can see the exemption reference displayed
    And I can see the landlord displayed
	And I will have an option to end each exemption	
	And I will remain on the 'personalised-dashboard' page

#View expired exemptions		
	Given I select 'Expired exemptions'
	Then I will see a list of my expired exemptions
	And I will remain on the 'personalised-dashboard' page

#Switch to current exemptions	
	When I select 'Current exemptions'
	Then I will see a list of my current exemptions
	And I will have an option to end each exemption
	And I will remain on the 'personalised-dashboard' page

#Switch to expired exemptions	
	When I select 'Expired exemptions'
	Then I will see a list of my expired exemptions
    And I can see the exemption reference displayed
    And I can see the landlord displayed
	And I will remain on the 'personalised-dashboard' page

#Hide exemptions	
	When I select 'View exemptions'
	Then the exemptions will be hidden
	And I will remain on the 'personalised-dashboard' page

#Re-display expired exemptions	
	When I select 'View exemptions' again
	Then I will see a list of my expired exemptions
	And I will remain on the 'personalised-dashboard' page
	
#Re-display current exemptions
	Given current exemptions are displayed
	And I select 'View exemptions'
	When I select 'View exemptions' again
	Then I will see a list of my current exemptions
	And I will have an option to end each exemption
	And I will remain on the 'personalised-dashboard' page

#Move to end exemption page	
	When I select 'End exemption'
	Then I will be taken to the 'personalised-end-exemption' page
	
Scenario: Agent dashboard
    Given I am on the personalised-dashboard page with signed as agent
    When the page is displayed
    Then I will see a summary of exemptions for the agent

Scenario: Find out more about exemptions
	Given I am on the personalised-dashboard page
	When I click on 'Find out more about exemptions'
	Then I will be taken to the page for that link
	
Scenario: Navigate to account summary for person
	Given I am on the personalised-dashboard page for a person	
	When I select Account details
	Then I will be taken to the personalised-account-summary page
	And details will be displayed with First name and Last name
	
Scenario: Navigate to account summary for organisation
	Given I am on the personalised-dashboard page for an organisation
	When I select Account details
	Then I will be taken to the personalised-account-summary page
	And details will be displayed with Organisation name
	
Scenario: Navigate to account summary for agent
	Given I am on the personalised-dashboard page with signed as agent
	When I select Account details
	Then I will be taken to the personalised-account-summary page
	And Agent name will be displayed
    And there will be a 'Change your agent details' link
	
Scenario: Navigate to select landlord type

	Given I am on the personalised-dashboard page with signed as agent
	When I select 'Register a new exemption'
	Then I will be taken to the 'select-landlord-type-agent' page
	And heading will be 'Is the landlord an organisation or a person?'
	
	
	