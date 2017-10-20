Feature: Search Exemptions - Register Search Exemptions with non-js mode

Scenario: Other ways to search for exemptions without javascript

    #Show extra fields 
    Given I am on the 'register-search-exemptions' page 
    And javascript is disabled 
    When the page is displayed 
    Then the Enter the postcode of the rental property will be displayed 
    And the Landlord’s name box will be displayed  
    And the Exempt property details box will be displayed  
    And the Town box will be displayed 
    And the Property type box will be displayed with a value of "All properties" 
    And a Domestic exemption type box will be displayed with a value of all "All types" populated with domestic exemption type descriptions 
    And a Non-domestic exemption type box will be displayed with a value of all "All types" populated with non-domestic exemption type descriptions 
    And I will remain on the 'register-search-exemptions' page
    
    #No search criteria selected 
    And I have not supplied Enter the postcode of the rental property 
    And I have not supplied Landlord’s name 
    And I have not supplied Exempt property details 
    And I have not supplied Town 
    And I have supplied "All properties" as Property type 
    And I have supplied "All types" as Domestic exemption type 
    And I have supplied "All types" as Non-domestic exemption type 
    When I select 'Find exemptions' 
    Then I will receive the validation message "Please enter at least one search term"
    And I will remain on the 'register-search-exemptions' page
    
    #No non-domestic exemption type selected 
    And I have not supplied Enter the postcode of the rental property 
    And I have not supplied Landlord’s name 
    And I have not supplied Exempt property details 
    And I have not supplied Town 
    And I have supplied "Non-domestic" as Property type
    And I have supplied "All types" as Non-domestic exemption type 
    When I select 'Find exemptions' 
    Then I will receive the validation message "Please enter at least one more search term" 
    And I will remain on the 'register-search-exemptions' page
    
    #domestic exemption type selected 
    And I have not supplied Enter the postcode of the rental property 
    And I have not supplied Landlord’s name 
    And I have not supplied Exempt property details 
    And I have not supplied Town 
    And I have supplied "Domestic" as Property type 
    And I have supplied "All types" as Domestic exemption type 
    When I select 'Find exemptions' 
    Then I will receive the validation message "Please enter at least one more search term" 
    And I will remain on the 'register-search-exemptions' page
    
    #Only property type selected 
    And I have not supplied Enter the postcode of the rental property  
    And I have not supplied Landlord’s name 
    And I have not supplied Exempt property details 
    And I have not supplied Town 
    And I have not supplied "All properties" as Property type 
    And I have supplied "All types" as Domestic exemption type 
    And I have supplied "All types" as Non-domestic exemption type 
    When I select 'Find exemptions' 
    Then I will receive the validation message "Please enter at least one more search term" 
    And I will remain on the 'register-search-exemptions' page
    
    #Non-domestic exemption type selected without property type 
    And I have supplied a Non-domestic exemption type 
    And I have supplied "All properties" as Property type 
    When I select 'Find exemptions' 
    Then I will receive the validation message "Property type must be supplied" 
    And I will remain on the 'register-search-exemptions' page
    
    #Domestic exemption type selected without property type 
    And I have supplied a Domestic exemption type 
    And I have supplied "All properties" as Property type 
    When I select 'Find exemptions' 
    Then I will receive the validation message "Property type must be supplied" 
    And I will remain on the 'register-search-exemptions' page
    
    #Domestic exemption type selected with Non-domestic property type 
    And I have supplied valid search criteria 
    And I have supplied a Domestic exemption type 
    And I have supplied "Non-domestic" as Property type 
    When I select 'Find exemptions' 
    Then the Domestic exemption type will be ignored in the search 
    And I will remain on the 'register-search-exemptions' page
    
    #Non-domestic exemption type selected with Domestic property type 
    And I have supplied valid search criteria 
    And I have supplied a Non-domestic exemption type 
    And I have supplied "Domestic" as Property type 
    When I select 'Find exemptions' 
    Then the Non-domestic exemption type will be ignored in the search 
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