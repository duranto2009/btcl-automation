$(document).ready(function(){
	var packageTypeValidation = function() {
   	// for more info visit the official plugin documentation: 
      // http://docs.jquery.com/Plugins/Validation
      var form1 = $('#fileupload');
      var error1 = $('.alert-danger', form1);
      var success1 = $('.alert-success', form1);

      form1.validate({
          errorElement: 'span', //default input error message container
          errorClass: 'help-block help-block-error', // default input error message class
          focusInvalid: false, // do not focus the last invalid input
          ignore: ":disabled",  // validate all fields including form hidden input
          errorPlacement: function(error, element) {
        	  if ( element.attr("name")=="regTypes" || element.attr('name')=="regiCategories" || element.attr("name")=="clientTypes") {
                  error.appendTo(element.closest(".col-md-6"));
                  element.addClass('has-error');
              } else {
                  error.insertAfter(element)
              }
          },
          messages: {
             
          },
          rules: {
        	  "packageName": {
                  required: true
              },
              "clientTypes":{
            	  required:false
              },
              "regTypes":{
            	  required:false
              },
              "regiCategories":{
            	  required: false
              }
          },
          invalidHandler: function (event, validator) { //display error alert on form submit              
              success1.hide();
              error1.show();
              App.scrollTo(error1, -200);
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
	}
	packageTypeValidation();
})
