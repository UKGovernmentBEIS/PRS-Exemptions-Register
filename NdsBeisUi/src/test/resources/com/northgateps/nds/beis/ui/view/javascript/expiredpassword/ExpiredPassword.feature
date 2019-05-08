Feature: Expired Password

Scenario: Log in as a user with expired password and attempt to reset password hitting all validation errors on the way
    Given I am on the login page
    When I have logged in as an expired user
    Then I must see the expired password page
    When I press submit
    Then I should see a validation error containing "Current password must be entered"
    Then I should see a validation error containing "New password must be entered"
    Then I should see a validation error containing "Confirm password must be entered"
    When I enter mis-matching password and confirmation passwords
    Then I should see a validation error containing "You must make the passwords the same"
    When I enter matching password and confirmation passwords but wrong old password
    Then I should see a validation error containing "Current password is invalid"
    When I enter the old password correctly but the new password is too short
    Then I should see a validation error containing "Your password must be a minimum of 10 characters long with at least 1 lower case letter and 1 number"
    When I enter a valid new password and confirmation password 
    Then I should see the password confirmation page
    When I click the next button
    Then I should see the "failover-landing" page with the sign in link and definitely not be logged in.