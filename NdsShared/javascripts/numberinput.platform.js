$(function () {
	// Accept only number
	function isNumberKey(evt){
	    var keyCode = (evt.which) ? evt.which : evt.keyCode
	    // < 31 - Shift, Ctrl, Alt, Enter, Esc.
	    // 46 - Delete        
	    // 48 - 57 incl. - top row number keys
	    // 96 - 105 incl. - number pad number keys when num lock is on
	    return (keyCode <= 31) || (keyCode === 46) || (keyCode >= 48 && keyCode <= 57) || (keyCode >= 96 && keyCode <= 105);
	}
	
	$(".number").on("keydown keypress", function(event) {
		return isNumberKey(event);
	}).on('paste', function (event) {
	    var element = $(this);
	    setTimeout(function () {
	      element.val(element.val().replace(/[^0-9]/g, '') );
	    }, 0);
	});
});