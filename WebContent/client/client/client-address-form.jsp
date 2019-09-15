<!-- Address -->
<%@page import="java.util.Locale"%>
<div>
	<h3 class="form-section">Address</h3>
	<div class="row">
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">Country<span class=required aria-required=true>*</span></label>
				<div class=col-sm-8>
					<select class="form-control" name=registrantContactDetails.country>
						<%String[] locales = Locale.getISOCountries();
						for (String countryCode : locales) {
							Locale obj = new Locale("", countryCode);%>
						<option value="<%= obj.getCountry()%>"><%=obj.getDisplayCountry()%></option>
						<%}%>
					</select>
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">City<span class=required aria-required=true>*</span></label>
				<div class=col-sm-8>
					<input type=text name=registrantContactDetails.city class="form-control regi"/>
				</div>
			</div>
		</div>
	</div>
		
	<div class="row">
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">Post Code</label>
				<div class=col-sm-8>
					<input type=text name=registrantContactDetails.postCode class="form-control regi"/>
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class=form-group>
				<label class="col-sm-4 control-label">Address<span class=required aria-required=true>*</span></label>
				<div class=col-sm-8>
					<textarea name=registrantContactDetails.address class="form-control border-radius textarea regi"></textarea>
				</div>
			</div>
		</div>
	</div>
</div>