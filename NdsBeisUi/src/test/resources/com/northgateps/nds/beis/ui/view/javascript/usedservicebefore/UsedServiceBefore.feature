Feature: Used Service Before

Scenario: No option selected
	Given I am on the 'used service before' page
	And I have not selected an option
	When I select Next
	Then I will receive the message "You must choose an option"
	And I will remain on the 'used service before'

Scenario: Navigate to select landlord type page
	Given I am on the 'used service before' page
	When I select 'No, I need to register'
	And I select Next
	Then I will be taken to the 'select-landlord-or-agent' page
	
Scenario: Navigate to sign on page
	Given I am on the 'used service before' page
	When I select 'Yes, I already have an account'
	And I select Next
	Then I will be taken to the sign-on page
	
	# Scenario: Navigate back from “sign-on” page
	Given I am on the 'sign-on' page
	When I select Back
	Then I will be taken to the 'used service before' page

	# Scenario: Sign in and navigate back to the used-service-before page
	When I select 'Yes, I already have an account'
	And I select Next
	Then I will be taken to the sign-on page

	When I sign in as user "dashboarduser" with password "password01"
	Then I should see the "personalised-dashboard" page
	When I go to the used-service-before page by entering it's url
	Then I should see the "used-service-before" page
	And I should see the text 'You are already logged in, click "Next" to proceed to the dashboard'
	When I click on the Next link-button
	Then I should see the "personalised-dashboard" page
