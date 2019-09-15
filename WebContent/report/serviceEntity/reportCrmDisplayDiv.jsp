<%@page import="common.ModuleConstants"%>
<div class="portlet light">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-tv"></i>Display
		</div>
	</div>
	<!-- /.box-header -->
	<div class="portlet-body form" style="height: 30vh; overflow-x: hidden; overflow-y:  scroll;">
		<div class="form-body">
		
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="1"></span>ID</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.CrmCommonPoolDTO.ID" value="ID" disabled
					data-operator="eq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
				
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="2"></span>Client Name</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientDTO.loginName" value="Client Name" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="3"></span>Client Complain</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.CrmCommonPoolDTO.clientComplain" value="Client Complain" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="4"></span>Feedback Of NOC</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.CrmCommonPoolDTO.feedbackOfNoc" value="Feedback Of NOC" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="5"></span>Resolver Name</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.CRMInventoryItem.name" value="Resolver Name" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="6"></span>Email</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientContactDetailsDTO.email" value="Email" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="7"></span>Mobile</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientContactDetailsDTO.phoneNumber" value="Mobile" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="8"></span>Priority</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.CrmCommonPoolDTO.priority" value="Priority" disabled
					data-operator="eq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="9"></span>Status</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.CrmCommonPoolDTO.status" value="Status" disabled
					data-operator="eq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="10"></span>Subject</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.CrmCommonPoolDTO.subject" value="Subject" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="11"></span>Submission Date From</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.CrmCommonPoolDTO.submissionTime" value="Submission Date From" disabled
					data-operator="geq" data-comment="datepicker">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="12"></span>Submission Date To</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.CrmCommonPoolDTO.submissionTime" value="Submission Date To" disabled
					data-operator="leq" data-comment="datepicker">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../upDownArrow.jsp"%></div>
			</div>
		</div>
	</div>
	<!-- /.box-body -->
</div>
