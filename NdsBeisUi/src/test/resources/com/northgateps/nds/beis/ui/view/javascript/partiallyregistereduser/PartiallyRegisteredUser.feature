Feature: Registration - Partially Registered User
	
Scenario: Logon and check pages for a person

	Given I am on the 'select-landlord-or agent' page for partially registered user
	And I will receive the message "Account details not found"
	And I have selected landlord
	When I select Next 
	Then I will be taken to the 'select-landlord-type' page
	And landlord type details held in LDAP will be displayed
	And I have selected person as landlord type
	When I select Next on the 'select-landlord-type' page
	Then I will be taken to the 'account-details' page
	And First name and Last name will be available for input
	And account details held in LDAP will be displayed
	When I select Next on account details
	Then I will be taken to the 'account-address' page
	And post code held in LDAP will be displayed

Scenario: Logon and check pages for a person with party ref

	Given I am on the 'select-landlord-or agent' page for partially registered user with party ref
	And I will receive the message "Account details not found"
	And I have selected landlord
	When I select Next 
	Then I will be taken to the 'select-landlord-type' page
	And landlord type details held in LDAP will be displayed
	And I have selected person as landlord type
	When I select Next on the 'select-landlord-type' page
	Then I will be taken to the 'account-details' page
	And First name and Last name will be available for input
	When I select Next on account details
	Then I will be taken to the 'account-address' page
	And post code held in LDAP will be displayed
	

Scenario: Logon and check some pages for an organisation

	Given I am on the 'select-landlord-or agent' page for partially registered organisation user
	And I will receive the message "Account details not found"
	And I have selected landlord
	When I select Next 
	Then I will be taken to the 'select-landlord-type' page
	And landlord type details held in LDAP for organisation will be displayed
	And I have selected organisation as landlord type
	When I select Next on the 'select-landlord-type' page
	Then I will be taken to the 'account-details' page
	And Organisation name will be available for input
	And details held in LDAP will be displayed
	
Scenario: Logon and check pages for an agent

	Given I am on the 'select-landlord-or agent' page for partially registered agent user
	And I will receive the message "Account details not found"
	And I have selected agent
	When I select Next
	Then I will be taken to the 'account-details' page
	And Agent name will be available for input
	And account details held in LDAP for agent will be displayed
	When I select Next on 'account details' page 
	Then I will be taken to the 'account-address' page
	And post code held in LDAP will be displayed
	
