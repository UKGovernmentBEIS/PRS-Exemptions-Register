"use strict";

(function() {    
    // Convert buttons with role=link to spans that submit the form. We do this in JS, so that
    // if JS is not available, the buttons remain as form submitting buttons.
    $("button[role='link']").each(function() {
        var button = $(this);
        var anchor = this.parentNode.insertBefore(document.createElement('span'), this);
        this.parentNode.insertBefore(document.createTextNode(' '), this.nextSibling);
        $(anchor).on("click", function () {
            // Click the button rather than submit the form, so that any validation rules
            // on the button are observed.
            button.trigger("click");
        }).attr("role", "link").attr("class", button.attr("class")).append(button.contents());
        if (this.id) {
            $(anchor).attr("id", this.id + "-linkreplacement");    
        }
        var child = $(anchor)[0].lastChild;
        if (child.nodeType == 3) {
            child.nodeValue = child.nodeValue.trimRight();
        }
        var child = $(anchor)[0].firstChild;
        if (child.nodeType == 3) {
            child.nodeValue = child.nodeValue.trimLeft();
        }
        button.addClass("replaced");
        button.attr("tabindex", "-1");
    });
})();