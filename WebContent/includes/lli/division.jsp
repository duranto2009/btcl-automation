<%@page import="inventory.InventoryConstants"%>
<%
	String requested = (String) session.getAttribute("divisionID");
	if (requested == null) {
		requested = "";
	}
%>
<div class="form-group">
	<label class="control-label col-md-4 col-sm-4 col-xs-4">Division</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select name="divisionID" class="form-control">
			<option value="" <%="".equals(requested)?"selected": "" %>>Select Division</option>
			<%
				for(int i: InventoryConstants.mapOfDivisionNameToDivisionID.keySet()){
			%>
				<option value=<%=i %> <%=(i+"").equals(requested) ? "selected" : "" %>><%=InventoryConstants.mapOfDivisionNameToDivisionID.get(i) %></option>
			<%		
				}
			%>
		</select>
	</div>
</div>