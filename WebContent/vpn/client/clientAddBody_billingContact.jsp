<div class="portlet box green-haze">
	<div class="portlet-title">
		<div class="caption"></i>Billing Contact</div>
	</div>
	<!-- /.box-header -->
	<div class="portlet-body form">
		<div class="form-body">
			<label class="checkbox"> <span><input type="checkbox" id="bill-checkbox-1" value="1"></span> Same as Registrant Info.</label>
			<label class="checkbox"> <span><input type="checkbox" id="bill-checkbox-2" value="2"></span> Same as Admin Info.</label>
			<label class="checkbox"> <span><input type="checkbox" id="bill-checkbox-3" value="3"></span> Same as Technical Info.</label>
			<hr>
			<div class="form-group">
				<label class="col-md-4  control-label">First Name</label>
				<div class="col-md-8 col-xs-12">
					<html:text property="billingContactDetails.registrantsName" styleClass="form-control simple-input bill-required bill" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Last Name</label>
				<div class="col-md-8 col-xs-12">
					<html:text property="billingContactDetails.registrantsLastName" styleClass="form-control simple-input  bill" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-md-4  control-label">Email</label>
				<div class="col-md-8 col-xs-12">
					<html:text property="billingContactDetails.email" styleClass="form-control simple-input bill-required bill" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4 control-label">Phone Number</label>
				<div class="col-md-8">
					<html:text property="billingContactDetails.phoneNumber" styleClass="form-control simple-input bill-required bill" />
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-md-4 control-label">Fax Number</label>
				<div class="col-md-8">
					<html:text property="billingContactDetails.faxNumber" styleClass="form-control simple-input  bill" />
				</div>
			</div>
		
			<hr>
			
			<div class="form-group">
				<label class="col-md-4  control-label">City</label>
				<div class="col-md-8 col-xs-12">
					<html:text property="billingContactDetails.city" styleClass="form-control simple-input bill-required bill" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Post Code</label>
				<div class="col-md-8 col-xs-12">
					<html:text property="billingContactDetails.postCode" styleClass="form-control simple-input  bill" />
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Address</label>
				<div class="col-md-8 col-xs-12">
					<textarea name="billingContactDetails.address" class="form-control simple-input textarea bill-required bill" data="textarea">${form.billingContactDetails.address}</textarea>
				</div>
			</div>
			
			<!-- <div class="form-group">
				<label class="col-md-4 control-label">Signature</label>
				<div class="col-md-8">
					<html:text property="billingContactDetails.signature" styleClass="form-control simple-input bill-required bill" />
				</div>
			</div> -->
		</div>
	</div>
	<!-- /.box-body -->
</div>