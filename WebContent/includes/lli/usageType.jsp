<%@page import="util.SOP"%>
<%@page import="inventory.InventoryConstants"%>
<%
	String requested = (String) session.getAttribute("usageType");
	if (requested == null) {
		requested = "";
	}
%>

<div class="form-group">
	<label class="control-label col-md-4 col-sm-4 col-xs-4">Usage Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select name="usageType"
			class="form-control">
			<option value="" <%=("").equals(requested) ? "selected" : "" %>>Select Usage Type</option>
			<option value="<%=InventoryConstants.USAGE_ESSENTIAL %>"
				<%=("" + InventoryConstants.USAGE_ESSENTIAL).equals(requested) ? "selected" : "" %> >
				<%=InventoryConstants.mapOfUsageTypeNameToUsageType.get(InventoryConstants.USAGE_ESSENTIAL) %>
			</option>
			<option value="<%=InventoryConstants.USAGE_ADDITIONAL%>" 
			 <%=("" + InventoryConstants.USAGE_ADDITIONAL).equals(requested) ? "selected" : "" %> >
				<%=InventoryConstants.mapOfUsageTypeNameToUsageType.get(InventoryConstants.USAGE_ADDITIONAL) %>
			</option>
		</select>
	</div>
</div>