$(document).ready(function(){
	var dnsAddForm = $('#dnsAddForm');
    var error1 = $('.alert-danger', dnsAddForm);
    var success1 = $('.alert-success', dnsAddForm);

    
    jQuery.validator.addMethod("dnsNameChecker", function(value, element) { 
		if( value.indexOf(" ") < 0 && value == ""){
			 return true;
		 }
		var reg = systemConfig.getDnsNameRegExpr(); // alert(value +" "+reg );
		return reg.test(value);
	}, "Invalid DNS address");

    jQuery.validator.addMethod('validIP', function(value) {
    	if(value.length == 0) return true;
    	var split = value.split('.');
    	if(split.length != 4) return false;
    	for(var i = 0; i < split.length; i++) {
    		var s = split[i];
    		if(s=="" || s.length == 0 || isNaN(s) || s < 0 || s > 255) return false;
    	}
    	return true;
    }, ' Invalid IP Address');
    
	  jQuery.validator.addMethod('validPrimaryDnsIP', function(value) {
		   	var domainName=($('#dnsDomainAddress').val().trim());
		   	var primaryDNS=$("#dnsDomainPrimaryAddress").val().trim();
		   	
		   	if(primaryDNS.length!=0 && primaryDNS.endsWith(domainName)){
		   		if(value.length == 0) {
		   			return false;
		   		}
		   	}
		    return true;
	   }, ' Invalid Primary Dns IP Address');
	  
	   jQuery.validator.addMethod('validSecondaryDnsIP', function(value) {
		   	var domainName=($('#dnsDomainAddress').val().trim());
		   	var secondaryDNS=$("#dnsDomainSecondaryAddress").val().trim();
		   	
		   	if(secondaryDNS.length!=0 && secondaryDNS.endsWith(domainName)){
		   		if(value.length == 0) {
		   			return false;
		   		}
		   	}
		    return true;
	   }, ' Invalid Secondary DNS IP Address');

	   jQuery.validator.addMethod('validTertiaryDnsIP', function(value) {
		   	var domainName=($('#dnsDomainAddress').val().trim());
		   	var tertiaryDNS=$("#dnsDomainTertiaryAddress").val().trim();
		   	
		   	if(tertiaryDNS.length!=0 && tertiaryDNS.endsWith(domainName)){
		   		if(value.length == 0) {
		   			return false;
		   		}
		   	}
		    return true;
	   }, ' Invalid Tertiary DNS IP Address');

	   
    
    jQuery.validator.addMethod('validClientID', function(value) {
    	if(value != -1) return true;
    }, ' Client Name is Required');


	dnsAddForm.validate({
		errorElement: 'span', // default input error message container
		errorClass: 'help-block help-block-error', // default input error
													// message class
		focusInvalid: false, // do not focus the last invalid input
		ignore: ":disabled",  // validate all fields including form hidden
								// input
		errorPlacement: function(error, element) {
	 		error.insertAfter(element);
		},
		rules: {
			dnsDomainAddress: {
				minlength: 4,
	            required: true,
	            dnsNameChecker: true,/*
			    remote: {
		    		url: context+"VerifyDNSDomainWithAjax.do",
		    		type: "post",
		    		data: {
			        		name: function() {
			        			$( "#dnsDomainAddress" ).val();
		        			return true;
			        	}
		        	}
		    	}*/
			},
			dnsDomainClientID: {
				required: true,
				validClientID: true,
			},
			dnsDomainPrimaryDNS: {
				minlength: 4,
	            required: true,
	            dnsNameChecker: true
			},
			dnsDomainSecondaryDNS: {
				minlength: 4,
	            required: true,
	            dnsNameChecker: true
			},
			dnsDomainTertiaryDNS: {
				minlength: 4,
	            dnsNameChecker: true
			},
			dnsDomainPrimaryDnsIP: {
				validIP: true,
				validPrimaryDnsIP: true,
			},
			dnsDomainSecondaryDnsIP: {
				validIP: true,
				validSecondaryDnsIP: true,
			},
			dnsDomainTertiaryDnsIP: {
				validIP: true,
				validTertiaryDnsIP: true,
			},
			dnsDomainExpiryDate: {
				required: true,
			}
			
		},
		messages: {
		},
		invalidHandler: function (event, validator) { // display error alert
														// on form submit
	        success1.hide();
	        error1.show();
	        App.scrollTo(error1, -200);
	        // validator.errorList contains an array of objects, where each
			// object has properties "element" and "message". element is the
			// actual HTML Input.
	        for (var i=0;i<validator.errorList.length;i++){
	            console.log(validator.errorList[i]);
	        }
	
	        // validator.errorMap is an object mapping input names -> error
			// messages
	        for (var i in validator.errorMap) {
	        	console.log(i, ":", validator.errorMap[i]);
		    }
	    },
	
	    highlight: function (element) { // hightlight error inputs
	        $(element)
	            .closest('.form-group').addClass('has-error'); // set error
																// class to the
																// control group
	    },
	
	    unhighlight: function (element) { // revert the change done by
											// hightlight
	        $(element)
	            .closest('.form-group').removeClass('has-error'); // set error
																	// class to
																	// the
																	// control
																	// group
	    },
	
	    success: function (label) {
	        label
	            .closest('.form-group').removeClass('has-error'); // set
																	// success
																	// class to
																	// the
																	// control
																	// group
	    },
	
	    submitHandler: function (form) {
	        success1.show();
	        error1.hide();
	        form.submit();
	    }
		
	});
});