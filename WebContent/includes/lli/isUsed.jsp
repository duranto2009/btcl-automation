<%@page import="inventory.InventoryConstants"%>
<%
	String requested = (String) session.getAttribute("isUsed");
	if (requested == null) {
		requested = "";
	}
%>

<div class=form-group>
	<label class="control-label col-md-4 col-sm-4 col-xs-4">Used</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select name="isUsed" class="form-control">
			<option value=""<%=("").equals(requested) ? "selected" : ""%> > Select</option>
			<option value="1" <%=("1").equals(requested) ? "selected" : ""%>>Yes</option>
					
			<option value="0" <%=("0").equals(requested) ? "selected" : ""%> >No </option>
		</select>
	</div>
</div>