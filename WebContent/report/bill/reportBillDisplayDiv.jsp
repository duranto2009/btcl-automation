<% Integer moduleIDForLocal = Integer.parseInt(request.getParameter("moduleID")); %>
<div class="portlet light">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-tv"></i>Display
		</div>
	</div>
	<!-- /.box-header -->
	<div class="portlet-body form" style="height: 30vh; overflow-x: hidden; overflow-y:  scroll;">
		<div class="form-body">
		
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="1"></span>ID</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.BillDTO.ID" value="ID" disabled
					data-operator="eq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="2"></span>Username</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientDTO.loginName" value="Username" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="3"></span>Payment Status</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.BillDTO.paymentStatus" value="Payment Status" disabled
					data-operator="eq" data-comment="select" data-values="Unpaid:0,Paid:2">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="4"></span>Bill Type</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.BillDTO.billType" value="Bill Type" disabled
					data-operator="eq" data-comment="select" data-values="Prepaid:0,Postpaid:1">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			
			<%-- 
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="5"></span>Bill Action Type</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.BillDTO.className" value="Bill Action Type" disabled
					data-operator="eq" data-comment="select" data-values="Renew:0,kllk:1">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			 --%>
			
			
			
			
			
			
			
			
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="6"></span>Net Payable</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.BillDTO.netPayable" value="Net Payble" disabled
					data-operator="geq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="7"></span>Discount</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.BillDTO.discount" value="Discount" disabled
					data-operator="geq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="8"></span>Late Fee</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.BillDTO.lateFee" value="Late Fee" disabled
					data-operator="geq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="9"></span>Mobile</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientContactDetailsDTO.phoneNumber" value="Mobile" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="10"></span>Email</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientContactDetailsDTO.email" value="Email" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="11"></span>Generation Time</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.BillDTO.generationTime" value="Generation Time" disabled
					data-operator="geq" data-comment="datepicker">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="12"></span>Grand Total</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.BillDTO.grandTotal" value="Grand Total" disabled
					data-operator="geq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="13"></span>Total Payable</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.BillDTO.totalPayable" value="Total Payable" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="14"></span>VAT</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.BillDTO.VAT" value="VAT" disabled
					data-operator="geq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="15"></span>Last Payment Date</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.BillDTO.lastPaymentDate" value="Last Payment Date" disabled
					data-operator="geq" data-comment="datepicker">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			
<!-- 			<div class="form-group"> -->
<!-- 				<div class="col-md-5"> -->
<!-- 					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="15"></span>Description</label> -->
<!-- 				</div> -->
<!-- 				<div class="col-md-5"> -->
<!-- 					<input type="text" class="display-input" name="display.BillDTO.description" value="Description" disabled -->
<!-- 					data-operator="like" data-comment=""> -->
<!-- 				</div> -->
<%-- 				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div> --%>
<!-- 			</div> -->
		</div>
	</div>
	<!-- /.box-body -->
</div>
