$(document).ready(function() {
   var cache = {};
   var nearEndPoint=CONFIG.get('endPoint','near');
   var farEndPoint=CONFIG.get('endPoint','far');
   var entityType=CONFIG.get('entityType','link');
   var moduleID=CONFIG.get('module','vpn');
   
   $("#btn-link-add").on('click', function(e){
	   	e.preventDefault();
		
		if($('.files tr').length === 0 ){
			toastr.error('Documents must be given');
			return false;
		}
		$('.jFile').attr('disabled',true); 
		$('#fileupload').submit();
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
    	  ajax(url, formData, response, "GET", [$('#clientIdStr')]);
      },
      minLength: 1,
      select: function(e, ui) {
         $('#clientIdStr').val(ui.item.data);
         $('#clientId').val(ui.item.id);
         return false;
      },
   }).autocomplete("instance")._renderItem = function(ul, item) {
      return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);   
   };

   
   var currentAutoComplete;
   var categoryObj;
   var parentItemObj;
   $(".category-item").each(function() {
      $(this).autocomplete({
         source: function(request, response) {
            currentAutoComplete = this.element;
            categoryObj = this.element.next(".category-id");
            parentItemObj = $(this.element).closest(".up-down-path").prev(".up-down-path").find(".category-item");;
            // old// this.element.closest(".up-down-path").prevUntil(".up-down-path").find(".category-item");
            var map = {};
            map['parentItemID'] = parentItemObj.next().next().val(); 
            map['categoryID'] = categoryObj.val();
            if(categoryObj.val() == 4){
            	map['isParentNeeded'] = "false";
            }
            var url = '../../AutoInventoryItem.do?partialName=' + request.term;
            ajax(url, map, response, "GET", [$(currentAutoComplete)]);
            currentAutoComplete.closest(".form-group").nextAll(".form-group").find(".category-item").val("");
         	currentAutoComplete.closest(".form-group").find(".item-id").val("");
		    currentAutoComplete.closest(".form-group").nextAll(".form-group").find(".item-id").val("");
		    
         },
         minLength: 1,
         select: function(e, ui) {
            currentAutoComplete.attr('data-category-item-id', ui.item.ID);
            currentAutoComplete.val(ui.item.name);
            currentAutoComplete.next().next().val(ui.item.ID);
            
            toastr.success( ui.item.name + "  is selected");
            currentAutoComplete.closest(".up-down-path").nextAll(".up-down-path").find(".category-item").val("");
            currentAutoComplete.closest("up-down-path").nextAll(".up-down-path").find(".category-item").removeAttr('data-category-item-id');
            return false;
         },
      }).autocomplete("instance")._renderItem = function(ul, item) {
         return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
      }
   });
   var nearPorts;
   var farPorts;
   
   //$(".endName").each(function() { //autocomplete for both ends
   $("input[name=neName]").each(function() {
      var currentAutoComplete = "";
      $(this).autocomplete({
         source: function(request, response) {
        	 //d reset whilet typing on field
        	$("input[name=neID]").val("");
        	$("#nearFormInputs").find(".ne-hide").val("");
            $("#nearFormInputs").find(".ne-hide").attr("disabled", false);
            $("#nearEndHidden").val("");
            $("#nearFormInputs select").val('-1');
            //
        	
            var url = '../../FetchAutoLoadAction.do?name=' + request.term;
            var formData = {};
            formData['mode'] = "findEndByName";
            if(this.element.attr("id") == "nearEnd") {
               formData['endType'] =  nearEndPoint;
            } else {
               formData['endType'] = farEndPoint;
            }
            formData['clientID'] = $("#clientId").val();
            console.log($("#clientId").val());
            if(formData['clientID']>0){
            	currentAutoComplete = this.element;
                ajax(url, formData, response, "GET", $(currentAutoComplete))
            }else{
            	toastr.error("Please Select a Client");
            	return ;
            }
            // currentAutoComplete.val(0);
         },
         minLength: 1,
         change: function(event, ui) {
        	// d
            if(($(this).attr('name'))=="neName"){
            	currentAutoComplete = $("input[name=neName]");
            }else{
            	currentAutoComplete = $("input[name=feName]");
            }//
            if((currentAutoComplete!=null) && (typeof currentAutoComplete !='undefined')){
            	  var tempName = currentAutoComplete.val();
                  if(!ui.item && currentAutoComplete.attr("id") == "nearEnd") {
                     $("#nearFormInputs").find(".ne-hide").val("");
                     $("#nearFormInputs").find(".ne-hide").attr("disabled", false);
                     $("#nearEndHidden").val("");
                     $("#nearFormInputs select").val('-1');
                  } else if(!ui.item && currentAutoComplete.attr("id") == "farEnd") {
                     $("#farFormInputs").find(".fe-hide").val("");
                     $("#farFormInputs").find(".fe-hide").attr("disabled", false);
                     $("#farEndHidden").val("");
                     $("#farFormInputs select").val('-1');
                  }
                  currentAutoComplete.val(tempName);
            }
         },
         select: function(e, ui) {
            if(currentAutoComplete.attr("id") == "nearEnd") {
            	$("#nearFormInputs").find(".ne-hide").prop("disabled", true);
               currentAutoComplete.val(ui.item.vepName);
               $("#nearEndHidden").val(ui.item.ID);
               //$("#linkConnectionID").val(ui.item.connectionID);
               $('input[name=neDistrictID]').val(ui.item.districtId);
               $("input[name=neDistrictStr]").val(ui.item.districtName);
               $("input[name=neUpazilaID]").val(ui.item.upazilaId);
               $("input[name=neUpazilaStr]").val(ui.item.upazilaName);
               $("input[name=neUnionID]").val(ui.item.unionId);
               $("input[name=neUnionStr]").val(ui.item.unionName);
               $("input[name=nePopID]").val(ui.item.popID);
               $("input[name=nePopIdStr]").val(ui.item.popName);
               $("textarea[name=neAddress]").val(ui.item.address);
               $("#nePortType").val(ui.item.portCategoryType);
               $("#neCoreType").val(ui.item.coreType);
               $("#neFibreType").val(ui.item.vepFibreType);
               $("#neOfcProvider").val(ui.item.ofcProviderID);
               $("#feCoreType").val(ui.item.coreType);
               $("#feCoreType").attr("disabled", true);
               $('input[type=hidden][name=feCoreType]').val(ui.item.coreType);
               $("#neTerminalDeviceProvider").val(ui.item.terminalDeviceProvider);
               
               currentAutoComplete.attr("disabled", false);
               
            } else {
               currentAutoComplete.val(ui.item.vepName);
               $("#farEndHidden").val(ui.item.ID);
               $('input[name=feDistrictID]').val(ui.item.districtId);
               $("input[name=feDistrictStr]").val(ui.item.districtName);
               $("input[name=feUpazilaID]").val(ui.item.upazilaId);
               $("input[name=feUpazilaStr]").val(ui.item.upazilaName);
               $("input[name=feUnionID]").val(ui.item.unionId);
               $("input[name=feUnionStr]").val(ui.item.unionName);
               $("input[name=fePopID]").val(ui.item.popID);
               $("input[name=fePopIdStr]").val(ui.item.popName);
               $("textarea[name=feAddress]").val(ui.item.address);
               
               $("#fePortType").val(ui.item.portCategoryType);
               $("#feCoreType").val(ui.item.coreType);
               $("#farFormInputs").find(".fe-hide").attr("disabled", true);
               $("#feFibreType").val(ui.item.vepFibreType);
               $("#feOfcProvider").val(ui.item.ofcProviderID);
               $("#feTerminalDeviceProvider").val(ui.item.terminalDeviceProvider);
               
               currentAutoComplete.attr("disabled", false);
            }
            
            return false;
         },
      }).autocomplete("instance")._renderItem = function(ul, item) {
         return $("<li>").append("<a>" + item.vepName + "</a>").appendTo(ul);
      }
   });
});