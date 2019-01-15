"use strict";

$(function () {
    
    $(".pagination-control").on("change", function() {
        this.setAttribute("name", this.getAttribute("data-controlname"));
        return window.document.forms[0].submit();
    });

});