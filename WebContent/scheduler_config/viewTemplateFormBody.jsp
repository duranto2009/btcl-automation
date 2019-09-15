
<%@page import="permission.PermissionRepository"%>
<div class="portlet box portlet-btcl">
	<div class="portlet-title">
		<div class="caption">
			<i class="fa fa-clock-o"></i><%=request.getParameter("title") %>
		</div>
	</div>
	<div class="portlet-body">
		<form class="form-horizontal" method="POST" action="" id="tableForm">
			<div class="form-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">Entity Type ID</label>
					<div class="col-sm-6">
						<input type="number" class="form-control" name="entityTypeID">	
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Entity Condition String</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="entityConditionString">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Client Contact Details Type</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="contactDetailsType">
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">Notification Template</label>
					<div class="col-sm-6">
						<textarea  class="form-control" name="notificationTemplate">
						</textarea>
					</div>
				</div>
				<div class="form-group">
					
					<div class="col-sm-5 control-label">
						<input type="checkbox" class="checkbox-inline" name="emailNotification">
						<label>Send Email</label>
					</div>
					
					<div class="col-sm-3 control-label">
						<input type="checkbox" class="checkbox-inline" name="smsNotification">
						<label>Send SMS</label>
					</div>
				</div>
				
			</div>
			<div class="form-actions text-center">
				<button class="btn btn-cancel-btcl" type="submit" id="preview" data-action="../Scheduler/getPreviewTemplate.do">Preview</button>
				<button class="btn btn-submit-btcl" type="submit" id="send" data-action="../Scheduler/sendTemplate.do">Send</button>
			</div>
		</form>
	</div>
</div>


<script>
$(document).ready(function(){
	var form,action;
	form = $('#tableForm');
	$('#preview').click(function(){
		action = $(this).data('action')
		form.attr('action', action);
		form.submit();
		return false;
	});
	$('#send').click(function(){
		action = $(this).data('action')
		form.attr('action', action);
		form.submit();
		return false;
	});
});
</script>
