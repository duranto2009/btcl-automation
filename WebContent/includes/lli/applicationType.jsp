<%@page import="lli.connection.LLIConnectionConstants"%>
<%@page import="inventory.InventoryConstants"%>
<%
	String requested = (String) session.getAttribute("applicationType");
	if (requested == null) {
		requested = "";
	}
%>
<div class="form-group">
	<label class="control-label col-md-4 col-sm-4 col-xs-4">Application Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select name="applicationType" class="form-control">
			<option value="" <%="".equals(requested)?"selected": "" %>>All</option>
			<%
				for(int i: LLIConnectionConstants.applicationTypeNameMap.keySet()){
			%>
				<option value=<%=i %> <%=(i+"").equals(requested) ? "selected" : "" %>><%=LLIConnectionConstants.applicationTypeNameMap.get(i) %></option>
			<%		
				}
			%>
		</select>
	</div>
</div>