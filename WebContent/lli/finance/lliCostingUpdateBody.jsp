<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="../../includes/checkLogin.jsp"%>
<%@page import="util.*"%>
<%@page import="java.util.*, costConfig.*"%>
<%
	Long tableID = (Long) request.getAttribute("tableID");
	System.out.println("The table id by -------------- get parameter: " + tableID);
	List<RowDTO> rowList = (List<RowDTO>) request.getAttribute("rowList");
	List<ColumnDTO> columnList = (List<ColumnDTO>) request.getAttribute("columnList");
	List<CostCellDTO> cellList = (List<CostCellDTO>) request.getAttribute("cellList");
	String message = (String) request.getAttribute("success_message");
	int lastUpperValue = columnList.get(columnList.size()-1).getUpperRange()+1; 
	int firstUpperValue = columnList.get(0).getUpperRange()+1;
%>



<script type="text/javascript">
	$(document).ready(function() {
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
	});

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
</script>

<script>
$(document).ready(function(){
		
		var rwCount = <%=rowList.size()%>;
		
		//Add Dynamic Row
		$('#addRow').click(function(e){	
			 e.preventDefault();
			rwCount++;
				$('#costingTable tbody').append($("#costingTable tbody tr:last").clone());
				$("#costingTable tbody tr:last td input").val('');
				$('#costingTable tbody tr:last>td:first').html(rwCount);
		});
		
		//Remove Dynamic Row
		$('.row-remove').click(function(){

			if(rwCount > 1){
					$("#costingTable tbody tr:last").remove();
					rwCount--;
				}
				
		});
		
		
		 $('.submit-btn').click(function(){	
			
			 var isEmpty = false;
			 var isNumeric = false;
			 var haveCostValue = false;
			 var costIsEmpty = false;
			 
			 var lowerArray = new Array();
			 var upperArray = new Array();
			 var lowerInputsIds = new Array();
			 var upperInputsIds = new Array();
			 var costArray = new Array();
			
			 
			 $('input[name="lowerRangeMb"]').each(function(){
				 $('.error-msg').hide();
				 $(this).css('border-color','#d2d6de');
				 
				 if($(this).val() == '' || !$.isNumeric($(this).val())){
					 $(this).css('border-color','red');
					 isEmpty = true;
					 isNumeric = true;
				}
				 
				 var prvUpperValue = 0;
				 var nextUpperValue = 0;
				 var currlowerValue = $(this).val();
				 prvUpperValue = $(this).closest('tr').prev('tr').find('input[name="upperRangeMb"]').val();
				 nextUpperValue = $(this).closest('tr').find('input[name="upperRangeMb"]').val();
				
				 if(typeof(prvUpperValue) == 'undefined' && currlowerValue > parseInt(nextUpperValue)){
						 $(this).css('border-color','red'); 
					
				 }else if(prvUpperValue > 0  && currlowerValue != ++prvUpperValue){ 
					 $(this).css('border-color','red');
				 }else if(currlowerValue > parseInt(nextUpperValue)){
					 $(this).css('border-color','red');  
				}
			
				 lowerArray.push($(this).val());

			 });
			 
			 var totalUpperRangeLen =  $('input[name="upperRangeMb"]').length;
			 var tmpLen = 0;
			 
			 $('input[name="upperRangeMb"]').each(function(){
				 tmpLen++;
				 $('.error-msg').hide();
				 $(this).css('border-color','#d2d6de');
				 if($(this).val() == ''){
					 $(this).css('border-color','red');
					 isEmpty = true;
				}
				 
				 if( !$.isNumeric($(this).val()) ){
					 if(tmpLen != totalUpperRangeLen){
						 $(this).css('border-color','red');
						 isNumeric = true; 
					 }else if($(this).val() !== "~"){
						 $(this).css('border-color','red');
						 isNumeric = true; 
					 }
					 
					
				}
				 
				 var prvLowerValue = 0;
				 var nextLowerValue = 0;
				 
				 var currUpperValue = $(this).val();
				 prvLowerValue = $(this).closest('tr').find('input[name="lowerRangeMb"]').val();
				 nextLowerValue = $(this).closest('tr').next('tr').find('input[name="lowerRangeMb"]').val();

				 if(typeof(nextLowerValue) == 'undefined' && currUpperValue < parseInt(prvLowerValue)){
						 $(this).css('border-color','red');
				 }else if(nextLowerValue > 0 && currUpperValue != --nextLowerValue){
					 $(this).css('border-color','red'); 
				 }else if(currUpperValue < parseInt(prvLowerValue)){
					 $(this).css('border-color','red');
				}
				 
				 upperArray.push($(this).val());
				 
			 });
			 
			 $('input[name="cost"]').each(function(){
				
				 $(this).css('border-color','#d2d6de');
				 if($(this).val() == '' || !$.isNumeric($(this).val())){
					
					 $(this).css('border-color','red');
					 isEmpty = true;
					 isNumeric = true;
				 }
					 costArray.push($(this).val());
					 var costValue = $(this).val();
					 
					 if(!checkCellValues(costArray,costValue)){
						 $(this).css('border-color','red');
						 haveCostValue = true;
					 }
				 });
				

		if( isEmpty && isNumeric && haveCostValue && !checkContinuity(lowerArray,upperArray)){
				$('.error-msg').show().html('Empty or non-numeric lower range,Upper range,Cost not allowed.<br>Cost must be greater than 0.<br>Set Valid lower and Upper range.');
			}else if( isEmpty && isNumeric && haveCostValue){
				$('.error-msg').show().html('Empty and non-numeric lower range,Upper range,Cost not allowed.<br>Cost must be greater than 0.');
			}else if( isEmpty && isNumeric && !checkContinuity(lowerArray,upperArray)){
				$('.error-msg').show().html('Empty or non-numeric lower range,Upper range,Cost not allowed.<br>Set Valid lower and Upper range.');
			}else if( haveCostValue && !checkContinuity(lowerArray,upperArray)){
				$('.error-msg').show().html('Cost must be greater than 0.<br>Set Valid lower and Upper range.');
			}else if( isEmpty && isNumeric){
				$('.error-msg').show().html('Empty or non-numeric lower range,Upper range,Cost not allowed.');
			}else if(haveCostValue){
				$('.error-msg').show().html('Cost must be greater than 0.');
			}else if(!checkContinuity(lowerArray,upperArray)){
				$('.error-msg').show().html('Set Valid lower and Upper range.');
			}else{
				 $("#costingUpdateForm").submit();
				
			}
		
		 });
		
		
		
		//Add Dynamic Column
		var colCount = <%=columnList.size()%>;
		$('#addCol').click(function(e){
			 e.preventDefault();
			 $('#costingModal').modal('show');
			 $('.error-msg').hide()

		});
		
		var lowerRange = <%=lastUpperValue%>;
		 $('.range-btn').click(function(e){
			 e.preventDefault();

			 var upperRange = $('#upperRange').val();
			 var colName = '('+lowerRange+'-'+upperRange+') BDT/Mb';

			 if(lowerRange && upperRange){
				 if(upperRange >= lowerRange){
					 	colCount++;
					 	$('#costingTable thead tr>td:last').find('.col-edit').hide();
						$('#costingTable tr').append($("<td id='col"+colCount+"'>"));
						$('#costingTable thead tr>td:last').html('<a  class="btn btn-xs btn-default col-edit" type="button" data-lower="'+lowerRange+'" data-upper="'+upperRange+'" accesskey="'+colCount+'"><i class="fa fa-pencil-square-o" style="color:#008d4c" aria-hidden="true"></i>&nbsp; Edit</a><br>'+colName);
						$('#costingTable tbody tr').each(function(){$(this).children('td:last').append($('<input type="text" name="cost" class="form-control">'))});
						$(".dynamic-hidedden-field").append('<input type="hidden" value="'+lowerRange+'" name="lowerRangeKm" id="lower'+colCount+'">');
						$(".dynamic-hidedden-field").append('<input type="hidden" value="'+upperRange+'" name="upperRangeKm" id="upper'+colCount+'">');
						
						lowerRange = upperRange;
						lowerRange++;
						$('#costingModal').modal('hide');
				 }else{ 
					 
					 $('.error-msg').show().html('Upper range should be greater than or equal to lower range('+lowerRange+').'); 
					 
				 }
					
				}else{alert('Enter your range.');}
		 });
		
		//Remove Dynamic Column
		$('.col-remove').click(function(e){
				 e.preventDefault();
				if(colCount > 1){
					$("#costingTable thead tr>td:last").remove();
					$('#costingTable tbody tr').each(function(){$(this).children('td:last').remove()});
					$('#costingTable thead tr>td:last').find('.col-edit').show();
					$( ".dynamic-hidedden-field > input" ).slice(-2).remove();
					lowerRange = $( ".dynamic-hidedden-field > input:last-child" ).val();
					lowerRange++;
					if(isNaN(lowerRange) || typeof(lowerRange)  === "undefined"){
						lowerRange = <%=firstUpperValue%>;
					} 

					colCount--;
				}else{
					
					
				}
				
		});
		
		//Edit Dynamic Column
		var colNo;
		var colLowerRange;
		$('#costingTable').on('click','.col-edit',function(){
			 
			$('#costingModalEdit').modal('show');
			colNo = $(this).attr('accesskey');
			colLowerRange = $(this).data('lower');
			$('.error-msg').hide(); 

				
		});
		
		 $('.col-edit-btn').click(function(e){
			 e.preventDefault();
			
			 var colUpperRange = $('#EditupperRange').val();
			 var updateColName = '('+colLowerRange+'-'+colUpperRange+') BDT/Mb';
			 
			 if(colLowerRange && colUpperRange){
				 if(colUpperRange >= colLowerRange){
					 
					 if(updateColName == ''){
						   return false;
					 }
					 if(updateColName != null){
						$('#costingTable thead tr').find('td#col'+colNo).html('<a  class="btn btn-xs btn-default col-edit" type="button" data-lower="'+colLowerRange+'" data-upper="'+colUpperRange+'" accesskey="'+colNo+'"><i class="fa fa-pencil-square-o" style="color:#008d4c" aria-hidden="true"></i>&nbsp; Edit</a><br>'+updateColName);
						$(".dynamic-hidedden-field").find('input#upper'+colNo).val(colUpperRange);
						lowerRange = $( ".dynamic-hidedden-field > input:last-child" ).val();
						lowerRange++;
						if(isNaN(lowerRange) || typeof(lowerRange)  === "undefined"){
							lowerRange = <%=firstUpperValue%>;
						}
						$('#costingModalEdit').modal('hide');
					 }
					 
						
				 }else{ 
					 
					 $('.error-msg').show().html('Upper range should be greater than or equal to lower range('+colLowerRange+').'); 
					 
				 }
					
				}else{alert('Enter your range.');}
		});
		
	
});


	function checkContinuity(lowerArray, upperArray) {
	
		for (var index = 1; index < lowerArray.length; index++) {

			var low = parseInt(lowerArray[index]);
			var high = 0;
			if (!upperArray[index] == "~") {
				high = parseInt(upperArray[index]);
			} else
				high = 99999;

			if (high <= 0 || low < 0 || high < low)
				return false;
				
			
				
			
			var lowerEnd = parseInt(lowerArray[index]);
			var upperLastEnd = parseInt(upperArray[index-1]);
			if (lowerEnd - 1 != upperLastEnd)
				return false;
				
		}
		return true;
	}

	function checkCellValues(cellValues,costValue) {
	
		for (var index = 0; index < cellValues.length; index++) {
			if (parseFloat(cellValues[index]) <= 0 && parseFloat(cellValues[index]) == costValue)
				return false;
				
		}
		return true;
	}
