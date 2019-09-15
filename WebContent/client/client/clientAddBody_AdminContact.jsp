<%@page import="common.ClientConstants"%>
<div class="portlet box green-haze">
	<div class="portlet-title">
		<div class="caption">Admin</div>
		<div class="actions">
			<input type=button id=reset-admin class="btn btn-default" value="Clear">
		</div>
	</div>
	<div class="portlet-body form">
	
		<input type=hidden name="adminContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_ADMIN%>" />
		
		<div class="form-body">
			<!-- 
			<label class="checkbox"> <span><input type="checkbox" id="admin-checkbox-1" value="option"></span> Same as Registrant Info.</label>
			<label class="checkbox"> <span><input type="checkbox" id="admin-checkbox-2" value="option"></span> Same as Billing Info.</label>
			<label class="checkbox"> <span><input type="checkbox" id="admin-checkbox-3" value="option"></span> Same as Technical Info.</label>
			-->
			<div class="form-group">
				<div class="col-md-12">
					<div class="col-md-3 control-label">Copy From</div>
					<button type=button id=copy-from-registrant-to-admin class="btn btn-default col-md-3">Registrant</button>
					<button type=button id=copy-from-bill-to-admin class="btn btn-default col-md-3">Billing</button>
					<button type=button id=copy-from-technical-to-admin class="btn btn-default col-md-3">Technical</button>
				</div>
			</div>
			<hr>
			<div class="form-group">
				<label class="col-md-4  control-label">First Name<span class=required aria-required=true>*</span></label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="adminContactDetails.registrantsName" class="form-control simple-input admin-required admin">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Last Name</label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="adminContactDetails.registrantsLastName" class="form-control simple-input  admin">
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-md-4  control-label">Email<span class=required aria-required=true>*</span></label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="adminContactDetails.email" class="form-control simple-input admin-required admin">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4 control-label">Mobile Number<span class=required aria-required=true>*</span></label>
				<div class="col-md-8">
					<input type=text name="adminContactDetails.phoneNumber" class="form-control simple-input admin-required admin">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4 control-label">Telephone Number</label>
				<div class="col-md-8">
					<input type=text name="adminContactDetails.landlineNumber" class="form-control simple-input admin">
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-md-4 control-label">Fax Number</label>
				<div class="col-md-8">
					<input type=text name="adminContactDetails.faxNumber" class="form-control simple-input  admin">
				</div>
			</div>

			
			<hr>
			
			<div class="form-group">
				<label class="col-md-4  control-label">City<span class=required aria-required=true>*</span></label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="adminContactDetails.city" class="form-control simple-input admin-required admin">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Post Code</label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="adminContactDetails.postCode" class="form-control simple-input admin">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Address<span class=required aria-required=true>*</span></label>
				<div class="col-md-8 col-xs-12">
					<textarea name="adminContactDetails.address" value="" class="form-control simple-input textarea admin-required admin" data="textarea">${form.adminContactDetails.address}</textarea>
				</div>
			</div>
		</div>
	</div>
</div>