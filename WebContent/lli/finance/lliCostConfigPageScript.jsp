<script type="text/javascript">
	$(document).ready(function() {


		
		var nextLowerRangeForColumn = <%=newCostChartColumns.size() == 0 ? 1 : newCostChartColumns.get(newCostChartColumns.size()-1).getUpperRange() + 1%>;
		if(nextLowerRangeForColumn === 1){
			$('#div-render-new .col-add').prop('disabled', true);
		}
		$('.submit-btn').click(function(e) {
			e.preventDefault();
			var isEmpty = false;
			var isNumeric = false;
			var haveCostValue = false;
			var costIsEmpty = false;
	
			var lowerArray = new Array();
			var upperArray = new Array();
			var lowerInputsIds = new Array();
			var upperInputsIds = new Array();
			var costArray = new Array();
			var tableID = $(this).attr('data-table-id');
			var form = '#costing-form-' + tableID;
			$(form + ' input[name="lowerRangeMb"]').each(function() {
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

			var totalUpperRangeLen = $(form + 'input[name="upperRangeMb"]').length;
			var tmpLen = 0;
			$(form + ' input[name="upperRangeMb"]').each(function() {
				
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

			$(form + ' input[name="cost"]').each(function() {
	
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
		
			if(!checkDate()){
				$("#activationDate").css('border-color', 'red');
			}else if (isEmpty && isNumeric && haveCostValue && !checkContinuity(lowerArray, upperArray)) {
				$('.error-msg').show().html('Empty or non-numeric lower range,Upper range,Cost not allowed.<br>Cost must be greater than 0.<br>Set Valid lower and Upper range.');
			} else if (isEmpty && isNumeric && haveCostValue) {
				$('.error-msg').show().html('Empty and non-numeric lower range,Upper range,Cost not allowed.<br>Cost must be greater than 0.');
			} else if (isEmpty && isNumeric && !checkContinuity(lowerArray, upperArray)) {
				$('.error-msg').show().html('Empty or non-numeric lower range,Upper range,Cost not allowed.<br>Set Valid lower and Upper range.');
			} else if (haveCostValue && !checkContinuity(lowerArray, upperArray)) {
				$('.error-msg').show().html('Cost must be greater than 0.<br>Set Valid lower and Upper range.');
			} else if (isEmpty && isNumeric) {
				$('.error-msg').show().html('Empty or non-numeric lower range,Upper range,Cost not allowed.');
			} else if (haveCostValue) {
				$('.error-msg').show().html('Cost must be greater than 0.');
			} else if (!checkContinuity(lowerArray, upperArray)) {
				$('.error-msg').show().html('Set Valid lower and Upper range.');
			} else {
				$(form).submit();
			}
	
		});
		
		//submit btn ses
		function checkContinuity(lowerArray, upperArray) {
			for (var index = 1; index < lowerArray.length; index++) {
				var lowPrev = parseInt(lowerArray[index-1]);
				var highPrev = 0;
				if(!(upperArray[index-1] == "~")){
					highPrev = parseInt(upperArray[index-1]);
				}
				
				var low = parseInt(lowerArray[index]);
				var high = 0;
				if (!(upperArray[index] == "~")) {
					high = parseInt(upperArray[index]);
				} else{
					high = 99999;
				}
				if (high <= 0 || low < 0 || high < low || highPrev<0 || lowPrev<0){
					return false;
				}
				var lowerEnd = parseInt(lowerArray[index]);
				var upperLastEnd = parseInt(upperArray[index - 1]);
				
				if (lowerEnd - 1 != upperLastEnd){
					return false;
				}
			}
			if(parseInt(upperArray[upperArray.length-1]) < 0){
				return false;
			}
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
				$('.error-msg').show().html("Please set a forward date");
				return false;
			}
			return true;
		}
		
		
	
	});
</script>