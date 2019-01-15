Feature: Registration - Account Address 

Scenario: Navigate back to landlord details
	Given I am on the account-address page
	When I select Back
	Then I will be taken to the account-details page
	And details previously entered will be displayed

Scenario: Process address details
	Given I am on the account-address page
	
	#Enter address manually
	Given I have not selected an address
	When I select Enter an address manually
	Then the address fields will be displayed
	And the address fields will be updatable
	And I will remain on the account-address page
	
	#No address supplied
	
	Given I have not selected an address from the address search
	And I have not supplied any manual address details
	When I select Next
	Then I will receive the message "You must enter an address"
	And I will remain on the account-address page
	
	#Not all address data supplied
	Given I have only supplied Postcode
	When I select Next
	Then I must receive "Building and street must be entered" as validation message
	And I must receive "Town must be entered" as validation message
	And I will remain on the account-address page
	
	Given I have only supplied Town
	When I select Next
	Then I must receive "Building and street must be entered" as validation message
	And I must receive "Postcode must be entered" as validation message
	And I will remain on the account-address page

	#Validate UK postcode
	Given I have selected Country as "United Kingdom"
	And I have supplied Postcode not in UK format
	When I select Next
	Then I will receive the message "You must enter a valid postcode"
	And I will remain on the account-address page
	
	#No postcode supplied
	
	Given I have not supplied a postcode
	When I select Find address
	Then I will receive the message "Postcode  must be entered"
	And I will remain on the account-address page
	
	#Invalid postcode supplied
	
	Given I have supplied an invalid postcode as "xx 8da"
	When I select Find address
	Then I will receive the message "You must enter a valid postcode"
	And I will remain on the account-address page

	#Valid postcode supplied with no addresses
	
	Given I have supplied a valid postcode with no Address exists
	When I select Find address
	Then I will receive the message "No Address found for given postcode" 
	And I will remain on the account-address page
	
	#Valid postcode supplied with addresses
	
	Given I have supplied a valid postcode as "RG1 1ET"
	And addresses exist for the postcode
	When I select Find address
	Then a pick an address selection box will be displayed
	And the number of addresses will be displayed in the box as "44 addresses found"
	And I will remain on the account-address page
	
	#Valid postcode address already displayed
	
	#Given I have supplied a valid postcode
	#And addresses exist for the postcode
	#And address details are already displayed
	#When I select Find address
	#Then the address fields will be hidden
	#And I will remain on the account-address page
	
	#Select address
	Given I have supplied a valid postcode as "RG1 1ET"
	And addresses exist for the postcode
	And I have selected Find address
	When I select an address
	Then the address will be displayed
	And the address fields will be display only
	And I will remain on the account-address page
	
	#Select address and enter manually
	Given I have supplied a valid postcode as "RG1 1ET"
	And addresses exist for the postcode
	And I have selected Find address
	And I have selected an address
	And the address is displayed
	When I select Enter an address manually
	Then the address fields will become updatable
	And I will remain on the account-address page
	
	#Move to security details page
	Given I have selected or entered a valid address
	When I select Next
	Then I must move to the security-details page
		
	
Scenario: Postcode length for Country as not United Kingdom
	Given I am on the account-address page
	And I have selected Enter an address manually
	And I have selected Country as not United Kingdom
	And I have supplied Postcode "NONUKPOSTCODE"
	And I have supplied other details 
	When I select Next
	Then I will receive the message "The Postcode must be between 1 and 8 characters long" 
	And I will remain on the account-address page
	
Scenario: Postcode optional
	Given I am on the account-address page
	And I have selected Enter an address manually
	And I have selected Country as not United Kingdom
	And I have not supplied Postcode
	And I have supplied other details 
	When I select Next
	Then I must move to the security-details page
	
	
	