$(document).ready(function(){
	
	var bdDomainExt=1;
	var banglaDomainExt=2;
	
	/*******domain search**********/
	$("#domainExt").change(function(e) {
	    e.preventDefault();
		$("#domainName").val("");
		if ($("#domainExt").val() == banglaDomainExt) {
		    $("[data-toggle='popover']").popover({
			    html: true, 
			    trigger: 'click',
				content: function() {
			          return $('#popover-content').html();
			    }
			});
		   $("[data-toggle='popover']").popover('show');
		}else{
		    $('#domainName').popover('destroy');
		}
		console.log($("#domainExt").val());
	});
	
	var domainTypes = [ ".com", ".co", ".edu", ".gov", ".info", ".net", ".org", ".ac", ".mil", ".sw", ".tv",".global", ".food", ".dhaka"];
	
	function handleAutocomplete(name) {
	var possibleNames = [];
		for (var i = 0; i < domainTypes.length; i++) {
			var item = {};
			item['name'] = name + domainTypes[i];
			possibleNames.push(item);
		}
		return possibleNames;
	}
	
	
	$("#domainName").autocomplete({
		minLength : 0,
		source : function(request, response) {
			var name = request.term;
			console.log(name);
			if (($("#domainExt").val() == bdDomainExt) /*&& name.endsWith(".")*/) {
				//$('.bangla-keyboard').hide();
				//if (name.indexOf(".") == (name.length - 1)) 
				{
					if (name.indexOf('.') == -1 && name != "" && name.indexOf(' ') == -1) {
						var data = handleAutocomplete(name); /* get answers from somewhere.. */
						return response(data);
					}else{
						return response();
					}
				}
			}
		},
		focus : function(event, ui) {
			$("#domainName").val(ui.item.name);
			return false;
		},

		select : function(event, ui) {
			$("#domainName").val(ui.item.name);
			return false;
		}
	}).autocomplete("instance")._renderItem = function(ul, item) {
		return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
	};
	

   jQuery.validator.addMethod('validIP', function(value) {
	  if(value.length == 0) return true;
      var split = value.split('.');
      if(split.length != 4) return false;
      for(var i = 0; i < split.length; i++) {
         var s = split[i];
         if(s==" " || s.length == 0 || isNaN(s) || s < 0 || s > 255) return false;
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
	   	var domainName=($('#domainName').val().trim())+tldMap[$('#domainExt').val()];
	   	var primaryDNS=$("input[name=primaryDNS]").val().trim();
	   	console.log("primary: "+ primaryDNS+", dom: "+ domainName);
	   	if(primaryDNS.length!=0 && primaryDNS.endsWith(domainName)){
	   		if(value.length == 0) {
	   			return false;
	   		}
	   	}
	   	return true;
	   }, ' Invalid Primary Dns IP  Address');
   
   jQuery.validator.addMethod('validSecondaryDnsIP', function(value) {
		var domainName=($('#domainName').val().trim())+tldMap[$('#domainExt').val()];
	   	var secondaryDNS=$("input[name=secondaryDNS]").val().trim();
	   	console.log("secondaryDNS: "+ secondaryDNS+", dom: "+ domainName);
	   	
	   	if(secondaryDNS.length!=0 && secondaryDNS.endsWith(domainName)){
	   		if(value.length == 0) {
	   			return false;
	   		}
	   	}
	   	return true;
	    
	   }, ' Invalid Secondary Dns IP  Address');
	var DomainFormValidation = function() {
		
      var handleValidation = function() {
         var form1 = $('#submit_for_check');
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
              }else {
                 error.insertAfter(element)
              }
             
            },
            messages: {
            	"domainClientID":{
             	   required: "Please select a valid client"
                },
               "domainName": {
                  required: "Please write a domain name",
                  minlength: "Domain name should be atleast {0} characters",
                  maxlength: "Domain name can’t be more than {0} characters",
                  validDomainBD: ".bd domain can only contain english digits(eg:1, 2, 3 ), hyphen (-) and english characters",
                  validDomainBangla: ".বাংলা domain can only contain  bangla digits(eg: ১,২,৩ ), hyphen (-) and bangla characters",
                  minlengthBD: ".bd domain must contain at least 6 characters",
                  minlengthBangla: ".বাংলা domain must contain atleast 2 characters",
               },
                "primaryDNS": {
                   required: "Please provide Primary DNS",
                    
                },
                "secondaryDNS": {
                   required: "Please provide Secondary DNS"
                },
                "primaryDnsIP": {
                   required: "Please provide Primary IP",
                   validPrimaryDnsIP:"IP address is required for this Primary DNS Name"  
                },
                "secondaryDnsIP": {
                   required: "Please provide Secondary IP",
                   validSecondaryDnsIP:"IP address is required for this Secondary DNS Name"  
                }
            },
            rules: {
            	"clientIdStr": {
                    minlength: 1,
                    required: true,
                    validClientID : true
               },
               "domainName": {
                  required: true,
                  minlengthBD: {
                     depends: function(element) {
                        return $('#domainExt').val() == bdDomainExt;
                     }
                  },
                  secondLevelBD:{
                	  depends: function(element) {
                          return $('#domainExt').val() == bdDomainExt;
                       }
                  },
                  minlengthBangla: {
                     depends: function(element) {
                        return $('#domainExt').val() == banglaDomainExt;
                     }
                  },
                  maxlength: 50,
                  validDomainBD: {
                     depends: function(element) {
                        return $('#domainExt').val() == bdDomainExt;
                     }
                  },
                  validDomainBangla: {
                     depends: function(element) {
                        return $('#domainExt').val() == banglaDomainExt;
                     }
                  }
               },
               "primaryDNS": {
                   minlength: 4,
                   required: false
                },
                "secondaryDNS": {
                   minlength: 4,
                   required: false
                },
                "primaryDnsIP": {
                   validIP: true,
                   validPrimaryDnsIP:true
                },
                "secondaryDnsIP": {
                   validIP: true,
                   validSecondaryDnsIP:true
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
            unhighlight: function(element) { // revert the change done by
               // hightlight
               $(element).closest('.form-group').removeClass('has-error'); // set
            },
            success: function(label) {
               label.closest('.form-group').removeClass('has-error'); // set
               //$('.domain-validation-message').html("<span style='color: green'>Your domain name is valid</span>");
            },
            submitHandler: function(form) {
               $('.jFile').attr('disabled', true);
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