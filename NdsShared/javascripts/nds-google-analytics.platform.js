// Global variable need for google analytics data-ga-trackId
(function(w) {
    try {
        var this_js_script = $("#gasrc");
        
        var googleAnalyticTrackingId = this_js_script.attr('data-ga-trackId');
        if (googleAnalyticTrackingId) { 
        
            // This script need global googleAnalyticTrackingId variable
            w._gaq = w._gaq || [];
            w._gaq.push([ '_setAccount', googleAnalyticTrackingId ])
            w._gaq.push([ '_trackPageview' ]);
            
            $(function() {
                (function() {
                    var ga = document.createElement('script');
                    ga.type = 'text/javascript';
                    ga.async = true;
                    ga.src = ('https:' == document.location.protocol ? 'https://ssl'
                            : 'http://www')
                            + '.google-analytics.com/ga.js';
                    var s = document.getElementsByTagName('script')[0];
                    s.parentNode.insertBefore(ga, s);
                })();
            
                // load and setup stageprompt. This avoids using JQuery, because that runs the 
                // script as Function object or eval(), which falls foul of the content security policy.  
                var version = this_js_script.attr('data-version');
                var contextUi = this_js_script.attr('data-contextUi') || '';
                var oScript = document.createElement("script");
                oScript.async = true;
                oScript.onload = function() {
                    GOVUK.performance.stageprompt.setupForGoogleAnalytics();
                };
                (document.head || document.getElementsByTagName("head")[0]).appendChild(oScript);
                oScript.src = contextUi+"assets/javascripts/govuk-stageprompt.platform.js?version="+version;
            });
        }
    } catch (e) {
        // discard any errors arising from google-analytics to protect the JS code of the app. 
    }    
})(window);