Feature: Register Exemption - Exemption confirmation details

Scenario: Navigate back to dashboard page
	Given I am on the personalised-exemption-confirmation page
	When I select 'View my exemptions'
	Then I will be taken to the personalised-dashboard page
	And the exemptions region will be expanded
	
Scenario: Moving back and selecting different exemption type and then submitting the exemption
    Given I am on personalised-select-exemption-type page 
    And I select 'WALL' as the exemption type
    When I select Next
    Then I must move to personalised-exemption-requirements page
    When I select back and move to exemption type page
    And I select a different exemption type 'ALLIMP' and try submitting the exemption
    Then I must be taken to personalised-exemption-confirmation page
    When I select 'View my exemptions'
	Then I will be taken to the personalised-dashboard page
	
	