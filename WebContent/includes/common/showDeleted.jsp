<%
	String selected = (String)session.getAttribute("showDeleted");
	
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Include Deleted</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<select class="form-control pull-right" name="showDeleted" >
			<option value="0" <%=(selected==null || (selected!=null && selected.equals("0")))?"selected":"" %> >No</option>
			
			<option value="1" <%=(selected !=null && selected.equals("1")) ? "selected" : "" %>>Yes</option>
		</select>
	</div>
</div>