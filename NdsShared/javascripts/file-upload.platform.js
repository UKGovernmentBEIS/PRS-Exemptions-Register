"use strict";

(function() {
    $("input[type='file']").before("<div class='chooseFile'><button type='button'></button><span></span></div>");
    $(".chooseFile").each(function() {
        var chooseFile = $(this);
        var fileInput = chooseFile.find("+ input");
        var chooseFileButton = chooseFile.find("button"); 
        chooseFileButton.text(fileInput.attr("data-choosefile"))
            .on("keypress", function(event) { 
                if (event.which == 13) {
                    fileInput.click();
                }
        });
        chooseFile.find("span").text(fileInput.attr("data-nofile"));
        fileInput.width(chooseFileButton.outerWidth())
            .attr("tabindex", "-1")
            .on("change", function() {
                var fname = fileInput.val().replace(/.*[\/\\]/g, "");
                if (fname.length === 0) {
                    fname = fileInput.attr("data-nofile");
                }
                chooseFile.find("span").text(fname);
        });
    });
})();