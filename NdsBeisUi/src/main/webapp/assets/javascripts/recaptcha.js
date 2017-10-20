var onloadCallback = function() {
	try {
            grecaptcha.render('g-recaptcha', {
                'sitekey' : document.getElementById('uiData.recaptchaForm.recaptchaSiteKey').value,
                'theme' : 'light'
            });
	}
	catch(err) {
	    alert(err.message);
	}
};
