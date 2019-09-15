var domainTypes = [ ".com", ".co", ".edu", ".gov", ".info", ".net", ".org", ".ac", ".mil", ".sw", ".tv",".global", ".food", ".dhaka"];
var bdDomainExt=1;
var banglaDomainExt=2;
var tldMap={
		1:".bd",
		2:".বাংলা"
}

$(document).ready(function() {
	
	$("#dnsForm").on("keyup", "#dnsDomainPrimaryAddress, #dnsDomainSecondaryAddress, #dnsDomainTertiaryAddress", function(){
		var fullDomainName=($('#dnsDomainAddress').val().trim());
		if($(this).val().endsWith(fullDomainName)){
			if($(this).attr('id')=='dnsDomainPrimaryAddress'){
				if(($('#primaryIpDiv').hasClass("hidden"))){
					$('#primaryIpDiv').removeClass('hidden');
				}
			}else if ($(this).attr('id')=='dnsDomainSecondaryAddress'){
				if(($('#secondaryIpDiv').hasClass("hidden"))){
					$('#secondaryIpDiv').removeClass('hidden');
				}
			}else if($(this).attr('id')=='dnsDomainTertiaryAddress'){
				if(($('#tertiaryIpDiv').hasClass("hidden"))){
					$('#tertiaryIpDiv').removeClass('hidden');
				}
			}
		}else{
			if($(this).attr('id')=='dnsDomainPrimaryAddress'){
				if(!($('#primaryIpDiv').hasClass("hidden"))){
					$('#primaryIpDiv').addClass('hidden');
				}
			}else if ($(this).attr('id')=='dnsDomainSecondaryAddress'){
				if(!($('#secondaryIpDiv').hasClass("hidden"))){
					$('#secondaryIpDiv').addClass('hidden');
				}
			}else if($(this).attr('id')=='dnsDomainTertiaryAddress'){
				if(!($('#tertiaryIpDiv').hasClass("hidden"))){
					$('#tertiaryIpDiv').addClass('hidden');
				}
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
	   var id=$("#dnsDomainClientID").val();
	      if(id <=0) {
	         return false;
	      } else {
	         return true;
	      }
   }, ' Invalid Client ID ');
   


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
   
   jQuery.validator.addMethod("dnsNameChecker", function(value, element) { 
		if( value.indexOf(" ") < 0 && value == ""){
			 return true;
		 }
		var reg = systemConfig.getDnsNameRegExpr();
		return reg.test(value);
	}, "Invalid DNS address");
   
	jQuery.validator.addMethod("wwwDNS", function(value, element) { 
		if (value.match("^www")) {
		   return false;
		}
		return true;
	}, "Do not start with www");
	
	jQuery.validator.addMethod("isAvailable", function(value, element) { 
		if ($("#isAvailable").val()==-1) {			
		   return false;
		}
		return true;
	}, "Select Valid Domain First");
   
   var DomainFormValidation = function() {
      var handleValidation = function() {
         var form1 = $('#dnsForm');
         var error1 = $('.alert-danger', form1);
         var success1 = $('.alert-success', form1);
         form1.validate({
            errorElement: 'span', // default input error message container
            errorClass: 'help-block help-block-error', // default input error message class
            focusInvalid: false, // do not focus the last invalid input  ignore: ":disabled",
            // validate all fields including form hidden input
            errorPlacement: function(error, element) {
                 error.insertAfter(element)
            },
            messages: {
            	dnsDomainAddress: {
				    remote: "Domain is not available."
				},
            },
            rules: {
            	dnsDomainAddress: {
					minlength: 4,
		            required: true,
		            dnsNameChecker: true,
		            wwwDNS: true,
		            
//				    remote: {
//			    		url: context+"VerifyDNSDomainWithAjax.do",
//			    		type: "post",
//			    		data: {
//				        		name: function() {
//				        			return $( "#dnsDomainAddress" ).val();
//				        	}
//			        	}	        	
//			    	}
				},
				clientIdStr: {
					required: true,
					minlength: 3,
					validClientID: true,
					isAvailable: true,
				},
				
				dnsDomainPrimaryDNS: {
					minlength: 4,
		            required: true,
		            dnsNameChecker: true,
		            wwwDNS: true,
				},
				dnsDomainSecondaryDNS: {
					minlength: 4,
		            required: true,
		            dnsNameChecker: true,
		            wwwDNS: true,
				},
				dnsDomainTertiaryDNS: {
					minlength: 4,
		            dnsNameChecker: true,
		            wwwDNS: true,
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
            invalidHandler: function(event, validator) { // display error  alert on form submit
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