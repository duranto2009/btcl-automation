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
					<input type="text" class="display-input" name="display.CommonRequestDTO.reqID" value="ID" disabled
					data-operator="eq" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../report/upDownArrow.jsp"%></div>
			</div>
				
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="2"></span>Request Name</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ActionStateDTO.description" value="Request Name" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../report/upDownArrow.jsp"%></div>
			</div>	
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="5"></span>Client Name</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.ClientDTO.loginName" value="Client Name" disabled
					data-operator="like" data-comment="">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../report/upDownArrow.jsp"%></div>
			</div>
			
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="6"></span>Request Date</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.CommonRequestDTO.requestTime" value="Request Date" disabled
					data-operator="geq" data-comment="datepicker">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../report/upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="7"></span>Request Date To</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.CommonRequestDTO.requestTime" value="Request Date To" disabled
					data-operator="leq" data-comment="datepicker">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../report/upDownArrow.jsp"%></div>
			</div>
			
			<div class="form-group">
				<div class="col-md-5">
					<label class="checkbox"><span><input type="checkbox" class="input-checkbox-display" value="8"></span>Status</label>
				</div>
				<div class="col-md-5">
					<input type="text" class="display-input" name="display.CommonRequestDTO.completionStatus" value="Status" disabled
					data-operator="eq" data-comment="select" data-values="Pending:0,Processed:1,Complete:2">
				</div>
				<div class="col-md-2" style="position: relative;"><%@include file="../report/upDownArrow.jsp"%></div>
			</div>
		
		</div>
	</div>
	<!-- /.box-body -->
</div>
