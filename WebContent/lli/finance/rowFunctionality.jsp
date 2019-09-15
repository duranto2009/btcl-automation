<script>
	$(document).ready(function(){
		//Add Dynamic Row
		$('.row-add').on('click', function() {
			
			var tableID = $(this).attr('data-table-id');
			var table = '#costingTable-' + tableID;
			var dynamicHiddenField = '.dynamic-hidden-field-row-' + tableID;
			var rowCount = $(table + ' tbody tr').length;
			
			$('#btn-create-row').attr('data-table-id', tableID);
			$('#row-add-modal').modal('show');
			$('.error-msg').hide();
			
			return false;
		});
						

		$('#btn-create-row').on('click', function() {
			var tableID = $('#btn-create-row').attr('data-table-id');
			$('#btn-create-column').removeAttr('data-table-id');
			var table = ('#costingTable-' + tableID);
			var thisTableRowCount = $(table + ' tbody tr').length;
// 			var dynamicHiddenField = ('.dynamic-hidden-field-row-' + tableID);
// 			var dynamicHiddenFieldLastInput = dynamicHiddenField + ' input[type="hidden"]:last-child';
			
			
// 			var lastInputUpperValue = $(dynamicHiddenFieldLastInput).attr('data-upper');
			
			var newUpperRange = $('#upperRangeRow').val();
			var tbodyTRLast = table + ' tbody tr:last';
			var thisTableColumnAddBtn ='#div-render-' + tableID + ' .col-add';
			
			var lastInputUpperValue = $(tbodyTRLast + ' >td:nth-child(2) > input:last').val();
			var newLowerRange = parseInt(lastInputUpperValue) + 1;
			if(newUpperRange !== ""){
				if(parseInt(newUpperRange) < newLowerRange) {
					$('.error-msg').show().html( 'Upper range should be greater than  previous lower range('
							+ (newLowerRange-1) + ').');	
					return false;
				}
				
				thisTableRowCount++;
				if(thisTableRowCount === 1){
					var tableBody = $(table + ' tbody');
					var serial = '<td>'+1+'</td>';
					var range = createRange(thisTableRowCount, 1, newUpperRange);
					tableBody.html('<tr>'+serial+range+'</tr>');
				}else {
					var rowName = rowHeading + ' '+ '(' + newLowerRange + '-' + newUpperRange + ') ' + rowUnit;
					$(table + ' thead tr>td:last').find('.row-edit').hide(); // this line RETHINKING necessary no modal found for this to trigger;
					$(table + ' tbody').append($(table + " tbody tr:last").clone());
					$(tbodyTRLast + ' td input').val('');
					$(tbodyTRLast + ' >td:nth-child(1)').html(thisTableRowCount);
					
					$(tbodyTRLast + ' >td:nth-child(2) > input:nth-child(1)').val(newLowerRange);
					$(tbodyTRLast + ' >td:nth-child(2) > input:nth-child(3)').val(newUpperRange);
					$(tbodyTRLast + ' >td').each( function() {
						$(this).children('td').append( $('<input type="text" name="cost" class="form-control">')) 
					});
					
				}
				$('#row-add-modal').modal('hide');
				$(thisTableColumnAddBtn).prop('disabled',false);
			}else {
				alert('Enter your upper range');
			}
			return false;
		});
		//Remove Dynamic Row
		$('.row-remove').on('click', function() {
			var tableID = $(this).data('table-id');
			var table = '#costingTable-' + tableID;
			var lastRow = table +' tbody tr:last'; 
			var thisTableRowCount = $(table + ' tbody tr').length;
			var dynamicHiddenField = '.dynamic-hidden-field-row-' + tableID;  
			if (thisTableRowCount> 1) {
				$(lastRow).remove();
				$(dynamicHiddenField + ' > input').slice(-2).remove();
			}		
		});
		return false;
	});
	function createRange(rowCount, lowerRangeRow, upperRangeRow){
		var input1 = 'input type="text" class="form-control lower-range-mb" id="lowerRangeMb'+(rowCount-1)+'"';
		var input2 = 'input type="text" class="form-control upper-range-mb" id="upperRangeMb'+(rowCount-1)+'"';
		var style1 = ' style="width:35%;float:left"';
		var style2 = ' style="width:35%;float:right"';
		var name1 = ' name="lowerRangeMb"';
		var name2 = ' name="upperRangeMb"';
		var span = ' <span>-</span>';
		var valueLow = ' value="'+lowerRangeRow+'"';
		var valueHigh = ' value="'+upperRangeRow+'"';
		var range = '<td><'+  input1 + style1 + name1 + valueLow + '/>'+span+'<' + input2 + style2 + name2 + valueHigh + '/></td>';
		return range;
	}
</script>