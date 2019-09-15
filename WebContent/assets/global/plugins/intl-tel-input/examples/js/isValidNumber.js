//var telInput = $("input");
var setValTo=$('input[name="registrantContactDetails.phoneNumber"]');
var telInput=$('input[name="intlMobileNumber"]');
telInput.each(function(){

// initialise plugin
	$(this).intlTelInput({
		allowExtensions: true,
		autoFormat: false,
		autoHideDialCode: true,
		autoPlaceholder: false,
		defaultCountry: "bd",
		initialCountry: "auto",
		geoIpLookup: function(callback) {
		    $.get('http://ipinfo.io', function() {}, "jsonp").always(function(resp) {
		      var countryCode = (resp && resp.country) ? resp.country : "";
		      callback(countryCode);
		    });
		},
		nationalMode: true,
		numberType: "MOBILE",
		formatOnDisplay: false,
		separateDialCode:true,
		//onlyCountries: ['us', 'gb', 'ch', 'ca', 'do'],
		preferredCountries: ['bd'],
		preventInvalidNumbers: true,
	    utilsScript: context+"/assets/scripts/mobile-utils.js"
	});
	
	var reset = function() {
		$(this).removeClass("error");
		$(this).closest('.col-md-9').find('.error-msg').addClass("hide");
		$(this).closest('.col-md-9').find('.valid-msg').addClass("hide");
	};
	
	// on blur: validate
	$(this).blur(function() {
	  reset();
	  if ($.trim($(this).val())) {
	    if ($(this).intlTelInput("isValidNumber")) {
	    	$(this).closest('.col-md-9').find('.valid-msg').removeClass("hide");
	    	var intlNumber = $(this).intlTelInput("getNumber");
	    	setValTo.val(intlNumber);
	    	console.log(telInput.val());
	    } else {
	    	$(this).addClass("error");
	    	$(this).closest('.col-md-9').find('.error-msg').removeClass("hide");
	    	setValTo.val('');
	    }
	  }
	});
	
	// on keyup / change flag: reset
	$(this).on("keyup change", reset);
	//var intlNumber =telInput.intlTelInput("getNumber");
	//telInput.val(intlNumber);
})