Feature: Content Security (mod_security) Checks

# The actual text used to test the content security/mod-security is in the Steps file so that Serenity doesn't try
# to include it with reports (which can cause errors as it fails to parse correctly in Serenity).
# Also not using the Examples pattern because Serenity seems to be mis-handling the example code which leads
# to this test being excluded from the main results page due to a json parse exception (caused by "code\u003dALLIMP").
 
Scenario: Content Security - form submission with free text pattern - NEW
	Given I am on the personalised-exemption-list-of-values page with option "NEW"
	When I enter the test text for "NEW"
	And I select mandatory field and submit
    Then I will be taken to personalised-exemption-confirmation page

Scenario: Content Security - form submission with free text pattern - ALLIMP
	Given I am on the personalised-exemption-list-of-values page with option "ALLIMP"
	When I enter the test text for "ALLIMP"
	And I select mandatory field and submit
    Then I will be taken to personalised-exemption-confirmation page