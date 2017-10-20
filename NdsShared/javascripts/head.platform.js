(function() {
    // If it's a GET request, and the viewName is not the requested url or the bare context folder url, then 
    // it's likely either a refresh, or an invalid bookmark.
    // For either, if there is a viewstate stored in session storage, re-post that to return to the
    // previous page. Otherwise, show the page that was returned, which will be the default
    // entry point page.
    // If the viewstate is the requested url, then it's likely an attempt to enter at an entry
    // point, and just continue with the returned page.
	// If the meta tag skipRefreshPagePreservation is set then skip this check.
	if (document.getElementById('skipRefreshPagePreservation').content + "" != "true") {
	    if (document.getElementById('request-method').content + "" === "GET") {
	        var entryPointName = document.getElementById('entryPointName').content || document.getElementById('springViewName').content;
	        if (entryPointName !== "SessionException") {
	            var viewNameRegEx = new RegExp("^\/.*?\/" + entryPointName + "(\.htm)?$")
	            if ((! viewNameRegEx.test(window.location.pathname)) && (! /^\/[^\/]*\/$/.test(window.location.pathname))) {
	                if ((window.sessionStorage) && (window.sessionStorage.getItem('state.navigationalState'))) {
	                    var form = document.createElement("form");
	                    form.setAttribute("hidden", "hidden");
	                    form.action = "index.htm";
	                    form.method = "POST";
	                    var el = document.createElement("input");
	                    el.name ="ndsViewState"; 
	                    el.value = window.sessionStorage.getItem("state.ndsViewState");
	                    form.appendChild(el);
	                    el = document.createElement("input");
	                    el.name = "navigationalState"; 
	                    el.value = window.sessionStorage.getItem("state.navigationalState");
	                    form.appendChild(el);
	                    el = document.createElement("input");
	                    el.name ="language"; 
	                    el.value = window.sessionStorage.getItem("state.language");
	                    form.appendChild(el);
	                    document.documentElement.appendChild(form);
	                    form.submit();
	                    return;
	                }
	            }
	        }
	    }
	}
    $(function() {
        // record the viewState, navigationState and preferred language in the sessionStorage, so that they can be 
        // recovered after a browser refresh
        if (window.sessionStorage) {
            window.sessionStorage.setItem("state.ndsViewState", $('#ndsViewState').val());
            window.sessionStorage.setItem("state.navigationalState", $('#navigationalState').val());
            window.sessionStorage.setItem("state.language", $('#language').val());
        }
        
        if (history.replaceState) {
            // create two history entries for this document so that clicking on the browser back button
            // will cause a navigation between history entries of this document and ensure that the 
            // onpopstate handler is called.
            
            history.replaceState("previous", document.title, document.getElementById('springViewName').content + "");
            history.pushState("", document.title, document.getElementById('springViewName').content + "");
            
            window.onpopstate = function(event) {
                // detect that this onpopstate is caused by a browser back action
                if (event.state === "previous") {
                    var backButton = document.getElementById("button.back");
                    if (backButton) { 
                        if (backButton.getAttribute("data-has-breadcrumbs") === "false") {
                            // if there's no breadcrumb trail yet, then there's no point in doing a 
                            // goto previous page in sitemap. But a history.back() will be safe here.
                            window.history.back();
                        } else {
                            // click the back button, if there is one
                            $(backButton).click();
                        }
                    } else {
                        // If the page has no back button, go to the app start page
                        window.location.href = "_";
                    }
                } else {
                    // if there's a popstate event for any other reason, repush the state.
                    history.pushState("", document.title, document.getElementById('springViewName').content);
                }
            }
        };
        
        // any on-page links will if followed, and JS is enabled, cause an undesired popstate event. So manually scroll the
        // page to the referenced id a suppress any further action
        $("a[href^='#']").on("click", function() {
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
    });
    
	// Add viewport styling for IEMobile    
	if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
		var d=document,c="appendChild",a=d.createElement("style");
		a[c](d.createTextNode("@-ms-viewport{width:auto!important}"));
		d.getElementsByTagName("head")[0][c](a);
	}
})();

// While the JS is loading, hide submit buttons so that they can't be clicked until
// any handlers are attached. This flag is cleared when the JS load is complete.
document.documentElement.setAttribute("data-js-loading", "");

// Make known to CSS that JS is enabled
document.documentElement.className = document.documentElement.className + ' js-enabled';

//Polyfills

//String.startsWith() 
//Thanks to MathRobin: https://github.com/MathRobin/string.startsWith/blob/master/src/string.startsWith.js
if (!String.prototype.startsWith) {
 (function () {
     'use strict'; // needed to support `apply`/`call` with `undefined`/`null`
     var defineProperty = (function () {
         // IE 8 only supports `Object.defineProperty` on DOM elements
         try {
             var object = {};
             var $defineProperty = Object.defineProperty;
             var result = $defineProperty(object, object, object) && $defineProperty;
         } catch (error) {
         }
         return result;
     }());
     var toString = {}.toString;
     var startsWith = function (search) {
         if (this == null) {
             throw TypeError();
         }
         var string = String(this);
         if (search && toString.call(search) == '[object RegExp]') {
             throw TypeError();
         }
         var stringLength = string.length;
         var searchString = String(search);
         var searchLength = searchString.length;
         var position = arguments.length > 1 ? arguments[1] : undefined;
         // `ToInteger`
         var pos = position ? Number(position) : 0;
         if (pos != pos) { // better `isNaN`
             pos = 0;
         }
         var start = Math.min(Math.max(pos, 0), stringLength);
         // Avoid the `indexOf` call if no match is possible
         if (searchLength + start > stringLength) {
             return false;
         }
         var index = -1;
         while (++index < searchLength) {
             if (string.charCodeAt(start + index) != searchString.charCodeAt(index)) {
                 return false;
             }
         }
         return true;
     };
     if (defineProperty) {
         defineProperty(String.prototype, 'startsWith', {
             'value'        : startsWith,
             'configurable' : true,
             'writable'     : true
         });
     } else {
         String.prototype.startsWith = startsWith;
     }
 }());
}