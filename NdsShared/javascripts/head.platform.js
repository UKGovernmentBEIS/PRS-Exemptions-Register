"use strict";
    
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
            // for static page there is no entrypoint name
            var entryPointName;
            if (document.getElementById('entryPointName') != null) {
                entryPointName = document.getElementById('entryPointName').content || document.getElementById('springViewName').content;
            } else {
                entryPointName = document.getElementById('springViewName').content;
            }

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
                        if (window.sessionStorage.getItem("state.clientErrorKey")) {
                            el = document.createElement("input");
                            el.name ="clientErrorKey"; 
                            el.value = window.sessionStorage.getItem("state.clientErrorKey");
                            form.appendChild(el);
                            window.sessionStorage.removeItem("state.clientErrorKey");
                        }
                        document.documentElement.appendChild(form);
                        form.submit();
                        return;
                    }
                }
            }
        }
    }

    $(function() {
        // don't record viewState if entry point name is blank
        if (document.getElementById('entryPointName') != null)
        {
            // record the viewState, navigationState and preferred language in the sessionStorage, so that they can be 
            // recovered after a browser refresh
            if (window.sessionStorage) {
                window.sessionStorage.setItem("state.ndsViewState", $('#ndsViewState').val());
                window.sessionStorage.setItem("state.navigationalState", $('#navigationalState').val());
                window.sessionStorage.setItem("state.language", $('#language').val());
            }
        }
        
        if (history.replaceState) {
            // create two history entries for this document so that clicking on the browser back button
            // will cause a navigation between history entries of this document and ensure that the 
            // onpopstate handler is called.
            
            history.replaceState("previous", document.title, document.getElementById('springViewName').content + "");
            var bookmarkParams = (document.getElementById('bookmarkParams') != null) ? document.getElementById('bookmarkParams').content : "";
            history.pushState("", document.title, document.getElementById('springViewName').content + "" + bookmarkParams);
            
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
        
        (function() {    
            // any on-page links will if followed, and JS is enabled, cause an undesired popstate event. So manually scroll the
            // page to the referenced id and suppress any further action
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
        })();
        
        (function() {
            // capture into session storage the values of the form fields immediately prior to submit. If a mod-security 
            // violation is detected, the current page is redisplayed and the values can be restored to the controls from the
            // values stored here.
            if (window.sessionStorage) {
                $('form').on("validation:doSubmit", function() {
                    new Spinner({ 'top' : '13px', 'left' : '22px', 'scale' : 0.5 }).spin($(".waitSpinner")[0]);
                    var controlValues = {};
                    $('input').each(function() {
                        if (this.type == "radio") { 
                            if (this.checked) {
                                controlValues[this.name] = this.value;
                            }
                        } else if (this.type == "checkbox") {
                            if (this.checked) {
                                controlValues[this.name] = controlValues[this.name] || [];
                                controlValues[this.name].push(this.value);
                            }
                        } else if (
                            (this.name !== "") &&
                            (this.name !== "language") &&
                            (this.name !== "navigationalState") &&
                            (this.name !== "ndsViewState") &&
                            (this.name.indexOf("refDataSets[") != 0)
                            ) {
                            controlValues[this.name] = this.value;
                        }
                    });
                    $('select').each(function() {
                        controlValues[this.name] = controlValues[this.name] || [];
                        if (controlValues[this.name].indexOf(this.value) === -1) {
                            controlValues[this.name].push(this.value);
                        }
                    });
                    $('textarea').each(function() {
                        controlValues[this.name] = this.value; 
                    });
                    window.sessionStorage.setItem("state.controlValues", JSON.stringify(controlValues));
                });
                if (document.getElementById("clientValuesRestore") != null) {
                    var controlValues = JSON.parse(window.sessionStorage.getItem("state.controlValues"));
                    $('input').each(function() {
                        if (controlValues[this.name]) {
                            if (this.type == "radio") {
                                if (controlValues[this.name] == this.value) {
                                    // click the control rather than just set the clicked state, so that
                                    // any event handlers on the control are fired.
                                    $(this).click();
                                }
                            } else if (this.type == "checkbox") {
                                if (controlValues[this.name].indexOf(this.value) > -1) {
                                    // click the control rather than just set the clicked state, so that
                                    // any event handlers on the control are fired.
                                    $(this).click();
                                }
                            } else if (
                                (this.name !== "") &&
                                (this.name !== "language") &&
                                (this.name !== "navigationalState") &&
                                (this.name !== "ndsViewState") &&
                                (this.name.indexOf("refDataSets[") != 0)
                                ) {
                                this.value = controlValues[this.name];
                            }
                        }
                    });
                    $('select').each(function() {
                        // the set of values and checking for options matching the values is to allow
                        // for the vehicle model selects, where multiple selects have the same name
                        var select = this;
                        $.each(controlValues[this.name], function(index, value) {
                            if ($(select).has('option[value="' + value + '"]').length) {
                                select.value = value;
                                // having set the select option, fire any click event
                                // handlers on the select.
                                $(select).triggerHandler('click');
                                $(select).triggerHandler('change');
                            }
                        });
                    });
                    $('textarea').each(function() {
                        if (controlValues[this.name]) {
                            this.value = controlValues[this.name];
                        }
                    });
                }
                // The state.controlValues object is a minor privacy issue, so 
                // don't allow it to linger
                window.sessionStorage.removeItem("state.controlValues");

            }
        })();
    });
})();

