$(document).ready(function(){
	var validator;
	
    var FormValidation = function () {
    	
    	jQuery.validator.addMethod("ruleOfStrID", function(value, element) {
			var nameOfID = element.name;
			nameOfID = nameOfID.replace("Str", "ID");
			nameOfID = "input[name='"+nameOfID+"']";
			
			var idElement = $(''+nameOfID+'');
			if(idElement.val().length<=0){
				return false;
			}
    		return true;
		}, 'This Field is Required');
    	
    	jQuery.validator.addMethod("ruleOfIdStrID", function(value, element) {
			var nameOfID = element.name;
			nameOfID = nameOfID.replace("IdStr", "ID");
			nameOfID = "input[name='"+nameOfID+"']";
			
			var idElement = $(''+nameOfID+'');
			if(idElement.val().length<=0){
				return false;
			}
    		return true;
		}, 'This Field is Required');
    	
    	
    	jQuery.validator.addMethod("selectValueNonzeroRequired", function(value, element) {
			if(value=='0'){
				return false;
			}
    		return true;
		}, 'Select a Valid Option');

    	jQuery.validator.addMethod("mandatoryVlanRequired", function(value, element) {
			var jQueryElement = $("input[name='"+element.id.replace("IdText", "ID")+"']");
			if(jQueryElement.val().length<=0){
				return false;
			}
    		return true;
		}, 'Mandatory VLAN is required');
    	
    	jQuery.validator.addMethod("neMandatoryAdditionalDisjoint", function(value, element) {
    		var neMandatoryVlanID = $("input[name=neMandatoryVlanID]").val();
    		var neAdditionalVlanValues = [];
    		$('select[name=neAdditionalVlanIDs] option').each(function(){
    			neAdditionalVlanValues.push($(this).val());
    		});
			$.each(neAdditionalVlanValues, function(index, neAdditionalVlanID){
				if(neAdditionalVlanID == neMandatoryVlanID){
					return false;
				}
			});
			return true;
		}, 'Mandatory VLAN cannot match Additional VLAN');
    	
    	jQuery.validator.addMethod("mandatoryVlanSame", function(value, element) {
    		if(($("#neMandatoryVlanTd").html().trim() == "-") || ($("#feMandatoryVlanTd").html().trim() == "-")){
    			return true;
    		} else if(($("#neMandatoryVlanTd").children().length == 0) && ($("#feMandatoryVlanTd").children().length == 0)){
    			return true;
    		} else if(($("#neMandatoryVlanTd").children().length == 0) && ($("#feMandatoryVlanTd").children().length > 0)){
    			if($("#neMandatoryVlanTd").html().trim() == $("#feMandatoryVlanIdText").val().trim()){
    				return true;
    			} return false;
    		} else if(($("#neMandatoryVlanTd").children().length > 0) && ($("#feMandatoryVlanTd").children().length == 0)){
    			if($("#neMandatoryVlanIdText").val().trim() == $("#feMandatoryVlanTd").html().trim()){
    				return true;
    			} return false;
    		} else{
    			if($("#neMandatoryVlanIdText").val().trim().length > 0){
	    			if($("#neMandatoryVlanIdText").val().trim() == $("#feMandatoryVlanIdText").val().trim()){
	    				return true;
	    			} return false;
    			} else{
    				return true;
    			}
    		}
    		
    		/*
    		if(currentMandatoryVlan == element.id){
    			if(element.id == "neMandatoryVlanIdText"){
        			$("#feMandatoryVlanIdText").trigger("keyup");
        		} else{
        			$("#neMandatoryVlanIdText").trigger("keyup");
        		}
    		}
    		*/
    	}, 'Local and Remote End must match');
    	
    	
    	
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
	                	bandWidthComment: {
	                		required: function(){
	                			if($("span .checked input[name=bandwidthAvailablity]").val()=='0'){
	                				return true;
	                			}
	                    		return false;
	                		}
	                	},
	                	nePopIdStr: {ruleOfIdStrID: true},
	                	fePopIdStr: {ruleOfIdStrID: true},
	                	neRouterStr: {ruleOfStrID: true},
	                	feRouterStr: {ruleOfStrID: true},
	                	nePortType: {selectValueNonzeroRequired: true},
	                	fePortType: {selectValueNonzeroRequired: true},
	                	nePortID: {selectValueNonzeroRequired: true},
	                	fePortID: {selectValueNonzeroRequired: true},
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

				jQuery.validator.addClassRules('neMandatoryVlan', {
					mandatoryVlanRequired: true,
					mandatoryVlanSame: true
			    });
				jQuery.validator.addClassRules('feMandatoryVlan', {
				   mandatoryVlanRequired: true,
				   mandatoryVlanSame: true
			    });
		    }
		};
    }();
    
    FormValidation.init();
});

