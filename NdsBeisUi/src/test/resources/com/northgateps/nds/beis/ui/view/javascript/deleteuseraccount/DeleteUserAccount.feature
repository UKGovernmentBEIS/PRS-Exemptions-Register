Feature: Delete user account

Scenario: Navigate to delete user account page
Given I am on the personalised-account-summary page
When I select 'Delete your account' link
Then I will be taken to the personalised-delete-account page
And I can see the delete guidance text and button
When I select Back
Then I will be taken to the 'personalised-account-summary' page