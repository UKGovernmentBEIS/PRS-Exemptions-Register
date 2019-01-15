Feature: Register Exemption - Exemption Property Address 

Scenario: Navigate back to exemption requirements page 
	Given I am on the personalised-property-address page
	When I select Back
	Then I will be taken to the personalised-exemption-requirements page
	
Scenario: Process address details
	Given I am on the personalised-property-address page
	
	#Enter address manually
	Given I have not selected an address
	When I select Enter an address manually
	Then the address fields will be displayed
	And the address fields will be updatable
	And I will remain on the personalised-property-address page
	
	#Not all address data supplied
	Given I have only supplied Postcode
	When I select Next
	Then I must receive "Building and street must be entered" as validation message
	And I must receive "Town must be entered" as validation message
	And I will remain on the personalised-property-address page
	
	Given I have only supplied Town
	When I select Next
	Then I must receive "Building and street must be entered" as validation message
	And I must receive "Postcode must be entered" as validation message
	And I will remain on the personalised-property-address page
	
	#No address supplied
	
	Given I have not selected an address from the address search
	And I have not supplied any manual address details
	When I select Next
	Then I must receive "You must enter an address" as validation message
	And I will remain on the personalised-property-address page
	
	#No postcode supplied
	
	Given I have not supplied a postcode
	When I select Find address
	Then I will receive the message "Postcode must be entered"
	And I will remain on the personalised-property-address page
	
	#Invalid postcode supplied
	
	Given I have supplied an invalid postcode
	When I select Find address
	Then I will receive the message "You must enter a valid postcode"
	And I will remain on the personalised-property-address page
	
	#Valid postcode supplied with no addresses
	
	Given I have supplied a valid postcode with no Address exists
	When I select Find address
	Then I will receive the message "No Address found for given postcode" 
	And I will remain on the personalised-property-address page
	
	#Valid postcode supplied with addresses
	
	Given I have supplied a valid postcode
	And addresses exist for the postcode
	When I select Find address
	Then a pick an address selection box will be displayed
	And the number of addresses will be displayed in the box as "44 addresses found"
	And I will remain on the personalised-property-address page
	
	#Valid postcode address already displayed
	
	#Given I have supplied a valid postcode
	#And addresses exist for the postcode
	#And address details are already displayed
	#When I select Find address
	#Then the address fields will be hidden
	#And I will remain on the personalised-property-address page
	
	#Select address
	Given I have supplied a valid postcode
	And addresses exist for the postcode
	And I have selected Find address
	When I select an address
	Then the address will be displayed
	And the address fields will be display only
	Then I will remain on the personalised-property-address page
	
	#Select address and enter manually
	Given I have supplied a valid postcode
	And addresses exist for the postcode
	And I have selected Find address
	And I have selected an address
	And the address is displayed
	When I select Enter an address manually
	Then the address fields will become updatable
	And I will remain on the personalised-property-address page
	
	#Enter Scottish or Northern Ireland postcodes
	Given I have selected or entered a valid address
	And the address postcode is outside England and Wales
	When I select Next
	Then I will receive the message "Check the postcode. You can only register exemptions for properties in England or Wales"
	And I will remain on the personalised-property-address page
	
	#Move to exemption document upload page
	Given I have selected or entered a valid address
	When I select Next
	Then I must move to the personalised-epc-details page
	