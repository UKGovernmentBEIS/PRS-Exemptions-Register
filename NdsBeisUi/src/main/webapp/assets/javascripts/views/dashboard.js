$( function() {
	$("details").removeAttr('open');
	var referrer = document.referrer;
	var prevPage = referrer.substr(referrer.lastIndexOf("/") + 1, referrer.length);
	   
	if (prevPage == "personalised-exemption-confirmation") {
		$("details").attr('open','open');
	} 
		
    //wire up the tab links to hide/show the correct tab
	$('#exemption-tabs a').click(function(event) {
        event.preventDefault();
        $(this).parent().addClass("active");
        $(this).parent().siblings().removeClass("active");
        var tab = $(this).attr("href");
        
        $(".tab-pane").not(tab).css("display", "none");
        $(tab).css("display", "block");
    });
    
} );