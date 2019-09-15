//Dhrubo
$(document).ready(function() {
	$("#uneditable input,#uneditable textarea").prop("disabled", true);

	$("#clientIDName").autocomplete({
		source: function(request, response) {
			var url = '../../AutoComplete.do?need=client&moduleID=6&status=active';
			//callAjax(url, {name : request.term}, response, "GET");
			ajax(url, {name : request.term}, response, "GET", [$("#clientIDName")]);
		},
		minLength: 1,
		select: function(e, ui) {
			$('#clientIDName').val(ui.item.data);
			$('#clientID').val(ui.item.id);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);   
	};
	
	function populateVpnLink(data){
		$('input:radio[name="servicePurpose"][value="'+data.vpnLinkDTO.ServicePurpose+'"]').prop("checked", true);
		$('input:radio[name="servicePurpose"][value="'+data.vpnLinkDTO.ServicePurpose+'"]').trigger("click");
		
		$('input:radio[name="isMigrated"][value="'+data.vpnLinkDTO.IsMigrated+'"]').prop("checked", true);
		$('input:radio[name="isMigrated"][value="'+data.vpnLinkDTO.IsMigrated+'"]').trigger("click");
		
		$('#linkBandwidth').val(data.vpnLinkDTO.VpnBandwidth);
		
		$('#linkBandwidthType input:radio[value="'+data.vpnLinkDTO.VpnBandwidthType+'"]').prop("checked", true);
		$('#linkBandwidthType input:radio[value="'+data.vpnLinkDTO.VpnBandwidthType+'"]').trigger("click");
		
		
		$('#layerType input:radio[value="'+data.vpnLinkDTO.LayerType+'"]').prop("checked", true);
		$('#layerType input:radio[value="'+data.vpnLinkDTO.LayerType+'"]').trigger("click");
		
		$('input[name=lanCounter]').val(data.vpnLinkDTO.LanCounter);
		
		$('#connectionType input:radio[value="'+data.vpnLinkDTO.ConnectionType+'"]').prop("checked", true);
		$('#connectionType input:radio[value="'+data.vpnLinkDTO.ConnectionType+'"]').trigger("click");
		
		$.uniform.update($('#uneditable input:radio'));
		
		$('#linkDescription').val(data.vpnLinkDTO.LinkDescription);
		
		
		//
		if(data.isNearEndEditable.isNearEndEditable == "false"){
			$("#nearFormInputs input, #nearFormInputs select, #nearFormInputs textarea").prop("disabled", true);
			$("#nearFormInputs").attr("title", "There are established Links using this Near End");
		}else{
			$("#nearFormInputs input, #nearFormInputs select, #nearFormInputs textarea").prop("disabled", false);
			$("#nearFormInputs").removeAttr("title");
		}

		//
		$("#nearEndHidden").val(data.vpnNearEndDTO.ID);
		$("#nearEnd").val(data.vpnNearEndDTO.Name);
		$('input[name=neEndPointID]').val(data.nearEndDetails.VpnEndPointID);
	 	$('input[name=neDistrictID]').val(data.nearEndDetails.DistrictId);
        $("input[name=neDistrictStr]").val(data.nearEndDetails.DistrictName);
        $("input[name=neUpazilaID]").val(data.nearEndDetails.UpazilaId);
        $("input[name=neUpazilaStr]").val(data.nearEndDetails.UpazilaName);
        $("input[name=neUnionID]").val(data.nearEndDetails.UnionId);
        $("input[name=neUnionStr]").val(data.nearEndDetails.UnionName);
        $("input[name=nePopID]").val(data.nearEndDetails.PopID);
        $("input[name=nePopIdStr]").val(data.nearEndDetails.PopName);
        $("textarea[name=neAddress]").val(data.nearEndDetails.Address);
		$("#nePortType").val(data.nearEndDetails.PortCategoryType);
		$("#neCoreType").val(data.nearEndDetails.CoreType);
		$("#neOfcProvider").val(data.nearEndDetails.OfcProviderTypeID);
		$("#neTerminalDeviceProvider").val(data.nearEndDetails.TerminalDeviceProvider);
		
		$("#farEndHidden").val(data.vpnFarEndDTO.ID);
		$("#farEnd").val(data.vpnFarEndDTO.Name);
		$('input[name=feEndPointID]').val(data.farEndDetails.VpnEndPointID);
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
	
	$("#vpnLinkIDName").autocomplete({
		source: function(request, response) {
			ajax('../../VpnAjax.do?mode=establishedLinkIDListByClientID', {partialName : request.term.trim(), clientID : $('#clientID').val(), completed : true}, response, "GET", [$(this)]);
		},
		minLength: 1,
		select: function(e, ui) {
			//callAjax('../../VpnAjax.do?mode=linkDetailsByVpnLinkID', {vpnLinkID : ui.item.id}, populateVpnLink, "GET");
			
			$("#vpnLinkIDName").val(ui.item.name);
			$("#vpnLinkID").val(ui.item.id);
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);   
	};
	
	var shiftModeName;
	$(".shiftMode").each(function(){
		$(this).on("change", function(){
			shiftModeName =  $(this).attr("name");
			
			$("#"+shiftModeName+"1").css("display", "none");
			$("#"+shiftModeName+"1").find("input").prop("disabled", true);
			$("#"+shiftModeName+"2").css("display", "none");
			$("#"+shiftModeName+"2").find("input, textarea, select").prop("disabled", true);
			if($("#"+shiftModeName+"3") != undefined){
				$("#"+shiftModeName+"3").css("display", "none");
				$("#"+shiftModeName+"3").find("select").prop("disabled", true);
			}
			
			if($(this).val() != 0){
				$("#"+shiftModeName+$(this).val()).css("display", "block");
				$("#"+shiftModeName+$(this).val()).find("input, textarea, select").prop("disabled", false);
				
				if($(this).val() == 3){
					if($("#clientID").val().trim().length==0){
						toastr.warning("Select Client");
					} else if($("#vpnLinkID").val().trim().length==0){
						toastr.warning("Select VPN Link");
					} else{
						ajax('../../VpnAjax.do?mode=availableNearEndListForShifting', {vpnLinkID : $("#vpnLinkID").val(), clientID : $('#clientID').val()}, populateNearEndList, "GET", [$("#neOfficeList")]);	
					}
					
				}
			}
		});
	});
   
   
});