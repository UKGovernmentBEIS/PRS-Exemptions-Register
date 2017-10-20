$(document).ready(function() {
	var selectedPropertyTypeValue = $('[name="uiData.penaltySearch.propertyType"] option:selected').val();
	if (selectedPropertyTypeValue == "PRSD") {
		$("#penalty-drop-down-prsd").show();
		$("#penalty-drop-down-prsn").hide();
	}

	if (selectedPropertyTypeValue == "PRSN") {
		$("#penalty-drop-down-prsn").show();
		$("#penalty-drop-down-prsd").hide();
	}

	if (selectedPropertyTypeValue == "ALL") {
		$("#penalty-drop-down-prsd").hide();
		$("#penalty-drop-down-prsn").hide();
	}
 
	$('[name="uiData.penaltySearch.propertyType"]').change(function () {
		var selectedValue = $('[name="uiData.penaltySearch.propertyType"] option:selected').val();

		if (selectedValue == "PRSD") {
			$("#penalty-drop-down-prsd").show();
			$("#penalty-drop-down-prsn").hide();
		}

		if (selectedValue == "PRSN") {
			$("#penalty-drop-down-prsn").show();
			$("#penalty-drop-down-prsd").hide();
		}

		if (selectedValue == "ALL") {
			$("#penalty-drop-down-prsd").hide();
			$("#penalty-drop-down-prsn").hide();
		}

	});
	
	$('#other-ways').click(function() {
		var isOpen = document.getElementById('other-ways').hasAttribute('open');
		if(isOpen == false)
			{
			document.getElementById('panelDisplayed').value = true;
			
			}
		else
			{
			document.getElementById('panelDisplayed').value = false;
			}
		
		});
	
	 
	 if($('table[id="penaltiesList"]').is(':visible')){
		 $('html,body').animate({
	         scrollTop: $("#local_authority_list").offset().top},
	         'slow');
	}


});
