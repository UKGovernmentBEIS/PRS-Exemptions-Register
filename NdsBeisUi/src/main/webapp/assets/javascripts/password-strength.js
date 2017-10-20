/** Modified from http://jsfiddle.net/HFMvX/ */
$(function() {
	
	var scorePassword = function (pass) {
	    var score = 0;
	    if (!pass)
	        return score;

	    // award every unique letter until 5 repetitions
	    // longer is better
	    var letters = new Object();
	    for (var i=0; i<pass.length; i++) {
	        letters[pass[i]] = (letters[pass[i]] || 0) + 1;
	        score += 5.8 / letters[pass[i]];
	    }

	    // bonus points for mixing it up
	    var variations = {
	        digits: /\d/.test(pass),
	        lower: /[a-z]/.test(pass),
	        upper: /[A-Z]/.test(pass),
	        nonWords: /\W/.test(pass),
	    }

	    variationCount = 0;
	    for (var check in variations) {
	        variationCount += (variations[check] == true) ? 1 : 0;
	    }
	    score += (variationCount - 1) * 5;

	    return parseInt(score);
	};

	var checkPassStrength = function (pass, target) {
	    var score = scorePassword(pass);
	    var meta = "";
	    var colour = "";
	    
	    if (score > 90) {
	    	meta = "strong";
	    	colour = "green";
	    } else if (score > 70) {
	        meta = "good";
	        colour = "DarkTurquoise";
	    } else if (score >= 30) {
	        meta = "weak";
	        colour = "orange";
	    } else if (score < 30) {
	    	meta = "bad";
	    	colour = "red";
	    }
	    
	    target.text(meta);
        target.css("color", colour);

	};
	
    $(".password-strength-target").on("keypress keyup keydown", function() {
        var pass = $(this).val();
        checkPassStrength(pass, $("#password-strength-result"));
    });
});
