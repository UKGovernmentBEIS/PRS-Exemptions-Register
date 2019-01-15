"use strict";

$('textarea').each(function() {
    var textarea = $(this); 
    textarea.find('+ .charlimit').hide();
    var maxlength = textarea.attr('data-maxlength');
    if (maxlength !== void 0) {
        /* calculates the number of characters that will be required to store the string on a Windows OS,
         * treating line endings as a {carriage return, line feed} pair. We know that both characters will
         * be sent to the server, (both application/x-www-form-urlencoded and multipart/form-data mandate it)
         * but the server may reduce them. Since the actually storage format is unknown here, this is a safe,
         * worst case, calculation. 
         */
        var lengthCalc = function() {
            var val = textarea.val();
            var nCrs = (val.match(/\r/g)||[]).length;
            var nLfs = (val.match(/\n/g)||[]).length;
            return val.length + nLfs - nCrs;
        }
        var charCountText = textarea.attr('data-charcounttext').replace(/\$\{id\}/g, this.id).replace();
        var keyTimeoutId;
        textarea.after('<div>' + charCountText + '</div>');
        $('[id="' + this.id + '-maxChars"]').text((function() {
            return maxlength;
        })());
        $('[id="' + this.id + '-remaining"]').text((function() {
            return maxlength - lengthCalc();
        })());
        textarea.on('keyup cut paste', function() {
            if (keyTimeoutId != null) {
                window.clearTimeout(keyTimeoutId);
            }
            keyTimeoutId = window.setTimeout(function() {
                $('[id="' + this.id + '-remaining"]').text((function() {
                    return maxlength - lengthCalc();
                })());
            }.bind(this), 350);
        });
    }
});