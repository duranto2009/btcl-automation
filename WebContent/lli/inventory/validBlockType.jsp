<%
	String requested = (String) session.getAttribute("isValidBlock");
	if (requested == null) {
		requested = "";
	}
	
	if(request.getParameter("isValidBlock")!=null){
%>

	<input type="hidden" name="isValidBlock" value="<%= request.getParameter("isValidBlock")%>"/>

<%}else{ %>
	<div class="form-group">
		<label  class="control-label col-md-4 col-sm-4 col-xs-4">Valid Block</label>
		<div class="col-md-8 col-sm-8 col-xs-8">
			<select name="isValidBlock"
				class="form-control">
				<option value="" <%=("").equals(requested)?"selected": "" %>>ALL</option>
				<option value="1" <%=("1").equals(requested)?"selected": "" %> >YES</option>
				<option value="0" <%=("0").equals(requested)?"selected": "" %>>NO</option>
			</select>
		</div>
	</div>
<% }%>