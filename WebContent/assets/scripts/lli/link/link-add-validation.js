$(document).ready(function(){
	var validator;
	
    var FormValidation = function () {
	    
    	jQuery.validator.addMethod( "validFiveYearBandwidth", function( value, element ){
    		
    		var val = $("input[name=connectionType]:checked").val();
    		   
    		if( val == fiveYearConnection ){
    			
    			var fiveYearBWStr = $("#fiveYearBandwidth").val();
    			
    			if( fiveYearBWStr == "" )
    				return false;
    			var fiveYearBW = parseInt( fiveYearBWStr );
    			var fiveYearBWType = $("input[name='fiveYearBandwidthType']:checked").val();
    			
    			var regularBWStr = $("#linkBandwidth").val();
    			var regularBW = parseInt( regularBWStr );
    			var regularBWType = $("input[name='linkBandwidthType']:checked").val();
    			
    			if( fiveYearBWType == bwGB )
    				fiveYearBW *= 1024;
    			
    			if( regularBWType == bwGB )
    				regularBW *= 1024;
    			
    			if( fiveYearBW > regularBW )
    				return false;
    		}
    		
    		return true;
    		
    	}, "Five year bandwidth should be less the or equal to bandwidth and greater then zero" );
    	
    	jQuery.validator.addMethod("validClient", function(value, element) {
    		if(($("#clientId")).val()<0){
    			return false;
    		}else{
    			return true;
    		}
		}, 'Select a valid client.');
		
		jQuery.validator.addMethod("validLocationStr", function(value, element) {
			var nameOfID = element.name;
			nameOfID = nameOfID.replace("Str", "ID");
			nameOfID = "input[name='"+nameOfID+"']";
			
			var idElement = $(''+nameOfID+'');
			if(idElement.val().length<=0){
				return false;
			}
    		return true;
		}, 'Valid location is required.');
		
		jQuery.validator.addMethod("validPopStr", function(value, element) {
			var nameOfID = element.name;
			nameOfID = nameOfID.replace("IdStr", "ID");
			nameOfID = "input[name='"+nameOfID+"']";
			
			var idElement = $(''+nameOfID+'');
			if(idElement.val().length<=0){
				return false;
			}
    		return true;
		}, 'Valid location is required.');
		jQuery.validator.addMethod("validEndName", function(value, element) {
			var nameOfID = element.name;
			nameOfID = nameOfID.replace("Name", "ID");
			nameOfID = "input[name='"+nameOfID+"']";
			
			var idElement = $(''+nameOfID+'');
			if(idElement.val().length<=0){
				return false;
			}
    		return true;
		}, 'Valid location is required.');
		
		
		jQuery.validator.addMethod("availableOfficeName", function(value, element) {
			if($("#officeNameAvailability").hasClass("unavailable")){
				return false;
			}
    		return true;
		}, 'This name is Not Available');
    

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
	                	if( element.siblings().length > 0 )
	                		error.insertAfter( element.siblings().last() );
	                	else
	                		error.insertAfter( element );
	                },
	                messages: {
	                	
	                	fiveYearBandwidth: {
	                		
	                		validFiveYearBandwidth: "Five year bandwidth should be less the or equal to bandwidth and greater then zero"
	                	}
	                },
	                rules: {
	                	clientIdStr: {
	                		validClient: true,
	                		required: true
	                	},
	                	feName: {
	                		availableOfficeName: true
	                	},
	                	fiveYearBandwidth: {
	                		
	                		validFiveYearBandwidth: true
	                	},
	                	
	                
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
				
				jQuery.validator.addClassRules('district', {
				    required: true,
				    validLocationStr: true,
			    });
				
			    jQuery.validator.addClassRules('upazila', {
				    required: true,
				    validLocationStr: true,
			    });
			   
			    jQuery.validator.addClassRules('endName', {
				    required: true,
			    });
			    
		    }
		};

    }();
    
    FormValidation.init();
});

