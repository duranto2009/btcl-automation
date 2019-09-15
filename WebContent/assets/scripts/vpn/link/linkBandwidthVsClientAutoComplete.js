//Dhrubo
$(document).ready(function() {
	var moduleID=CONFIG.get('module','vpn');
	var entityType=CONFIG.get('entityType','link');
	
	$("#clientIDName").autocomplete({
		source: function(request, response) {
			var url = '../../AutoComplete.do?need=client&moduleID=6&status=active';
			ajax(url, {name : request.term}, response, "GET", [$(this)]);
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
	
	function populateVpnLinkBandwidth(data){
		$("#currentBandwidth").html(data.bandwidth + " " + data.bandwidthType);
	}
	
	$("#vpnLinkIDName").autocomplete({
		source: function(request, response) {
			callAjax('../../VpnAjax.do?mode=establishedLinkIDListByClientID', {partialName : request.term, clientID : $('#clientID').val(), completed : true}, response, "GET");
			$("#linkHyperlink").closest('div').addClass("hidden");
		},
		minLength: 1,
		select: function(e, ui) {
			$("#currentBandwidth").addClass("ui-autocomplete-loading");
			ajax('../../VpnAjax.do?mode=bandwidthByVpnLinkID', {vpnLinkID : ui.item.id}, populateVpnLinkBandwidth, "GET", [$(this)]);
			
			$("#vpnLinkIDName").val(ui.item.name);
			$("input[name=linkID]").val(ui.item.id);
			
			$("#linkHyperlink").attr("href","../../VpnLinkAction.do?entityID="+ui.item.id+"&entityTypeID="+ entityType);
			$("#linkHyperlink").closest('div').removeClass("hidden");
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);   
	};
	
	$("#vpnDisabledLinkIDName").autocomplete({
		source: function(request, response) {
			callAjax('../../VpnAjax.do?mode=disabledLinkIDListByClientID', {partialName : request.term, clientID : $('#clientID').val(), completed : true}, response, "GET");
			$("#linkHyperlink").closest('div').addClass("hidden");
		},
		minLength: 1,
		select: function(e, ui) {
			$("#currentBandwidth").addClass("ui-autocomplete-loading");
			callAjax('../../VpnAjax.do?mode=bandwidthByVpnLinkID', {vpnLinkID : ui.item.id}, populateVpnLinkBandwidth, "GET");
			
			$("#vpnDisabledLinkIDName").val(ui.item.name);
			$("input[name=linkID]").val(ui.item.id);
			
			$("#linkHyperlink").attr("href","../../VpnLinkAction.do?entityID="+ui.item.id+"&entityTypeID="+ entityType);
			$("#linkHyperlink").closest('div').removeClass("hidden");
			return false;
		},
	}).autocomplete("instance")._renderItem = function(ul, item) {
		return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);   
	};
   
   
});


/*
$(document).ready(function() {
   var cache = {};
   var nearEndPoint=CONFIG.get('endPoint','near');
   var farEndPoint=CONFIG.get('endPoint','far');
   var entityType=CONFIG.get('entityType','link');
   var moduleID=CONFIG.get('module','vpn');
   
   $("#fileupload").submit(function(){
	$('.jFile').attr('disabled',true); 
	return true;
   });
   
   //client selection functionalities
   $("#clientIdStr").autocomplete({
      source: function(request, response) {
	  $("#clientId").val(-1);
         var term = request.term;
         var url = '../../AutoComplete.do?need=client&moduleID='+$("#moduleID").val()+"&status=active";
         var formData = {};
         formData['name']=term;
         callAjax(url, formData, response, "GET");
      },
      minLength: 1,
      select: function(e, ui) {
    	 clearSelectedData(true);
         $('#clientIdStr').val(ui.item.data);
         $('#clientId').val(ui.item.id);
         
         $("#clientHyperLink").closest('div').removeClass("hidden");
         $("#clientHyperLink").attr("href","../../GetClientForView.do?moduleID="+moduleID+"&entityID="+ui.item.id);
         
         return false;
      },
   }).autocomplete("instance")._renderItem = function(ul, item) {
      return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);   
   };

  
  $(".linkName").autocomplete({
     source: function(request, response) {
        var url = '../../FetchAutoLoadAction.do?linkName=' + request.term;
        var formData = {};
        formData['mode'] = "vpnLinkDetails";
        formData['clientID'] = $("#clientId").val();
        formData['status'] = 'active';
        
        console.log($("#clientId").val());
        if(formData['clientID']>0){
        	clearSelectedData(false);
            callAjax(url, formData, response, "GET");
        }else{
        	toastr.error("Please Select a Client");
        	return ;
        }
        // currentAutoComplete.val(0);
     },
     minLength: 1,
     select: function(e, ui) {
    	 $(".linkName").val(ui.item.data.linkName);
    	 $('input[name=linkID]').val(ui.item.data.ID);
    	 $('input[name=linkBandwidthCurrent]').val(ui.item.data.vpnBandwidth);
    	 $('input[name=bandwidthTypeCurrent]').val(ui.item.data.vpnBandwidthType);
         $("#nePortType").val(ui.item.endPoints.nearEnd.portCategoryType);
         $("#fePortType").val(ui.item.endPoints.farEnd.portCategoryType);
         
         /****for migration***
         $("input[name=totalDistance]").val(ui.item.data.vpnLoopDistance);
         $("input[name=neLoopDistance]").val(ui.item.endPoints.nearEnd.distanceFromNearestPopInMeter);
         $("input[name=feLoopDistance]").val(ui.item.endPoints.farEnd.distanceFromNearestPopInMeter);
         $("input[name=securityMoney]").val(ui.item.data.securityMoney);
         $("input[name=balance]").val(ui.item.data.balance);
         
         $("#linkHyperlink").closest('div').removeClass("hidden");
         $("#linkHyperlink").attr("href","../../VpnLinkAction.do?entityID="+ui.item.data.ID+"&entityTypeID="+ entityType);
         $('#updateBtn').attr('disabled', false);
         
         return false;
     },
  }).autocomplete("instance")._renderItem = function(ul, item) {
     return $("<li>").append("<a>" + item.data.linkName + "</a>").appendTo(ul);
  };
  function clearSelectedData(isLinkNameClear){
	  if(isLinkNameClear){
		  $(".linkName").val("");
	  }
 	  $('input[name=linkID]').val(0);
 	  $('input[name=linkBandwidthCurrent]').val("");
 	  $('input[name=bandwidthTypeCurrent]').val(0);
 	 
      $("#nePortType").val(0);
      $("#fePortType").val(0);
      
      $("#linkHyperlink").closest('div').addClass("hidden");
      $('#updateBtn').attr('disabled', true);
  }
});
*/