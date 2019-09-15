<div style="padding:10px" class="client-autocomplete">
	<div class="row" style="margin-bottom:10px;display:none" id="checkbox-using-existing-client-info">
		<div class="col-md-6">
			<label class="checkbox"> <span><input type="checkbox" id="existing-check" ></span>
				<%if(loginDTO.getUserID()>0){%>Add module to existing Client <%}
				else{%>Copy information from another module<%}%>
			</label>
		</div>
	</div>
	<%if(loginDTO.getUserID()>0){%>
	<div id="using-existing-client-div" style="display:none">
		<div class="row" style="margin-bottom:10px">
			<div class="col-md-3">
				Client Name
			</div>
			<div class="col-md-6">
				<input class="form-control"  id="client-name-autocomplete" type="text">
				<input id="client-id-autocomplete" type="hidden" value="-1" name="clientDetailsDTO.existingClientID">
			</div>
		</div>
		<div class="row">
			<div class="col-md-3">
				Copy Information from Module
			</div>
			<div class="col-md-6">
				<select class="form-control" id="client-module-selected">
					<option value="-1" selected="selected">Select Module</option>
				</select>
			</div>
		</div>
	</div>
	<%}else{%>
	<div id="using-existing-client-div" style="display:none">
		<input id="client-id-autocomplete" type="hidden" value="<%=loginDTO.getAccountID() %>" name="clientDetailsDTO.existingClientID">
		<div class="row">
			<div class="col-md-3">Copy Information from Module</div>
			<div class="col-md-6">
				<select class="form-control" id="client-module-selected">
					<option value="-1" selected="selected">Select Module</option>
				</select>
			</div>
		</div>
	</div>
	<%}%>
</div>