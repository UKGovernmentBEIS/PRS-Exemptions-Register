"use strict";

(function() {
    if (window.sessionStorage) {
        window.sessionStorage.removeItem("state.ndsViewState");
        window.sessionStorage.removeItem("state.navigationalState");
        window.sessionStorage.removeItem("state.language");
    }
})();