$(function() {
    $('a.back').click(function () {
        parent.history.back();
        return false;
    });
});

// This should be deleted once PE-58 is implemented
$(".nextproxy").click(function () {
    function getParameterByName(name) {
        name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regexS = "[\\?&]" + name + "=([^&#]*)";
        var regex = new RegExp(regexS);
        var results = regex.exec(window.location.href);
        if (results == null)
            return "";
        else
            return decodeURIComponent(results[1].replace(/\+/g, " "));
    }
    
    function nextflow(page, arg1, arg2) {
        var queryString = "";
        if (arg1 == null) {
            queryString = page + ".html";
        } else if (arg2 == null) {
            queryString = page + ".html?" + arg1;
        } else {
            queryString = page + ".html?" + arg1 + "&" + arg2;
        };

        if ($("form").length) {
            // 
            $("form").attr('action', queryString)
        } else {
            window.location.href = queryString;
        }
    }
    
    var nextpage = $(".nextpage").val();
    var selectedOption = getParameterByName('Type');
    var selectedFor = getParameterByName('WHOFOR');

    if (selectedFor == '' && selectedOption == '') {
        nextflow(nextpage);
    } else if (selectedOption == '' && selectedFor != '') {
        nextflow(nextpage, 'WHOFOR=' + selectedFor);
    } else if (selectedOption != '' && selectedFor == '') {
        nextflow(nextpage, 'Type=' + selectedOption);
    } else {
        nextflow(nextpage, 'Type=' + selectedOption, 'WHOFOR=' + selectedFor)
    };
});









