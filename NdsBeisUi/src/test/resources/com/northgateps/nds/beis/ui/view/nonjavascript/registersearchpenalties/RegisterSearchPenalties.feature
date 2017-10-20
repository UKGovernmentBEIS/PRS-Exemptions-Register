Feature: Search Exemptions - Register Search Penalties with non-js mode

Scenario: Other ways to search for exemptions without javascript
    Given I am on the 'register-search-penalties' page

    #Show extra fields 
    And javascript is disabled 
    When the page is displayed 
    Then the I have not supplied Enter the postcode of the rental property will be displayed
    And the Landlord’s name box will be displayed 
    And the Rental property details box will be displayed 
    And the Town box will be displayed 
    And the Property type box will be displayed with a value of "All properties" 
    And a Domestic penalty type box will be displayed with a value of all "All types" populated with domestic penalty type descriptions 
    And a Non-domestic penalty type box will be displayed with a value of all "All types" populated with non-domestic penalty type descriptions 
    And I will remain on the 'register-search-penalties' page 
    
    #No search criteria selected 
    Given I have not supplied Enter the postcode of the rental property 
    And I have not supplied Landlord’s name 
    And I have not supplied Rental property details 
    And I have not supplied Town 
    And I have supplied "All properties" as Property type 
    And I have supplied "All types" as Domestic penalty type 
    And I have supplied "All types" as Non-domestic penalty type 
    When I select 'Find penalty notices' 
    Then I will receive the validation message "Please enter at least one search term"
    And I will remain on the 'register-search-penalties' page
    
    #No non-domestic penalty type selected 
    Given I have not supplied Enter the postcode of the rental property 
    And I have not supplied Landlord’s name 
    And I have not supplied Rental property details 
    And I have not supplied Town 
    And I have supplied "Non-domestic" as Property type 
    And I have supplied "All types" as Non-domestic penalty type 
    When I select 'Find penalty notices' 
    Then I will receive the validation message "Please enter at least one more search term" 
    And I will remain on the 'register-search-penalties' page
    
    #No domestic penalty type selected 
    Given I have not supplied Enter the postcode of the rental property 
    And I have not supplied Landlord’s name 
    And I have not supplied Rental property details 
    And I have not supplied Town 
    And I have supplied "Domestic" as Property type 
    And I have supplied "All types" as Domestic penalty type 
    When I select 'Find penalty notices' 
    Then I will receive the validation message "Please enter at least one more search term"  
    And I will remain on the 'register-search-penalties' page
    
    #Only property type selected 
    Given I have not supplied Enter the postcode of the rental property 
    And I have not supplied Landlord’s name 
    And I have not supplied Rental property details 
    And I have not supplied Town 
    And I have not supplied "All properties" as Property type 
    And I have supplied "All types" as Domestic penalty type 
    And I have supplied "All types" as Non-domestic penalty type 
    When I select 'Find penalty notices' 
    Then I will receive the validation message "Please enter at least one more search term" 
    And I will remain on the 'register-search-penalties' page
    
    #Non-domestic penalty type selected without property type 
    Given I have supplied a Non-domestic penalty type 
    And I have supplied "All properties" as Property type 
    When I select 'Find penalty notices' 
    Then I will receive the validation message "Property type must be supplied" 
    And I will remain on the 'register-search-penalties' page
    
    #Domestic penalty type selected without property type 
    Given I have supplied a Domestic penalty type 
    And I have supplied "All properties" as Property type 
    When I select 'Find penalty notices' 
    Then I will receive the validation message "Property type must be supplied" 
    And I will remain on the 'register-search-penalties' page
    
    #Domestic penalty type selected with Non-domestic property type 
    Given I have supplied valid search criteria 
    And I have supplied a Domestic penalty type 
    And I have supplied "Non-domestic" as Property type 
    When I select 'Find penalty notices' 
    Then the Domestic penalty type will be ignored in the search 
    And I will remain on the 'register-search-penalties' page
    
    #Non-domestic penalty type selected with Domestic property type 
    Given I have supplied valid search criteria 
    And I have supplied a Non-domestic penalty type 
    And I have supplied "Domestic" as Property type 
    When I select 'Find penalty notices' 
    Then the Non-domestic penalty type will be ignored in the search 
    And I will remain on the 'register-search-penalties' page
    
    #Valid criteria supplied with no penalty notices 
    Given I have supplied valid search criteria 
    And no penalty notices exist for the details supplied 
    When I select 'Find penalty notices' 
    Then I will receive the message "No penalty notices found for the details entered" 
    And I will remain on the 'register-search-penalties' page 
    
    #Valid criteria supplied with penalty notices and addresses published 
    Given I have supplied valid search criteria 
    And penalty notices exist 
    And the property addresses can be published 
    When I select 'Find penalty notices' 
    Then the property address will be displayed for each penalty notice 
    And I will remain on the 'register-search-penalties' page
    
    #Valid criteria supplied with penalty notices and addresses not published 
    Given I have supplied valid search criteria 
    And penalty notices exist  
    And the property addresses cannot be published 
    When I select 'Find penalty notices' 
    Then the message "Address not published" will be displayed for each penalty 
    And I will remain on the 'register-search-penalties' page