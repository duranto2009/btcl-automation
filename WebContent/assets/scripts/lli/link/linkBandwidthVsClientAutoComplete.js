//Alam, copied from drhubo
$(document).ready(function() {
	var moduleID=CONFIG.get('module','lli');
	var entityType=CONFIG.get('entityType','connection');
	
	$("#clientIDName").autocomplete({
		source: function(request, response) {
			var url = '../../AutoComplete.do?need=client&moduleID=7&status=active';
			callAjax(url, {name : request.term}, response, "GET");
			$("#clientHyperLink").closest('div').addClass("hidden");
		},
		minLength: 1,
		select: function(e, ui) {
			$('#clientIDName').val(ui.item.data);
			$('#clientID').val(ui.item.id);
			
			$("#clientHyperLink").closest('div').removeClass("hidden");
			$("#clientHyperLink").attr("href","../../GetClientForView.do?moduleID="+moduleID+"&entityID="+ui.item.id);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);   
	};
	
	function populateLliLinkBandwidth(data){
		
		$("#currentBandwidth").val( data.bandwidth + " " + data.bandwidthType );
	}
	
	function populateLliLinkPortType( data ){
		
		$("#portTypeCurrent").empty();
		var option = $( "<option>" + data["farEndDetails"]["PortCateogryTypeName"] + "</option>" );
		$("#portTypeCurrent").append( option );
	}
	
	$("#lliLinkIDName").autocomplete({
		source: function(request, response) {
			callAjax('../../LliAjax.do?mode=establishedLinkIDListByClientID', {partialName : request.term, clientID : $('#clientID').val(), completed : true}, response, "GET");
			$("#linkHyperlink").closest('div').addClass("hidden");
		},
		minLength: 1,
		select: function(e, ui) {
			$("#currentBandwidth").addClass("ui-autocomplete-loading");
			callAjax('../../LliAjax.do?mode=bandwidthByLliLinkID', {lliLinkID : ui.item.id}, populateLliLinkBandwidth, "GET");
			callAjax('../../LliAjax.do?mode=linkDetailsByLliLinkID', {lliLinkID : ui.item.id}, populateLliLinkPortType, "GET");
			
			$("#lliLinkIDName").val(ui.item.name);
			$("input[name=linkID]").val(ui.item.id);
			
			$("#linkHyperlink").attr("href","../../LliLinkAction.do?entityID="+ui.item.id+"&entityTypeID="+ entityType);
			$("#linkHyperlink").closest('div').removeClass("hidden");
			$('#updateBtn').attr('disabled', false);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		
		return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);   
	};
	
	$("#lliDisabledLinkIDName").autocomplete({
		source: function(request, response) {
			callAjax('../../LliAjax.do?mode=disabledLinkIDListByClientID', {partialName : request.term, clientID : $('#clientID').val(), completed : true}, response, "GET");
			$("#linkHyperlink").closest('div').addClass("hidden");
		},
		minLength: 1,
		select: function(e, ui) {
			$("#currentBandwidth").addClass("ui-autocomplete-loading");
			callAjax('../../LliAjax.do?mode=bandwidthByLliLinkID', {lliLinkID : ui.item.id}, populateLliLinkBandwidth, "GET");
			callAjax('../../LliAjax.do?mode=linkDetailsByLliLinkID', {lliLinkID : ui.item.id}, populateLliLinkPortType, "GET");
			
			$("#lliDisabledLinkIDName").val(ui.item.name);
			$("input[name=linkID]").val(ui.item.id);
			
			$("#linkHyperlink").attr("href","../../LliLinkAction.do?entityID="+ui.item.id+"&entityTypeID="+ entityType);
			$("#linkHyperlink").closest('div').removeClass("hidden");
			$('#updateBtn').attr('disabled', false);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		
		return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);   
	};
   
});