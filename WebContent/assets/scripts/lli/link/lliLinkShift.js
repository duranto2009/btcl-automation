//Dhrubo
$(document).ready(function() {
	var entityType=702;
	var moduleID=7;
	
	$("#uneditable input,#uneditable textarea").prop("disabled", true);

	$("#clientIDName").autocomplete({
		source: function(request, response) {
			var url = '../../AutoComplete.do?need=client&moduleID=7&status=active';
			ajax(url, {name : request.term}, response, "GET", [$("#clientIDName")]);
		},
		minLength: 1,
		select: function(e, ui) {
			$('#clientIDName').val(ui.item.data);
			$('#clientID').val(ui.item.id);
			
	        $("#clientHyperLink").attr("href","../../GetClientForView.do?moduleID="+moduleID+"&entityID="+ui.item.id);
	        $("#clientHyperLink").show();
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);   
	};
	
	function populateLliLink(data){
		$('input:radio[name="servicePurpose"][value="'+data.lliLinkDTO.ServicePurpose+'"]').prop("checked", true);
		$('input:radio[name="servicePurpose"][value="'+data.lliLinkDTO.ServicePurpose+'"]').trigger("click");
		
		$('input:radio[name="isMigrated"][value="'+data.lliLinkDTO.IsMigrated+'"]').prop("checked", true);
		$('input:radio[name="isMigrated"][value="'+data.lliLinkDTO.IsMigrated+'"]').trigger("click");
		
		$('#linkBandwidth').val(data.lliLinkDTO.LliBandwidth);
		
		$('#linkBandwidthType input:radio[value="'+data.lliLinkDTO.LliBandwidthType+'"]').prop("checked", true);
		$('#linkBandwidthType input:radio[value="'+data.lliLinkDTO.LliBandwidthType+'"]').trigger("click");
		
		
		$('#layerType input:radio[value="'+data.lliLinkDTO.LayerType+'"]').prop("checked", true);
		$('#layerType input:radio[value="'+data.lliLinkDTO.LayerType+'"]').trigger("click");
		
		$('input[name=lanCounter]').val(data.lliLinkDTO.LanCounter);
		
		$('#connectionType input:radio[value="'+data.lliLinkDTO.ConnectionType+'"]').prop("checked", true);
		$('#connectionType input:radio[value="'+data.lliLinkDTO.ConnectionType+'"]').trigger("click");
		
		$.uniform.update($('#uneditable input:radio'));
		
		$('#linkDescription').val(data.lliLinkDTO.LinkDescription);
		

		$("#farEndHidden").val(data.lliFarEndDTO.ID);
		$("#farEnd").val(data.lliFarEndDTO.Name);
		$('input[name=feEndPointID]').val(data.farEndDetails.LliEndPointID);
	 	$('input[name=feDistrictID]').val(data.farEndDetails.DistrictId);
        $("input[name=feDistrictStr]").val(data.farEndDetails.DistrictName);
        $("input[name=feUpazilaID]").val(data.farEndDetails.UpazilaId);
        $("input[name=feUpazilaStr]").val(data.farEndDetails.UpazilaName);
        $("input[name=feUnionID]").val(data.farEndDetails.UnionId);
        $("input[name=feUnionStr]").val(data.farEndDetails.UnionName);
        $("input[name=fePopID]").val(data.farEndDetails.PopID);
        $("input[name=fePopIdStr]").val(data.farEndDetails.PopName);
        $("textarea[name=feAddress]").val(data.farEndDetails.Address);
		$("#fePortType").val(data.farEndDetails.PortCategoryType);
		$("#feCoreType").val(data.farEndDetails.CoreType);
		$("#feOfcProvider").val(data.farEndDetails.OfcProviderTypeID);
		$("#feTerminalDeviceProvider").val(data.farEndDetails.TerminalDeviceProvider);
	}
	
	function populateNearEndList(data){
		var selectOptions = "";
		data.length>0 ? (
			selectOptions += "<option value='-1'>Select an Office</option>",
			$.each(data, function(index, value){
				selectOptions += "<option value='"+value.id+"'>"+value.name+"</option>";
			})
		)
		: selectOptions = "<option value='-1'>No Office is Available</option>";
			
		$("#neShiftMode3 select[name=neID]").html(selectOptions);
	}
	
	$("#lliLinkIDName").autocomplete({
		source: function(request, response) {
			ajax('../../LliAjax.do?mode=establishedLinkIDListByClientID', {partialName : request.term.trim(), clientID : $('#clientID').val(), completed : true}, response, "GET", [$(this)]);
		},
		minLength: 1,
		select: function(e, ui) {
			//callAjax('../../LliAjax.do?mode=linkDetailsByLliLinkID', {lliLinkID : ui.item.id}, populateLliLink, "GET");
			
			$("#lliLinkIDName").val(ui.item.name);
			$("#lliLinkID").val(ui.item.id);
			
	        $("#linkHyperlink").attr("href","../../LliLinkAction.do?entityID="+ui.item.id+"&entityTypeID="+ entityType);
	        $("#linkHyperlink").show();
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);   
	};
});