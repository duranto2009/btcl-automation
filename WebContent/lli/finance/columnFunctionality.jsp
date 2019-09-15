<script>
$(document).ready(function(){
	$('#btn-create-column').click(function(e) {
		e.preventDefault();
		var tableID = $('#btn-create-column').attr('data-table-id');
		$('#btn-create-column').removeAttr('data-table-id');
		var table = ('#costingTable-' + tableID);
		var thisTableColumnCount = $(table + ' thead td').length - 2;
		var dynamicHiddenField = ('.dynamic-hidden-field-col-' + tableID);
		var newLowerRange = parseInt($(dynamicHiddenField + ' > input[type="hidden"]:last-child').val()) + 1;
		if(isNaN(newLowerRange)){
			newLowerRange = 1;
		}
		var newUpperRange = $('#upperRangeCol').val();
		//
		if(newUpperRange!==""){
			if(newLowerRange > parseInt(newUpperRange)){
				$('.error-msg').show().html( 'Upper range should be greater than or equal to previous lower range(' + (newLowerRange-1) + ').' );
				return;
			}
			thisTableColumnCount ++;
			var colName = colHeading + ' '+thisTableColumnCount + '<br>(' + newLowerRange+ '-' + newUpperRange+ ' ' + colInnerUnit + ') '+ colOuterUnit;
			$(table+' thead tr>td:last').find('.col-edit').hide();
			
			$(table + ' tr').append($("<td>"));
			$(table + ' thead tr > td:last').html('<a  class="btn btn-xs btn-default col-edit" type="button" data-lower="' +newLowerRange+
					'" data-upper="'+newUpperRange +'"data-table-id="' +tableID+ '" accesskey="' +thisTableColumnCount+
					'"><i class="fa fa-pencil-square-o" style="color:#008d4c" aria-hidden="true"></i>&nbsp; Edit</a>' + colName);
			$(table + ' tbody tr').each( function() {
				$(this).children('td:last').append( $('<input type="text" name="cost" class="form-control">'))
			});

			$(dynamicHiddenField).append( '<input type=hidden value='+newLowerRange+' name=lowerRangeKm >');
			$(dynamicHiddenField).append( '<input type=hidden value='+newUpperRange+' name=upperRangeKm >');
			$('#col-add-modal').modal('hide');
		}else {
			alert('Enter your upper range');
		}

	});
	//Add Dynamic Column
	$('.col-add').click(function(e) {
		e.preventDefault();
		var tableID = $(this).data('table-id');
		$('#btn-create-column').attr('data-table-id', tableID);
		$('#col-add-modal').modal('show');
		$('.error-msg').hide()

	});

	//Remove Dynamic Column
	$('.col-remove').on('click',function() {
		var commonPrefix = '#costingTable-';
		var tableID = $(this).data('table-id');
		var table = commonPrefix + tableID;
		var lastTD = 'td:last';
		var lastHeader = table +' thead tr>' + lastTD; 
		var tableRows = table + ' tbody tr';
		var colEdit = '.col-edit';
		var dynamicHiddenFieldColumn = '.dynamic-hidden-field-col-' + tableID;  
		var thisColumnCount = $(table + ' thead td').length - 2; // 1 for SL and 1 for BW 
		if (thisColumnCount > 1) {
			$(lastHeader).remove();
			$(tableRows).each(function() {
				$(this).children(lastTD).remove();
			});
			$(lastHeader).find(colEdit).show();
			$(dynamicHiddenFieldColumn + " > input").slice(-2).remove();
			
		}
		return false;
	});

	//Edit Dynamic Column
	
	$('.table-edit').on('click','.col-edit', function(){
		var tableID = $(this).data('table-id');
		var columnNumber = $(this).attr('accesskey');
		var columnLowerRange = $(this).data('lower');
		
		$('#costingModalEditCol').modal('show');
		$('#col-edit-btn').attr('data-table-id', tableID);
		$('#col-edit-btn').attr('data-lower-range', columnLowerRange);
		
		$('.error-msg').hide();
		
	});
	$('#col-edit-btn').on('click', function(){
		var thisButton = $('#col-edit-btn');
		var tableID = $('#col-edit-btn').attr('data-table-id');
		$('#col-edit-btn').removeAttr('data-table-id');
		var table = '#costingTable-' + tableID;
		var columnLowerRange = parseInt($('#col-edit-btn').attr('data-lower-range'));
		$('#col-edit-btn').removeAttr('data-lower-range');
		var newUpperRange = $('#edit-upper-range').val();
		var table = '#costingTable-' + tableID;
		var thisTableColumnCount = $(table + ' thead td').length - 2;
		var dynamicHiddenField = ('.dynamic-hidden-field-col-' + tableID);
		var updateColumnName = colHeading + ' ' + thisTableColumnCount+ '<br>(' + columnLowerRange + '-' + newUpperRange+ ' '+colInnerUnit+') '+colOuterUnit;
		
		
		if(newUpperRange !==""){
			if(parseInt(newUpperRange) < columnLowerRange) {
				$('.error-msg').show().html( 'Upper range should be greater than previous lower range(' + columnLowerRange + ').');
				return false;
			}
			$(table+' thead tr > td:last').html( '<a  class="btn btn-xs btn-default col-edit" type="button" data-lower="'
					+columnLowerRange+'" data-upper="'+newUpperRange+'"data-table-id="' +tableID+'" accesskey="'+thisTableColumnCount+
					'"><i class="fa fa-pencil-square-o" style="color:#008d4c" aria-hidden="true"></i>&nbsp; Edit</a>' 
					+ updateColumnName);
			$(dynamicHiddenField + ' input[name="upperRangeKm"]:last-child').val(newUpperRange);
			$('#costingModalEditCol').modal('hide');
			
		}else {
			alert("Enter Upper Range");
		}
		return false;
	});
});
	
</script>