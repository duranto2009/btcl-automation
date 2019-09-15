var clientAutocompletePrototype = Object.create(HTMLElement.prototype);

clientAutocompletePrototype.createdCallback = function() {
	this.innerHTML = '<input id="clientName" placeholder="Type to Select Client..." type="text" class="form-control" required>' +
 						'<input id="clientID" type="hidden" name="clientID" required>';
	
	
	var need, moduleID, status, callback;
	need = this.getAttribute("data-need");
	moduleID = this.getAttribute("data-module");
	status = (this.getAttribute("data-status")) ? '&status=active' : '';
	callback = this.getAttribute("data-selectCallback");
	
	$(document).ready(function(){
		$("#clientName").autocomplete({
			source: function(request, response) {
				var url = '../../AutoComplete.do?need='+ need +'&moduleID='+ moduleID + status;
				ajax(url, {name : request.term.trim()}, response, "GET", [$("#clientName")]);
			},
			minLength: 1,
			select: function(e, ui) {
				$('#clientName').val(ui.item.data);
				$('#clientID').val(ui.item.id);
				
				if(callback){
					window[callback](ui.item.id);
				}
				return false;
			},
		}).autocomplete("instance")._renderItem = function(ul, item) {
			return $("<li>").append("<a>" + item.data + "</a>").appendTo(ul);   
		};
	});
};

var clientAutocomplete = document.registerElement('client-autocomplete', {
	prototype: clientAutocompletePrototype
});