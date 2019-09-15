<%@page import="lli.LLILongTermContract"%>
<%@ page import="java.util.Map"%>
<%
	String title = request.getParameter("title");
	String requested = (String) session.getAttribute(title);
	if (requested == null) {
		requested = "";
	}
	
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Status</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control pull-right" name="<%=title %>" >
			<option value="" <%if (requested.equals("") ){%> selected <%}%>>All</option>
		<% for(Map.Entry<Integer, String> entry : LLILongTermContract.statusMap.entrySet()) {%>
			<option value="<%=entry.getKey() %>" <%=requested.equals(""+entry.getKey()) ? "selected":"" %> ><%=entry.getValue() %></option>
		<% }	%>	
		</select>
	</div>
</div>