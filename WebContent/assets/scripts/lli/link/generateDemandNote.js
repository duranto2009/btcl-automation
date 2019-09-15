$(document).ready(function() {
   var cache = {};
   var nearEndPoint=1;
   var farEndPoint=2;
   var entityType=702;
   var moduleID=7;
  
   
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
         $('#clientID').val(ui.item.id);
         $("#requestToAccountID").val(ui.item.id);
         
         $("#clientHyperLink").val( context + "/GetClientForView.do?moduleID=7&entityID=" + ui.item.id );
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
        
        formData['mode'] = "lliLinkDetails";
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
    	 $('input[name=linkBandwidth]').val(ui.item.data.lliBandwidth);
         $("#fePortType").val(ui.item.endPoints.farEnd.portCategoryType);
         
         $("#entityID").val( ui.item.data.ID );
         $("#clientID").val( ui.item.data.clientID );
         $("#requestToAccountID").val( ui.item.data.clientID );
         $("#actionName").val( context + "LliLinkAction.do?entityID=" + ui.item.data.ID + "&entityTypeID=702" );
         
         $("#linkHyperlink").closest('div').removeClass("hidden");
         $("#linkHyperlink").attr("href","../../LliLinkAction.do?entityID="+ui.item.data.ID+"&entityTypeID="+ entityType);
         $('#updateBtn').attr('disabled', false);
         
         var param = {};
         param['method'] = "getDistanceByLinkID";
         param['id'] = ui.item.data.ID;
         callAjax( context + "/api/lli/link.do" , param, updateDistance,"GET");
         
         return false;
     },
  }).autocomplete("instance")._renderItem = function(ul, item) {
     return $("<li>").append("<a>" + item.data.linkName + "</a>").appendTo(ul);
  };
  
  $(".linkNameMigration").autocomplete({
	  
	     source: function(request, response) {
	        var url = '../../FetchAutoLoadAction.do?linkName=' + request.term;
	        
	        var formData = {};
	        formData['mode'] = "lliLinkDetails";
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
	    	 
	    	 //window.location = context + "/lli/link/generateDemandNoteMigration.jsp?clientID=" + $("#clientId").val() + "&lliLinkID=" + ui.item.data.ID;
	    	 
	    	 $(".linkNameMigration").val(ui.item.data.linkName);
	    	 $('input[name=linkID]').val(ui.item.data.ID);
	    	 $('input[name=linkBandwidth]').val(ui.item.data.lliBandwidth);
	         $("#fePortType").val(ui.item.endPoints.farEnd.portCategoryType);
	         
	         $("#entityID").val( ui.item.data.ID );
	         $("#clientID").val( ui.item.data.clientID );
	         $("#requestToAccountID").val( ui.item.data.clientID );
	         $("#actionName").val( context + "LliLinkAction.do?entityID=" + ui.item.data.ID + "&entityTypeID=702" );
	         
	         $("#linkHyperlink").closest('div').removeClass("hidden");
	         $("#linkHyperlink").attr("href","../../LliLinkAction.do?entityID="+ui.item.data.ID+"&entityTypeID="+ entityType);
	         $('#updateBtn').attr('disabled', false);
	         
	         $("#lliDistance").val( 0 );
	         
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
		  $("#establishmentCost_remote").val( sixteenPercent );
	  }
	  else{
		  $("#establishmentCost_remote").val( "0" );
	  }
  });
  
  $("#useMinOFCCharge").on( "change", function( e ){
		 
	  if( this.checked == true ){
		  
		  if( farEndNotReused )
			  $("#farEndOFCCharge").val( "1000" );
		  
		  if( farEndNotReused )
			  $("#OFCoreCharge").val( "1000" );
		  else
			  $("#OFCoreCharge").val( "0" );
	  }
	  else{
		  
		  $("#farEndOFCCharge").val( remoteOFCChargeFull );
		  $("#OFCoreCharge").val( totalOFCChargeFull );
	  }
	  
	  updateSummary();
  });
  
  
  $("#useMinOFCCharge").on( "change", function( e ){
		 
	  if( this.checked == true ){
		  
		  if( farEndNotReused )
			  $("#farEndOFCCharge").val( "1000" );
		  
		  if( farEndNotReused )
			  $("#OFCoreCharge").val( "1000" );
		  else
			  $("#OFCoreCharge").val( "0" );
	  }
	  else{
		  
		  $("#farEndOFCCharge").val( remoteOFCChargeFull );
		  $("#OFCoreCharge").val( totalOFCChargeFull );
	  }
	  
	  updateSummary();
  });
  
  function updateSummary(){
		 
	  var bwConnectionCharge = $("#bwConnectionCharge").val();
	  var ofcInstallationCharge = $("#ofcInstallationCharge").val();
	  var establishmentCost_local = $("#establishmentCost_local").val();
	  var establishmentCost_remote = $("#establishmentCost_remote").val();
	  var ofcLayingCost_local = $("#ofcLayingCost_local").val();
	  var ofcLayingCost_remote = $("#ofcLayingCost_remote").val();
	  var mediaConverterPiece = $("#mediaConverterPiece").val();
	  var mediaConverterPrice = $("#mediaConverterPrice").val();
	  var sfpModulePiece = $("#sfpModulePiece").val();
	  var sfpModulePrice = $("#sfpModulePrice").val();
	  var others = $("#others").val();
	  var securityCharge = $("#securityCharge").val();
	  var ipAddressCost = $("#ipAddressCost").val();
	  var numOfCore = $("#noOfOFCore").val();
	  
	  var bwUpgradationCost = $("#bwUpgradationCharge").val();
	  var additionalSecurityCharge = $("#additionalSecurityCharge").val();
	  
	  var farEndOFCCharge = $("#farEndOFCCharge").val();
	  var OFCoreCharge = $("#OFCoreCharge").val();
	  var bwCharge = $("#bwCharge").val();
	  var noOfMonth = $("#month").val();
	  
	  var discount = $("#discount").val();
	  
	  var discountPercentage = $("#discountPercentage").val();
	  if( discountPercentage || discountPercentage === ""){
		  if(discountPercentage === ""){
			  discountPercentage = 0;
		  }
		  if(discountPercentage >= 0){
			  if( bwCharge ){
				  
				  discount = bwCharge * ( discountPercentage/100.0 );
				  discount = Math.round( discount );
				  
				  $("#discount").val( discount );
				  $("#discount").disabled = true;
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
	  if( securityCharge && !additionalSecurityCharge ) total += parseFloat( securityCharge );
	  
	  if( OFCoreCharge && noOfMonth ) total += parseFloat( OFCoreCharge * noOfMonth );
	  
	  if( bwCharge && noOfMonth ) total += parseFloat( bwCharge * noOfMonth );
	  if( bwCharge && !noOfMonth ) total += parseFloat( bwCharge );
	  
	  if( bwUpgradationCost ) total += parseFloat( bwUpgradationCost );
	  if( additionalSecurityCharge ) total += parseFloat( additionalSecurityCharge );
	  
	  if( ipAddressCost ) total += parseFloat( ipAddressCost );
	  
	  var grandTotal = total;
	  
	  if( discount ) 
		  grandTotal = parseFloat( grandTotal ) - parseFloat( discount ); 
	  
	  var vat = ( grandTotal - securityCharge ) * 0.15;
	  
	  if( additionalSecurityCharge ) vat = ( grandTotal - additionalSecurityCharge ) * 0.15;
	  
	  $("#totalPayable").val( grandTotal );
	  $("#grandTotal").val( total );
	  $("#vat").val( vat );
	  $("#netPayable").val( grandTotal + vat );
  }
  
  $("#demandNoteForm input").on( "input" , updateSummary);
  $("#discountPercentage").on( "change", updateSummary );
  updateSummary();
});