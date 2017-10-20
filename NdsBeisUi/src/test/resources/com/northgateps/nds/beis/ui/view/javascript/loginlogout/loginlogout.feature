Feature: Login and logout
	
Scenario: Login and logout Successfully
	Given I'm on login page	
	When I log in
	Then I'm taken to the dashboard page
	When I log out
	Then I'm taken to the done page
	When I return to the site
	Then I am not logged in