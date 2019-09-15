<%@page import="request.EntityActionGenerator"%>
<%@page import="common.bill.BillService"%>
<%@page import="request.CommonRequestDTO"%>
<%@page import="common.bill.BillDTO"%>
<%@page import="common.EntityTypeConstant"%>
<%@page import="common.bill.BillConstants"%>
<%@page import="java.util.Set"%>
<%@page import="common.ModuleConstants"%>
<div class="portlet box portlet-btcl">
	
	<div class="portlet-title portlet-title-btcl">
		
		<div class="caption">
			<i class="fa fa-link" aria-hidden="true"></i>
			Merge and Split Bill
		</div>
		
	</div>
	
	<div class="portlet-body portlet-body-btcl form">
		
		<form class="form-horizontal" method="post" action="" >
			
			<input type="hidden" name="method" value="insert" />
			
			<div class="form-body">
							
				<div class="form-group">
					<label for="cnName" class="col-sm-3 control-label"> Select Module </label>
					<div class="col-sm-4">
				     	<select id="moduleID" name="moduleID" class="form-control">
				     		<% for( Integer i : ModuleConstants.ActiveModuleMap.keySet() ){ %>
				     			<option value="<%=i%>"> <%=ModuleConstants.ActiveModuleMap.get(i) %></option>
				     		<%} %>
				     	</select>
					</div>
				</div>
				
				<div class="form-group">
			   	
					<label for="cnName" class="col-sm-3 control-label">Client Name</label>
					
					<div class="col-sm-4">
				     	<input id="clientIdStr"  placeholder="Client Name" type="text" class="form-control" name="clientIdStr" value="" required >
					 	<input id="clientId" type="hidden" class="form-control" name="clientID" value="" required>
					</div>
					
				</div>
				
				<div class="form-group">
					
					<div class="col-sm=12">
						<h3 style="padding-left:15px" class="text-left">Pending Bill List</h3>
					</div>
					
					<div class="col-sm-12">
						<table class="table table-striped table-hover table-bordered"
							id="sample_editable_1">
							<thead>
								<tr>
									<th>Bill ID</th>
									<th>Month</th>
									<th>Link Name</th>
									<th>BTCL Amount</th>
									<th>Vat Amount</th>
									<th>Payable Amount</th>
								</tr>
							</thead>
							<tbody id="billTableBody">
							</tbody>
						</table>
					</div>
				</div>
			</div>				
		</form>
		
		<div style="overflow:auto">
			<div class="container" style="margin-bottom: 25px; padding-bottom:25px;">
				
				<div class="form-group " style="margin-bottom: 0px;">
					<div class="portlet light" style="margin-bottom: 0px;">
						<div class="col-sm-7 portlet-title">
							<div class="caption">
								 <span class="caption-subject  font-grey-gallery "> Monthly Installation </span>
							</div>
						</div>
					</div>
				</div>
				<br/>
				<br/>
				<div class="form-group ">
					<label class="col-sm-3 control-label text-right">Installation month</label>

					<div class="col-sm-6 ">
						<input type="number" class="form-control" id="noOfMonths"/>		
					</div>
				</div>
				<br/>
				<br/>
				<div class="form-group ">
					<label class="col-sm-3 control-label text-right">Installation start date</label>

					<div class="col-sm-6 ">
						<input name="payBillFrom" class="form-control datepicker" id="payBillFrom" value="" autocomplete="off" type="text">		
					</div>
				</div>
				
				<br/>
				<br/>
				<div class="form-group">
					<label class="col-sm-3 control-label text-right">Distribute evenly</label>

					<div class="col-sm-6 ">
						<input type="checkbox" id="distributeEvenly" class="form-control" />
					</div>
				</div>
				
				
				<br/>
				
				
				
				<div id="duePaymentContainer" > 
				
				</div>
				
				<div class="form-group" id="totalAmountDiv">
					<label class="col-sm-3 control-label text-right">
						<b>Total:</b>
					</label>
					<div class="col-sm-6 ">
						<label id="totalAmount" ></label> 
					</div>
				</div>
				
				<br/>
				
				<div class="form-actions text-center">
					<button id="distributeReset" class="btn btn-sm btn-danger">Reset</button>
					<button id="payDue" class="btn btn-sm btn-primary">Submit</button>
				</div>
				
			</div>
		</div>
	</div>
</div>

<script>
	var context = "<%=request.getContextPath() %>";
</script>

<script src="${context}assets/global/scripts/datatable.js" type="text/javascript"></script>
<script src="${context}assets/global/plugins/datatables/datatables.min.js" type="text/javascript"></script>
<script src="${context}assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.js" type="text/javascript"></script>
<script src="${context}assets/scripts/vpn/payment/payment-table-editable.js" type="text/javascript"></script>
<script src="${context}assets/scripts/vpn/bill/splitBill.js" type="text/javascript"></script>