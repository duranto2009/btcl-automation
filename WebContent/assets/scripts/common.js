$(document).ready(function() {
    var placeholder = "Type to select";
    
    $(".select2").select2({
		width : '100%',
		allowClear : true,
		multiple : false,
		maximumSelectionSize : 1,
		placeholder : placeholder
    })
    /*
    toastr.options = {
		"closeButton" : false,
		"debug" : false,
		"positionClass" : "toast-top-right",
		"onclick" : null,
		"showDuration" : "1000",
		"hideDuration" : "1000",
		"timeOut" : "5000",
		"extendedTimeOut" : "1000",
		"showEasing" : "swing",
		"hideEasing" : "linear",
		"showMethod" : "fadeIn",
		"hideMethod" : "fadeOut"
    }
    */
    toastr.options = {
	  "closeButton": false,
	  "debug": false,
	  "newestOnTop": true,
	  "progressBar": false,
	  "positionClass": "toast-top-right",
	  "preventDuplicates": true,
	  "showDuration": "0",
	  "hideDuration": "1000",
	  "timeOut": "0",
	  "extendedTimeOut": "0",
	  "showEasing": "swing",
	  "hideEasing": "linear",
	  "showMethod": "fadeIn",
	  "hideMethod": "fadeOut"
	}
})