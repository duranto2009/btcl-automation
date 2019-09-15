<%@page import="vpn.constants.EndPointConstants"%>
<%@page import="common.EntityTypeConstant"%>
<%
	String requested = (String) session.getAttribute("portType");
	if (requested == null) {
		requested = "";
	}
%>
<div class="form-group">
	<label for="" class="control-label col-md-3 ">Port Type</label>
	<div class="col-md-4">
		<select id="portType" name="portType" class="form-control">
			<%
			 for (Integer key : EndPointConstants.portCategoryMap.keySet()) {
			 %>
				 	<option value="<%=key%>"><%= EndPointConstants.portCategoryMap.get(key)%></option>
			<% 
				}
			%>
		</select>
	</div>
</div>
