"use strict";

$(function () {
    $(".submitradiobutton").each(function() {
        var spinnerEl = window.document.createElement("div");
        spinnerEl.setAttribute("class", "spinnerWrapper");
        var lastLineButton = $(this);
        while (lastLineButton.next()[0] && lastLineButton.next().hasClass("submitradiobutton") && (lastLineButton.offset().top === lastLineButton.next().offset().top)) {
            lastLineButton = lastLineButton.next(); 
        }
        lastLineButton.append(spinnerEl);
        var submitradiobutton = $(this);
        submitradiobutton.find("button").click(function() {
            if (submitradiobutton.find("input:checked").length > 0) {
                return false;
            }
            var selectedVal = submitradiobutton.closest("fieldset").find("input:checked").val();
            submitradiobutton.find("input").prop('checked',true);
            if ((selectedVal === void 0) && submitradiobutton.hasClass("as-unselected")) {
                return false;
            } else {    
                new Spinner({ 'top' : '13px', 'left' : '16px', 'scale' : 0.5 }).spin(spinnerEl);
            }
        });
    });
});