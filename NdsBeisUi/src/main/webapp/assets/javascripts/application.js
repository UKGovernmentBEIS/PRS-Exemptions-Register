$(document).ready(function() {
/* global $ */
/* global jQuery */
/* global GOVUK */
	  
	  //focus on the error block
	  var errorBlock = $(".error-summary");
	  
	  if( errorBlock != null &&
	  	  $(errorBlock).is(":visible")){
		  
		  setTimeout(function() {
			  errorBlock.focus();}, 20);
	  }
	  
  // Where .multiple-choice uses the data-target attribute
  // to toggle hidden content
  var showHideContent = new GOVUK.ShowHideContent()
  showHideContent.init()
  
  // Use GOV.UK shim-links-with-button-role.js to trigger a link styled to look like a button,
  // with role="button" when the space key is pressed.
  GOVUK.shimLinksWithButtonRole.init()
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

/*** GLOBAL FUNCTIONS ***/

/***
 * Display an error message on the screen for a path
 * @param path - the field path
 * @param inlineMessage - the text to show inline
 * @param message - the message to show at the top
 * @returns
 */
function displayErrorMessage(path, inlineMessage, message) {

    var idSelector = "#" + pathToIdSelector(path + ".field");

    $(idSelector).addClass('error');

    var field = $(idSelector + ', ' + idSelector + '> fieldset');
    field.children('.invalid').append("<span class='error-message'>" + inlineMessage + "</span>");

    $(".fault .error-summary").removeAttr('hidden');

    $(".fault ul").append("<li id='" + path + ".fault'><span><a data-href='#" + path + ".field'>" + message + "</a></span></li>");
}

/**
 * Clear an error message from the screen
 * @param path - path to the field
 * @param clearStickies - whether or not to clear sticky errors
 * @returns
 */
function clearErrorMessage(path, clearStickies) {

    var idSelector = "#" + pathToIdSelector(path + ".field");

    // check if any stickies if interested and clear inline
    var anyStickies = false;

    var field = $(idSelector + ', ' + idSelector + '> fieldset');

    if (!clearStickies) {
         if (field.children('.invalid').children('span.sticky-error').length > 0){
             anyStickies = true;
         }

         field.children('.invalid').children('span').not(".sticky-error").remove();
    } else {
        field.children('.invalid').children('span').remove();
    }

    if (! anyStickies) {
        $(idSelector).removeClass('error');
    }

    //clear the top errors
    idSelector = "#" + pathToIdSelector(path + ".fault");

    if (clearStickies) {
        $(".fault " + idSelector + " span").remove();
    } else {
        $(".fault " + idSelector + " span").not(".sticky-error").remove();
    }

    if ($(".fault " + idSelector + " span").length == 0 ) {
        $(".fault " + idSelector).remove();
    }

    // hide the error summary box
    if ($(".fault li span").length == 0 ) {
        $(".fault .error-summary").attr('hidden', 'hidden');
    }
}
/***
 * Convert a field path to a safe jquery selector
 * @param path - the field path
 * @returns the safe path 
 */
function pathToIdSelector(path){
    return path.replace("/\[/g", "").replace("/\]/g", "").replace("/'/g", "").replace(/\./g, "\\.").replace("/\$/g", "\\$");;
}