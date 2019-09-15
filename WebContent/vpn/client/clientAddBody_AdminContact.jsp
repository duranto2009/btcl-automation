<div class="portlet box green-haze">
	<div class="portlet-title">
		<div class="caption">Admin Contact</div>
	</div>
	<!-- /.box-header -->
	<div class="portlet-body form">
		<div class="form-body">
			<label class="checkbox"> <span><input type="checkbox" id="admin-checkbox-1" value="option"></span> Same as Registrant Info.</label>
			<label class="checkbox"> <span><input type="checkbox" id="admin-checkbox-2" value="option"></span> Same as Billing Info.</label>
			<label class="checkbox"> <span><input type="checkbox" id="admin-checkbox-3" value="option"></span> Same as Technical Info.</label>
			<hr>
			<div class="form-group">
				<label class="col-md-4  control-label">First Name</label>
				<div class="col-md-8 col-xs-12">
					<html:text property="adminContactDetails.registrantsName" styleClass="form-control simple-input admin-required admin" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Last Name</label>
				<div class="col-md-8 col-xs-12">
					<html:text property="adminContactDetails.registrantsLastName" styleClass="form-control simple-input admin" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-md-4  control-label">Email</label>
				<div class="col-md-8 col-xs-12">
					<html:text property="adminContactDetails.email" styleClass="form-control simple-input admin-required admin" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4 control-label">Phone Number</label>
				<div class="col-md-8">
					<html:text property="adminContactDetails.phoneNumber" styleClass="form-control simple-input admin-required admin" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-md-4 control-label">Fax Number</label>
				<div class="col-md-8">
					<html:text property="adminContactDetails.faxNumber" styleClass="form-control simple-input  admin" />
				</div>
			</div>

			
			<hr>
			
			<div class="form-group">
				<label class="col-md-4  control-label">City</label>
				<div class="col-md-8 col-xs-12">
					<html:text property="adminContactDetails.city" styleClass="form-control simple-input admin-required admin" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Post Code</label>
				<div class="col-md-8 col-xs-12">
					<html:text property="adminContactDetails.postCode" styleClass="form-control simple-input admin" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Address</label>
				<div class="col-md-8 col-xs-12">
					<textarea name="adminContactDetails.address" value="" class="form-control simple-input textarea admin-required admin" data="textarea">${form.adminContactDetails.address}</textarea>
				</div>
			</div>
			
			<!-- <div class="form-group">
				<label class="col-md-4 control-label">Signature</label>
				<div class="col-md-8">
					<html:text property="adminContactDetails.signature" styleClass="form-control simple-input admin-required admin" />
				</div>
			</div>
 -->
		</div>
	</div>
	<!-- /.box-body -->
</div>