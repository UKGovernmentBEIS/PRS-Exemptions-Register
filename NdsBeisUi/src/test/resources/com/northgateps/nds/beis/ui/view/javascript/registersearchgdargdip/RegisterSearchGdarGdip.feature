Feature: Search Gdar Gdip

Scenario: Search
	#No reference supplied
		Given I am on the 'register-search-gdar-gdip' page
		And I have not supplied a reference
		When I select 'Download'
		Then I will receive the error "Reference number must be entered"
		And I will remain on the 'register-search-gdar-gdip' page
	
	#Invalid reference supplied
		Given I have supplied an invalid reference
		When I select 'Download'
		Then I will receive the error "The reference number is invalid"
		And I will remain on the 'register-search-gdar-gdip' page
		
	#Valid reference supplied with no results
		Given I have supplied a valid reference
		And no document exists for the reference
		When I select 'Download'
		Then I will receive the message "There were no results found for the reference number. Your green deal assessor can provide you with the current reference number that is held on the register."
		And I will remain on the 'register-search-gdar-gdip' page
	
	#Valid reference supplied but not latest
		Given I have supplied a valid reference
		And document identified is not the latest for the property
		When I select 'Download'
		Then I will receive the message 
			"""
			The reference number you have entered belongs to a GDAR or GDIP that has been superseded by a more recent one. Please enter the reference number of the most recent GDAR or GDIP created for the property.
			If you don't know the latest reference number please contact your green deal assessor
			"""
		And I will remain on the 'register-search-gdar-gdip' page
	
	#Unexpected error
		#Given I have supplied a valid reference
		#But a document cannot be returned due to an unexpected error
		#When I select 'Download'
		#Then I will receive the message "A GDAR or GDIP could not be downloaded for the reference number.  Please try again later"
		#And I will remain on the 'register-search-gdar-gdip' page
	
	#Valid reference supplied with document
		Given I have supplied a valid reference
		And a document exists for the reference
		When I select 'Download'
		Then I will see the name of document to download
		When I click on the name of the document
		Then the document will be downloaded as a PDF file
		And I will remain on the 'register-search-gdar-gdip' page
		
