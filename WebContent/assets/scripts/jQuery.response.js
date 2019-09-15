jQuery(document).ready(function() {
    jQuery.extend({
		getResponse : function(url, params, callback) {
		    var result = null;
			    $.ajax({
				url : url,
				type : 'get',
				data : params,
				dataType : 'html',
				async : false,
				success : function(data) {
				    callback(data);
				}
		    });
		    return result;
		}
    });
});