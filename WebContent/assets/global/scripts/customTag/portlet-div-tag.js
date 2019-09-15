var portletDivPrototype = Object.create(HTMLElement.prototype);

portletDivPrototype.attachedCallback = function() {
	var portletBody = '<div class="portlet box portlet-btcl">' +
						'<div class="portlet-title"><div class="caption"><i class="'+ this.getAttribute('data-title-icon') +'" aria-hidden="true"></i>'+ this.getAttribute('data-title') +'</div></div>' +
						'<div class="portlet-body form">' +
						'<form class="form-horizontal" '+ 
						(this.getAttribute("data-form-id") ? "id="+this.getAttribute('data-form-id') : "") +
						' method="post" action="'+ this.getAttribute('data-action') +'">' +
						'<div class="form-body">' +
						this.innerHTML +
						'</div>' +
						'<div class="form-actions text-center"><button class="btn btn-reset-btcl" type="reset" >Cancel</button><button class="btn btn-submit-btcl" type="submit">Submit</button></div>' +
						'<form>' +
						'<div>' +
					'<div>';
	
	$(this).before(portletBody);
	$(this).remove();
};

var MyElement = document.registerElement('portlet-div', {
	prototype: portletDivPrototype
});