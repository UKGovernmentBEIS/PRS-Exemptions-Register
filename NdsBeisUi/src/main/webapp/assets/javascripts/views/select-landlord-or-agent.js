$(function () {	
	if ($('input[name="beisRegistrationDetails.userDetails.userType"]:checked').val() == 'AGENT') {
        $("#userDetails").show();
    } else {
        $("#userDetails").hide();
    }
	
    $(".yesno").click(function () {
        var selectedOption = this.value;
        if (selectedOption == 'AGENT') {
        	$("#userDetails").show();
        } else {
        	$("#userDetails").hide();
        }
    });
    
    
})