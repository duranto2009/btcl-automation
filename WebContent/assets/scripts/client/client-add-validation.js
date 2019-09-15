$(document).ready(function(){
	var govtCompanyType=CONFIG.get('domRegType','govt');
	var militaryCompanyType=CONFIG.get('domRegType','mil');
	var foreignCompanyType=CONFIG.get('domRegType','foreign');
	
	var clientTypeIndividual=CONFIG.get('clientType','ind');;
	var clientTypeCompany=CONFIG.get('clientType','com');;
	var allowedFileSize=5242880;
	
    var FormValidation = function () {
    	
    	jQuery.validator.addMethod("usernameRequiredIncaseOfNewClient", function (value, element) {
    		if(!(($("#client-id-autocomplete").val())==-1)){
    	        return true;
    	    } else {
    	        return jQuery.validator.methods.required.call(this, value, element);
    	    }
    	}, "This Field is Required");
    	
    	jQuery.validator.addMethod("noSpaceIncaseOfNewClient", function (value, element) {
    		if(!(($("#client-id-autocomplete").val())==-1)){
    	        return true;
    	    } else {
    	        return jQuery.validator.methods.noSpace.call(this, value, element);
    	    }
    	}, "No space please and don't leave it empty");
    	
    	jQuery.validator.addMethod("minlengthIncaseOfNewClient", function (value, element) {
    		if(!(($("#client-id-autocomplete").val())==-1)){
    	        return true;
    	    } else {
    	        if(value.length<3){
    	        	return false;
    	        }
    	        return true;
    	    }
    	}, "Must be atleast 3 characters long");
    	
    	jQuery.validator.addMethod("maxlengthIncaseOfNewClient", function (value, element) {
    		if(!(($("#client-id-autocomplete").val())==-1)){
    	        return true;
    	    } else {
    	        if(value.length>75){
    	        	return false;
    	        }
    	        return true;
    	    }
    	}, "Must be less than 75 characters long");
    	
    	
		jQuery.validator.addMethod("validUsername", function(value, element) {
			if(!(($("#client-id-autocomplete").val())==-1)){
				return true;
			}
			var reg = systemConfig.getUsernameRegExpr();
		    console.log("invalid username: "+ reg.test(value));
		    return reg.test(value);
		}, 'Invalid username. A valid user can contain characters, digits, dash(-), dot(.) and/or hyphen(_) ');
	    	
		jQuery.validator.addMethod("noSpace", function(value, element) { 
			  return value.indexOf(" ") < 0 && value != ""; 
			}, "");
		
		jQuery.validator.addMethod("urlChecker", function(value, element) { 
			if( value.indexOf(" ") < 0 && value == ""){
				 return true;
			 }
			var reg = systemConfig.getUrlRegExpr();
			return reg.test(value);
		}, "Invalid web address");
		
		jQuery.validator.addMethod('validNID', function(value) {
		      if(value.length ==13 || value.length==17 || value.length==10) {
		    	  return true;
		      } else {
		    	  console.log("Please select a valid NID");
		    	  return false;
		      }
		 }, ' Invalid NID Number. Must be 13, 17, or 10 digits');

        jQuery.validator.addMethod('validPassport', function(value) {
            if(value.length ==9) {
                return true;
            } else {
                console.log("Please select a valid Passport");
                return false;
            }
        }, ' Invalid Passport Number. Must be 9 digits');
		

		jQuery.validator.addMethod("validEmail", function(value, element) { 
			if( value.indexOf(" ") < 0 && value == ""){
				 return true;
			 }
			var reg = systemConfig.getEmailRegExpr();
			return reg.test(value);
		}, "Invalid email address");
		 
		 jQuery.validator.addMethod('validPhone', function(value) {
		      var reg = systemConfig.getMobileNumRegExpr();
		      console.log("mobile number validation: "+ reg.test(value));
		      return reg.test(value);
			  /*var phoneNumber=$('input[name="registrantContactDetails.phoneNumber"]').val();
			  return (phoneNumber.indexOf(" ") < 0 && phoneNumber != "");*/
		   }, ' Invalid mobile number ');
		 
		 jQuery.validator.addMethod('validCompanyType', function(value) {
			 var clientCategoryType=$("#clientCategoryType").val();
			 console.log("Client Type: "+ clientCategoryType);
		     if(value>0 || (clientCategoryType==clientTypeIndividual)){ // client type : individual
		    	 return true
		     }else{
		    	 console.log("Please select your company type");
		    	 return false;
		     }
		   }, ' Please select your company type ');
		 
		 jQuery.validator.addMethod('identityNoRequired', function(value) {
			 var selectedCompanyType=$('select[name="clientDetailsDTO.registrantType"]').val();
			 var clientCategoryType=$("#clientCategoryType").val();
		     if((clientCategoryType==clientTypeCompany) && ((selectedCompanyType==govtCompanyType) ||(selectedCompanyType==clientTypeIndividual) || (selectedCompanyType==foreignCompanyType))){
		    	 return true
		     }else if(value==''){
		    	 console.log('ID no is required '+value);
		    	 return false;
		     }else{
		    	 return true;
		     }
		     console.log("selectedCompanyType: "+ selectedCompanyType);
		   }, 'ID no is required ');
		 
		 jQuery.validator.addMethod('validDate', function(value) {
			 var dateData=value.split("/");;
			 var date1= new Date();
			 var date2= new Date(dateData[2], dateData[1] - 1, dateData[0]);
			 
		     if(date1-date2>(18*365*24*60*60*1000)){
		    	 return true
		     }else{
		    	 console.log(" Your age must be greater than 18 year");
		    	 return false;
		     }
		     
		   }, ' Your age must be greater than 18 year ');
		 
		 jQuery.validator.addMethod('fileSize', function(value) {
			 var documentSize=parseInt($('#documentSize').val());
			 if(documentSize>allowedFileSize ){
				 return false;
			 }
			 return true
		     
		   }, ' Maximum File Size 5MB ');
		 
		 jQuery.validator.addMethod('validTIN', function(value) {
			return value.match("[0-9]+") && (value.length == 10 || value.length == 12);
	 	}, ' TIN Number length must equal 10 or 12 digits');
		 
		 // basic validation// basic validation
		var handleValidation1 = function() {
	        // for more info visit the official plugin documentation: 
	            // http://docs.jquery.com/Plugins/Validation
	
	            var form1 = $('#fileupload');
	            var error1 = $('.alert-danger', form1);
	            var success1 = $('.alert-success', form1);
	
	            form1.validate({
	                errorElement: 'span', //default input error message container
	                errorClass: 'help-block help-block-error', // default input error message class
	                focusInvalid: false, // do not focus the last invalid input
	                ignore: ":disabled",  // validate all fields including form hidden input
	                errorPlacement: function(error, element) {
	                    if (element.attr("name") == "clientDetailsDTO.moduleIDs" || element.attr("name")=="clientDetailsDTO.regiCategories" 
	                    	|| element.attr('name')=='clientDetailsDTO.identityType' ||   element.attr('name')=='document' ) {
	                        error.appendTo(element.closest(".col-md-9"));
	                        element.addClass('has-error');
	                    }else {
	                        error.insertAfter(element)
	                    }
	                },
	                messages: {
	                    "registrantContactDetails.email": {
	                    	required: "We need your email address to contact you",
	                    	email: "Your email address must be in the format of name@domain.com"
	                    },
	                    "clientDetailsDTO.loginName": {
							required: "Please enter a username",
							minlength: "Your username must consist of at least {0} characters",
							noSpace: "No space please and don't leave it empty",
							remote: {
			                    url: "UsernameAvailability.do",
			                    type: "post",
			                    success: function(response) {
			                    	alert(response);
			                        return false;
			                    }
			                }
						},
						"clientDetailsDTO.loginPassword": {
							required: "Please provide a password",
							minlength: "Your password must be at least {0} characters long",
							noSpace: "No space please and don't leave it empty"
						},
						"clientDetailsDTO.registrantType": {
							validCompanyType: "Please select your company type",
						},
						"confirmLoginPassword": {
							required: "Please provide a password",
							minlength: "Your password must be at least {0} characters long",
							equalTo: "Please enter the same password as above",
							noSpace: "No space please and don't leave it empty"
						},
						"intlMobileNumber": {
							required: "Please provide a mobile number",
							validPhone: "Please provide a valid mobile number",
						}
	
	                },
	                rules: {
	                    "registrantContactDetails.registrantsName": {
	                        minlength: 2,
	                        required: true
	                    },
	                    "registrantContactDetails.email": {
	                        required: true,
	                        email: true,
	                        validEmail:true
	                    },
	                    "registrantContactDetails.webAddress": {
	                    	urlChecker: true
	                    },
	                    "intlMobileNumber": {
	                        required: true,
	                        validPhone: true
	                    },
	                    "registrantContactDetails.dateOfBirth": {
	                        required: true,
	                        validDate:true
	                    },
	                    "clientDetailsDTO.regiCategories":{
	                    	required:true
	                    },
	                    "clientDetailsDTO.registrantType":{
	                    	required: true,
	                    	validCompanyType:true
	                    },
	                    "clientDetailsDTO.identityType": {
	                        required: true
	                    },
	                    "clientDetailsDTO.identityNo": {
	                        identityNoRequired:true,
	                    },
	                    "registrantContactDetails.city": {
	                		required: true,
	                    },
	                    "registrantContactDetails.address": {
	                    	required: true,
	                    },
	                    "clientDetailsDTO.loginName": {
	                    	usernameRequiredIncaseOfNewClient: true,
	                    	minlengthIncaseOfNewClient: true,
	                    	noSpaceIncaseOfNewClient: true,
	            			validUsername: true
	                    },
	                    "clientDetailsDTO.loginPassword": {
	                    	usernameRequiredIncaseOfNewClient: true,
	                    	minlengthIncaseOfNewClient: true,
	                    	maxlengthIncaseOfNewClient: true,
	                    	noSpaceIncaseOfNewClient: true
			            },
	                    "confirmLoginPassword": {
	                    	usernameRequiredIncaseOfNewClient: true,
	                    	minlengthIncaseOfNewClient: true,
	                    	maxlengthIncaseOfNewClient: true,
	                    	noSpaceIncaseOfNewClient: true
	                    },
	                    "clientDetailsDTO.moduleIDs": {
	                        required: true
	                    },
	                    "answer": {
	                        required: true
	                    },

	                    'document':{
	                    	fileSize: true
	                    },
	                    ".bill": {
	                        required: true
	                    },
	                    "identityType_70103": {
	                    	validTIN : true
	                    },
	                    "identityType_dependent_60103_60104": {
	                    	validTIN: function(){
	                    		if (parseInt($("input[name=radio_dependent_60103_60104]:checked").val()) == 60103){
	                    			return true;
	                    		}
	                    	}
	                    },
                        "identityType_70101": {
                            validNID : true
                        },
                        "identityType_70102": {
                            validPassport: true
                        },
	                    "registrantContactDetails.landlineNumber": {
	                    	number: true
	                    }
	                    
	                },
	
	                invalidHandler: function (event, validator) { //display error alert on form submit              
	                    success1.hide();
	                    error1.show();
	                    App.scrollTo(error1, -200);
	                    //validator.errorList contains an array of objects, where each object has properties "element" and "message".  element is the actual HTML Input.
	                    for (var i=0;i<validator.errorList.length;i++){
	                        console.log(validator.errorList[i]);
	                    }
	
	                    //validator.errorMap is an object mapping input names -> error messages
	                    for (var i in validator.errorMap) {
	                      console.log(i, ":", validator.errorMap[i]);
	                    }
	                },
	
	                highlight: function (element) { // hightlight error inputs
	                    $(element)
	                        .closest('.form-group').addClass('has-error'); // set error class to the control group
	                },
	
	                unhighlight: function (element) { // revert the change done by hightlight
	                    $(element)
	                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
	                },
	
	                success: function (label) {
	                    label
	                        .closest('.form-group').removeClass('has-error'); // set success class to the control group
	                },
	
	                submitHandler: function (form) {
	                    success1.show();
	                    error1.hide();
	                    $('.jFile').attr('disabled',true); 
	                    form.submit();
	                }
	            });
	
	
		}
	
	  
		return {
		    init: function () {
				handleValidation1();
				jQuery.validator.addClassRules('bill-required', {
				        required: true /*,  other rules */
			    });
				jQuery.validator.addClassRules('admin-required', {
				        required: true /*,  other rules */
			    });
				jQuery.validator.addClassRules('technical-required', {
				        required: true /*, other rules */
			    });
		    }
		};

    }();
    FormValidation.init();
    
    //$("input[name='clientDetailsDTO.loginName']").attr('disabled','disabled');
    
    $('select[name="clientDetailsDTO.registrantType"]').change(function(){
    	console.log("changed");
    	$.uniform.update($('.identityTypeCom').attr('checked',false));
    	if(($(this).val()==govtCompanyType)|| ($(this).val()==militaryCompanyType) || ($(this).val()==foreignCompanyType)){
    		$('#regCompanyInfo input[name="clientDetailsDTO.identityNo"]').closest('.form-group').hide();
    		$('.identityTypeCom').closest('.radio-inline').hide();
    		$('#forwardingLetter').closest('.radio-inline').show();
    		$.uniform.update($('#forwardingLetter').attr('checked',true));
    	}else{
    		$('#regCompanyInfo input[name="clientDetailsDTO.identityNo"]').closest('.form-group').show();
    		$('.identityTypeCom').closest('.radio-inline').show();
    		$('#forwardingLetter').closest('.radio-inline').hide();
    		$.uniform.update($('#forwardingLetter').attr('checked', false));
    		$('.identityLabel').html('');
    	}
    });
});

