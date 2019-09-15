<script type="text/javascript">
	function setCost() {
		var f = document.forms[1];

		var e = f.iigBANDWIDTH;

		if (e.options[e.selectedIndex].value == '0') {
			f.iigCost.value = '960';
		} else if (e.options[e.selectedIndex].value == '1') {
			f.iigCost.value = '720';
		} else if (e.options[e.selectedIndex].value == '2') {
			f.iigCost.value = '660';
		} else if (e.options[e.selectedIndex].value == '3') {
			f.iigCost.value = '600';
		} else if (e.options[e.selectedIndex].value == '4') {
			f.iigCost.value = '540';
		} else if (e.options[e.selectedIndex].value == '5') {
			f.iigCost.value = '460';
		} else if (e.options[e.selectedIndex].value == '6') {
			f.iigCost.value = '400';
		}

	}

	$(document)
			.ready(
					function() {
						var lowerRangeCol =<%=lastUpperValue%>;
						if(lowerRangeCol == -1){
							$('#addCol').prop('disabled',true);
						}
						
						var rowHeading = '<%=moduleUnitList[0]%>';
						var rowUnit = '<%=moduleUnitList[1]%>';
						var colHeading = '<%=moduleUnitList[2]%>';
						var colInnerUnit = '<%=moduleUnitList[3]%>';
						var colOuterUnit = '<%=moduleUnitList[4]%>';
						var clicked = false;
						$('#ip_submenu').hide();
						$('.ip-address').click(function() {
							if (clicked == false) {
								$(this).css('padding-right', '0');
								$('#ip_submenu').show();
								clicked = true;
							} else {
								$(this).css('padding-right', '15px');
								$('#ip_submenu').hide();
								clicked = false;
							}

						});
						var rowCount =<%=rowList.size()%>;

						//Add Dynamic Row
						$('#addRow').click(function(e) {
// 							e.preventDefault();
// 							rowCount++;
// 							$('#costingTable tbody').append($("#costingTable tbody tr:last").clone());
// 							$("#costingTable tbody tr:last td input").val('');
// 							$('#costingTable tbody tr:last>td:first').html(rowCount);


							e.preventDefault();
							$('#costingModalRow').modal('show');
							$('.error-msg').hide();
							$('#addCol').prop('disabled',false);
						});
						
// 						R@!h@n
						
						var lowerRangeRow =<%=lastUpperValueRow%>;
						var prevUpper = 0;
						$('.range-btn-row')
								.click(
										function(e) {
											
											
// 											var table = $('#costingTable tbody>tr').html();
											e.preventDefault();
											var upperRangeRow = $('#upperRangeRow').val();
											
											if (lowerRangeRow && upperRangeRow) {
												if(lowerRangeRow == -1){
													rowCount++;
													var tableBody = $('#costingTable tbody');
													var serial = '<td>'+(rowCount)+'</td>';
													var range = createRange(rowCount, 1, upperRangeRow);
													tableBody.html('<tr>'+serial+range+'</tr>');
													lowerRangeRow = upperRangeRow;
													lowerRangeRow++;
													$('#costingModalRow').modal('hide');
													
												}
												
												
												else if (upperRangeRow >= lowerRangeRow) {
													rowCount++;
													var rowName = rowHeading + ' '+ '(' + lowerRangeRow + '-' + upperRangeRow + ') ' + rowUnit;
													$('#costingTable thead tr>td:last').find('.row-edit').hide();
													$('#costingTable tbody').append($("#costingTable tbody tr:last").clone());
													$("#costingTable tbody tr:last td input").val('');
													$('#costingTable tbody tr:last>td:nth-child(1)').html(rowCount);
													
													$('#costingTable tbody tr:last>td:nth-child(2) > input:nth-child(1)').val(lowerRangeRow);
													$('#costingTable tbody tr:last>td:nth-child(2) > input:nth-child(3)').val(upperRangeRow);
// 													$('#costingTable tbody tr:last>td:first')
// 															.html(
// 																	'<a  class="btn btn-xs btn-default row-edit" type="button" data-lower="'+lowerRangeRow+'" data-upper="'+upperRangeRow+'" accesskey="'+rowCount+'"><i class="fa fa-pencil-square-o" style="color:#008d4c" aria-hidden="true"></i>&nbsp; Edit</a><br>'
// 																			+ rowName);
													$('#costingTable tbody tr:last>td').each(
															function() {
																$(this).children('td').append(
																		$('<input type="text" name="cost" class="form-control">'))
															});
// 													$(".dynamic-hidden-field-row")
// 															.append(
// 																	'<input type="hidden" value="'+lowerRangeRow+'" name="lowerRangeMb" id="lower'+rowCount+'">');
// 													$(".dynamic-hidden-field-row")
// 															.append(
// 																	'<input type="hidden" value="'+upperRangeRow+'" name="upperRangeMb" id="upper'+rowCount+'">');

													lowerRangeRow = upperRangeRow;
													lowerRangeRow++;
													$('#costingModalRow').modal('hide');
												} else {

													$('.error-msg').show().html(
															'Upper range should be greater than or equal to previous lower range(' + lowerRangeRow
																	+ ').');

												}

											} else {
												alert('Enter your range.');
											}
										});


// 						/R@!h@n							
						
						
						
						
						
						

						//Remove Dynamic Row
						$('.row-remove').click(function(e) {
// 							if (rowCount > 1) {
// 								$("#costingTable tbody tr:last").remove();
// 								rowCount--;
// 							}
							
							
							e.preventDefault();
							if (rowCount > 1) {
								$("#costingTable tbody tr:last").remove();
								
// 							$('#costingTable thead tr>td:last').find('.col-edit').show();
								$(".dynamic-hidden-field-row > input").slice(-2).remove();
								lowerRangeRow = $("#costingTable tbody tr:last td:nth-child(2)> input:last-child").val();
								
								if (isNaN(lowerRangeRow) || typeof (lowerRangeRow) === "undefined") {
									lowerRangeCol =<%=firstUpperValueRow%>;
								}
								lowerRangeRow++;
								rowCount--;
							} else {

							}
							
							
							
							

						});

						$('.submit-btn')
								.click(
										function() {
											var isEmpty = false;
											var isNumeric = false;
											var haveCostValue = false;
											var costIsEmpty = false;

											var lowerArray = new Array();
											var upperArray = new Array();
											var lowerInputsIds = new Array();
											var upperInputsIds = new Array();
											var costArray = new Array();

											$('input[name="lowerRangeMb"]').each(function() {
												$('.error-msg').hide();
												$(this).css('border-color', '#d2d6de');

												if ($(this).val() == '' || !$.isNumeric($(this).val())) {
													$(this).css('border-color', 'red');
													isEmpty = true;
													isNumeric = true;
												}

												var prvUpperValue = 0;
												var nextUpperValue = 0;
												var currlowerValue = $(this).val();
												prvUpperValue = $(this).closest('tr').prev('tr').find('input[name="upperRangeMb"]').val();
												nextUpperValue = $(this).closest('tr').find('input[name="upperRangeMb"]').val();

												if (typeof (prvUpperValue) == 'undefined' && currlowerValue > parseInt(nextUpperValue)) {
													$(this).css('border-color', 'red');

												} else if (prvUpperValue > 0 && currlowerValue != ++prvUpperValue) {
													$(this).css('border-color', 'red');
												} else if (currlowerValue > parseInt(nextUpperValue)) {
													$(this).css('border-color', 'red');
												}

												lowerArray.push($(this).val());

											});

											var totalUpperRangeLen = $('input[name="upperRangeMb"]').length;
											var tmpLen = 0;

											$('input[name="upperRangeMb"]').each(function() {
												tmpLen++;
												$('.error-msg').hide();
												$(this).css('border-color', '#d2d6de');
												if ($(this).val() == '') {
													$(this).css('border-color', 'red');
													isEmpty = true;
												}

												if (!$.isNumeric($(this).val())) {
													if (tmpLen != totalUpperRangeLen) {
														$(this).css('border-color', 'red');
														isNumeric = true;
													} else if ($(this).val() !== "~") {
														$(this).css('border-color', 'red');
														isNumeric = true;
													}

												}

												var prvLowerValue = 0;
												var nextLowerValue = 0;

												var currUpperValue = $(this).val();
												prvLowerValue = $(this).closest('tr').find('input[name="lowerRangeMb"]').val();
												nextLowerValue = $(this).closest('tr').next('tr').find('input[name="lowerRangeMb"]').val();

												if (typeof (nextLowerValue) == 'undefined' && currUpperValue < parseInt(prvLowerValue)) {
													$(this).css('border-color', 'red');
												} else if (nextLowerValue > 0 && currUpperValue != --nextLowerValue) {
													$(this).css('border-color', 'red');
												} else if (currUpperValue < parseInt(prvLowerValue)) {
													$(this).css('border-color', 'red');
												}

												upperArray.push($(this).val());

											});

											$('input[name="cost"]').each(function() {

												$(this).css('border-color', '#d2d6de');
												if ($(this).val() == '' || !$.isNumeric($(this).val())) {

													$(this).css('border-color', 'red');
													isEmpty = true;
													isNumeric = true;
												}
												costArray.push($(this).val());
												var costValue = $(this).val();

												if (!checkCellValues(costArray, costValue)) {
													$(this).css('border-color', 'red');
													haveCostValue = true;
												}
											});
// 											R@!h@n
											
											if(!checkDate()){
												$("#activationDate").css('border-color', 'red');
											}
// 											/R@!h@n
											
											else if (isEmpty && isNumeric && haveCostValue && !checkContinuity(lowerArray, upperArray)) {
												$('.error-msg')
														.show()
														.html(
																'Empty or non-numeric lower range,Upper range,Cost not allowed.<br>Cost must be greater than 0.<br>Set Valid lower and Upper range.');
											} else if (isEmpty && isNumeric && haveCostValue) {
												$('.error-msg')
														.show()
														.html(
																'Empty and non-numeric lower range,Upper range,Cost not allowed.<br>Cost must be greater than 0.');
											} else if (isEmpty && isNumeric && !checkContinuity(lowerArray, upperArray)) {
												$('.error-msg')
														.show()
														.html(
																'Empty or non-numeric lower range,Upper range,Cost not allowed.<br>Set Valid lower and Upper range.');
											} else if (haveCostValue && !checkContinuity(lowerArray, upperArray)) {
												$('.error-msg').show().html(
														'Cost must be greater than 0.<br>Set Valid lower and Upper range.');
											} else if (isEmpty && isNumeric) {
												$('.error-msg').show().html(
														'Empty or non-numeric lower range,Upper range,Cost not allowed.');
											} else if (haveCostValue) {
												$('.error-msg').show().html('Cost must be greater than 0.');
											} else if (!checkContinuity(lowerArray, upperArray)) {
												$('.error-msg').show().html('Set Valid lower and Upper range.');
											} else {
												$("#costingUpdateForm").submit();

											}

										});

						//Add Dynamic Column
						var colCount =<%=columnList.size()%>;
						$('#addCol').click(function(e) {
							e.preventDefault();
							$('#costingModalCol').modal('show');
							$('.error-msg').hide()

						});

						
						$('.range-btn-col')
								.click(
										function(e) {
											e.preventDefault();
											
											
											var upperRangeCol = $('#upperRangeCol').val();
											
											if (lowerRangeCol && upperRangeCol) {
												
												
												if(lowerRangeCol == -1){
													lowerRangeCol = 1;
												}
												if (upperRangeCol >= lowerRangeCol) {
													colCount++;
													var colName = colHeading + ' '+colCount+ '<br>(' + lowerRangeCol + '-' + upperRangeCol + ' ' + colInnerUnit + ') '+ colOuterUnit;

													$('#costingTable thead tr>td:last').find('.col-edit').hide();
													$('#afterColEditbr').hide();
													$('#afterEditbr').hide();
													$('#costingTable tr').append($("<td id='col"+colCount+"'>"));
													$('#costingTable thead tr>td:last')
															.html(
																	'<a  class="btn btn-xs btn-default col-edit" type="button" data-lower="'
																	+lowerRangeCol+'" data-upper="'+upperRangeCol+'" accesskey="'
																	+colCount+'"><i class="fa fa-pencil-square-o" style="color:#008d4c" aria-hidden="true"></i>&nbsp; Edit</a><br id="afterColEditbr">'
																			+ colName);
													$('#costingTable tbody tr').each(
															function() {
																$(this).children('td:last').append(
																		$('<input type="text" name="cost" class="form-control">'))
															});
													$(".dynamic-hidden-field")
															.append(
																	'<input type="hidden" value="'+lowerRangeCol+'" name="lowerRangeKm" id="lower'+colCount+'">');
													$(".dynamic-hidden-field")
															.append(
																	'<input type="hidden" value="'+upperRangeCol+'" name="upperRangeKm" id="upper'+colCount+'">');

													lowerRangeCol = upperRangeCol;
													lowerRangeCol++;
													$('#costingModalCol').modal('hide');
												} else {

													$('.error-msg').show().html(
															'Upper range should be greater than or equal to previous lower range(' + lowerRangeCol
																	+ ').');

												}

											} else {
												alert('Enter your range.');
											}
										});

						//Remove Dynamic Column
						$('.col-remove').click(function(e) {
							e.preventDefault();
							if (colCount > 1) {
								$("#costingTable thead tr>td:last").remove();
								$('#costingTable tbody tr').each(function() {
									$(this).children('td:last').remove()
								});
								$('#costingTable thead tr>td:last').find('.col-edit').show();
								$(".dynamic-hidden-field > input").slice(-2).remove();
								lowerRangeCol = $(".dynamic-hidden-field > input:last-child").val();
								
								if (isNaN(lowerRangeCol) || typeof (lowerRangeCol) === "undefined") {
									lowerRangeCol =<%=firstUpperValue%>;
								}
								lowerRangeCol++;
								colCount--;
							} else {

							}

						});

						//Edit Dynamic Column
						var colNo;
						var colLowerRange;
						$('#costingTable').on('click', '.col-edit', function() {

							$('#costingModalEditCol').modal('show');
							colNo = $(this).attr('accesskey');
							colLowerRange = $(this).data('lower');
							$('.error-msg').hide();

						});

						$('.col-edit-btn')
								.click(
										function(e) {
											
											e.preventDefault();

											var colUpperRange = $('#EditupperRange').val();
											var updateColName = colHeading + ' ' + colCount+ '<br>(' + (colLowerRange==-1?1:colLowerRange) + '-' + colUpperRange + ' '+colInnerUnit+') '+colOuterUnit;

											if (colLowerRange && colUpperRange) {
												if (colUpperRange >= colLowerRange) {

													if (updateColName == '') {
														return false;
													}
													if (updateColName != null) {
														$('#costingTable thead tr')
																.find('td#col' + colNo)
																.html(
																		'<a  class="btn btn-xs btn-default col-edit" type="button" data-lower="'+colLowerRange+'" data-upper="'+colUpperRange+'" accesskey="'+colNo+'"><i class="fa fa-pencil-square-o" style="color:#008d4c" aria-hidden="true"></i>&nbsp; Edit</a><br id="afterEditbr">'
																				+ updateColName);
														$(".dynamic-hidden-field").find('input#upper' + colNo).val(colUpperRange);
														lowerRangeCol = $(".dynamic-hidden-field > input:last-child").val();
														lowerRangeCol++;
														if (isNaN(lowerRangeCol) || typeof (lowerRangeCol) === "undefined") {
															lowerRangeCol =<%=firstUpperValue%>;
														}
														$('#costingModalEditCol').modal('hide');
													}

												} else {

													$('.error-msg').show().html(
															'Upper range should be greater than or equal to previous lower range(' + colLowerRange
																	+ ').');

												}

											} else {
												alert('Enter your range.');
											}
										});

					});

	function checkContinuity(lowerArray, upperArray) {
		for (var index = 1; index < lowerArray.length; index++) {
// 			R@!h@n
			var lowPrev = parseInt(lowerArray[index-1]);
			var highPrev = 0;
			if(!(upperArray[index-1] == "~")){
				highPrev = parseInt(upperArray[index-1]);
			}
			
// 			/R@!h@n
			var low = parseInt(lowerArray[index]);
			var high = 0;
			if (!(upperArray[index] == "~")) {
				high = parseInt(upperArray[index]);
			} else
				high = 99999;

			if (high <= 0 || low < 0 || high < low || highPrev<0 || lowPrev<0)
				return false;

			var lowerEnd = parseInt(lowerArray[index]);
			var upperLastEnd = parseInt(upperArray[index - 1]);
			
			if (lowerEnd - 1 != upperLastEnd)
				return false;

		}
// 		R@!h@n
		if(parseInt(upperArray[upperArray.length-1]) < 0){
			return false;
		}
// 		/R@!h@n
		
		return true;
	}

	function checkCellValues(cellValues, costValue) {

		for (var index = 0; index < cellValues.length; index++) {
			if (parseFloat(cellValues[index]) <= 0 && parseFloat(cellValues[index]) == costValue)
				return false;

		}
		return true;
	}
	
	function checkDate(){
		$("#activationDate").css('border-color', '#d2d6de');
		var activationDate = $('#activationDate').val();
		if(activationDate === ""){
			$('.error-msg').show().html('Empty Activation Date');
			return false;
		}
		
		var selectedDate = $('#activationDate').datepicker('getDate');
		var today = new Date();
		today.setHours(0);
		today.setMinutes(0);
		today.setSeconds(0);
		if(Date.parse(today) > Date.parse(selectedDate)){
			$('.error-msg')
			.show()
			.html("Please set a forward date");
			return false;
			
		}
		
		return true;
	}
	
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