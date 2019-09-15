$(document).ready(function() {
	
	$("input[name=feName]").keyup(function(){
		$("input[name=feID]").val("");
		$("#remoteAvailabilityStatus").find("p").html("");
		if($(this).val().trim().length>0){
			data = {};
			data.clientID = $("#clientID").val();
			data.officeName = $(this).val();
			data.mode="officeNameAvailability";
			if(data.clientID > 0){
				ajax(context+"LliAjax.do?", data, function(data){
					if(data==true){
						$("#remoteAvailabilityStatus").css("color", "green");
						$("#remoteAvailabilityStatus").find("p").html("Available");
					}else{
						$("#remoteAvailabilityStatus").css("color", "red");
						$("#remoteAvailabilityStatus").find("p").html("Not Available");
					}
				}, "GET", [$("#farEnd"), $("#remoteAvailabilityStatus")]);
			}else{
				toastr.error("Please Select a Client");
			}
		}else{
			$(this).val($(this).val().trim());
			$("#remoteAvailabilityStatus").find("p").html("");
		}
	});
	
});