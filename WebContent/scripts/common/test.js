$(document).ready(function() {
		
		$('#originatingDistrict').autocomplete({

			source : function(request, response) {
				
				$.ajax({
					url : '../AutoDistrict.do?name=' + request.term,
					data : "",
					dataType : "json",
					type : "POST",
					contentType : "application/json",
					success : function(data) {
						response(data);
					},
					error : function(response) {

					},
					failure : function(response) {

					}
				});
			},
			minLength : 1,
			select : function(e, ui) {

			},

		});
		
});