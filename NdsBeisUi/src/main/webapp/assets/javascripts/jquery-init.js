if (!window.jQuery) { 
    // If the fetch from the CDN failed, serve jQuery from this server
	document.write('<script src="/NdsBeisUi/assets/jquery/1.11.2/jquery.min.js?version='+document.head.getAttribute('data-nds-version')+'"><\/script>');
}
