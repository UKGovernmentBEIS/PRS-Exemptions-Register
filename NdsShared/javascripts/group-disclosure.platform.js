$(function () {
    var updatePanelsOpenness = function() {
        /* Refresh the "open-ness" of all the panels, as opening one panel should close 
         * the others 
         */ 
        $('label[data-group-disclosure]').each(function() {
            var disclosure = $(this);
            var checked = disclosure.find('input')[0].checked;
            disclosure.find('+ fieldset.radio').each(function () {
                $(this).css("display", checked ? "block" : "none");
            });;
        });
    }
    
    // Rule 1: If a single level radiobutton is clicked - autoclick the uiData.nul proxy radio button.
    

	var single = $('label[data-single]');
	if (!$('[data-non-select-group-proxy]').prop("disabled")) {
		single.on('click', function() {
			$('[data-non-select-group-proxy]').click();
			$('input[name="uiData.nul"]').parent().each(function() {
				$(this).removeClass('selected');
			});
			updatePanelsOpenness();
		});
	}
    
    // Rule 2: If a dual level, uiData.nul radiobutton is clicked, autoclick the
	// real path proxy radio button
    // unless the real path radio currently selected is a child of the clicked button

	if (!$('[data-non-select-group-proxy]').prop("disabled")) {
		var groupTop = $('label[data-group-top]');
		groupTop.on('click', function() {
			if ($(this).find("+ fieldset input:checked").length == 0) {
				$('[data-non-select-proxy] input').each(function() {
					$(this).click();
				});
				var name = $('[data-non-select-proxy] input').attr('name');
				$('input[name="' + name + '"]').parent().each(function() {
					$(this).removeClass('selected');
				});
			}
			updatePanelsOpenness();
		});
	}
    
    // Rule 3: if a non uiData.nul radiobutton is initially checked, check the
	// disclosure control that contains it
    
    $('label[data-group-disclosure]').each(function() {
        var disclosure = $(this);
        if (disclosure.find('+ fieldset input:checked').length > 0) {
            disclosure.find('input')[0].checked = true;
            updatePanelsOpenness();
        }
    });
});