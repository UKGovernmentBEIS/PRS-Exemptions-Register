"use strict";

(function() {
    $('[data-scroll-target]').first().each(function() {
        var scrollTarget = this; 
        $('html, body').animate({
            scrollTop: $(scrollTarget).offset().top
        }, 0);
    });
})();

// Once the JS has loaded, clear the loading flag so that any hidden submit actions are n
document.documentElement.removeAttribute("data-js-loading"); 
