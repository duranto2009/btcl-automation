$(document).ready(function(){
	var currentAutoComplete;
	var categoryObj;
	var parentItemObj;
	$(".category-item").each(function() {
		$(this).autocomplete({
			source: function(request, response) {
            currentAutoComplete = this.element;
            categoryObj = this.element.next(".category-id");
            parentItemObj = $(this.element).closest(".up-down-path").prev(".up-down-path").find(".category-item");;
            var map = {};
            map['parentItemID'] = parentItemObj.next().next().val(); 
            map['categoryID'] = categoryObj.val();
            var url = '../../AutoInventoryItem.do?partialName=' + request.term;
            callAjax(url, map, response, "GET")
            
            currentAutoComplete.closest(".up-down-path").nextAll(".up-down-path").find(".category-item").val("");
            currentAutoComplete.closest("up-down-path").nextAll(".up-down-path").find(".category-item").removeAttr('data-category-item-id');
         },
         minLength: 1,
         select: function(e, ui) {
            currentAutoComplete.attr('data-category-item-id', ui.item.ID);
            currentAutoComplete.val(ui.item.name);
            currentAutoComplete.next().next().val(ui.item.ID);
            
            toastr.success( ui.item.name + "  is selected");
            return false;
         },
      }).autocomplete("instance")._renderItem = function(ul, item) {
         console.log(item);
         return $("<li>").append("<a>" + item.name + "</a>").appendTo(ul);
      }
   });
});
