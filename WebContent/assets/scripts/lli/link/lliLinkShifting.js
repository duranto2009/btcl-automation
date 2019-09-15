$(document).ready(function() {
   var cache = {};
   var nearEndPoint=CONFIG.get('endPoint','near');
   var farEndPoint=CONFIG.get('endPoint','far');
   var entityType=CONFIG.get('entityType','link');
   var moduleID=CONFIG.get('module','vpn');
   

   var currentAutoComplete;
   var categoryObj;
   var parentItemObj;
   
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

   $(".category-item").each(function() {
      $(this).autocomplete({
         source: function(request, response) {
            currentAutoComplete = this.element;
            categoryObj = this.element.next(".category-id");
            parentItemObj = parentItemObj = $(this.element).closest(".up-down-path").prev(".up-down-path").find(".category-item");
            // this.element.closest(".up-down-path").prevUntil(".up-down-path").prev().find(".category-item");
            var map = {};
            map['parentItemID'] = parentItemObj.next().next().val();
            map['categoryID'] = categoryObj.val();
            var url = '../../AutoInventoryItem.do?partialName=' + request.term;
            callAjax(url, map, response, "GET")
         },
         minLength: 1,
         select: function(e, ui) {
        	$(".ports").val(0);
            
        	currentAutoComplete.attr('data-category-item-id', ui.item.ID);
            currentAutoComplete.val(ui.item.name);
            currentAutoComplete.next().next().val(ui.item.ID);
            
            toastr.success( ui.item.name + "  is selected");
            currentAutoComplete.closest(".up-down-path").nextAll(".up-down-path").find(".category-item").val("");
            currentAutoComplete.closest("up-down-path").nextAll(".up-down-path").find(".category-item").removeAttr('data-category-item-id');
            return false;
         },
      }).autocomplete("instance")._renderItem = function(ul, item) {
         console.log(item);
         return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
      }
   });
   
  $("input[name=endName]").autocomplete({
     source: function(request, response) {
        var url = '../../FetchAutoLoadAction.do?name=' + request.term;
        var formData = {};
        formData['mode'] = "findEndByName";
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
    	 
    	 $("#endPointID").val(ui.item.ID);
         $("#portType").val(ui.item.portCategoryType);
         $("#coreType").val(ui.item.coreType);
         
         $('input[name=districtID]').val(ui.item.districtId);
         $("input[name=districtStr]").val(ui.item.districtName);
         $("input[name=upazilaID]").val(ui.item.upazilaId);
         $("input[name=upazilaStr]").val(ui.item.upazilaName);
         $("input[name=unionID]").val(ui.item.unionId);
         $("input[name=unionStr]").val(ui.item.unionName);
         $("input[name=popID]").val(ui.item.popID);
         $("input[name=popIdStr]").val(ui.item.popName);
         $("textarea[name=address]").val(ui.item.address);
         $("#ofcProvider").val(ui.item.ofcProvider);
         $("#terminalDeviceProvider").val(ui.item.terminalDeviceProvider);
         
         $("#endPointHyperlink").closest('div').removeClass("hidden");
         $("#endPointHyperlink").attr("href","endPointPreview.jsp?endPointID="+ui.item.ID);
         $("input[name=endName]").val(ui.item.vepName);
         
         $('#updateBtn').attr('disabled', false);
         return false;
     },
  }).autocomplete("instance")._renderItem = function(ul, item) {
     return $("<li>").append("<a>" + item.vepName + "</a>").appendTo(ul);
  };
  function clearSelectedData(isEndPointNameClear){
	  if(isEndPointNameClear){
		  $("input[name=endName]").val("");
	  }
	  $("#endPointID").val("");
      $("#portType").val(0);
      
      $('input[name=districtID]').val("");
      $("input[name=districtStr]").val("");
      $("input[name=upazilaID]").val("");
      $("input[name=upazilaStr]").val("");
      $("input[name=unionID]").val("");
      $("input[name=unionStr]").val("");
      $("input[name=popID]").val("");
      $("input[name=popIdStr]").val("");
      
      $("textarea[name=address]").val("");
      $("input[name=ofcProvider]").val(0);
      
      $("#endPointHyperlink").closest('div').addClass("hidden");
      $('#updateBtn').attr('disabled', true);
  }
});