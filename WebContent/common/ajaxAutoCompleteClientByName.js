$(document).ready(function() {
	$("#clientIdStr").autocomplete({
		source : function(request, response) {
			$("#clientId").val(-1);
			var term = request.term;
			var url = context + 'AutoComplete.do?need=client&moduleID=' + $("#moduleID").val();
			var formData = {};
			formData['name'] = term;
			if (term.length >= 3) {
				callAjax(url, formData, response, "GET");
			} else {
				delay(function() {
					toastr.info("Your search name should be at lest 3 characters");
				}, systemConfig.getTypingDelay());
			}
		},
		minLength : 1,
		select : function(e, ui) {
			$('#clientIdStr').val(ui.item.data);
			$('#clientId').val(ui.item.id);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		/* console.log(item); */
		return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);
	};
});