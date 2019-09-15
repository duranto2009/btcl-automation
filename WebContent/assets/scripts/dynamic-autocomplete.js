$(document).ready(function() {
	$("#clientIdStr").autocomplete({
		source : function(request, response) {
			$("#clientId").val(-1);
			var term = request.term;
			var url = '../../AutoComplete.do?moduleID=1&need=client';
			var formData = {};
			formData['name'] = term;
			callAjax(url, formData, response, "GET");
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
