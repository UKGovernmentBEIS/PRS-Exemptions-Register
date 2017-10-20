Feature: Register Exemption - Exemption declaration details

Scenario: Navigate back to exemption list of values page 
	Given I am on the personalised-exemption-declaration page
	When I select Back
	Then I will be taken to the personalised-exemption-list-of-values page
	And the details previously entered will be displayed
	
Scenario: Process exemption declaration
    Given I am on the personalised-exemption-declaration page
    
    #Box not ticked
    Given I have not ticked the declaration box
    When I select Submit exemption registration
    Then I will receive "I understand and agree with the declaration above must be selected" as validation message    
    And I will remain on the personalised-exemption-declaration page
	 
    #Move to personalised-exemption-confirmation page
    Given I select mandatory field
    When I select Submit exemption registration
    Then I will be taken to personalised-exemption-confirmation page
    
Scenario: domestic option selection
	Given I am registering a domestic exemption
	And I am on the 'exemption-declaration' page
	When the page is displayed
	Then I will see text "You can be fined up to £1000 if you provide false or misleading information"
	And I will remain on the personalised-exemption-declaration page
	
Scenario: landlord details on declaration page
	Given I am on the personalised-exemption-declaration page with registered as agent
	When the page is displayed
	Then I will see Landlord name 
	And I will see Landlord email address
	And I will see Landlord telephone number
	And I will remain on the personalised-exemption-declaration page
	