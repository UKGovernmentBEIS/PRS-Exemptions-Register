Feature: Register Exemption - Exemption Start Date

Scenario: Date validation
	Given I am on the 'personalised-exemption-start-date' page
	
	Given I have not entered a date
	When I select Next
	Then I will receive the error message "Date must be entered" as validation message
	And I will remain on the 'personalised-exemption-start-date' page
	
	Given I have supplied a date in the future
	When I select Next
	Then I must receive "Date must be on or in the past of" as validation message
	And I will remain on the 'personalised-exemption-start-date' page
	
	
	Given I have supplied a start date that means the exemption has already ended.
	When I select Next
	Then I will receive validation message "You can't register the exemption because it has already ended.  This exemption lasts for "
	And I will remain on the 'personalised-exemption-start-date' page