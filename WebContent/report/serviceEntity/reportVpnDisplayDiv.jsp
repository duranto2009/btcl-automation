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
		<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="1"></span>ID</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.VpnLinkDTO.ID" value="ID" disabled
					data-operator="eq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>		
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="2"></span>Link Name</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.VpnLinkDTO.linkName" value="Link Name" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="3"></span>Client Name</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientDTO.loginName" value="Client Name" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="4"></span>Balance</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.VpnLinkDTO.balance" value="Balance" disabled
					data-operator="le" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="5"></span>Email</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientContactDetailsDTO.email" value="Email" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="6"></span>Mobile</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientContactDetailsDTO.phoneNumber" value="Mobile" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="7"></span>Bandwidth</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.VpnLinkDTO.vpnBandwidth" value="Bandwidth" disabled
					data-operator="geq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="8"></span>Status</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.VpnLinkDTO.currentStatus" value="Status" disabled
					data-operator="in" data-comment="select" data-values="Approved:1,Ongoing:2">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="9"></span>Security Money</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.VpnLinkDTO.securityMoney" value="Security Money" disabled
					data-operator="geq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="10"></span>Activation Date</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.VpnLinkDTO.activationDate" value="Activation Date" disabled
					data-operator="geq" data-comment="datepicker">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
		</div>
	</div>
	<!-- /.box-body -->
</div>
