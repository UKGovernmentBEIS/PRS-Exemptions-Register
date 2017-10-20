Feature: Search Penalties - Register Search Penalties

Scenario: Postcode search
    Given I am on the 'register-search-penalties' page 
    
    #Remove landlord option 
    When the page is first displayed 
    Then I will only have the option to search by postcode 
    And I will remain on the 'register-search-penalties' page 
    
    #What is a penalty notice 
    When I select 'What is a penalty notice' 
    Then I will be taken to the 'What is a penalty notice information page'
    
    #Select Finish 
    When I select Finish 
    Then I will be taken to the finish page 
    
Scenario: Other ways to search for penalties
    Given I am on the 'register-search-penalties' page 
    
    #Show extra fields 
    When I select 'Other ways to search for penalties' 
    Then the Landlord’s name box will be displayed 
    And the Rental property details box will be displayed 
    And the Town box will be displayed 
    And the Property type box will be displayed with a value of "All properties" 
    And I will remain on the 'register-search-penalties' page
    
    #Select Non-domestic 
    When I select "Non-domestic" as a value for Property type 
    Then the Non-domestic Penalty type box will be displayed with a value of "All types" 
    And the Penalty type box will be populated with the short descriptions of non-domestic penalty types
    And I will remain on the 'register-search-penalties' page
    
    #Select Domestic 
    When I select "Domestic" as a value for Property type 
    Then the Domestic Penalty type box will be displayed with a value of "All types" 
    And the Penalty type box will be populated with the short descriptions of domestic penalty types
    And I will remain on the 'register-search-penalties' page
    
    #Select All properties 
    When I select "All properties" as a value for Property type 
    Then the Penalty type box will be hidden 
    And I will remain on the 'register-search-penalties' page
    
    #No search criteria selected 
    Given I have not supplied Enter the postcode of the rental property 
    And I have not supplied Landlord’s name 
    And I have not supplied Rental property details 
    And I have not supplied Town 
    When I select "All properties" as a value for Property type 
    When I select 'Find penalty notices' 
    Then I will receive the validation message "Please enter at least one search term"
    And I will remain on the 'register-search-penalties' page 
    
    #No non-domestic penalty type selected 
    When I select 'Other ways to search for penalties'
    And I have not supplied Enter the postcode of the rental property 
    And I have not supplied Landlord’s name 
    And I have not supplied Rental property details 
    And I have not supplied Town 
    When I select "Non-domestic" as a value for Property type 
    And I have supplied "All types" as Non-domestic Penalty type 
    When I select 'Find penalty notices' 
    Then I will receive the validation message "Please enter at least one more search term"
    And I will remain on the 'register-search-penalties' page
    
    #No domestic penalty type selected 
    And I have not supplied Enter the postcode of the rental property 
    And I have not supplied Landlord’s name 
    And I have not supplied Rental property details 
    And I have not supplied Town 
    When I select "Domestic" as a value for Property type 
    And I have supplied "All types" as Domestic Penalty type 
    When I select 'Find penalty notices' 
    Then I will receive the validation message "Please enter at least one more search term"
    And I will remain on the 'register-search-penalties' page
    
    #Not enough characters supplied with wild card 
    And I have entered a wild card in Landlord’s name and entered less than 2 other characters in Landlord’s name
    And I have entered a wild card in rental property details and entered less than 2 other characters in rental property details
    And I have entered a wild card in Town and entered less than 2 characters in Town
    When I select 'Find penalty notices' 
    Then I will receive the validation message "Landlord's name must be at least 2 characters excluding *" 
    And I will receive the validation message "This must be at least 2 characters excluding *" above the Landlord's name field 
    And I will receive the validation message "Rental property details must be at least 2 characters excluding *" 
    And I will receive the validation message "This must be at least 2 characters excluding *" above the Rental property details field 
    And I will receive the validation message "Town must be at least 2 characters excluding *" 
    And I will receive the validation message "This must be at least 2 characters excluding *" above the Town field 
    And I will remain on the 'register-search-penalties' page
    
    #Not enough characters supplied without wild card 
    And I have not entered a wild card in Landlord’s name and entered less than 2 other characters in Landlord’s name
    And I have not entered a wild card in rental property details and entered less than 2 other characters in rental property details
    And I have not entered a wild card in Town and entered less than 2 characters in Town
    When I select 'Find penalty notices' 
    Then I will receive the validation message "Landlord's name must be at least 2 characters" 
    And I will receive the validation message "This must be at least 2 characters" above the Landlord's name field 
    And I will receive the validation message "Rental property details must be at least 2 characters" 
    And I will receive the validation message "This must be at least 2 characters" above the Rental property details field 
    And I will receive the validation message "Town must be at least 2 characters" 
    And I will receive the validation message "This must be at least 2 characters" above the Town field 
    And I will remain on the 'register-search-penalties' page
    
    #Not enough characters supplied in postcode 
    Given I have entered less than 2 characters in Enter the postcode of the rental property 
    When I select 'Find penalty notices' 
    Then I will receive the validation message "Postcode of the rental property must be at least 2 characters" 
    And I will receive the validation message "This must be at least 2 characters" above the Enter the postcode of the rental property field 
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
    
    #Hide extra fields 
    When I select 'Other ways to search for penalties' again 
    Then the Landlord’s name box will be hidden 
    And the Rental property details box will be hidden 
    And the Town box will be hidden 
    And the Property type box will be hidden 
    And the Penalty type box will be hidden
    And I will remain on the 'register-search-penalties' page