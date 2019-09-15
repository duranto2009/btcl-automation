<%@page import="lli.connection.LLIConnectionConstants"%>
<%@page import="inventory.InventoryConstants"%>
<%
	String requested = (String) session.getAttribute("applicationStatus");
	if (requested == null) {
		requested = "";
	}
%>
<div class="form-group">
	<label class="control-label col-md-4 col-sm-4 col-xs-4">Application Status</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select name="applicationStatus" class="form-control">
			<option value="" <%="".equals(requested)?"selected": "" %>>All</option>
			<%
				for(int i: LLIConnectionConstants.applicationStatusMap.keySet()){
			%>
				<option value=<%=i %> <%=(i+"").equals(requested) ? "selected" : "" %>><%=LLIConnectionConstants.applicationStatusMap.get(i) %></option>
			<%		
				}
			%>
		</select>
	</div>
</div>