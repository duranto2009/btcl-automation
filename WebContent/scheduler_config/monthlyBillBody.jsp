<%
	String action =(String) request.getAttribute("title");
	String actionName = "Monthly Bill".equals(action) ? "../SchedulerVpn/generateMonthlyBill.do" : "../SchedulerVpn/cancelMonthlyBill.do";
	request.removeAttribute("title");
	String actionBtnName = "Monthly Bill".equals(action) ? "Generate" : "Cancel";
	boolean isCancelBill = "Cancel Bill".equals(action);
	String cancelBillIDsAction = "../SchedulerVpn/cancelMonthlyBillByIDs.do";
%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-gift"></i><%=request.getParameter("title") %>
		</div>
	</div>
	<div class="portlet-body form">
		<form class="form-horizontal" id="tableForm" method="POST" action="<%=actionName%>">
			<div class="form-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">Generation From</label>
					<div class="col-sm-6">
						<input type="text" class="form-control raihan" name="fromTime">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Generation To</label>
					<div class="col-sm-6">
						<input type="text" class="form-control raihan" name="toTime">
					</div>
				</div>
			</div>
			<div class="form-actions text-center">
				<button class="btn btn-submit-btcl" type="submit" id="submitbtn"><%=actionBtnName %></button>
			</div>
		</form>
		<% if(isCancelBill){%>
			<form class="form-horizontal" id="cancelBillForm" method="POST" action="<%=cancelBillIDsAction%>">
				<div class="form-body">
					<div class="form-group">
						<label class="col-sm-3 control-label">Bill IDs</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" name="ids" placeholder="type bill ids">
						</div>
							
					</div>
				</div>
				<div class="form-actions text-center">
					<button class="btn btn-submit-btcl" type="submit"><%=actionBtnName %></button>
				</div>
			</form>
		<%} %>
	</div>
</div>
<script>
	$(document).ready(function(){
		$('resetbtn').click(function(){
			$('tableForm').trigger('reset');
		});
		 $(".raihan").datepicker({
	         autoclose: true,
	         format: "dd/mm/yyyy",
	         pickerPosition: "bottom-right", 
	         todayBtn: 'linked',         
	         todayHighlight: true 
	     });
	});
</script>
