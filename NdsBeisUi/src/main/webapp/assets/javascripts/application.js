$(document).ready(function() {
/* global $ */
/* global jQuery */
/* global GOVUK */
	  // Use GOV.UK selection-buttons.js to set selected
	  // and focused states for block labels
	  var $blockLabels = $(".block-label input[type='radio'], .block-label input[type='checkbox']");
	  new GOVUK.SelectionButtons($blockLabels);
	  
	  //focus on the error block
	  var errorBlock = $(".error-summary");
	  
	  if( errorBlock != null &&
	  	  $(errorBlock).is(":visible")){
		  
		  setTimeout(function() {
			  errorBlock.focus();}, 20);
	  }
});

// jQuery datepicker
$(function () {
   $(".datepicker").datepicker({
       showOn: "both",
       buttonImage: "assets/images/calendar.png",
       dateFormat:"dd/mm/yy",
       buttonImageOnly: true,
       buttonText: "Select date"
   });
});