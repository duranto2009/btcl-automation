$(document).ready(function(){
	var FormValidation = function () {
		var handleValidation1 = function() {
	    	// for more info visit the official plugin documentation: 
	        // http://docs.jquery.com/Plugins/Validation
			$('.inventory-add-form').each(function( ) {
				var form1 = $(this);
		        var error1 = $('.alert-danger', form1);
		        var success1 = $('.alert-success', form1);
		        form1.validate({
		            errorElement: 'span', //default input error message container
		            errorClass: 'help-block help-block-error', // default input error message class
		            focusInvalid: false, // do not focus the last invalid input
		            ignore: ":disabled",  // validate all fields including form hidden input
		            errorPlacement: function(error, element) {
		            	 //error.insertAfter(element)
		            	error.appendTo(element.closest(".form-group").find('.error-div'));
		            	element.addClass('has-error');
		            },
		            messages: {
		                "itemName": {
		                    minlength: jQuery.validator.format("At least {0} characters needed")
		                }/*,
			            "partialName": {
		                    minlength: jQuery.validator.format("At least {0} characters needed")
		                }*/
		            },
		            rules: {
		                "itemName": {
		                    minlength: 2,
		                    required: true
		                }/*,
		                "partialName": {
		                    minlength: 2,
		                    required: true
		                }*/
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
		                form.submit();
		            }
		        });
		       
			});
		}
	
		return {
		    //main function to initiate the module
		    init: function () {
				handleValidation1();
		    }
		};
    }();
    
    FormValidation.init();
});