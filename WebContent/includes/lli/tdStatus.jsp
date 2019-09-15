<%@page import="lli.LLIClientTDStatus"%>
<%@ page language="java"%>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*"%>
<%
	String requested = (String) session.getAttribute("status");
	if (requested == null) {
		requested = "";
	}
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Status</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control pull-right" name="status" >
			<option value="" <%if (requested.equals("") ){%> selected <%}%>>All</option>
			<option value="<%=LLIClientTDStatus.ACTIVE %>" <%if (requested.equals(""+LLIClientTDStatus.ACTIVE) ){%> selected <%}%>>Active</option>
			<option value="<%=LLIClientTDStatus.TD %>" <%if (requested.equals(""+LLIClientTDStatus.TD) ){%> selected <%}%>>TD</option>
			
		</select>
	</div>
</div>