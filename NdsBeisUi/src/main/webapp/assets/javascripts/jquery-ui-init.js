//try to get JQuery-ui from a cdn, since the client may already have it cached 
document.write('<script src="https://code.jquery.com/ui/1.13.0/jquery-ui.min.js"><\/script>');
if (window.jQuery.ui === void 0) { 
    // If the fetch from the CDN failed, serve jQuery from this server
    document.write('<script src="assets/jquery/ui/1.13.0/jquery-ui.min.js?version='+document.head.getAttribute('data-nds-version')+'"><\/script>');
}
