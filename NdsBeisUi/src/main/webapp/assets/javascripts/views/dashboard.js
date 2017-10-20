$( function() {
			
	//wire up the tab links to hide/show the correct tab
	$('#exemption-tabs a').click(function(event) {
        event.preventDefault();
        $(this).parent().addClass("active");
        $(this).parent().siblings().removeClass("active");
        var tab = $(this).attr("href");
        
        $(".tab-pane").not(tab).css("display", "none");
        $(tab).css("display", "block");
    });
    
	//wire up the links
	$("#show-current-exemptions").click(function(event){
		event.preventDefault();
		showExemptionsTab(this);
	});
	
	$("#show-expired-exemptions").click(function(event){
		event.preventDefault();
		showExemptionsTab(this);
	});
	
	function eventFire(el, etype){
		  
		//fire the event and fake the target, this allows the polyfill to correctly pick up
		//the click on the summary link
	    var evObj = document.createEvent('Events');
	    evObj.initEvent(etype, true, false);
	    Object.defineProperty(evObj, 'target', {value: el, enumerable: true});
	    	    	    
	    if (el.fireEvent) {	    	
	    	el.fireEvent('on' + etype, evObj);
	    }else{
	    	
	    	el.dispatchEvent(evObj);
	    }	  
	};
	
	function showExemptionsTab(link){
			
		//open the details control if not open
		if( !document.getElementById('exemption-details').hasAttribute('open') ){
		
			eventFire($('#exemption-details-link')[0], 'mouseup'); //hack for the polyfill		
			document.getElementById('exemption-details').open = true; //true html5 support
		}
			
		//display the correct tab
		var tab = $(link).attr("href");
		
		var tabControl = $('li a[href="' + tab + '"]').parent();
		tabControl.addClass("active");
		tabControl.siblings().removeClass("active");
        
        $(".tab-pane").not(tab).css("display", "none");
        $(tab).css("display", "block");
        $(tab)[0].scrollIntoView();
	};
} );