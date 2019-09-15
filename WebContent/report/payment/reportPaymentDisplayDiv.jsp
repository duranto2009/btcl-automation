<%@page import="common.ModuleConstants"%>

<div class="portlet light">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-tv"></i>Display
		</div>
	</div>
	<!-- /.box-header -->
	<div class="portlet-body form" style="height: 30vh; overflow-x: hidden; overflow-y:  scroll;">
		<div class="form-body">
		<%if(request.getParameter("moduleID") != null){ %>
			<input type="hidden"  name="criteria.PaymentDTO.moduleID.eq" value="<%=request.getParameter("moduleID")%>">
		<%} %>
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="1"></span>ID</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.PaymentDTO.ID" value="ID" disabled
					data-operator="eq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../../report/upDownArrow.jsp"%></div>
			</div>
				
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="2"></span>Bank Name</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.PaymentDTO.bankName" value="Bank Name" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../../report/upDownArrow.jsp"%></div>
			</div>	
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="5"></span>Branch Name</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.PaymentDTO.bankBranchName" value="Branch Name" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../../report/upDownArrow.jsp"%></div>
			</div>
			
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="6"></span>Payment Date</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.PaymentDTO.paymentTime" value="Payment Date" disabled
					data-operator="geq" data-comment="datepicker">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../../report/upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="8"></span>Description</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.PaymentDTO.description" value="Description" disabled
					data-operator="like" data-comment="" >
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="8"></span>Verification Message</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.PaymentDTO.verificationMessage" value="Verification Message" disabled
					data-operator="like" data-comment="" >
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../../report/upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="9"></span>VAT Amount</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.PaymentDTO.vatAmount" value="VAT Amount" disabled
					data-operator="geq" data-comment="" >
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../../report/upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="10"></span>BTCL Amount</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.PaymentDTO.btclAmount" value="BTCL Amount" disabled
					data-operator="geq" data-comment="" >
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../../report/upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="11"></span>Payable Amount</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.PaymentDTO.payableAmount" value="Payable Amount" disabled
					data-operator="geq" data-comment="" >
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../../report/upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="12"></span>Paid Amount</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.PaymentDTO.paidAmount" value="Paid Amount" disabled
					data-operator="geq" data-comment="" >
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../../report/upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="13"></span>Payment Gateway</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.PaymentDTO.paymentGatewayType" value="Payment Gateway" disabled
					data-operator="eq" data-comment="select" data-values="Teletalk:1,Bank:2,SSL:3">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../../report/upDownArrow.jsp"%></div>
			</div>
		
		</div>
	</div>
	<!-- /.box-body -->
</div>
