<div class="portlet box light">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-exclamation-circle"></i>Technical Contact
		</div>
	</div>
	<div class="portlet-body form">
		<div class="form-body">
			<label class="checkbox"> <span><input type="checkbox" id="technical-checkbox-1" value="option"></span> Same as Registrant Info.</label>
			<label class="checkbox"> <span><input type="checkbox" id="technical-checkbox-2" value="option"></span> Same as Billing Info.</label>
			<label class="checkbox"> <span><input type="checkbox" id="technical-checkbox-3" value="option"></span> Same as Admin Info.</label>
			<hr>
			<div class="form-group">
				<label class="col-md-4  control-label">First Name</label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="technicalContactDetails.registrantsName" class="form-control simple-input technical-required  technical">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Last Name</label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="technicalContactDetails.registrantsLastName" class="form-control simple-input   technical">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Email</label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="technicalContactDetails.email" class="form-control simple-input technical-required  technical">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4 control-label">Phone Number</label>
				<div class="col-md-8">
					<input type=text name="technicalContactDetails.phoneNumber" class="form-control simple-input technical-required  technical">
				</div>
			</div>
		
			<div class="form-group">
				<label class="col-md-4 control-label">Fax Number</label>
				<div class="col-md-8">
					<input type=text name="technicalContactDetails.faxNumber" class="form-control simple-input  technical">
				</div>
			</div>
			
			<hr>
			
			<div class="form-group">
				<label class="col-md-4  control-label">City</label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="technicalContactDetails.city" class="form-control simple-input technical-required  technical">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Post Code</label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="technicalContactDetails.postCode" class="form-control simple-input  technical">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Address</label>
				<div class="col-md-8 col-xs-12">
					<textarea name="technicalContactDetails.address" value="" class="form-control simple-input textarea technical-required  technical" data="textarea">${form.technicalContactDetails.address}</textarea>
				</div>
			</div>
		</div>
	</div>
</div>