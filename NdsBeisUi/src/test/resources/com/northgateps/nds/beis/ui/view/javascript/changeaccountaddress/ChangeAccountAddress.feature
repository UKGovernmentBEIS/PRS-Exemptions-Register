Feature: Change Account Address

Scenario: Navigation
	Given I am on the 'personalised-account-summary' page

#Scenario: Navigate to change account address	
	When I select Change address link
	Then I will be taken to the 'personalised-change-account-address' page

#Scenario: Navigate back to account details	
	When I select Back
	Then I will be taken to the 'personalised-account-summary' page
	And details will be displayed

Scenario: Selecting address
	Given I am on the 'personalised-change-account-address' page

	#No postcode supplied	
	When  I have not supplied a postcode
	When I select Find address
	Then I will receive the message "Postcode must be entered"
	And I will remain on the 'personalised-change-account-address' page	
	
	#Valid postcode supplied with addresses
	Given I have supplied a valid postcode
	And addresses exist for the postcode
	When I select Find address
	Then a pick an address selection box will be displayed
	And the number of addresses will be displayed in the box as "44 addresses found"
	And I will remain on the 'personalised-change-account-address' page
	
	#Select address	
	When I select an address
	Then the address will be displayed
	And the address fields will be display only
	And I will remain on the 'personalised-change-account-address' page
	
	#Success	
	When I select Change address	
	Then I will be taken to the 'personalised-account-summary' page
	And details for the account will be displayed
	
Scenario: Postcode validation when updating an account address manually

	#Validate UK postcode
	Given I am on the 'personalised-change-account-address' page
	And I have selected Enter an address manually
	And I have selected Country as United Kingdom
	And I have supplied Postcode not in UK format
	When I select Submit address detail changes
	Then I will receive the message "You must enter a valid postcode"
	And I will remain on the 'personalised-change-account-address' page

	#Postcode optional
	
	Given I have selected Country as not United Kingdom
	And I have not supplied Postcode 
	When I select Next
	Then I will be taken to the 'personalised-account-summary' page	