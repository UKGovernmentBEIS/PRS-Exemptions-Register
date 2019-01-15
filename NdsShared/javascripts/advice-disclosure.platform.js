"use strict";

$(function () {
    $('.disclosure.advice').each(function () {
        var disclosure = $(this);
        disclosure.find('.summary').click(function() {
            disclosure.toggleClass('open');
        }).find('a').click(function() { 
            disclosure.toggleClass('open'); 
            return false;
        });
    });
});