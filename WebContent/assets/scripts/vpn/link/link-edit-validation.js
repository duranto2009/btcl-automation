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
			if(idElement.val()<=0){
				return false;
			}
    		return true;
		}, 'Valid End Name is required.');
		
		
    

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
	                	/*
	                	if(element.parent().parent().hasClass("radio")){
	                		error.insertAfter(element.siblings().last());
	                	}else{
	                		error.insertAfter(element);
	                	}
	                	*/
	                	error.insertAfter(element.siblings().last());
	                },
	                messages: {
	                },
	                rules: {
	                	clientIdStr: {
	                		validClient: true,
	                		required: true
	                	},
	                	lanCounter: {
	                		required: function(element){
	                			if($(element).parent().parent().css('display') == 'none'){
	                				return false;
	                			}else{
	                				return true;
	                			}
	                		}
	                	},
	                	linkBandwidth: {
	                		required: true,
	                		minlength: 1
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
				
				jQuery.validator.addClassRules('endName', {
				    required: true,
			    });
				jQuery.validator.addClassRules('district', {
				    required: true,
				    validLocationStr: true,
			    });
			    jQuery.validator.addClassRules('upazila', {
				    required: true,
				    validLocationStr: true,
			    });
			    jQuery.validator.addClassRules('union', {
				    required: true,
				    validLocationStr: true,
			    });
			    jQuery.validator.addClassRules('coreType', {
			    	required: function(element){
            			if($(element).children("option").filter(":selected").val()=="-1"){
            				return true;
            			}else{
            				return false;
            			}
            		}
			    });
			    jQuery.validator.addClassRules('ofcProvider', {
			    	required: function(element){
            			if($(element).children("option").filter(":selected").val()=="-1"){
            				return true;
            			}else{
            				return false;
            			}
            		}
			    });
			    jQuery.validator.addClassRules('terminal-device-provider', {
			    	required: function(element){
            			if($(element).children("option").filter(":selected").val()=="-1"){
            				return true;
            			}else{
            				return false;
            			}
            		}
			    });
			   
				 
				/*
			   
			    $("form select").rules( "add", {
			    	selectRequired: true
			    });
			    */
			    
		    }
		};

    }();
    
    FormValidation.init();
    
    $("input[name='linkName']").keyup(function(){
    	//$("#linkID").val(-1);
    });
    $("input[name='clientIdStr']").keyup(function(){
    	$("input[name='clientID']").val(-1);
    });
    $("input[name='neName']").change(function(){
    	$("input[name='linkName']").val($("input[name='neName']").val()+" --> "+$("input[name='feName']").val());
    });
    $("input[name='feName']").change(function(){
    	$("input[name='linkName']").val($("input[name='neName']").val()+" --> "+$("input[name='feName']").val());
    });
});

