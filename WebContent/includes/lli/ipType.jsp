<%@page import="inventory.InventoryConstants"%>
<%
	String requested = (String) session.getAttribute("IPType");
	if (requested == null) {
		requested = "";
	}
%>

<div class=form-group>
	<label class="control-label col-md-4 col-sm-4 col-xs-4">IP Type</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select name="IPType" class="form-control">
			<option value=""<%=("").equals(requested) ? "selected" : ""%> > Select IP</option>
			<option value="<%=InventoryConstants.IP_PUBLIC %>" 
				<%=("" + InventoryConstants.IP_PUBLIC).equals(requested) ? "selected" : ""%>>
					<%=InventoryConstants.mapOfIPTypeNameToIPType.get(InventoryConstants.IP_PUBLIC) %>
			</option>
			<option value="<%=InventoryConstants.IP_PRIVATE %>" 
				<%=("" + InventoryConstants.IP_PRIVATE).equals(requested) ? "selected" : ""%> >
				<%=InventoryConstants.mapOfIPTypeNameToIPType.get(InventoryConstants.IP_PRIVATE) %>
			</option>
		</select>
	</div>
</div>