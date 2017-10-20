$(document).ready(function() {
	
	var selectedPropertyTypeValue = $('[name="uiData.exemptionSearch.service"] option:selected').val();
	if (selectedPropertyTypeValue == "PRSD") {
        $("#exemption-drop-down-prsd").show();
		$("#exemption-drop-down-prsn").hide();
	}

	if (selectedPropertyTypeValue == "PRSN") {
		$("#exemption-drop-down-prsn").show();
		$("#exemption-drop-down-prsd").hide();
	}

	if (selectedPropertyTypeValue == "ALL") {
		$("#exemption-drop-down-prsd").hide();
		$("#exemption-drop-down-prsn").hide();
	}
 
	$('[name="uiData.exemptionSearch.service"]').change(function () {
		var selectedValue = $('[name="uiData.exemptionSearch.service"] option:selected').val();

		if (selectedValue == "PRSD") {

			$("#exemption-drop-down-prsd").show();
			$("#exemption-drop-down-prsn").hide();
		}

		if (selectedValue == "PRSN") {
			$("#exemption-drop-down-prsn").show();
			$("#exemption-drop-down-prsd").hide();
		}

		if (selectedValue == "ALL") {
			$("#exemption-drop-down-prsd").hide();
			$("#exemption-drop-down-prsn").hide();
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
	
	if($('table[id="exemptionsList"]').is(':visible')){
	 $('html,body').animate({
         scrollTop: $("#local_authority_list").offset().top},
         'slow');
}
		
});
