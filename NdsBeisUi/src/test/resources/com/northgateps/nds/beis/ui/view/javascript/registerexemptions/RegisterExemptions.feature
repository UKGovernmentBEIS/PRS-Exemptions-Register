Feature: Register Exemptions (Search Exemptions)

Scenario: View exemption using address link
	Given I am on the register-search-exemptions page
	When I select the property address
    Then I will be taken to the register-exemptions page
    
Scenario: View exemption using landlord name link
    Given I am on the register-search-exemptions page
    When I select the landlord name
    Then I will be taken to the register-exemptions page
    
Scenario: Move back to search results
    Given I am on the register-exemptions page
    
    #Use Back
    When I select Back
    Then I will be taken to the register-search-exemptions page
    And the search results will be displayed
    
    #Use finish
    When I select 'New search'
    Then I will be taken to the register-search-exemptions page
    And the search results will be displayed
    
Scenario: Download EPC

   Given I am on the register-exemptions page with EPC available
   
   #Download available 
   And the exemption has a suitable EPC
   When the page is displayed 
   Then I will have the option to download the EPC 
   And I will have the option to report the content 
   And I will remain on the 'register-exemptions' page
   
   #Download EPC 
   When I will have selected the option to download the EPC 
   Then the EPC will be downloaded 
   And I will remain on the 'register-exemptions' page
   
   #Report content 
   When I will have selected the option to report the content 
   Then I will be taken to the 'report content email link page'
    
    
    