//Session timeout warning 
$(document).ready(function() {
    if ($("#session-timeout-warning").length === 0) {
        return;
    }
    var sessionWarningCountTo = -1;
    var configSet = false;
    var sessionCounter = 0;
    var showingMessage = false;
    var buttonClicked = false;
    
    // check if session is likely to be expiring soon
    var sessionWarningTimer = setInterval(function() {

        // give the page some time to load before checking what the sessionWarningCountTo value is
        if (configSet === false) {
            
            var metas = document.getElementsByTagName("meta");
            
            for (var i=0; i<metas.length; i++) {
                var name = metas[i].getAttribute("name")

                if (metas[i].getAttribute("name") == "sessionWarningCountTo") {
                    sessionWarningCountTo = metas[i].getAttribute("content");
                    break;
                }
            }
            
            // only run this once (means if the meta tag isn't set the timer will be disabled)
            configSet = true;
        } else {
            
            // check if session warning should be shown
            if (showingMessage != true) {
                if (sessionCounter > sessionWarningCountTo) {
                    
                    // show the session warning message
                    showingMessage = true;
                    $("#session-timeout-warning").css("display", "block");
                    document.getElementById("session-timeout-warning").scrollIntoView(false);
                    
                    // handle session-extension button being clicked
                    $("#extend-session").click(function() {
                        
                        // debounce button
                        if (buttonClicked == false) {
                            buttonClicked = true;
                            
                            // contact UI which extends session
                            $.ajax({url: "session-extension", type: "GET", timeout: "3000", success: function(result) {
                                var resetMessageForNextTime = true;
                                
                                if (result) {
    
                                    // check if the result was the session-extension page (ie not session-expired)
                                    var isSessionExtension = result.search(document.getElementById("Paragraph_session_extension_page_key").textContent);
                                    
                                    if (isSessionExtension > 0) {
                                        
                                        // flash success message
                                        $("#session-timeout-message-area").html(document.getElementById("Paragraph_session_extended").textContent);
                                        
                                        // reset the session counter
                                        sessionCounter = 0;
                                    } else {
                                        $("#session-timeout-message-area").html(document.getElementById("Paragraph_session_extension_failed").textContent);
                                        resetMessageForNextTime = false;
                                        $("#extend-session").hide();
                                    }
                                } else {
                                    $("#session-timeout-message-area").html(document.getElementById("Paragraph_session_extension_access_error").textContent);
                                    $("#extend-session").hide();
                                    resetMessageForNextTime = false;
                                }
                                
                                // we usually want to reset session warning ready for next time (which could be immediately in error message case)
                                if (resetMessageForNextTime === true) { 
                                    var hideTimer = setInterval(function() {
                                        $("#session-timeout-warning").css("display", "none");
                                        $("#session-timeout-message-area").html(document.getElementById("Paragraph_session_warning_message").textContent);
                                        
                                        clearInterval(hideTimer);
                                        showingMessage = false;
                                        
                                        // button available again
                                        buttonClicked = false;
                                    }, 2000);
                                }
                            }});
                        }
                    });
                    
                } else if (sessionWarningCountTo < 0) {
                
                    // stop the timer since it's not been set for this application
                    clearInterval(sessionWarningTimer);
                }
            }
        }
        
        sessionCounter++;
          
    }, 1000);
    
    
});


(function() {    
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
 
 //String.trimLeft(), String.trimRight() 
 !function(
     a, // trimming object that defined String.prototype extensions and their related Regular Expressions
     b // placeholder variable for iterating over 'a'
   ){
     for(b in a) // iterate over each of the trimming items in 'a'
       String.prototype[b]=b[b] || // use the native string trim/trimLeft/trimRight method if available, if not:
       Function('return this.replace('+a[b]+',"")') // generate a function that will return a new string that replaces the relevant whitespace
     }({
     trimLeft: /^\s+/, // regex to trim the left side of a string (along with prototype name)
     trimRight: /\s+$/ // regex to trim the right side of a string (along with prototype name)
   })
}