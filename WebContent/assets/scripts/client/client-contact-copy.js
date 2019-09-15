$(document).ready(function() {
   var fullMobileNumber = $('input[name="registrantContactDetails.phoneNumber"]');
   /*
   $('#bill-checkbox-1').change(function() {
      $('#bill-checkbox-2').closest('span').removeClass("checked");
      $('#bill-checkbox-3').closest('span').removeClass("checked");
      if($(this).is(':checked')) {
         $(".regi").each(function(i, item) {
            if($(this).attr('name') == 'intlMobileNumber') {
               $(".bill:eq( " + i + " )").val(fullMobileNumber.val());
            } else {
               $(".bill:eq( " + i + " )").val($(item).val());
            }
         })
      } else {
         $(".bill").each(function(i, item) {
            $(".bill:eq( " + i + " )").val("");
         })
      }
   });
   $('#bill-checkbox-2').change(function() {
      $('#bill-checkbox-1').closest('span').removeClass("checked");
      $('#bill-checkbox-3').closest('span').removeClass("checked");
      if($(this).is(':checked')) {
         $(".admin").each(function(i, item) {
            $(".bill:eq( " + i + " )").val($(item).val());
         })
      } else {
         $(".bill").each(function(i, item) {
            $(".bill:eq( " + i + " )").val("");
         })
      }
   });
   $('#bill-checkbox-3').change(function() {
      $('#bill-checkbox-1').closest('span').removeClass("checked");
      $('#bill-checkbox-2').closest('span').removeClass("checked");
      if($(this).is(':checked')) {
         $(".technical").each(function(i, item) {
            $(".bill:eq( " + i + " )").val($(item).val());
         })
      } else {
         $(".bill").each(function(i, item) {
            $(".bill:eq( " + i + " )").val("");
         })
      }
   });
   $('#admin-checkbox-1').change(function() {
      $('#admin-checkbox-2').closest('span').removeClass("checked");
      $('#admin-checkbox-3').closest('span').removeClass("checked");
      if($(this).is(':checked')) {
         $(".regi").each(function(i, item) {
            if($(this).attr('name') == 'intlMobileNumber') {
               $(".admin:eq( " + i + " )").val(fullMobileNumber.val());
            } else {
               $(".admin:eq( " + i + " )").val($(item).val());
            }
         })
      } else {
         $(".admin").each(function(i, item) {
            $(".admin:eq( " + i + " )").val("");
         })
      }
   });
   $('#admin-checkbox-2').change(function() {
      $('#admin-checkbox-1').closest('span').removeClass("checked");
      $('#admin-checkbox-3').closest('span').removeClass("checked");
      if($(this).is(':checked')) {
         $(".bill").each(function(i, item) {
            $(".admin:eq( " + i + " )").val($(item).val());
         })
      } else {
         $(".admin").each(function(i, item) {
            $(".admin:eq( " + i + " )").val("");
         })
      }
   });
   $('#admin-checkbox-3').change(function() {
      $('#admin-checkbox-2').closest('span').removeClass("checked");
      $('#admin-checkbox-1').closest('span').removeClass("checked");
      if($(this).is(':checked')) {
         $(".technical").each(function(i, item) {
            $(".admin:eq( " + i + " )").val($(item).val());
         })
      } else {
         $(".admin").each(function(i, item) {
            $(".admin:eq( " + i + " )").val("");
         })
      }
   });
   $('#technical-checkbox-1').change(function() {
      $('#technical-checkbox-2').closest('span').removeClass("checked");
      $('#technical-checkbox-3').closest('span').removeClass("checked");
      if($(this).is(':checked')) {
         $(".regi").each(function(i, item) {
            if($(this).attr('name') == 'intlMobileNumber') {
               $(".technical:eq( " + i + " )").val(fullMobileNumber.val());
            } else {
               $(".technical:eq( " + i + " )").val($(item).val());
            }
         })
      } else {
         $(".technical").each(function(i, item) {
            $(".technical:eq( " + i + " )").val("");
         })
      }
   });
   $('#technical-checkbox-2').change(function() {
      $('#technical-checkbox-1').closest('span').removeClass("checked");
      $('#technical-checkbox-3').closest('span').removeClass("checked");
      if($(this).is(':checked')) {
         $(".bill").each(function(i, item) {
            $(".technical:eq( " + i + " )").val($(item).val());
         })
      } else {
         $(".technical").each(function(i, item) {
            $(".technical:eq( " + i + " )").val("");
         })
      }
   });
   $('#technical-checkbox-3').change(function() {
      $('#technical-checkbox-2').closest('span').removeClass("checked");
      $('#technical-checkbox-1').closest('span').removeClass("checked");
      if($(this).is(':checked')) {
         $(".admin").each(function(i, item) {
            $(".technical:eq( " + i + " )").val($(item).val());
         })
      } else {
         $(".technical").each(function(i, item) {
            $(".technical:eq( " + i + " )").val("");
         })
      }
   });
   */
   
   function copyContactInfo(classFrom, classTo){
	   $(classFrom).each(function(i, item) {
           if($(this).attr('name') == 'intlMobileNumber') {
              $(classTo+":eq( " + i + " )").val(fullMobileNumber.val());
           } else {
              $(classTo+":eq( " + i + " )").val($(item).val());
           }
        });
   }
   
   function resetContactInfo(contactType){
	   $(contactType).each(function(i, item) {
           $(contactType + ":eq( " + i + " )").val("");
        });
   }
   
   function copyAllContactInformationFromRegistrant(){
	   copyContactInfo(".regi", ".bill");
	   copyContactInfo(".regi", ".admin");
	   copyContactInfo(".regi", ".technical");
   }
   
   
   $('#copy-from-registrant-to-bill').click(function() {
	   copyContactInfo(".regi", ".bill");
   });
   $('#copy-from-admin-to-bill').click(function() {
	   copyContactInfo(".admin", ".bill");
   });
   $('#copy-from-technical-to-bill').click(function() {
	   copyContactInfo(".technical", ".bill");
   });
   $('#reset-bill').click(function() {
	   resetContactInfo(".bill");
   });
   
   $('#copy-from-registrant-to-admin').click(function() {
	   copyContactInfo(".regi", ".admin");
   });
   $('#copy-from-bill-to-admin').click(function() {
	   copyContactInfo(".bill", ".admin");
   });
   $('#copy-from-technical-to-admin').click(function() {
	   copyContactInfo(".technical", ".admin");
   });
   $('#reset-admin').click(function() {
	   resetContactInfo(".admin");
   });
   
   $('#copy-from-registrant-to-technical').click(function() {
	   copyContactInfo(".regi", ".technical");
   });
   $('#copy-from-bill-to-technical').click(function() {
	   copyContactInfo(".bill", ".technical");
   });
   $('#copy-from-admin-to-technical').click(function() {
	   copyContactInfo(".admin", ".technical");
   });
   $('#reset-technical').click(function() {
	   resetContactInfo(".technical");
   });
   
   
   $("#copyAllContactInformationFromRegi").on("click", function(){
		copyAllContactInformationFromRegistrant();
	});
})