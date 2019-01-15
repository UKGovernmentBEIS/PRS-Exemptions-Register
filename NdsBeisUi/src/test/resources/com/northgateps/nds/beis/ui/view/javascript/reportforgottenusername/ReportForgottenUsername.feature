Feature: Report forgotten username

  Scenario: Navigate to forgotten username
    Given I am on the 'login-form' page
    When  I select 'Forgotten your username?'
    Then  I will be taken to the 'report-forgotten-username' page

  Scenario: Navigate back to sign on page
    Given I am on the 'report-forgotten-username' page
    When  I select 'Back'
    Then  I will be taken to the 'login-form' page

  Scenario: Process forgotten username validations
#  Scenario: Process forgotten username where no email address has been given
    Given I am on the 'report-forgotten-username' page
    When  I select 'Next'
    Then  I will receive the message "Email address must be entered"
    And   I will remain on the 'report-forgotten-username' page

#  Scenario: Process forgotten username where an invalid email address has been entered
#    Given I am on the 'report-forgotten-username' page
    And   I have supplied the email address "not-an-email-address"
    When  I select 'Next'
    Then  I will receive the message "You must enter a valid email address in Email address"
    And   I will remain on the 'report-forgotten-username' page

#  Scenario: Process forgotten username with a valid email address
#    Given I am on the 'report-forgotten-username' page
    And   I have supplied the email address "example@northgateps.com"
    When  I select 'Next'
    Then  I will be taken to the 'report-forgotten-username-confirmation' page


