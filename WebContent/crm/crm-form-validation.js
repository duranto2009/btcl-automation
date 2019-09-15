$(document).ready(function(){

    var FormValidation = function () {
    	var validForm = true;
//    	jQuery.validator.addMethod("usernameRequiredIncaseOfNewClient", function (value, element) {
//    		if(!(($("#client-id-autocomplete").val())==-1)){
//    	        return true;
//    	    } else {
//    	        return jQuery.validator.methods.required.call(this, value, element);
//    	    }
//    	}, "This Field is Required");
    	
    	jQuery.validator.addMethod("validClient", function (value, element){
    		if($('#clientID').val() == -1){
    			return false;
    		}else {
    			return true;
    		}
    	}, "Select a valid client");
    	
		 
		var handleValidation1 = function() {
	            var form1 = $('#fileupload');
	            var error1 = $('.alert-danger', form1);
	            var success1 = $('.alert-success', form1);
	
	            form1.validate({
	                errorElement: 'span', //default input error message container
	                errorClass: 'help-block help-block-error', // default input error message class
	                focusInvalid: false, // do not focus the last invalid input
	                ignore: ":disabled",  // validate all fields including form hidden input
	                errorPlacement: function(error, element) {
	                	error.insertAfter(element);
	                },
	                messages: {
	                	
	                },
	                rules: {
	                    "clientIdStr": {
	                        validClient: true,
	                    	required: true
	                    },
	                   
	                    
	                    "clientComplain":{
	                    	required: true
	                    }
	                    	
	                    
	                },
	
	                invalidHandler: function (event, validator) { //display error alert on form submit              
	                    success1.hide();
	                    error1.show();
	                    App.scrollTo(error1, -200);
	                    for (var i=0;i<validator.errorList.length;i++){
	                        console.log(validator.errorList[i]);
	                    }
	                    for (var i in validator.errorMap) {
	                      console.log(i, ":", validator.errorMap[i]);
	                    }
	                },
	
	                highlight: function (element) { // hightlight error inputs
	                    $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
	                },
	
	                unhighlight: function (element) { // revert the change done by hightlight
	                    $(element).closest('.form-group').removeClass('has-error'); // set error class to the control group
	                },
	
	                success: function (label) {
	                    label.closest('.form-group').removeClass('has-error'); // set success class to the control group
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
//				jQuery.validator.addClassRules('bill-required', {
//				        required: true /*,  other rules */
//			    });
		    }
		};
    }();
    FormValidation.init();
});

