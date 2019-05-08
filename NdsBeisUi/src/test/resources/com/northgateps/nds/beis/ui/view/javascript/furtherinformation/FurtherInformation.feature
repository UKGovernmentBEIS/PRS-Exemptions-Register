Feature: Delete user account

Scenario: Navigate to further information page
Given I am on the personalised-further-information page
And I enter text in the confirmation text area
And I click next 
Then I will receive the message "The confirmation checkbox must be selected"
And I will remain on the personalised-further-information page
When I select the confirm checkbox
And I click next
Then I will be taken to the personalised-exemption-declaration page
