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
         callAjax(url, formData, response, "GET");
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
            map['isParentNeeded'] = 'false';
            var url = '../../AutoInventoryItem.do?partialName=' + request.term;
            callAjax(url, map, response, "GET");
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
            currentAutoComplete.closest(".up-down-path").nextAll(".up-down-path").find(".category-item").removeAttr('data-category-item-id');
            return false;
         },
      }).autocomplete("instance")._renderItem = function(ul, item) {
         return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
      }
   });
   
   
   $("input[name=connectionType]").on( "change", function(){
	   
	   var val = $("input[name=connectionType]:checked").val();
	   
	   val == fiveYearConnection ? $("#fiveYearBandwidthBlock").show() : $("#fiveYearBandwidthBlock").hide();
	   val == temporaryDateRange ? $("#temporaryDateRange").show() : $("#temporaryDateRange").hide();  
   });
});