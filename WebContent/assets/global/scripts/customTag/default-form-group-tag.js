var defaultFormGroupPrototype = Object.create(HTMLElement.prototype);

defaultFormGroupPrototype.createdCallback = function() {
	var labelWidth = (this.getAttribute('data-width') === '3-6' ? 'col-sm-3' : 'col-sm-4');
	var inputWidth = (this.getAttribute('data-width') === '3-6' ? 'col-sm-6' : 'col-sm-8');
	var defaultFormGroupBody = '<div class="form-group">' +
								'<label class="' + labelWidth + ' control-label">'+ this.getAttribute("data-title") +
								((this.getAttribute("data-required")) ? '<span class="required" aria-required="true"> * </span>' : '') +
								'</label>' +
								//(this.getAttribute("data-fullwidth") ? '<div class=col-sm-8>' : '<div class=col-sm-6>') +
								'<div class=' + inputWidth + '>' +  
								this.innerHTML +
								'</div>' +
								'</div>';
	$(this).before(defaultFormGroupBody);
	$(this).remove();
};

var defaultFormGroup = document.registerElement('default-form-group', {
	prototype: defaultFormGroupPrototype
});