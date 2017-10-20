// When JS is available, the validation can stop the language switch submit. ForceSubmit overrides
$('[formnovalidate]').click(function() { 
    $('#pageForm')[0].forceSubmit = true; 
});

// Add script to disable HTML5 validation, if JS validation is available  
$('form').attr('novalidate', '');

// If field is numeric type, then the user can type in alpha (firefox) or "eeeeee" chrome and the value of the
// control will be '', meaning that validation does not detect a problem and the user doesn't know. To avoid this 
// numeric inputs are changed to text inputs here 
$('input[type=\"number\"]').attr('type', 'text');


// Initialise the blur and submit event handlers.
// Each control (HTML input, select, textarea, etc) can have multiple formatters and validators. On a blur event
// the formatters and validators are applied for that control. The formatters and validators are applied in two 
// phases, the "preblur" phase clears all the message texts, then the "blurset" phase runs the formatters and 
// validators, formatting the value and adding validation messages as necessary.
// On a submit event, there's three phases. In the "presubmit" phase, the formatter and validators for all the 
// controls are run, and if all validators pass, then the submit continues, and the other phases are skipped.
// If the any of the validators do not pass, then no messages are altered in the "presubmit" phase, but the submit
// is aborted and the "preblockedsubmit" and the "blockedsubmit" phase are run.
// In the "preblockedsubmit" phase, all validation messages are cleared. In the "blockedsubmit" phase, the validators
// for all the controls is re-run, but this time the validation messages are added as necessary. 
// This process stops the page from being re-rendered during the page submit action if all validations pass, which 
// otherwise might confuse the user.
//
// if (false) // uncommment this line to disable client-side JS-based form validation  
(function () {
    // Each phase has an array of functions that are called on that phase.
    if ($('#pageForm').length > 0) {
        $('#pageForm')[0].presubmit = $('#pageForm')[0].presubmit || [];
        $('#pageForm')[0].preblockedsubmit = $('#pageForm')[0].preblockedsubmit || [];
        $('#pageForm')[0].blockedsubmit = $('#pageForm')[0].blockedsubmit || [];
        // For the blur event, there's an array of functions for each control
        $('#pageForm')[0].preBlur = $('#pageForm')[0].preBlur || [];
        $('#pageForm')[0].blurset = $('#pageForm')[0].blurset || [];
    }

    // Loop over the controls, setting up the arrays of functions that will be called.
    $('.pageField').each(function(fldIndex, field) {
        var hideFaultBoxIfEmpty = function() {
            if ($('.fault li:not([hidden]), .fault .sysErr, .fault .validationMessage:not([hidden])').length === 0) {
                $('.fault > div').attr('hidden', 'hidden'); 
            } 
        };
        
        var initializeControlValidations = function(controlName, process, validating) {
            var addPreSubmit = function(fn) {
                $('#pageForm')[0].presubmit.push(fn);
            };
            var addPreBlockedSubmit = function(fn) {
                $('#pageForm')[0].preblockedsubmit.push(fn);
            };
            var addBlockedSubmit = function(fn) {
                $('#pageForm')[0].blockedsubmit.push(fn);
            };
            var addPreBlur = function(controlName, fn) {
                $('#pageForm')[0].preBlur[controlName] =  $('#pageForm')[0].preBlur[controlName] || [];
                $('#pageForm')[0].preBlur[controlName].push(fn);
                addPreBlockedSubmit(fn);
            };
            var addBlurSet = function(controlName, fn) {
                $('#pageForm')[0].blurset[controlName] =  $('#pageForm')[0].blurset[controlName] || [];
                $('#pageForm')[0].blurset[controlName].push(fn);
            };

            // This is the core of the formatting and validation process
            var doValidation = function(event, process, onNoMatch) {
                // First deal with radio buttons
                var lst = $('[name = "' + process.controlName + '"]:checked');
                var target = (lst.length > 0) ? lst[0] : null;
                // If a checked radio button is found, then that provides the value to validate
                if (target === null) { 
                    // If a checked radio button is not found, then look for a "stand in". This 
                    // provides a value for when the control is a set of radio buttons, but none
                    // of them are currently selected.
                    var standins = $('[name = "__' + process.controlName + '"]');
                    if (validating) {
                        if (standins.length > 0) {
                            target = standins[0];
                        } else { 
                            // If there's no "stand in", then control is not a radio button
                            // and the control is text input or similar. Just validate the 
                            // value of that.
                            target = $('[name = "' + process.controlName + '"]')[0]; 
                        }
                    } else {
                        if (standins.length == 0) {
                            // If this is a formatter process, and there is no radio button "stand in"
                            // then just apply the formatter to the value of the control
                            target = $('[name = "' + process.controlName + '"]')[0]; 
                            process.fn.call(target); 
                        }
                        return;
                    }
                }
                
                // Check that at least one of the controls with the given name is visible and writable. If not then just return
                var possibleTargets = $('[name = "' + process.controlName + '"]:visible').filter(function() {
                    return ! $(target).prop('readonly'); 
                });
                if (possibleTargets.length == 0) {
                    return;
                }
                
                // Check to see if validation of the field is masked for the action being performed, if so just return.
                if (event.sourceElement) {
                    if ($(event.sourceElement).attr('data-validateapplies')) {
                        if ($.inArray(process.controlName, $(event.sourceElement).attr('data-validateapplies').split(' ')) == -1) { 
                            return;
                        }
                    }
                }
                
                // Having identified the control whose value is to be formatted or validated, apply the specific
                // format or validation to that control's value.
                var violationProperties = null;
                // override the control's dataProperties with those from the process where necessary
                var baseDataProperties = target.dataProperties;
                target.dataProperties = {};
                for (var propertyName in baseDataProperties) {
                    target.dataProperties[propertyName] = baseDataProperties[propertyName];
                }
                for (var propertyName in process.dataProperties) {
                    target.dataProperties[propertyName] = process.dataProperties[propertyName];
                }
                var cmp = process.fn.call(target);
                // restore the controls dataProperties
                target.dataProperties = baseDataProperties;
                if ((typeof cmp === "object") && (cmp !== null)) {
                    violationProperties = cmp.properties;
                    cmp = cmp.result; 
                }
                if (! cmp) { 
                    // If the validation failed, take the appropriate failure action for the phase being run
                    onNoMatch(process, violationProperties || {});
                } else if (process.chained && process.chained.length > 0) {
                    // If the validation passed, then run the chained validation rules. The chained validation may apply to
                    // the same control, or to a different one.
                    for (var i = 0, len = process.chained.length; i < len ; i++) {
                        doValidation(event, process.chained[i], onNoMatch);
                    }
                }
            };

            // Add the current format or validation to the presubmit phase 
            addPreSubmit(function(event) {
                doValidation(event, process, function(process) {
                    event.preventDefault(); 
                    event.blocked = true;
                });
            });

            if (validating) {
                // When a validation fails, add the failure message to the "invalid" div underneath the control, and to
                // the summary of failures at the top of the page. If the validator is a chained one, add the message to
                // those already there, otherwise replace any existing messages with the newly identified one.
                var updateMessages = function(process, field, fault, violationProperties) {
                    
                    var invalidContainerField = field;
                    while (invalidContainerField[0].attributes['data-invalid-id']) {
                        invalidContainerField = $("[id='" + invalidContainerField[0].getAttribute('data-invalid-id') + "']");
                    }
                    
                    var propertySubstitute = function(msg, violationProperties) {
                        for (var key in violationProperties)  {
                            msg = msg.replace("<" + key + ">", violationProperties[key], "g");
                        }
                        return msg;
                    };
                    var invalidMsg = propertySubstitute(process["invalidMsg" + (violationProperties.variant || '')], violationProperties);
                    var faultMsg = propertySubstitute(process["faultMsg" + (violationProperties.variant || '')], violationProperties);
                    if (process.replace) {
                        invalidContainerField.find('.invalid' + process.invalidClassQualifier).html('<span class="error-message">' + invalidMsg + '</span>');
                        fault.html('<span><a href="#' + field[0].id + '" data-validator="'+ process.validatorType +'">' + faultMsg + '</a></span>'); 
                    } else {
                        invalidContainerField.find('.invalid' + process.invalidClassQualifier).append('<span class="error-message">' + invalidMsg + '</span>');
                        fault.append('<span><a href="#' + field[0].id + '" data-validator="'+ process.validatorType +'">' + faultMsg + '</a></span>'); 
                    }
                    $("a[href='#" + field[0].id + "']").on("click", function() {
                        try {
                            var scrollTarget = "[id='" + $(this).attr('href').substring(1) + "']";
                            $('html, body').animate({
                                scrollTop: $(scrollTarget).offset().top
                            }, 2000);
                        }
                        finally {
                            return false;
                        }
                    });
                    fault.removeAttr('hidden'); 
                    $('.fault > div').removeAttr('hidden');
                };

                var control = $('[name = "' + controlName + '"]');
                var field = $("[id='" + control[0].getAttribute('data-field-id') + "']");
                // Day, month and year fields have a container field that is the whole date. Find that.
                // For fields other than dates, the masterfield is just the container field of the
                // control.
                var masterField = field;
                while (masterField[0].attributes['data-field-id']) {
                    masterField = $("[id='" + masterField[0].getAttribute('data-field-id') + "']");
                }
                // And also find the control within the master field. For other than date fields, this
                // is just the control matching the controlName, but for date fields it finds the combined 
                // date input, rather than the individual day. month and year inputs 
                var masterControl = masterField.find(".pageControl")[0];
                
                // Add the current format or validation to the blockedsubmit phase 
                addBlockedSubmit(function(event) { 
                    doValidation(event, process, function(process, violationProperties) {
                        var processFaultControl = document.getElementsByName(process.faultControlName)[0];
                        var fault = $("[id='" + processFaultControl.getAttribute('data-fault-id') + "']");
                        masterField.addClass('violation error');
                        masterField.addClass(process.className);
                        updateMessages(process, masterField, fault, violationProperties);
                    });

                    if ($('.fault li:not([hidden])').length === 0) { 
                        $('.fault > div').attr('hidden', 'hidden'); 
                    } 
                });
                
                // Add the current format or validation to the preBlur phase 
                addPreBlur(controlName, function() {
                	// Stickies are error messages that are only cleared when the page is refreshed, not by validation.
                	var anyStickies = false;
                	                	                    
                    // Remove the text from the "invalid" div underneath the control
                    var clear = function(process) {
                        var invalidContainerField = masterField;
                        while (invalidContainerField[0].attributes['data-invalid-id']) {
                            invalidContainerField = $("[id='" + invalidContainerField[0].getAttribute('data-invalid-id') + "']");
                        }
                        
                        if (invalidContainerField.find('.invalid' + process.invalidClassQualifier + " span.sticky-error").length > 0){
                        	anyStickies = true;
                        	invalidContainerField.find('.invalid' + process.invalidClassQualifier + " span").not(".sticky-error").remove();
                        } else {
                        	invalidContainerField.find('.invalid' + process.invalidClassQualifier).html('');
                        }                            
                        
                        if (process.chained) {
                            for (var i = 0, len = process.chained.length ; i < len; i++) {
                                clear(process.chained[i]);
                            }
                        }
                    };
                    clear(process);
                                        
                    if (anyStickies) {
                    	
                    	// Remove the text from the fault summary at the top of the web page for non stickies                	
                    	var fault = $("[id='" + control[0].getAttribute('data-fault-id') + "'] span").not(".sticky-error");                        
                    	fault.html('');
                    	fault.attr('hidden', 'hidden');                        
                        fault = $("[id='" + masterField[0].getAttribute('data-fault-id') + "'] span").not(".sticky-error"); 
                        fault.html('');
                    	fault.attr('hidden', 'hidden');
                        fault = $("[id='" + masterControl.getAttribute('data-fault-id') + "'] span").not(".sticky-error"); 
                        fault.html('');
                    	fault.attr('hidden', 'hidden');  
                    	    
                    } else {
                    	//remove the field violation
                    	masterField.removeClass('violation error');
	                    masterField.removeClass(process.className);
	                    
                    	// Remove the text from the fault summary at the top of the web page
                    	var fault = $("[id='" + control[0].getAttribute('data-fault-id') + "']");                        
                    	fault.html('');
                    	fault.attr('hidden', 'hidden');                        
                        fault = $("[id='" + masterField[0].getAttribute('data-fault-id') + "']"); 
                        fault.html('');
                    	fault.attr('hidden', 'hidden');
                        fault = $("[id='" + masterControl.getAttribute('data-fault-id') + "']"); 
                        fault.html('');
                    	fault.attr('hidden', 'hidden');
                    }
                               	
                    
                });
                
                // Add the current format or validation to the blurset phase 
                addBlurSet(controlName, function(event) {
                    doValidation(event, process, function(process, violationProperties) {
                        var processFaultControl = document.getElementsByName(process.faultControlName)[0];
                        var fault = $("[id='" + processFaultControl.getAttribute('data-fault-id') + "']");     
                        masterField.addClass('violation error');
                        masterField.addClass(process.className);
                        updateMessages(process, masterField, fault, violationProperties);
                    });
                    
                    hideFaultBoxIfEmpty();
                });
            } else {
                // When formatting, just add the format operation to the blur event directly
                // since it won't be disruptive to the page rendering
                var control = $('[name = "' + controlName + '"]');
                control.blur(function(event) {
                    doValidation(event, process, function(process) {} );
                });
            }
        };
        
        // loop over the formatters and validators for the given control, and attach the event handlers to the phases 
        var initializeControlValidationsList = function(controlName, processList) {
            for (var i = 0, len = processList.length ; i < len ; i++) {
                initializeControlValidations(controlName, processList[i], processList[i].validator);
            };
        };

        // Re-initialise the fault summary block to just the general prompt
        var fault = $('.fault'); 
        if ($('.fault > div').length === 0) { 
        	fault.html('<div class="error-summary" role="group" hidden tabindex="-1"><h1 class="heading-medium error-summary-heading">' + $('#i18n-please-check-the-form')[0].content + '</h2><ul class="error-summary-list"></ul></div>');
        }

        // loop over every control on the page, and initialise it formatters and validators
        $(field).find('.pageControl').each(function(ctrlIndex, control) {
            // ignore nested controls like day, month, year, as these are processed under their individual fields 
            if (control.getAttribute('data-field-id') && (control.getAttribute('data-field-id') !== field.id)) {
                return;
            }
            
            // Create a insertion point in the fault block for the validation message for each control in the field
            if ($("[id='" + control.getAttribute('data-fault-id') + "']").length === 0) { 
                $('.fault > div > ul').append('<li hidden id="' + control.getAttribute('data-fault-id') + '"></li>');
            }
            
            // The HTML5 attributes and other validation properties are placed in an object property of the control 
            // called "dataProperties" so they are visible to the validators
            control.dataProperties = {};
            var attribsToRemove = [];
            for (var i = 0, len = control.attributes.length ; i < len ; i++) {
                if (control.attributes[i].name.indexOf("data-constraint-") === 0) {
                    attribsToRemove.push(control.attributes[i].name.substr(16));
                    control.dataProperties[control.attributes[i].name.substr(16)] = control.getAttribute(control.attributes[i].name.substr(16));
                }
                if (control.attributes[i].name.indexOf("data-property-") === 0) {
                    control.dataProperties[control.attributes[i].name.substr(14)] = control.getAttribute(control.attributes[i].name);
                }
            }
            // If JS is enabled, then it is responsible for the validation rules, not HTML5,
            // so remove the HTML5 validation attributes
            for (var i = 0, len = attribsToRemove.length ; i < len ; i++) {
                control.removeAttribute(attribsToRemove[i]);
            }
        });

        // Attach the real blur event handlers to the controls. and construct the formatters and 
        // validators configurations from the data-* attributes of <data> elements written by the nds:field tags.
        var control = $(field).find('.pageControl');
        // the find may find controls in nested fields. Ignore these, they'll get picked up by the nested field processing
        if ((control.length > 0) && (control[0].getAttribute('data-field-id') === field.id) && (control[0].getAttribute('type') !== 'hidden')) {
            var data = $(field).find('data')[0];
            if (data) {
                $(control[0]).blur(function(event) {
                    // The blur actions are delayed, so that if the user clicks on a submit button while the focus is in another
                    // field, the submit happens first. If that didn't happen, the blur can cause the page can get redrawn, moving 
                    // the button from under the mouse cursor, stopping the submit from happening.
                    window.blurUpdate = window.setTimeout($.proxy(function() {
                        window.blurUpdate = null;
                        // initialise the blur event handler list so that the messages get cleared at the start 
                        var preBlurFns = $('#pageForm')[0].preBlur[control[0].name] || [];
                        for (var i = 0, len = preBlurFns.length ; i < len ; i++) {
                            preBlurFns[i](event);
                        }
                        var blurSetFns = $('#pageForm')[0].blurset[control[0].name] || [];
                        for (var i = 0, len = blurSetFns.length ; i < len ; i++) {
                            blurSetFns[i](event);
                        }
                        
                        /* provide a hook for external scripts to clean up validation messages */
                        $('form').trigger("validation:afterBlur");
                    }, this), 300);
                });
                var buildMetadataFunctionsList = function(data, replace) {
                    var metadataFunctionsList = [];
                    if (data) {
                        for (var i = 0, len = data.attributes.length ; i < len ; i++) {
                            if (data.attributes[i].name.indexOf("data-formatters") === 0) {
                                var formatters = JSON.parse(data.attributes[i].value);
                                $.each(formatters, function() {
                                    this.fn = document.formatFunctions[this.formatterType];
                                    this.dataProperties = {};
                                    this.validator = false;
                                    metadataFunctionsList.push(this);
                                });
                            }
                        }
                        var hasValidators = false;
                        for (var i = 0, len = data.attributes.length ; i < len ; i++) {
                            if (data.attributes[i].name.indexOf("data-validators") === 0) {
                                hasValidators = true;
                                var validators = JSON.parse(data.attributes[i].value);
                                $.each(validators, function() {
                                    this.fn = document.validationFunctions[this.validatorType];
                                    this.validator = true;
                                    this.replace = replace;
                                    metadataFunctionsList.push(this);
                                });
                            }
                        }
                        if (hasValidators) {
                            var chained = [];
                            $(data).find('> data').each(function(index, el) {
                                var list = buildMetadataFunctionsList(el, true);
                                for (var i = 0, len = list.length ; i < len ; i++) {
                                    chained.push(list[i]);    
                                }
                            });
                            metadataFunctionsList[metadataFunctionsList.length - 1].chained = chained;
                        }
                    }
                    return metadataFunctionsList;
                };
                initializeControlValidationsList(control[0].name, buildMetadataFunctionsList(data, false));
            }
        }

        // When the page loads, if there are no validation failure or error messages, hide the fault box completely
        hideFaultBoxIfEmpty(); 
    });

    // Attach the submit event handler. See above for the description of the phases.
    $('#pageForm').submit(function(event) {
        if (window.blurUpdate) {
            window.clearTimeout(window.blurUpdate); 
            window.blurUpdate = null;
        }
        if ($('#pageForm')[0].forceSubmit) {
            $('form').trigger("validation:doSubmit");
            return;
        }
        event.sourceElement = document.activeElement;
        if (this.presubmit) {
            for (var i = 0 ; i < this.presubmit.length ; i++) {
                this.presubmit[i](event);
            }
        }
        if (event.blocked && this.blockedsubmit) {
            for (var i = 0 ; i < this.preblockedsubmit.length ; i++) {
                this.preblockedsubmit[i](event);
            }
            for (var i = 0 ; i < this.blockedsubmit.length ; i++) {
                this.blockedsubmit[i](event);
            }
            $('.transientServiceUnavailable').removeClass("transientServiceUnavailable");
            
            /* provide a hook for external scripts to clean up validation messages */
            $('form').trigger("validation:afterBlockedSubmit");
        } else {
            $('form').trigger("validation:doSubmit");
        }
    });
})();


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
	 
	if (!anyStickies) {
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
