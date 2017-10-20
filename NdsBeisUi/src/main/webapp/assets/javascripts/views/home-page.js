$(function() {
	var incidentReportDoesNotExist = document
			.getElementById("uiData.incidentReportExist");
	if (incidentReportDoesNotExist.value === 'true') {
		$("#incident-report").show();
	} else {
		$("#incident-report").hide();
	}

	
});