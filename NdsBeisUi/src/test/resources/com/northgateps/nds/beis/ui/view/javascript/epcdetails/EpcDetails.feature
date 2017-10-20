Feature: Register Exemption - Energy Performance Certificate upload 

Scenario: Navigate back to property address page 
	Given I am on the personalised-epc-details page
	When I select Back
	Then I will be taken to the personalised-property-address page
	And the details previously entered will be displayed
	
Scenario: Process epc details
	Given I am on the personalised-epc-details page
	
	#No details uploaded
	Given I have not uploaded any file
	When I select Next
	Then I must receive "Your Energy Performance Certificate must be uploaded" as validation message
	And I will remain on the personalised-epc-details page
	
	#Select file of wrong type
    Given I select a file with an incorrect file type
    Then I must receive the custom message "The file you are trying to upload is not an accepted format" as validation message
    And the file will not be uploaded
    And I will remain on the personalised-epc-details page
    
    #Select file that is too large
    Given I select a file that is larger than the maximum size
    Then I must receive the custom message "The file you are trying to upload exceeds the file size limit. Please try again" as validation message
    And the file will not be uploaded
    And I will remain on the personalised-epc-details page
    
    #Attached a wrong media file before submitting my certificate proof and would want to remove this file now
    Given I have select a file with a correct file type
    When I click on 'Remove File' link for the uploaded file
    Then I will remain on the personalised-epc-details page
    And The file is not listed as uploaded 
    
    #Happy path,move to personalised-exemption-document-upload page
    Given I have select a file with a correct file type
    And I enter the description for the file
    When I select Next
    Then I must move to personalised-exemption-start-date page