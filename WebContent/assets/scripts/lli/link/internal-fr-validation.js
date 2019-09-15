$(document).ready(function(){
	var validator;
	
    var FormValidation = function () {
		
    	jQuery.validator.addMethod("routerRequired", function(value, element) {
    		if($("input[name=mandatoryIpAvailableCount]").val()=='0' && $("textarea[name=mandatoryIpUnavailabilityReason]").val().length >0){
    			return true;
    		}
			var idElement = $("input[name=feRouterID]");
			if(idElement.val().length<=0){
				return false;
			}
    		return true;
		}, 'Router/Switch required');
    	jQuery.validator.addMethod("portTypeRequired", function(value, element) {
    		if($("input[name=mandatoryIpAvailableCount]").val()=='0' && $("textarea[name=mandatoryIpUnavailabilityReason]").val().length >0){
    			return true;
    		}
			var idElement = $("select[name=fePortType]");
			if(idElement.val()=='0'){
				return false;
			}
    		return true;
		}, 'Port Type is required');
    	jQuery.validator.addMethod("portRequired", function(value, element) {
    		if($("input[name=mandatoryIpAvailableCount]").val()=='0' && $("textarea[name=mandatoryIpUnavailabilityReason]").val().length >0){
    			return true;
    		}
			var idElement = $("select[name=fePortID]");
			if(idElement.val()=='0' || idElement.val() === null){
				toastr.error('Port Required');
				return false;
			}
    		return true;
		}, 'Port is required');
    	jQuery.validator.addMethod("mandatoryVlanRequired", function(value, element) {
			var idElement = $("input[name=feMandatoryVlanID]");
			if(idElement.val().length<=0){
				return false;
			}
    		return true;
		}, 'Mandatory VLAN is required');
    	
    	jQuery.validator.addMethod("mandatoryIpAvailableBlockIDRequired", function(value, element) {
    		return true;
    		if($("input[name=mandatoryIpAvailableCount]").val()=='0' && $("textarea[name=mandatoryIpUnavailabilityReason]").val().length >0){
    			return true;
    		}
			var idElement = $("select[name=mandatoryIpAvailableBlockID]");
			if(idElement.val()=='-1'){
				return false;
			}
    		return true;
		}, 'Mandatory IP Blocks is required');
    	jQuery.validator.addMethod("additionalIpAvailableBlockIDRequired", function(value, element) {
    		if($("#isAdditionalEmpty").val() == '0'){
    			return true;
    		}
    		if($("select[name=additionalIpAvailableCount]").val()=='0'){
    			return true;
    		}
			var idElement = $("select[name=additionalIpAvailableBlockID]");
			if(idElement.val()=='-1'){
				return false;
			}
    		return true;
		}, 'Additional IP Blocks is required');

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
	                     error.insertAfter(element.siblings().last());
	                },
	                messages: {
	                },
	                rules: {
	                	mandatoryIpUnavailabilityReason: {
	                		required: function(){
	                			if($("input[name=mandatoryIpAvailableCount]").val()=='0'){
	                				return true;
	                			}else{
	                				return false;
	                			}
	                		}
	                	},
	                	additionalIpUnavailabilityReason: {
	                		required: function(){
	                			if($("select[name=additionalIpAvailableCount]").val()=='0' && $("#isAdditionalEmpty").val() != '0'){
	                				return true;
	                			}else{
	                				return false;
	                			}
	                		}
	                	},
	                	neRouterStr: {
	                		routerRequired: true
	                	},
	                	neSwitchStr: {
	                		switchRequired: true
	                	},
	                	neCardStr: {
	                		cardRequired: true
	                	},
	                	feSlotStr: {
	                		slotRequired: true
	                	},
	                	fePortType: {
	                		portTypeRequired: true
	                	},
	                	fePortID: {
	                		portRequired: true
	                	},
	                	mandatoryIpAvailableBlockID: {
	                		mandatoryIpAvailableBlockIDRequired: true
	                	},
	                	additionalIpAvailableBlockID: {
	                		additionalIpAvailableBlockIDRequired: true
	                	},
	                	feMandatoryVLANComment: {
	                		required: function(){
	                			if($("input[name=feMandatoryVlanID]").val().length<=0){
	                				return true;
	                			}
	                    		return false;
	                		}
	                	}
	                	
	                },
	                invalidHandler: function (event, validator) {
	                    success1.hide();
	                    error1.show();
	                    App.scrollTo(error1, -200);
	                    
	                    //validator.errorMap is an object mapping input names -> error messages
	                    for (var i in validator.errorMap) {
	                      console.log(i, ":", validator.errorMap[i]);
	                    }
	                    
	                },
	                highlight: function (element) {
	                	if($(element).hasClass("inside-table")){
	                		$(element).closest('tr').addClass('has-error');
	                	}else{
	                		$(element).closest('.form-group').addClass('has-error');
	                	}
	                     
	                },
	                unhighlight: function (element) {
	                	if($(element).hasClass("inside-table")){
	                		$(element).closest('tr').removeClass('has-error');
	                	}else{
	                		$(element).closest('.form-group').removeClass('has-error');
	                	}
	                },
	                success: function (label) {
                		label.closest('tr').removeClass('has-error');
                		label.closest('.form-group').removeClass('has-error');
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

				jQuery.validator.addClassRules('mandatoryVlan', {
				   mandatoryVlanRequired: true
			    });
		    }
		};
    }();
    
    FormValidation.init();
});

