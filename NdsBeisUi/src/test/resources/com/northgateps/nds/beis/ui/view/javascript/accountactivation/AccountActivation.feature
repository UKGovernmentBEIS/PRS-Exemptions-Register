Feature: Registration - Account Activation

Scenario: Navigate to activate account

	Given I am on the login page
    When I select activate your account
    Then I will be taken to the account-activation page

	#No data selected    
    Given I have not entered any data
    When I select activate registration
    Then I will receive the message "Activation code must be entered"
    And I will remain on the account activation page
    
    #Invalid code entered
    Given I have not entered a valid code
    When I select activate registration
    Then I will receive the message "Your activation code is not working. Go to the account login page and use the forgotten password link to get another code."    
    And I will remain on the account activation page
    
    Given I am on the account-activation page
    When I select Back
    Then I will be taken to the login page