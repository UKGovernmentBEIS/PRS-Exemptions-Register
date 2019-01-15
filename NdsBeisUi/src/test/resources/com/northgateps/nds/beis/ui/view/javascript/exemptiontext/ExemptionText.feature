Feature: Register Exemption - Exemption text and document upload 

Scenario: Navigate back to personalised-exemption-document-upload page 
	Given I am on the personalised-exemption-text page
	When I select Back
	Then I will be taken to the personalised-exemption-document-upload page
	And the details previously entered will be displayed
	
Scenario: Process exemption text details
	Given I am on the personalised-exemption-text page
	
	#No documents uploaded/text entered
	Given I have not loaded a document
	And I have not entered any text 
	When I select Next
	Then I will receive "You must either upload a file or enter details" as validation message
	And I will remain on the personalised-exemption-text page	
	
    #Select file of wrong type
     Given I select a file "testProperties.json" with an incorrect file type
    Then I will receive "You must upload a file that is one of the listed types" as validation message
    And the document "testProperties.json" is not listed as uploaded
    And I will remain on the personalised-exemption-text page
    
    #Select file that is too large
    Given I select a file "Spring in Action, 4th Edition.pdf" that is larger than the maximum size
    Then I will receive "The file you are trying to upload exceeds the maximum size allowed" as validation message
    And the document "Spring in Action, 4th Edition.pdf" is not listed as uploaded
    And I will remain on the personalised-exemption-text page
    
    #Select OK file
    Given I select a file "test.docx" that is of the correct type and size
    Then the file "test.docx" will be uploaded
    And I will be displayed the name "test.docx" of the file I have loaded
    And I will have an option to remove the file
    And I will be able to add a description of the file
    And I will remain on the personalised-exemption-text page
    
    #Remove document
    When I select Remove file
    Then the document "test.docx" is not listed as uploaded
    Then I will remain on the personalised-exemption-text page
    
    #Happy path,move to personalised-exemption-declaration page
   	Given I select a file "test.docx" that is of the correct type and size
    When I select Next
    Then I will be taken to the personalised-exemption-declaration page
    