if (!window.jQuery) { 
    // If the fetch from the CDN failed, serve jQuery from this server
    document.write('<script src="/NdsBeisUi/assets/jquery/3.6.0/jquery.min.js?version='+document.head.getAttribute('data-nds-version')+'"><\/script>');
}