</script>



<html:base />


<div class="col-lg-offset-1 col-lg-10">

	<div class="panel panel-success">

		<div class="panel-heading box-header with-border">
			<h3 class="box-title">Cost Config</h3>
			
		</div>
		<!-- <div class="panel-heading" style="text-align: center; font-size: 14pt; background-color: #aeeaae;"><div class="div_title">Cost Config</div></div> -->

		<div class="panel-body">

			<%if(message!=null){ %>
			<div class="text-center">

				<div class="alert alert-success alert-dismissible ">
					<%=message%>
				</div>

			</div>
			<%} %>
			<div class="form-horizontal">
				<div class="alert alert-danger error-msg" style="display:none;text-align:center;"></div>
				
				<form method="post" action="../../UpdateCostConfig.do" id="costingUpdateForm"
					>
					<input type=hidden name="tableID" value="<%=tableID%>" />
					<% for(int rowIndexer = 0 ; rowIndexer<rowList.size();rowIndexer++){ %>
					<input type=hidden name="rowIndex"
						value='<%=rowList.get(rowIndexer).getIndex()%>' />
					<input type=hidden name="rowIDs"
						value='<%=rowList.get(rowIndexer).getID()%>' />
					<%} %>
					

					<div class="dynamic-hidedden-field">
					<% for(int columnIndexer = 0 ; columnIndexer<columnList.size();columnIndexer++){ %>
						<!-- 	<input type=hidden name="columnIndex"
						value='<%=columnList.get(columnIndexer).getIndex()%>' />
						 -->
					<input type=hidden name="lowerRangeKm"
						value='<%=columnList.get(columnIndexer).getLowerRange()%>' />
				

					<input type=hidden name="upperRangeKm"
						value='<%=columnList.get(columnIndexer).getUpperRange()%>' />

					<% } %>
					
					</div>



					<div class="table-responsive">

						<table id="costingTable"
							class="table table-bordered table-striped"
							style="margin-top: 30px; text-align: center">

							<button class="btn btn-default col-remove pull-right"
								type="button" style="margin-bottom: 15px">
								<i class="fa fa-times" style="color: #d36b7e" aria-hidden="true"></i>
							</button>
							<button id="addCol" class="btn btn-default pull-right"
								type="button" style="margin-right: 5px; margin-bottom: 15px">
								Col&nbsp;<i style="color: #008d4c" class="fa fa-arrow-right"
									aria-hidden="true"></i>
							</button>


							<thead>
								<tr>
									<td class="td_viewheader">Sl</td>
									<td class="align-center td_viewheader">Bandwidth(Mbkm)</td>
									<%
									for(int columnIndexer = 0 ; columnIndexer < columnList.size(); columnIndexer++) {
								%>
									<%
									if (columnList.get(columnIndexer).getUpperRange() != Integer.MAX_VALUE) {
								%>
									<td class="align-center td_viewheader"><%="Step " + (columnIndexer + 1)%><br><%="(" + columnList.get(columnIndexer).getLowerRange() + "-"
									+ columnList.get(columnIndexer).getUpperRange() + " KM) BDT/MbKm "%>
									</td>
									<%
									} else{
								%>
									<td class="align-center td_viewheader"><%="Step " + (columnIndexer + 1)%><br><%="(" + columnList.get(columnIndexer).getLowerRange()
									+ "+"+ " KM) BDT/MbKm "%></td>

									<%} %>

									<%
									}
								%>
								</tr>
							</thead>
							<tbody>
								<%
                                          	int cellIndex = 0;
                                          		for (int rowIndexer = 0; rowIndexer < rowList.size(); rowIndexer++) {
                                          %>
								<tr>

									<td><%=""+(rowIndexer+1)+"." %></td>
									<%
									if (rowList.get(rowIndexer).getUpperRange() != Integer.MAX_VALUE) {
								%>
									<td><input type="text" class="form-control lower-range-mb" id="lowerRangeMb<%= rowIndexer %>"
										style="width: 35%; float: left" name="lowerRangeMb"
										value="<%= rowList.get(rowIndexer).getLowerRange()%>" /><span>-</span><input
										type="text" class="form-control upper-range-mb" id="upperRangeMb<%= rowIndexer %>"
										style="width: 35%; float: right" name="upperRangeMb"
										value="<%= rowList.get(rowIndexer).getUpperRange()%>" /></td>

									<%} else{%>
									<td><input type="text" class="form-control lower-range-mb" style="width: 35%; float: left"
										name="lowerRangeMb" id="lowerRangeMb<%= rowIndexer %>"
										value="<%= rowList.get(rowIndexer).getLowerRange()%>" /><span>-</span><input
										type="text" class="form-control upper-range-mb" id="upperRangeMb<%= rowIndexer %>"
										style="width: 35%; float: right" name="upperRangeMb" value="~" /></td>

									<%} %>
									<% for(int columnIndexer = 0 ; columnIndexer<columnList.size();columnIndexer++){ %>

									<td><input type="text" class="form-control" name="cost"
										value='<%=cellList.get(cellIndex++).getValue() %>' /></td>
									<input type="hidden" name="cellIDs"
										value='<%=cellList.get(cellIndex-1).getID() %>' />
									<%} %>
								</tr>
								<%} %>

							</tbody>
						</table>
					</div>
					<button id="addRow" class="btn btn-default" type="button">
						<i class="fa fa-arrow-down" style="color: #008d4c"
							aria-hidden="true"></i>&nbsp;Row
					</button>
					<button class="btn btn-default row-remove" type="button">
						<i class="fa fa-times" style="color: #d36b7e" aria-hidden="true"></i>
					</button>

					<div class="text-center">
						<input type="reset" value="Reset" class="btn btn-reset-btcl" />
						 <input	type="button" value="Submit" class="btn btn-submit-btcl submit-btn" />
					</div>
				</form>
			</div>
		</div>
	</div>
