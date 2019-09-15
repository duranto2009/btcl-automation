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
					<input type="text" class="display-input" name="display.DomainDTO.ID" value="ID" disabled
					data-operator="eq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="2"></span>Domain Address</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.DomainDTO.domainAddress" value="Domain Address" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="3"></span>Login Name</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientDTO.loginName" value="Login Name" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="4"></span>Expiry Date</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.DomainDTO.expiryDate" value="Expiry Date" disabled
					data-operator="geq" data-comment="datepicker">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="6"></span>Primary DNS</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.DomainDTO.primaryDNS" value="Primary DNS" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="7"></span>Secondary DNS</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.DomainDTO.secondaryDNS" value="Secondary DNS" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="8"></span>Tertiary DNS</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.DomainDTO.tertiaryDNS" value="Tertiary DNS" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="9"></span>Email</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientContactDetailsDTO.email" value="Email" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="10"></span>Mobile</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientContactDetailsDTO.phoneNumber" value="Mobile" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="11"></span>Status</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.DomainDTO.currentStatus" value="Status" disabled
					data-operator="in" data-comment="select" data-values="Not Active:0,Approved:1,Processing:2">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="12"></span>Activation Date</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.DomainDTO.activationDate" value="Activation Date" disabled
					data-operator="geq" data-comment="datepicker">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="13"></span>Client Type</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientDetailsDTO.registrantType" value="Client Type" disabled
					data-operator="eq" data-comment="select" data-values="All:,Govt:1,Military:2,Private:3,Foreign:4,Others:5">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
		</div>
	</div>
	<!-- /.box-body -->
</div>
