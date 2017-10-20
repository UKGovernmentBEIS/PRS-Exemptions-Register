$(function () {
    $(".geographicAddress").each(function() {
        var address = $(this);
        var addressElement = this;

        address.find(".findAddressSpinner").css('position', 'relative');
        address.find("button[value^='FindAddress:']").click(function() {
            new Spinner({ 'top' : '13px', 'left' : '22px', 'scale' : 0.5 }).spin(address.find(".findAddressSpinner")[0]);
        });
        
        address.find(".getAddressSpinner").css('position', 'relative');
        address.find("button[value^='GetAddress:']").click(function() {
            new Spinner({ 'top' : '13px', 'left' : '22px', 'scale' : 0.5 }).spin(address.find(".getAddressSpinner")[0]);
        });
        
        address.find(".candidateAddressList").on('change', function() {
            address.find("button[value^='GetAddress:']").click();
        }).each(function() {
            address.find(".candidateAddressPicker").toggle($(this).children().length > 0);
        });
        
        var manualAddress = address.find(".disclosure.manual_address");
        var openState = manualAddress.find(".summary input");

        manualAddress.removeClass("inactive");
        if (openState.val() !== "manual") {
            manualAddress.find(".content input").prop('readonly', true);
            manualAddress.find(".content select").prop('disabled', true);
         }
        
        if(openState.val () === "auto"){
		   $('html,body').animate({
		         scrollTop: $('input[id$="line0"]').offset().top},
		         'slow');
		}
        
       if (openState.val() === "false") {
            manualAddress.removeClass("open");
        }

        // initialise qasMoniker from qasMonikerIn so qasMoniker only gets set when JS in enabled
        manualAddress.find(".content input.qasMoniker").val(manualAddress.find(".content input.qasMonikerIn").val());
        manualAddress.find(".content input.uprn").val(manualAddress.find(".content input.uprnIn").val());
        manualAddress.find(".content input.easting").val(manualAddress.find(".content input.eastingIn").val());
        manualAddress.find(".content input.northing").val(manualAddress.find(".content input.northingIn").val());
        manualAddress.find(".content input.localEducationAuthorityCode").val(manualAddress.find(".content input.localEducationAuthorityCodeIn").val());
        
        
        manualAddress.find(".summary a").click(function (event) {
            manualAddress.find(".content input").prop('readonly', false);
        	manualAddress.find(".content select").prop('disabled', false);
            manualAddress.find(".content input.qasMoniker").val('');
            manualAddress.find(".content input.uprn").val('');
            manualAddress.find(".content input.easting").val('');
            manualAddress.find(".content input.northing").val('');
            manualAddress.find(".content input.localEducationAuthorityCode").val('');
            if (openState.val() === "false") {
                manualAddress.addClass("open");
                openState.val("manual");
            } else if (openState.val() === "manual") {
                manualAddress.removeClass("open");
                openState.val("false");
            } else { // openState.val() === "auto"
                openState.val("manual");
            }
            event.preventDefault();
        });
        
        var consolidateMessages = function() {
            var faultShown = function(faultId, verifyRequiredValidator) {
                var faultLi = $("[id = '"+ faultId + "']");
                if (faultLi.length === 0) {
                    return null;
                }
                if (faultLi.attr('hidden') === 'hidden') {
                    return null;
                }
                if (! verifyRequiredValidator) {
                    return faultLi;
                }
                var faultLink = faultLi.find("a[data-validator='RequiredFieldValidator']");
                return faultLink.length > 0 ? faultLi : null;
            }
            address.find("[id$='line0.field']").each(function() {
                var faultId = $(this).attr('id').replace('field', 'fault');
                var line0FaultShown = faultShown(faultId, true);
                var townFaultShown = faultShown(faultId.replace('line0', 'town'), true);
                var postcodeFaultShown = faultShown(faultId.replace('line0', 'postcode'), true);
                var consolidatedFaultShown = faultShown(faultId.replace('line0', 'consolidated'), false);
    
                if (line0FaultShown && townFaultShown && postcodeFaultShown) {
                    line0FaultShown.addClass('validation-consolidated'); 
                    townFaultShown.addClass('validation-consolidated');
                    postcodeFaultShown.addClass('validation-consolidated');
                    if (consolidatedFaultShown === null) {
                        consolidatedFaultShown = line0FaultShown.clone();
                        consolidatedFaultShown.attr('id', consolidatedFaultShown.attr('id').replace('line0', 'consolidated'));
                        consolidatedFaultShown.removeClass('validation-consolidated'); 
                        consolidatedFaultShown.find("a[data-validator='RequiredFieldValidator']").text('An address is required');
                        consolidatedFaultShown.appendTo(line0FaultShown.parent());
                    }
                } else {
                    if (consolidatedFaultShown !== null) {
                        consolidatedFaultShown.remove();
                    	clearErrorMessage(faultId.replace('.line0.fault', ''), false);
                    }
                    $("[id = '"+ faultId + "']").removeClass('validation-consolidated'); 
                    $("[id = '"+ faultId.replace('line0', 'town') + "']").removeClass('validation-consolidated');
                    $("[id = '"+ faultId.replace('line0', 'postcode') + "']").removeClass('validation-consolidated');
                }
            });
               
        }
        $('form').on("validation:afterBlockedSubmit", function() {
            consolidateMessages();
        });
        $('form').on("validation:afterBlur", function() {
            consolidateMessages();
        });
    })
});