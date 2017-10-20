/*ensure the JSON objects exist*/
if( document.regularExpressions == null ){
	document.regularExpressions = {};
}

if( document.validationFunctions == null ){
	document.validationFunctions = {};
}

if( document.formatFunctions == null ){
	document.formatFunctions = {};
}

document.regularExpressions['EPCReferenceNumberFieldValidator'] =
	
	function() {
			/*Allow the user to enter with or without the hypens*/
        	return /^[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}?$|^[0-9]{20}$/;
        };
        
document.regularExpressions['GdipGdarReferenceNumberFieldValidator'] =

	function() { 
    		return /^([0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}|[0-9]{10}-[0-9]{10}|[0-9]{3}-[0-9]{4}-[0-9]{5}-[0-9]{4}-[0-9]{4})?$/;
       	};

document.validationFunctions['EPCReferenceNumberFieldValidator'] =

	function() {
            return document.regularExpressions['EPCReferenceNumberFieldValidator']().test(this.value.trim()+''); 
        };
        
document.validationFunctions['GdipGdarReferenceNumberFieldValidator'] =
	
	function() {
            return document.regularExpressions['GdipGdarReferenceNumberFieldValidator']().test(this.value.trim()+''); 
        };
        
document.formatFunctions['EPCReferenceNumberFieldFormatter'] = 
     function() {
			
		if( (/^[0-9]{20}$/).test(this.value.trim()+'')){
			/* Remove any hypens and then format with the regex*/	
			var unformatted = this.value.replace(/-/g, '');
			this.value = unformatted.replace(/(\d{4})(\d{4})(\d{4})(\d{4})(\d{4})/g, "$1-$2-$3-$4-$5");
		}
		return this.value;
    };