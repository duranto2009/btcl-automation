var domainTypes = [ ".com", ".co", ".edu", ".gov", ".info", ".net", ".org", ".ac", ".mil", ".sw", ".tv",".global", ".food", ".dhaka"];
var bdDomainExt=1;
var banglaDomainExt=2;
var tldMap={
		1:".bd",
		2:".বাংলা"
}
var mainFrom=$('#fileupload');
$(document).ready(function() {
	
	$("input[name=primaryDNS], input[name=secondaryDNS],input[name=tertiaryDNS]").blur(function(){
		var fullDomainName=($('#domainName').val().trim());
		if($(this).val().endsWith(fullDomainName)){
			if($(this).attr('name')=='primaryDNS'){
				$('.primary-dns-ip').removeClass('hidden');
			}else if ($(this).attr('name')=='secondaryDNS'){
				$('.secondary-dns-ip').removeClass('hidden');
			}else if($(this).attr('name')=='tertiaryDNS'){
				$('.tertiary-dns-ip').removeClass('hidden');
			}
		}
	});

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

   jQuery.validator.addMethod('validClientID', function(value) {
	   var id=$("input[name='domainClientID']").val();
	      if(id <=0) {
	         return false;
	      } else {
	         return true;
	      }
   }, ' invalid client id ');
   
   jQuery.validator.addMethod('isFileRequired', function(value) {
      var length = form1.find('input[name=documents]').length;
      if(length <= 0) {
         return false;
      } else {
         return true;
      }
   }, ' Please select a file');
   jQuery.validator.addMethod('minlengthBD', function(value) {
      var reg = /^[a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9]\.[a-zA-Z]{2,}$/;
      if(value.length < 6) {
         return false;
      } else {
         return true;
      }
   }, ' Invalid .bd Domain ');
   
   jQuery.validator.addMethod('secondLevelBD', function(value) {
 	  for (var i = 0; i < domainTypes.length; i++) {
 		  if(value.endsWith(domainTypes[i])){
 			  return true;
 		  }
 	  }
	 	  return false;
   }, ' Invalid second level domain. Please select the suggested second level domains ');
   
   jQuery.validator.addMethod('minlengthBangla', function(value) {
      if(value.length < 2) {
         return false;
      } else {
         return true;
      }
   }, ' Invalid .bd Domain ');
   jQuery.validator.addMethod('validDomainBD', function(value) {
      var reg = /^[a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9]\.[a-zA-Z]{2,}$/;
      return reg.test(value);
   }, ' Invalid .bd Domain ');
   
   jQuery.validator.addMethod('validDomainBangla', function(value) {
      value.replace("-", ""); // ingore - as unicode
      var result = true;
      for(var i = 0; i < value.length; i++) {
         if(value[i] == "-") {
            continue;
         }
         var code = value.charCodeAt(i);
         console.log("code: " + code + " char : " + value[i]);
         if((code!=0x098c)&&(code >= 0x0980 && code <= 0x09FF)) {
            result = true;
         } else {
            return false;
         }
      }
      console.log("domain name: "+ result);
      return result;
   }, ' Invalid .বাংলা  Domain');
   
   jQuery.validator.addMethod('validPrimaryDnsIP', function(value) {
	   	var domainName=($('#domainName').val().trim());
	   	var primaryDNS=$("input[name=primaryDNS]").val().trim();
	   	console.log("primary: "+ primaryDNS+", dom: "+ domainName);
	   	
	   	if(primaryDNS.length!=0 && primaryDNS.endsWith(domainName)){
	   		if(value.length == 0) {
	   			console.log(1)
	   			return false;
	   		}
	   	}
	    return true;
	    
   }, ' Invalid Primary Dns IP Address');
   
   jQuery.validator.addMethod('validSecondaryDnsIP', function(value) {
	   	var domainName=($('#domainName').val().trim());
	   	var secondaryDNS=$("input[name=primaryDNS]").val().trim();
	   	console.log("primary: "+ secondaryDNS+", dom: "+ domainName);
	   	
	   	if(secondaryDNS.length!=0 && secondaryDNS.endsWith(domainName)){
	   		if(value.length == 0) {
	   			console.log(1)
	   			return false;
	   		}
	   	}
	    return true;
	    
  }, ' Invalid Secondary Dns IP Address');
   
  jQuery.validator.addMethod('validTertiaryDnsIP', function(value) {
		var domainName=($('#domainName').val().trim());
	   	var tertiaryDNS=$("input[name=tertiaryDnsIP]").val().trim();
	   	console.log("tertiaryDNS: "+ tertiaryDNS+", dom: "+ domainName);
	   	
	   	if(tertiaryDNS.length!=0 && tertiaryDNS.endsWith(domainName)){
	   		if(value.length == 0) {
	   			return false;
	   		}
	   	}
	   	return true;
	    
   }, ' Invalid Tertiary Dns IP Address');
   
   jQuery.validator.addMethod("dnsNameChecker", function(value, element) { 
		if( value.indexOf(" ") < 0 && value == ""){
			 return true;
		 }
		var reg = systemConfig.getDnsNameRegExpr();
		return reg.test(value);
	}, "Invalid DNS address");
   
   var DomainFormValidation = function() {
      var handleValidation = function() {
         var form1 = $('#fileupload');
         var error1 = $('.alert-danger', form1);
         var success1 = $('.alert-success', form1);
         form1.validate({
            errorElement: 'span', // default input error message container
            errorClass: 'help-block help-block-error', // default input error message class
            focusInvalid: false, // do not focus the last invalid input  ignore: ":disabled",
            // validate all fields including form hidden input
            errorPlacement: function(error, element) {
              if(element.attr("name") == "domainName") {
                 $('.domain-validation-message').html("");
                 error.appendTo(element.closest("form").find('.domain-validation-message'));
                 element.addClass('has-error');
              } else  if(element.attr("name") == "10501") {
                  error.appendTo("#fileRequired");
              } else {
                 error.insertAfter(element)
              }
             
            },
            messages: {
            	"domainClientID":{
             	   required: "Please select a valid client"
                },
                "primaryDNS": {
                   required: "Please provide Primary DNS",
                    
                },
                "secondaryDNS": {
                   required: "Please provide Secondary DNS"
                },
                "tertiaryDNS": {
                    required: "Please provide tertiary DNS"
                },
                "primaryDnsIP": {
                   required: "Please provide Primary IP",
                   validPrimaryDnsIP:"IP address is required for this Primary DNS Name"  
                },
                "secondaryDnsIP": {
                   required: "Please provide Secondary IP",
                   validSecondaryDnsIP:"IP address is required for this Secondary DNS Name"  
                },
                "tertiaryDnsIP": {
                    required: "Please provide Primary IP",
                    validTertiaryDnsIP:"IP address is required for this tertiary DNS Name"  
                }
            },
            rules: {
            	"clientIdStr": {
                    minlength: 3,
                    required: true,
                    validClientID : true
                },
                "primaryDNS": {
                   minlength: 4,
                   required: true,
                   dnsNameChecker: true
                },
                "secondaryDNS": {
                   minlength: 4,
                   required: true,
                   dnsNameChecker: true
                },
                "tertiaryDNS": {
                    dnsNameChecker: true
                 },
                "primaryDnsIP": {
                   validIP: true,
                   validPrimaryDnsIP:true
                },
                "secondaryDnsIP": {
                   validIP: true,
                   validSecondaryDnsIP:true
                },
                "tertiaryDnsIP": {
                    validIP: true,
                    validTertiaryDnsIP:true
                }
                
            },
            invalidHandler: function(event, validator) { // display error  alert on form submit
               success1.hide();
               error1.show();
               App.scrollTo(error1, -200);
               $('#termsModal').modal('hide');
               
               for (var i=0;i<validator.errorList.length;i++){
                   console.log(validator.errorList[i]);
               }

               for (var i in validator.errorMap) {
                 console.log(i, ":", validator.errorMap[i]);
               }
            },
            highlight: function(element) { // hightlight error inputs
               $(element).closest('.form-group').addClass('has-error'); // set
            },
            unhighlight: function(element) { // revert the change done by hightlight
               $(element).closest('.form-group').removeClass('has-error'); // set
            },
            success: function(label) {
               label.closest('.form-group').removeClass('has-error'); // set $('.domain-validation-message').html("<span style='color: green'>Your domain name is valid</span>");
            },
            submitHandler: function(form) {
               success1.show();
               error1.hide();
               form.submit();
               console.log("submitted");
            }
         });
      }
      return {
         // main function to initiate the module
         init: function() {
        	 handleValidation();
         }
      };
   }();
   DomainFormValidation.init();
});