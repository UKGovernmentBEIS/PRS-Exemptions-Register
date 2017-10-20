Feature: Session Bleed Attack Prevention

Scenario: Two logged in users
	Given I've logged in
	Then I'm taken to the dashboard page
	When I wait 30 seconds for the CAS cookie to expire
	And I open a new tab directly to the login page
	And I log in using the sessionbleed user
	Then I'm taken to the dashboard page
	And I'm definitely logged in as the sessionbleed user
	When I switch to the first tab
	And click the My account link
	Then I should see the Session Exception page
	And clicking Start again link should take me back to the start page

# Check we can't overwrite the sessionId, which would trigger the session-exception page
Scenario: Hack the sessionId
	Given I've logged in
	When I enter a url crafted to overwrite uiData.sessionId
	Then I'm not taken to the session-exception page