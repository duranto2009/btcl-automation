<div class="portlet box green-haze">
	<div class="portlet-title">
		<div class="caption">Technical Contact</div>
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
					<html:text property="technicalContactDetails.registrantsName" styleClass="form-control simple-input technical-required  technical" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Last Name</label>
				<div class="col-md-8 col-xs-12">
					<html:text property="technicalContactDetails.registrantsLastName" styleClass="form-control simple-input  technical" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Email</label>
				<div class="col-md-8 col-xs-12">
					<html:text property="technicalContactDetails.email" styleClass="form-control simple-input technical-required  technical" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4 control-label">Phone Number</label>
				<div class="col-md-8">
					<html:text property="technicalContactDetails.phoneNumber" styleClass="form-control simple-input technical-required  technical" />
				</div>
			</div>
		
			<div class="form-group">
				<label class="col-md-4 control-label">Fax Number</label>
				<div class="col-md-8">
					<html:text property="technicalContactDetails.faxNumber" styleClass="form-control simple-input   technical" />
				</div>
			</div>
			
			<hr>
			
			<div class="form-group">
				<label class="col-md-4  control-label">City</label>
				<div class="col-md-8 col-xs-12">
					<html:text property="technicalContactDetails.city" styleClass="form-control simple-input technical-required  technical" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Post Code</label>
				<div class="col-md-8 col-xs-12">
					<html:text property="technicalContactDetails.postCode" styleClass="form-control simple-input  technical" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Address</label>
				<div class="col-md-8 col-xs-12">
					<textarea name="technicalContactDetails.address" value="" class="form-control simple-input textarea technical-required  technical" data="textarea">${form.technicalContactDetails.address}</textarea>
				</div>
			</div>
			
			<!-- <div class="form-group">
				<label class="col-md-4 control-label">Signature</label>
				<div class="col-md-8">
					<html:text property="technicalContactDetails.signature" styleClass="form-control simple-input technical-required  technical" />
				</div>
			</div> -->
		</div>
	</div>
	<!-- /.box-body -->
</div>