$(function() {
	var field = $(".validationMessage");
	$("[action^='upload'] .file").each(function() {
		var file = $(this);
		var opts = {
			'top' : '13px',
			'left' : '22px',
			'scale' : 0.5
		}

		var maxUploadSize = file.attr('data-maxsize') - 0;

		file.find(".uploadspinner").css('position',	'relative');

		file.find("button[value^='AddResource']")
			.click(function() {
				var fileinput = file.find("input[name='file']")[0];
				var fileSize = (fileinput.size || fileinput.fileSize);

				if (checkFileSize(fileSize,	maxUploadSize)) {
					new Spinner(opts).spin(file.find(".uploadspinner")[0]);
				} else {
					return false;
				}
			});

		file.find("button[value^='DeleteResource']")
			.click(function() {
				new Spinner(opts).spin(file.find(".uploadspinner")[0]);
			});
	});

	function checkFileSize(fileSize, maxUploadSize) {
		if (fileSize > maxUploadSize) {
			field.find('.invalid' + process.invalidClassQualifier).append(
					'<p>' + file.attr('data-maxsize-invalid-msg') + '</p>');
			return false;
		}
		return true;
	}

});
