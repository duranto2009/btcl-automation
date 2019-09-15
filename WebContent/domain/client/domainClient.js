$(document).ready(function() {
	var moduleDomain = CONFIG.get('module','domain');
	$("#clientIdStr").autocomplete({
		source : function(request, response) {
			$("#clientId").val(-1);
			var term = request.term;
			var url = context + 'AutoComplete.do?moduleID=' + moduleDomain + '&need=client&status=active';
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
			if ($("#specialDomain").val() == 1) {
				updatePriceListBasedOnClient(ui.item.clientType, ui.item.registrantType);
			}
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		/* console.log(item); */
		return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);
	};

	function updatePriceListBasedOnClient(clientType, registrantType) {
		if (clientType == CONFIG.get('clientType','com') && (registrantType ==  CONFIG.get('domRegType','govt') 
				|| registrantType ==  CONFIG.get('domRegType','mil'))) {
			
			$("#secondaryDomainPackage").show();
			$("#primaryDomainPackage").hide();
		} else {
			$("#secondaryDomainPackage").hide();
			$("#primaryDomainPackage").show();
		}
	}
});