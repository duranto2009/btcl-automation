<%@page import="common.EntityTypeConstant"%>
<%@page import="crm.*" %>
<div class="form-group">
	<label class="control-label col-md-4 col-sm-4 col-xs-4">Action Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select name="actionType"	class="form-control">
			<option value="">Action Type</option>
			<option value="<%=CrmActivityLog.COMPLAIN_PASS%>"><%=EntityTypeConstant.PASS_COMPLAIN %></option>
			<option value="<%=CrmActivityLog.COMPLAIN_ASSIGN%>"><%=EntityTypeConstant.ASSIGN_COMPLAIN %></option>
			<option value="<%=CrmActivityLog.COMPLAIN_FEEDBACK%>"><%=EntityTypeConstant.FEEDBACK_COMPLAIN %></option>
			<option value="<%=CrmActivityLog.COMPLAIN_STATUS_CHANGE%>"><%=EntityTypeConstant.STATUS_CHANGE_COMPLAIN %></option>
			<option value="<%=CrmActivityLog.COMPLAIN_REJECT%>"><%=EntityTypeConstant.REJECT_COMPLAIN %></option>
			<option value="<%=CrmActivityLog.ATTEMP_TO_COMPLETE_COMPLAIN%>"><%=EntityTypeConstant.ATTEMPT_TO_COMPLETE_COMPLAIN %></option>
			<option value="<%=CrmActivityLog.COMPLETE_COMPLAIN%>"><%=EntityTypeConstant.COMPLETE_COMPLAIN %></option>
		</select>
	</div>
</div>