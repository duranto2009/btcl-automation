<!-- Individual -->
<%@page import="client.ClientTypeConstants"%>
<%@page import="client.ClientTypeService"%>
<div id="regIndividualInfo">
	<h3 class="form-section">Person Info</h3>
	<input type=hidden name="clientDetailsDTO.registrantType" value="0">
	<input type=hidden name="clientDetailsDTO.regiCat" value="0">
	
	<div class="row">
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">First Name<span class=required aria-required=true>*</span></label>
				<div class=col-sm-8>
					<input type=text name="registrantContactDetails.registrantsName" class="form-control individual regi">
				</div>
			</div>
		</div>
     	<div class="col-md-6">
     		<div class=form-group>
				<label class="col-sm-4 control-label">Last Name</label>
				<div class=col-sm-8>
					<input type=text name="registrantContactDetails.registrantsLastName" class="form-control individual regi">
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">Father Name</label>
				<div class=col-sm-8>
					<input type=text name="registrantContactDetails.fatherName" class="form-control">
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">Mother Name</label>
				<div class=col-sm-8>
					<input type=text name="registrantContactDetails.motherName" class="form-control">
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">Gender</label>
				<div class=col-sm-8>
					<select name="registrantContactDetails.gender" class="form-control">
						<option value="male">Male</option><option value="female">Female</option>
					</select>
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">Email<span class=required aria-required=true>*</span></label>
				<div class=col-sm-8>
					<input type=text name="registrantContactDetails.email" class="form-control individual regi">
				</div>
			</div>
		</div>
	</div>
          
	<div class="row">
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">Mobile Number<span class=required aria-required=true>*</span></label>
				<div class=col-sm-8>
					<input name="intlMobileNumber" class="phoneNumber form-control individual regi" type="tel" value="">
					<span class="hide valid-msg"> Mobile number is valid</span> 
					<span class="hide error-msg"> Mobile number is invalid </span>
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">Telephone Number</label>
				<div class=col-sm-8>
					<input type=text name="registrantContactDetails.landlineNumber" class="form-control individual regi"> 
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">Fax</label>
				<div class=col-sm-8>
					<input type=text name="registrantContactDetails.faxNumber" class="form-control individual regi">
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">Date of Birth<span class=required aria-required=true>*</span></label>
				<div class=col-sm-8>
					<input type=text name="registrantContactDetails.dateOfBirth" class="form-control datepicker">
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
     	<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">Occupation</label>
				<div class=col-sm-8>
					<input type=text name="registrantContactDetails.occupation" class="form-control">
				</div>
			</div>
		</div>
	</div>
</div>

<script>
$(document).ready(function(){
	$('.datepicker').datepicker({
	   	orientation: "top",
	    autoclose: true,
	    format: 'dd/mm/yyyy',
	});
});
</script>

