$(document).ready(function(){
	var validator;
	
    var FormValidation = function () {
	    	
    	jQuery.validator.addMethod("validClient", function(value, element) {
    		if(($("#clientId")).val()<0){
    			return false;
    		}else{
    			return true;
    		}
		}, 'Select a valid client.');
    	
		jQuery.validator.addMethod("validLink", function(value, element) {
    		if(($("#linkID")).val()<=0){
    			return false;
    		}else{
    			return true;
    		}
		}, 'Select a valid client.');
		
		jQuery.validator.addMethod("selectRequired", function(value, element) {
			console.log(value+" selected value");
			if(value<=0){
				return false;
			}
			return true;
			
		}, 'Select a Port');
		
		jQuery.validator.addMethod("newBandwidth", function(value, element) {
			if(($("#newBandwidth")).val()<=0){
				return false;
			}
			return true;
			
		}, 'Select a positive value');
    	
		
		
		
		var handleValidation1 = function() {
	            var form1 = $('#fileupload');
	            var error1 = $('.alert-danger', form1);
	            var success1 = $('.alert-success', form1);
	
	            validator = form1.validate({
	                errorElement: 'span',
	                errorClass: 'help-block help-block-error',
	                focusInvalid: false,
	                ignore: ":disabled",
	                errorPlacement: function(error, element) {
	                     error.insertAfter(element);
	                },
	                messages: {
	                },
	                rules: {
	                	clientIdStr: {
	                		validClient: true,
	                		required: true
	                	},
	                	linkName: {
	                		required: true,
	                		validLink: true
	                	},
	                	newBandwidth:{
	                		required : true,
	                		newBandwidth: true
	                	}
	                
	                },
	                invalidHandler: function (event, validator) {
	                    success1.hide();
	                    error1.show();
	                    App.scrollTo(error1, -200);
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
			    
			    jQuery.validator.addClassRules('ports', {
				    required: true,
				    selectRequired: true
			    });
				 
	   /*
			    $("select").rules( "add", {
			    	
			    });
		*/
			    
		    }
		};

    }();
    
    FormValidation.init();
    
    $("input[name='linkName']").keyup(function(){
    	$("#linkID").val(-1);
    });
    $("input[name='clientIdStr']").keyup(function(){
    	$("input[name='clientID']").val(-1);
    	$("#linkID").val(-1);
    	$("input[name='linkName']").val("");
    });
    
});

