<%@page import="common.ClientConstants"%>
<div class="portlet box green-haze">
	<div class="portlet-title">
		<div class="caption">Billing</div>
		<div class="actions">
			<input type=button id=reset-bill class="btn btn-default" value="Clear">
		</div>
	</div>
	<div class="portlet-body form">
	
		<input type=hidden name="billingContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_BILLING%>" />
	
		<div class="form-body">
			<!-- 
			<label class="checkbox"> <span><input type="checkbox" id="bill-checkbox-1" value="1"></span> Same as Registrant Info.</label>
			<label class="checkbox"> <span><input type="checkbox" id="bill-checkbox-2" value="2"></span> Same as Admin Info.</label>
			<label class="checkbox"> <span><input type="checkbox" id="bill-checkbox-3" value="3"></span> Same as Technical Info.</label>
			 -->
			<div class="form-group">
				<div class="col-md-12">
					<div class="col-md-3 control-label">Copy From</div>
					<button type=button id=copy-from-registrant-to-bill class="btn btn-default col-md-3">Registrant</button>
					<button type=button id=copy-from-admin-to-bill class="btn btn-default col-md-3">Admin</button>
					<button type=button id=copy-from-technical-to-bill class="btn btn-default col-md-3">Technical</button>
				</div>
			</div>
			<hr>
			<div class="form-group">
				<label class="col-md-4 control-label">First Name<span class=required aria-required=true>*</span></label>
				<div class="col-md-8 col-xs-12">
					<input type=text name=billingContactDetails.registrantsName
						   class="form-control simple-input bill-required bill" >
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Last Name</label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="billingContactDetails.registrantsLastName" class="form-control simple-input  bill">
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-md-4  control-label">Email<span class=required aria-required=true>*</span></label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="billingContactDetails.email" class="form-control simple-input bill-required bill">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4 control-label">Mobile Number<span class=required aria-required=true>*</span></label>
				<div class="col-md-8">
					<input type=text name="billingContactDetails.phoneNumber" class="form-control simple-input bill-required bill">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4 control-label">Telephone Number</label>
				<div class="col-md-8">
					<input type=text name="billingContactDetails.landlineNumber" class="form-control simple-input bill">
				</div>
			</div>
			
			<div class="form-group">
				<label class="col-md-4 control-label">Fax Number</label>
				<div class="col-md-8">
					<input type=text name="billingContactDetails.faxNumber" class="form-control simple-input bill">
				</div>
			</div>
		
			<hr>
			
			<div class="form-group">
				<label class="col-md-4  control-label">City<span class=required aria-required=true>*</span></label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="billingContactDetails.city" class="form-control simple-input bill-required bill">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Post Code</label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="billingContactDetails.postCode" class="form-control simple-input  bill">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4  control-label">Address<span class=required aria-required=true>*</span></label>
				<div class="col-md-8 col-xs-12">
					<textarea name="billingContactDetails.address" class="form-control simple-input textarea bill-required bill" data="textarea">${form.billingContactDetails.address}</textarea>
				</div>
			</div>
		</div>
	</div>
</div>