Feature: Search Penalties - Result

Scenario: View penalty
	Given I am on the register-search-penalties page
	And I search exemption using postcode
	
	#View penalty using property address
	When I select the property address
    Then I will be taken to the register-penalties page
 
 	#Use Back
    When I select Back
    Then I will be taken to the register-search-penalties page
    And the search results will be displayed
 
    #View penalty using landlord name link
    When I select the landlord name
    Then I will be taken to the register-penalties page
    
    #Use Finish
    When I select 'New search'
    Then I will be taken to the register-search-penalties page
    And the search results will be displayed