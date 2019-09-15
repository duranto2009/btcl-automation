$(document).ready(function() {
   var cache = {};
   var nearEndPoint=1;
   var farEndPoint=2;
   var entityType=602;
   var moduleID=6;
   function updateDistance(data) {
	   if (data[0]['distance']) {
		   $("#vpnDistance").val(data[0]['distance']);
	   }
   }
   $("#fileupload").submit(function(){
	$('.jFile').attr('disabled',true); 
	return true;
   });
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
         $('#clientID').val(ui.item.id);
         $("#requestToAccountID").val(ui.item.id);
         $("#clientHyperLink").val( context + "/GetClientForView.do?moduleID=6&entityID=" + ui.item.id );
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
    	 $('input[name=linkBandwidth]').val(ui.item.data.vpnBandwidth);
         $("#nePortType").val(ui.item.endPoints.nearEnd.portCategoryType);
         $("#fePortType").val(ui.item.endPoints.farEnd.portCategoryType);
         $("#entityID").val( ui.item.data.ID );
         $("#clientID").val( ui.item.data.clientID );
         $("#requestToAccountID").val( ui.item.data.clientID );
         $("#actionName").val( context + "VpnLinkAction.do?entityID=" + ui.item.data.ID + "&entityTypeID=602" );
         $("#linkHyperlink").closest('div').removeClass("hidden");
         $("#linkHyperlink").attr("href","../../VpnLinkAction.do?entityID="+ui.item.data.ID+"&entityTypeID="+ entityType);
         $('#updateBtn').attr('disabled', false);
         var param = {};
         param['method'] = "getDistanceByLinkID";
         param['id'] = ui.item.data.ID;
         callAjax( context + "/api/vpn/link.do" , param, updateDistance,"GET");
         return false;
     },
  }).autocomplete("instance")._renderItem = function(ul, item) {
     return $("<li>").append("<a>" + item.data.linkName + "</a>").appendTo(ul);
  };
  
  $(".linkNameMigration").autocomplete({
	     source: function(request, response) {
	        var url = '../../FetchAutoLoadAction.do?linkName=' + request.term;
	        var formData = {};
	        formData['mode'] = "vpnLinkDetails";
	        formData['clientID'] = $("#clientId").val();
	        formData['status'] = "active";
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
	    	 
	    	 //window.location = context + "/vpn/link/generateDemandNoteMigration.jsp?clientID=" + $("#clientId").val() + "&vpnLinkID=" + ui.item.data.ID;
	    	 
	    	 $(".linkNameMigration").val(ui.item.data.linkName);
	    	 $('input[name=linkID]').val(ui.item.data.ID);
	    	 $('input[name=linkBandwidth]').val(ui.item.data.vpnBandwidth);
	         $("#nePortType").val(ui.item.endPoints.nearEnd.portCategoryType);
	         $("#fePortType").val(ui.item.endPoints.farEnd.portCategoryType);
	         
	         $("#entityID").val( ui.item.data.ID );
	         $("#clientID").val( ui.item.data.clientID );
	         $("#requestToAccountID").val( ui.item.data.clientID );
	         $("#actionName").val( context + "VpnLinkAction.do?entityID=" + ui.item.data.ID + "&entityTypeID=602" );
	         
	         $("#linkHyperlink").closest('div').removeClass("hidden");
	         $("#linkHyperlink").attr("href","../../VpnLinkAction.do?entityID="+ui.item.data.ID+"&entityTypeID="+ entityType);
	         $('#updateBtn').attr('disabled', false);
	         
	         var param = {};
	         param['method'] = "getDistanceByLinkID";
	         param['id'] = ui.item.data.ID;
	         callAjax( context + "/api/vpn/link.do" , param, updateDistance,"GET");
	         
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
 	  $('input[name=linkBandwidth]').val("");
      $("#nePortType").val(0);
      $("#fePortType").val(0);
      
      $("#linkHyperlink").closest('div').addClass("hidden");
      $('#updateBtn').attr('disabled', true);
  }
  
  $("#ofcLayingCost_local").on("change keyup", function(){
		 
	  var val = $(this).val();
	  
	  if( val != "" ){
		  
		  var sixteenPercent = parseFloat( val)  * 0.16;
		  sixteenPercent = Math.round( sixteenPercent );
		  $("#establishmentCost_local").val( sixteenPercent );
	  }
	  else{
		  $("#establishmentCost_local").val( "0" );
	  }
  });
  
  $("#ofcLayingCost_remote").on("change keyup", function(){
		 
	  var val = $(this).val();
	  
	  if( val != "" ){
		  
		  var sixteenPercent = parseFloat( val)  * 0.16;
		  sixteenPercent = Math.round( sixteenPercent );
		  $("#establishmentCost_remote").val( sixteenPercent );
	  }
	  else{
		  $("#establishmentCost_remote").val( "0" );
	  }
  });
  
  $("#useMinOFCCharge").on( "change", function( e ){
	 
	  if( this.checked == true ){
		  
		  if( nearEndNotReused )
			  $("#localEndOFCCharge").val( "1000" );
		  
		  if( farEndNotReused )
			  $("#farEndOFCCharge").val( "1000" );
		  
		  if( nearEndNotReused && farEndNotReused )
			  $("#OFCoreCharge").val( "2000" );
		  else if( nearEndNotReused || farEndNotReused )
			  $("#OFCoreCharge").val( "1000" );
		  else if( !nearEndNotReused && !farEndNotReused )
			  $("#OFCoreCharge").val( "0" );
	  }
	  else{
		  
		  $("#localEndOFCCharge").val( localOFCChargeFull );
		  $("#farEndOFCCharge").val( remoteOFCChargeFull );
		  $("#OFCoreCharge").val( totalOFCChargeFull );
	  }
	  
	  updateSummary();
  });
  
  var bwCharge = $("#bwCharge").val();
  var existingSecurityCharge = $('#existingSecurityCharge').val();
  
  function updateSummary(){
	  var bwConnectionCharge = $("#bwConnectionCharge").val();
	  var ofcInstallationCharge = $("#ofcInstallationCharge").val();
	  var establishmentCost_local = Math.round( $("#ofcLayingCost_local").val() * 0.16 );
	  var establishmentCost_remote = Math.round( $("#ofcLayingCost_remote").val() * 0.16 );
	  var ofcLayingCost_local = $("#ofcLayingCost_local").val();
	  var ofcLayingCost_remote = $("#ofcLayingCost_remote").val();
	  var mediaConverterPiece = $("#mediaConverterPiece").val();
	  var mediaConverterPrice = $("#mediaConverterPrice").val();
	  var sfpModulePiece = $("#sfpModulePiece").val();
	  var sfpModulePrice = $("#sfpModulePrice").val();
	  var others = $("#others").val();
	  var securityCharge = $("#securityCharge").val();
	  
	  var localEndOFCCharge = $("#localEndOFCCharge").val();
	  var farEndOFCCharge = $("#farEndOFCCharge").val();
	  var OFCoreCharge = $("#OFCoreCharge").val();
	  
	  var noOfMonth = $("#month").val();
	  var additionalSecurityCharge = $('#additionalSecurityCharge').val();
	  var discount = $("#discount").val();
	  var discountPercentage = $("#discountPercentage").val();
	  
	  if( discountPercentage || discountPercentage === ""){
		  if(discountPercentage === ""){
			  discountPercentage = 0;
		  }
		  if(discountPercentage >= 0){
			  if( bwCharge ){
				  //console.log(bwCharge + " " + securityCharge + " " + OFCoreCharge+" " + (bwCharge+(securityCharge-OFCoreCharge)));
				  discount = (parseFloat(bwCharge)) * ( discountPercentage/100.0 );
				  discount = Math.round( discount );
				  $("#discount").val( discount );
				  $("#discount").disabled = true;
				  $("#securityCharge").val(parseFloat(bwCharge)-parseFloat(discount));
				  securityCharge = $('#securityCharge').val();
//				  $('#bwCharge').val(securityCharge);
				  $('#existingSecurityCharge').val(parseFloat(existingSecurityCharge) - parseFloat(existingSecurityCharge)*(discountPercentage/100.0));
			  	  $('#additionalSecurityCharge').val(parseFloat($('#securityCharge').val()) - parseFloat($('#existingSecurityCharge').val()));
			  }
		  }
	  }
	  if( !discountPercentage || discountPercentage == 0 || discountPercentage == "" ){
		  $("#discount").disabled = false;
	  }
	  var total = 0.0;
	  if( bwConnectionCharge )  total += parseFloat( bwConnectionCharge );
	  if( ofcInstallationCharge ) total += parseFloat( ofcInstallationCharge );
	  
	  if( establishmentCost_local ) total += parseFloat( establishmentCost_local );
	  if( establishmentCost_remote ) total += parseFloat( establishmentCost_remote );
	  
	  if( ofcLayingCost_local ) total += parseFloat( ofcLayingCost_local );
	  if( ofcLayingCost_remote ) total += parseFloat( ofcLayingCost_remote );
	  
	  if( mediaConverterPiece && mediaConverterPrice ) total += parseFloat( mediaConverterPiece * mediaConverterPrice );
	  if( sfpModulePiece && sfpModulePrice ) total += parseFloat( sfpModulePiece * sfpModulePrice );
	  
	  if( others ) total += parseFloat( others );
	  if( securityCharge ) {
		  total += parseFloat($('#additionalSecurityCharge').val());
	  }
	  
	  if( OFCoreCharge ) total += parseFloat( OFCoreCharge * noOfMonth );
	  if( bwCharge ) total += parseFloat( $('#bwCharge').val() * noOfMonth );
	  
	  var grandTotal = total;
	  var totalPayable = total;
	  if( discount )
		  totalPayable = parseFloat( total ) - parseFloat( discount ); 
	  
	  var vat = (totalPayable- parseFloat($('#additionalSecurityCharge').val()))*0.15;
	  $("#totalPayable").val( Math.round( totalPayable ) );
	  $("#grandTotal").val( Math.round(  grandTotal ) );
	  $("#vat").val(  Math.round( vat ) );
	  $("#netPayable").val(  Math.round( totalPayable + vat )  );
	  
  }
  
  $("#fileupload input").on( "input" , updateSummary);
  $("#discountPercentage").on( "change", updateSummary );
  updateSummary();
  
  var param = {};
  param['method'] = "getDistanceByLinkID";
  param['id'] = vpnLinkID;
  
  if( vpnLinkID != "null" )
	  callAjax( context + "/api/vpn/link.do" , param, updateDistance,"GET");
  
});