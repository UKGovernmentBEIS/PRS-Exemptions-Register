/* 
	This function will load script and call the callback once the script has loaded 
	adapted from https://stackoverflow.com/questions/51833090/put-google-analytics-code-in-an-js-file
*/
function loadScriptAsync(scriptSrc, callback) {
    if (typeof callback !== 'function') {
        throw new Error('Not a valid callback for async script load');
    }
    var script = document.createElement('script');
    script.onload = callback;
    script.src = scriptSrc;
    document.head.appendChild(script);
}

/* This is the part where you call the above defined function and "call back" your code which gets executed after the script has loaded */
loadScriptAsync('https://www.googletagmanager.com/gtag/js', function(){
	window.dataLayer = window.dataLayer || [];
	function gtag(){dataLayer.push(arguments);}
	gtag('js', new Date());	
	
	// the tracking id from the properties is set as an attribute on the line that includes this file
	var gaTrackId = document.getElementsByTagName('meta')['gaId'].getAttribute('content');
	gtag('config', gaTrackId);
})