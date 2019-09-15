<%@page import="common.ClientConstants"%>
<div class="portlet box green-haze">
	<div class="portlet-title">
		<div class="caption">Technical</div>
		<div class="actions">
			<input type=button id=reset-technical class="btn btn-default" value="Clear">
		</div>
	</div>
	<div class="portlet-body form">
	
		<input type=hidden name="technicalContactDetails.detailsType" value="<%=ClientConstants.DETAILS_TYPE_TECHNICAL%>" />
		
		<div class="form-body">
			<!-- 
			<label class="checkbox"> <span><input type="checkbox" id="technical-checkbox-1" value="option"></span> Same as Registrant Info.</label>
			<label class="checkbox"> <span><input type="checkbox" id="technical-checkbox-2" value="option"></span> Same as Billing Info.</label>
			<label class="checkbox"> <span><input type="checkbox" id="technical-checkbox-3" value="option"></span> Same as Admin Info.</label>
			 -->
			<div class="form-group">
				<div class="col-md-12">
					<div class="col-md-3 control-label">Copy From</div>
					<button type=button id=copy-from-registrant-to-technical class="btn btn-default col-md-3">Registrant</button>
					<button type=button id=copy-from-bill-to-technical class="btn btn-default col-md-3">Billing</button>
					<button type=button id=copy-from-admin-to-technical class="btn btn-default col-md-3">Admin</button>
				</div>
			</div>
			<hr>
			<div class="form-group">
				<label class="col-md-4  control-label">First Name<span class=required aria-required=true>*</span></label>
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
				<label class="col-md-4  control-label">Email<span class=required aria-required=true>*</span></label>
				<div class="col-md-8 col-xs-12">
					<input type=text name="technicalContactDetails.email" class="form-control simple-input technical-required  technical">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4 control-label">Mobile Number<span class=required aria-required=true>*</span></label>
				<div class="col-md-8">
					<input type=text name="technicalContactDetails.phoneNumber" class="form-control simple-input technical-required  technical">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-4 control-label">Telephone Number</label>
				<div class="col-md-8">
					<input type=text name="technicalContactDetails.landlineNumber" class="form-control simple-input  technical">
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
				<label class="col-md-4  control-label">City<span class=required aria-required=true>*</span></label>
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
				<label class="col-md-4  control-label">Address<span class=required aria-required=true>*</span></label>
				<div class="col-md-8 col-xs-12">
					<textarea name="technicalContactDetails.address" value="" class="form-control simple-input textarea technical-required  technical" data="textarea">${form.technicalContactDetails.address}</textarea>
				</div>
			</div>
		</div>
	</div>
</div>