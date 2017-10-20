$(function() {
	
	 $(".file").each(function() {
	        var file = $(this);
	        // to adjust spinner position
	        var opts = {
	            'top' : '20px',
	            'left' : '22px',
	            'scale' : 0.5
	        }

	        file.find("#resource").on('change',function() {
				 file.find("button[value^='AddResource']").click();
			});
	        
	        var maxUploadSize = file.attr('data-maxsize') - 0;

	        file.find(".uploadspinner").css('position', 'relative');

	        var button = file.find("button[value^='AddResource']");
	        
	        button.click(function() {
	                var fileinput = file.find("input[type='file']")[0];
	                if (fileinput.files && fileinput.files.length > 0) {
	                    var fileSize = fileinput.files[0].size;
	    
	                    if (checkFileSize(fileSize, maxUploadSize, button.attr('field-path'), file.attr('data-maxsize-invalid-msg'))) {
	                            new Spinner(opts).spin(file.find(".uploadspinner")[0]);
	                    } else {
	                        return false;
	                    }
	                }
	            });
	    });
	$(".removefile").each(function() {
        var file = $(this);
        // to adjust spinner position
        var opts = {
                'top' : '11px',
                'left' : '12px',
                'scale' : 0.4
            }
        file.find(".uploadspinner").css('position', 'relative');
        file.find("button[value^='DeleteResource']")
            .click(function() {
                    new Spinner(opts).spin(file.find(".uploadspinner")[0]);
            });
    });
	
	
	function checkFileSize(fileSize, maxUploadSize, path, msg) {
        
		clearErrorMessage(path, true);
		
        if (fileSize > maxUploadSize) {            
            displayErrorMessage(path, msg, msg);            
            return false;
        } else {                       
                        
            return true;
        }
    }	
	
});
