<%@page import="common.EntityTypeConstant"%>
<%@ page language="java"%>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = (String) session.getAttribute("isActive");
	if (requested == null) {
		requested = "";
	}
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Status</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control pull-right" name="isActive" >
			<option value="" <%if (requested.equals("") ){%> selected <%}%>>All</option>
			<option value="0" <%if (requested.equals(""+0) ){%> selected <%}%>>Unavailable</option>
			<option value="1" <%if (requested.equals(""+1) ){%> selected <%}%>>Available</option>
		</select>
	</div>
</div>