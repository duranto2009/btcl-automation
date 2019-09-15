<div class="portlet box light">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-exclamation-circle"></i>Admin Contact
		</div>
	</div>
	<div class="portlet-body form">
		<div class="form-body">
			<label class="checkbox"> <span><input type="checkbox" id="admin-checkbox-1" value="option"></span> Same as Registrant Info.</label>
			<label class="checkbox"> <span><input type="checkbox" id="admin-checkbox-2" value="option"></span> Same as Billing Info.</label>
			<label class="checkbox"> <span><input type="checkbox" id="admin-checkbox-3" value="option"></span> Same as Technical Info.</label>
			<hr>
			<div class="form-group">
				<label class="col-md-4  control-label">First Name</label>
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
				<label class="col-md-4  control-label">Email</label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="adminContactDetails.email" class="form-control simple-input admin-required admin">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4 control-label">Phone Number</label>
				<div class="col-md-8">
					<input type=text name="adminContactDetails.phoneNumber" class="form-control simple-input admin-required admin">
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
				<label class="col-md-4  control-label">City</label>
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
				<label class="col-md-4  control-label">Address</label>
				<div class="col-md-8 col-xs-12">
					<textarea name="adminContactDetails.address" value="" class="form-control simple-input textarea admin-required admin" data="textarea">${form.adminContactDetails.address}</textarea>
				</div>
			</div>
		</div>
	</div>
</div>