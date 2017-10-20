Feature: Account Details - Change

Scenario: Navigate to change account details

	Given I am on the personalised-account-summary page
	When I select Change details
	Then I will be taken to the personalised-change-account-details page
	
Scenario: Navigate to change account details for agent

    Given I am on the personalised-account-summary page for an agent
    When I select the 'Change your agent details' link
    Then I will be taken to the personalised-change-account-details page
    And heading will be 'Change your agent details'
    And Agent name will be displayed

Scenario: Navigate back to account details
	
	Given I am on the personalised-change-account-details page
	When I select Back
	Then I will be taken to the personalised-account-summary page

Scenario: Process account details
		Given I am on the personalised-change-account-details page
		
	#No data entered for organisation	
		Given Organisation name is available for input
		And I have not entered any data
		When I select Submit
		Then I will receive the message "Organisation name must be entered"
		And I will receive the message "Telephone number must be entered"
		And I will remain on the personalised-change-account-details page
	
	#Invalid data supplied
		Given I have supplied an invalid phone number as "xyz" which does not consist of numeric or space characters with an optional leading +
		And supplied valid organisation name as "TestOrg"
		When I select Submit
		And I will receive the message "Telephone number must be a valid phone number"
		And I will remain on the personalised-change-account-details page
		
	#Happy path, move to account summary page
		Given I have supplied valid phone number as "02085822870"
		When I select Submit
		Then I will be taken to the personalised-account-summary page
		And details for the account will be displayed
		
Scenario: no data entered for person
	
	Given I am on the personalised-change-account-details page for person Category
	And First name and Last name are available for input
	And I have not entered any data for person
	When I Submit details
	Then I will receive the message "First name must be entered"
	And I will receive the message "Last name must be entered"
	And I will receive the message "Telephone number must be entered"
	And I will remain on the personalised-change-account-details page
	
	
Scenario: Agent name mandatory
    Given I am on the personalised-change-account-details page for an agent
    And I have not supplied Agent name
    When I select Submit
    Then I will receive the message "Agent name must be entered"
    And I will remain on the personalised-change-account-details page
	
	