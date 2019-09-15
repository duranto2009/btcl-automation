<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i><%=request.getParameter("title") %>
		</div>
	</div>
	<div class="portlet-body form">
		<form id = "tableForm" class="form-horizontal">
			<div class="form-body">
				
				<div class="form-group">
					<label class="col-sm-3 control-label">LLI ID</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="lliID" 
						name="lliID" required>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Client ID</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="clientID" 
						name="clientID" required>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">Division ID</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="divisionID" 
						name="divisionID" required>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Essential Block Size</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="essentialBlockSize" 
						name="essentialBlockSize" required>
						
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">Select ID</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="essentialIPRange-0" 
						name="essentialIPRange" value="" required>
					</div>
					<div class='col-sm-3'>
						<button type=button class='btn btn-default btn-circle' id=btn-more-essential><i class='fa fa-plus'></i></button>
					</div>
				</div>
				<div id="div-to-insert-essential"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">Additional Block Size</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="additionalBlockSize" 
						name="additionalBlockSize">
						
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Select ID</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="additionalIPRange-0" 
						name="additionalIPRange" value="">
					</div>
					<div class='col-sm-3'>
						<button type=button class='btn btn-default btn-circle' id=btn-more-additional><i class='fa fa-plus'></i></button>
					</div>
				</div>
				<div id="div-to-insert-additional"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Expiration Date</label>
					<div class="col-sm-6">
						<div class="input-group date">
							<div class=input-group-addon>
								<i class="fa fa-calendar"></i>
							</div>
							<input type="text" class="form-control datepicker" name="expirationDate" value="">
						</div>
					</div>
				</div>
			</div>	
			<div class="form-actions text-center">
				<button class="btn btn-reset-btcl" type="reset" id="reset-btn">Reset</button>
				<button class="btn btn-submit-btcl" type="submit" id="submit-btn">Submit</button>
			</div>
		</form>
	</div>
</div>

<script>
	function createInputBlock(name) {
		var string, btn, divFormGroupStart, divFormGroupEnd, label, divInputStart, divInputEnd, input, divBtnStart, divBtnEnd; 
		divFormGroupStart = '<div class=form-group>';
		label = '<label class="col-sm-3 control-label"></label>';
		divInputStart = '<div class=col-sm-6>';
		input = '<input type=text class=form-control name='+name+' value="">'
		divInputEnd = '</div>';
		divFormGroupEnd = '</div>';
		
		
		return string = divFormGroupStart + label + divInputStart + input + divInputEnd + divFormGroupEnd;
	}
	
	function getIpRangeList(name) {
		var ipRanges = '';
		$('input[name='+name + ']').each(function (idx){
			ipRanges += $(this).val() + ',';
		});
		
		return ipRanges;
	}
	var getConfiguration = function (){
		var usageType = arguments[0];
		var whichBlockSize = arguments[1];
		var whichRangeList = arguments[2];
		var object = {
			source : function (request, response){
				var url = context + 'lli/inventory/ipAddress/getSuggestion.do';
				var param = {};
				param.usageType = usageType;
				param.divisionID = $('#divisionID').val();
				param.blockSize = $(whichBlockSize).val();
				param.ipRanges= getIpRangeList(whichRangeList);
				ajax(url, param, response, 'GET', [$(this)]);
			}, 
			minLength : 0,
			select : function (e, ui) {
				$(this).val(ui.item.from + '-' + ui.item.to);
				return false;
			}
		};
		return object;
	};
	var renderElements = function (ul, data) {
		return $('<li>').append('<a>' + data.from + '-' + data.to + '</a>').appendTo(ul);
	}
	
	$(document).ready(function(){
		
		var form = $('#tableForm');
		var submitBtn = $('#submit-btn');
		var resetBtn = $('#reset-btn');
		var divisionID = $('#divisionID');
		var essentialRange = $('#essentialIPRange');
		var additionalRange = $('#additionalIPRange');
		var essentialBlockSize = $('#essentialBlockSize');
		var additionalBlockSize = $('#additionalBlockSize');
		resetBtn.click(function(){
			resetBtn.trigger('reset');
			return false;
		});
		submitBtn.click(function(){
			var url = context+"lli/inventory/ipAddress/allocate.do";
			var formData = form.serialize();
			LOG(formData);
			callAjax(url, formData, function(data){
				(data.responseCode == 1) ? toastr.success(data.msg) : toastr.error(data.msg);
			}, "POST");
			return false;
		});
		

		
		$('input[name=essentialIPRange]').autocomplete(getConfiguration(1, '#essentialBlockSize', 'essentialIPRange')).autocomplete('instance')._renderItem = renderElements;
		$('#btn-more-essential').on('click', function (){
			$('#div-to-insert-essential').append(createInputBlock('essentialIPRange'));
			$('#div-to-insert-essential').find('input[type=text]:last').autocomplete(getConfiguration(1, '#essentialBlockSize', 'essentialIPRange')).autocomplete('instance')._renderItem = renderElements;
			return false;
		});
		$('input[name=additionalIPRange]').autocomplete(getConfiguration(2, '#additionalBlockSize', 'additionalIPRange')).autocomplete('instance')._renderItem = renderElements;
		$('#btn-more-additional').on('click', function () {
			$('#div-to-insert-additional').append(createInputBlock('additionalIPRange'));
			$('#div-to-insert-additional').find('input[type=text]:last').autocomplete(getConfiguration(2, '#additionalBlockSize', 'additionalIPRange')).autocomplete('instance')._renderItem = renderElements;
			return false;
		});
	});
</script>

<script type="text/javascript" src="../../scripts/util.js"></script>