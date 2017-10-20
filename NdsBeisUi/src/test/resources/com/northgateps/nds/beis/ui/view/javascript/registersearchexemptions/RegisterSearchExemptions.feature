Feature: Search Exemptions - Register Search Exemptions

Scenario: Minimum standard of energy efficiency 
	Given I am on the 'register-search-exemptions' page 
    When I select 'minimum standard of energy efficiency' 
    Then I will be taken to the 'Minimum standard of energy efficiency information' page
    
Scenario: Other ways to search for exemptions
    Given I am on the 'register-search-exemptions' page 
    
    #Show extra fields
    When I select 'Other ways to search for exemptions' 
    Then the Landlord’s name box will be displayed 
    And the Exempt property details box will be displayed 
    And the Town box will be displayed 
    And the Property type box will be displayed with a value of "All properties" 
    And I will remain on the 'register-search-exemptions' page
    
    #Select Non-domestic 
    When I select 'Non-domestic' as a value for Property type 
    Then the Non-Domestic Exemption type box will be displayed with a value of "All types" 
    And the Exemption type box will be populated with the short descriptions of non-domestic exemption types 
    And I will remain on the 'register-search-exemptions' page 
    
    #Select Domestic 
    When I select 'Domestic' as a value for Property type  
    Then the Domestic Exemption type box will be displayed with a value of "All types" 
    And the Exemption type box will be populated with the short descriptions of domestic exemption types
    And I will remain on the 'register-search-exemptions' page
    
    #Select All properties 
    When I select 'All properties' as a value for Property type 
    Then the Exemption type box will be hidden 
    And I will remain on the 'register-search-exemptions' page
    
    #No search criteria selected 
    And I have not supplied Enter the postcode of the rental property 
    And I have not supplied Landlord’s name 
    And I have not supplied Exempt property details 
    And I have not supplied Town 
    And I have supplied 'All properties' as Property type 
    When I select 'Find exemptions' 
    Then I will receive the validation message "Please enter at least one search term"
    And I will remain on the 'register-search-exemptions' page 
    
    #No non-domestic exemption type selected
    When I select 'Other ways to search for exemptions' 
    And I have not supplied Enter the postcode of the rental property  
    And I have not supplied Landlord’s name 
    And I have not supplied Exempt property details 
    And I have not supplied Town 
    And I have supplied 'Non-domestic' as Property type 
    And I have supplied 'All types' as Exemption type 
    When I select 'Find exemptions' 
    Then I will receive the validation message "Please enter at least one more search term" 
    And I will remain on the 'register-search-exemptions' page 
    
    #No domestic exemption type selected 
    And I have not supplied Enter the postcode of the rental property 
    And I have not supplied Landlord’s name 
    And I have not supplied Exempt property details 
    And I have not supplied Town 
    And I have supplied 'Domestic' as Property type 
    And I have supplied 'All types' as Domestic Exemption type 
    When I select 'Find exemptions' 
    Then I will receive the validation message "Please enter at least one more search term" 
    And I will remain on the 'register-search-exemptions' page 
    
    #Not enough characters supplied with wild card 
    And I have entered a wild card in Landlord’s name and entered less than 2 other characters in Landlord’s name
    And I have entered a wild card in Exempt property details and entered less than 2 other characters in Exempt property details
    And I have entered a wild card in Town and entered less than 2 other characters in Town
    When I select 'Find exemptions' 
    Then I will receive the validation message "Landlord's name must be at least 2 characters excluding *" 
    And I will receive the validation message "This must be at least 2 characters excluding *" above the Landlord's name field 
    And I will receive the validation message "Exempt property details must be at least 2 characters excluding *" 
    And I will receive the validation message "This must be at least 2 characters excluding *" above the Exempt property details field 
    And I will receive the validation message "Town must be at least 2 characters excluding *" 
    And I will receive the validation message "This must be at least 2 characters excluding *" above the Town field 
    And I will remain on the 'register-search-exemptions' page
    
    #Not enough characters supplied without wild card 
    And I have not entered a wild card in Landlord’s name and entered less than 2 characters in Landlord’s name
    And I have not entered a wild card in Exempt property details and entered less than 2 characters in Exempt property details 
    And I have not entered a wild card in Town and entered less than 2 characters in Town
    When I select 'Find exemptions' 
    Then I will receive the validation message "Landlord's name must be at least 2 characters" 
    And I will receive the validation message "This must be at least 2 characters" above the Landlord's name field 
    And I will receive the validation message "Exempt property details must be at least 2 characters" 
    And I will receive the validation message "This must be at least 2 characters" above the Exempt property details field 
    And I will receive the validation message "Town must be at least 2 characters" 
    And I will receive the validation message "This must be at least 2 characters" above the Town field 
    And I will remain on the 'register-search-exemptions' page
    
    #Not enough characters supplied in postcode 
    And I have entered less than 2 characters in Enter the postcode of the rental property 
    When I select 'Find exemptions' 
    Then I will receive the validation message "Postcode of the rental property must be at least 2 characters" 
    And I will receive the validation message "This must be at least 2 characters" above the Enter the postcode of the rental property field 
    And I will remain on the 'register-search-exemptions' page
    
    #Valid criteria supplied with no exemptions 
    And I have supplied valid search criteria 
    And no exemptions exist for the details supplied 
    When I select 'Find exemptions' 
    Then I will receive the message "No exemptions found for the details entered" 
    And I will remain on the 'register-search-exemptions' page 
    
    #Valid criteria supplied with exemptions
    And I have supplied valid search criteria
    And exemptions exist 
    When I select 'Find exemptions' 
    Then the property address will be displayed for each exemption 
    And the organisation name will be displayed for each exemption 
    And I will remain on the 'register-search-exemptions' page
    
    #Hide extra fields 
    When I select 'Other ways to search for exemptions' again 
    Then the Landlord’s name box will be hidden 
    And the Exempt property details box will be hidden 
    And the Town box will be hidden 
    And the Property type box will be hidden 
    And the Exemption type box will be hidden 
    And I will remain on the 'register-search-exemptions' page
    
Scenario: Finish 
	Given I am on the 'register-search-exemptions' page 
    When I select Finish 
    Then I will be taken to the finish page