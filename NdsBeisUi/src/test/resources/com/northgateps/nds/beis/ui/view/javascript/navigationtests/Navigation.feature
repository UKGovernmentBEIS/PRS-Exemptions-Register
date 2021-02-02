Feature: Navigation Tests

   Scenario Outline: Upload Only for non domestic category
    Given I have selected a non-domestic exemption of "<code>"
    And I am on the personalised-epc-details page
    Then I can see the digital assist guidance text
    And I have uploaded "test.docx" as EPC 
    When I select Next
    Then I will be taken to the personalised-exemption-document-upload page
    
    #Go To further information
    Given I have loaded greater than or equal to Min Documents
    When pressing Next button I will be taken to the personalised-further-information page
    
    #Back to Upload
    When I select Back
    Then I will be taken to the personalised-exemption-document-upload page
    And previous exemption document upload details will be displayed
    
    #Back to EPC Details
    When Pressing Back I will be navigated to the personalised-epc-details page
    And previous epc details will be displayed
    Examples:
    |code	|
    |WALL	|
    |NOIMP	|
    |PAYBACK|
    |CONSENT|
    |DEVAL	|
    
Scenario:  Document Upload and Text for non domestic category
    Given I have selected a non-domestic exemption of "ALLIMP"
    And I am on the personalised-epc-details page
    And I have uploaded "test.docx" as EPC
    When I select Next
    Then I will be taken to the personalised-exemption-document-upload page
    
Scenario Outline: Upload Only for Domestic category

	#Go To Upload
	
	Given I have selected a domestic exemption code of "<code>"
	And I am on the personalised-epc-details page
    And I have uploaded "test.docx" as EPC 
    When I select Next
    Then I will be taken to the personalised-exemption-document-upload page

	#Go To further information
	
    Given I have loaded greater than or equal to Min Documents
    When pressing Next button I will be taken to the personalised-further-information page
    
    #Back to Upload
    
    When I select Back
    Then I will be taken to the personalised-exemption-document-upload page
    And previous exemption document upload details will be displayed
    
    #Back to EPC Details
    
    When Pressing Back I will be navigated to the personalised-epc-details page
    And previous epc details will be displayed
    
 Examples:
    |code	|
    |WALL	|
    |CONSENT|
    |DEVAL	|
    
Scenario: Document Upload and Text for domestic category
  
	#Go To Upload
	Given I have selected a domestic exemption code of "ALLIMP"
	And I am on the personalised-epc-details page
    And I have uploaded "test.docx" as EPC 
    When I select Next
    Then I will be taken to the personalised-exemption-document-upload page

	#Go To Text
	
	Given I have loaded greater than or equal to Min Documents
	When I select Next button
	Then I will be taken to personalised-exemption-text page
	
	#Go To further information
	
	Given I supplied valid data
	When I select Next
	Then I will be taken to the personalised-further-information page

	#Back to Text
	When I select Back option
	Then I will be taken to the personalised-exemption-text page
	And previous exemption text details will be displayed

	#Back to Upload
	When I click Back
	Then I will be taken to the personalised-exemption-document-upload page
	And previous exemption document details will be displayed.

	#Back to EPC Details
	When Pressing Back I will be navigated to the personalised-epc-details page
    And previous epc details will be displayed
    
Scenario: Document Upload and Text for domestic category
	#Go To Upload
	Given I have selected a domestic exemption code of "HIGHCOST"
	And I am on the personalised-epc-details page
    And I have uploaded "test.docx" as EPC 
    When I select Next
    Then I will be taken to the personalised-exemption-document-upload page

	#Go To further information
	
	Given I have loaded greater than or equal to Min Documents
	When pressing Next button I will be taken to the personalised-further-information page

	#Back to Upload
	When I select Back
	Then I will be taken to the personalised-exemption-document-upload page
	And previous exemption document details will be displayed.

	#Back to EPC Details
	When Pressing Back I will be navigated to the personalised-epc-details page
    And previous epc details will be displayed

    
Scenario: Start Date and Select Option  

	#Go To Start
	Given I have selected a domestic exemption code of "NEW"
	And I am on the personalised-epc-details page
    And I have uploaded "test.docx" as EPC 
    When I select Next
	Then I will be taken to the personalised-exemption-start-date page
	
	#Go To Option
	Given I have entered a valid date
	When I select Next
	Then I will be taken to the personalised-exemption-list-of-values page
	
	#Go To further information
	Given I have selected an option
	When I select Next on list of values page
	Then I will be taken to personalised-further-information page
	
	
	#Back to Option
	When I select Back option
	Then I will be taken to the personalised-exemption-list-of-values page
	And previous exemption list of values details will be displayed
	
	#Back to Start
	When I select back option
	Then I will be taken to the personalised-exemption-start-date page
	And previous exemption start date details will be displayed
	
	#Back to EPC Details
	When I select the Back option
    Then I will be taken to personalised-epc-details page
    And previous epc details will be displayed	
	