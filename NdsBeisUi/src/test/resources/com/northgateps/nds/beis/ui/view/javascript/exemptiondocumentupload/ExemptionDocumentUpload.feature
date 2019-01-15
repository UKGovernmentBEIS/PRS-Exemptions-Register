Feature: Register Exemption - Exemption document upload 

Scenario: Navigate back to epc details page 
	Given I am on the personalised-exemption-document-upload page
	When I select Back
	Then I will be taken to the personalised-epc-details page
	And the details previously entered will be displayed
	
Scenario: Process exemption upload details
	Given I am on the personalised-exemption-document-upload page
	
	#No documents uploaded
	Given I have not uploaded any file
	When I select Next
	Then I must receive "You must upload at least one file" as validation message
	And I will remain on the personalised-exemption-document-upload page
	
	#Select file of wrong type
    Given I select a file "testProperties.json" with an incorrect file type
    Then I must receive "You must upload a file that is one of the listed types" as validation message
    And the file will not be uploaded
    And I will remain on the personalised-exemption-document-upload page
	
	#Select file that is too large
    Given I select a file "Spring in Action, 4th Edition.pdf" that is larger than the maximum size
    Then I must receive "The file you are trying to upload exceeds the maximum size allowed" as validation message
    And the file will not be uploaded
    And I will remain on the personalised-exemption-document-upload page
    
    #Select OK file
    Given I select a file "test.docx" that is of the correct type and size
    Then the file will be uploaded
    And I will be displayed the name of the file I have loaded
    And I will have an option to remove the file
    And I will be able to add a description of the file
    And I will remain on the personalised-exemption-document-upload page
    
    #Upload button shown
    Given I select a file "test.docx" that is of the correct type and size
    Then the Upload button will be shown
    And I will remain on the personalised-exemption-document-upload page
    
    #Remove document
    Given I have loaded a file "test.docx" with a correct file type
    When I select Remove file
    Then the document "test.docx" is not listed as uploaded
    And I will remain on the personalised-exemption-document-upload page
    
   	#Upload button hidden
    Given I have loaded "test.docx" with Max Documents
    And I have loaded "test.docx" with Max Documents
    And I have loaded "test.docx" with Max Documents
    And I have loaded "test.docx" with Max Documents
    And I have loaded "test.docx" with Max Documents
    And I have loaded "test.docx" with Max Documents
    And I have loaded "test.docx" with Max Documents
    And I have loaded "test.docx" with Max Documents
    And I have loaded "test.docx" with Max Documents
    And I have loaded "test.docx" with Max Documents
    Then the Upload button will be hidden
    And I will remain on the personalised-exemption-document-upload page
    
    When I select Remove file
    Then the Upload button will be shown
    And I will remain on the personalised-exemption-document-upload page
    
    #Happy path, move to personalised-account-summary page
    When I select Next
    Then I will be taken to the personalised-exemption-declaration page
    
  
    
    