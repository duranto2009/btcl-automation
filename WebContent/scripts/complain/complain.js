var fileAdd = function() {
    $('input[id=lefile]').change(function() {
	$('#attachment').val($(this).val());
    });
};

var replyStatusSelect = function() {
    $('#othersDiv').hide();
    $('#reply_status').on('change', function(e) {
	var selected = $("#reply_status option:selected").val();
	if (selected > 0) {
	    $('#othersDiv').show();
	}
    });
};