</div>


<div class="modal fade costing-modal" id="costingModal" tabindex="-1"
	role="dialog" aria-labelledby="costingModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">Set Your Range</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="alert alert-danger error-msg" style="display: none;"></div>

					<br>
					<div class="form-group">
						<label class="col-md-3  control-label">Upper Range</label>
						<div class="col-md-8 col-xs-12">
							<input name="upperRange" value="" id="upperRange" min=""
								class="form-control border-radius bill" type="number" autocomplete="off"
								required="required">
						</div>
					</div>
					<br>

				</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-xs btn-default"
					data-dismiss="modal" style="height: 25px;">Close</button>
				<button type="button" class="btn btn-xs btn-primary range-btn"
					style="height: 25px;">ok</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade costing-modal-edit" id="costingModalEdit"
	tabindex="-1" role="dialog" aria-labelledby="costingModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">Edit Your Range</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="alert alert-danger error-msg" style="display: none;"></div>

					<br>
					<div class="form-group">
						<label class="col-md-3  control-label">Edit Upper Range</label>
						<div class="col-md-8 col-xs-12">
							<input name="upperRange" value="" id="EditupperRange" min=""
								class="form-control border-radius bill" type="number" autocomplete="off"
								required="required">
						</div>
					</div>
					<br>

				</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-xs btn-default"
					data-dismiss="modal" style="height: 25px;">Close</button>
				<button type="button" class="btn btn-xs btn-primary col-edit-btn"
					style="height: 25px;">ok</button>
			</div>
		</div>
	</div>
</div>
