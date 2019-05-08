Feature: Register Exemption - Select Property Type

	
Scenario: Select property type
	Given I am on the 'personalised-select-property-type' page
	
	Given I have not entered any data
	When I select Next
	Then I will receive the message "You must choose an option for What type of property are you registering"
	And I will remain on the 'personalised-select-property-type' page
	
	Given I select 'More about property types'
	Then help text will be displayed
	And I will remain on the 'personalised-select-property-type' page
	
	Given I select 'More about property types'
	Then help text will be hidden
	And I will remain on the 'personalised-select-property-type' page

	#Select non-domestic
	
	When I select the "PRSN" option
	Then the Next button will be shown
	And I will remain on the 'personalised-select-property-type' page
	
	
	When I select Next
	Then I will be taken to the 'personalised-select-exemption-type' page
	And exemptions for the type of property selected will be displayed
	
	
