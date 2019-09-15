<%
	
	String toSize = (String)session.getAttribute("blockSizeTo");
	if(toSize == null)toSize="";
%>

<div class=form-group>
	<label class="control-label col-md-4 col-sm-4 col-xs-4">Block Size To</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
		<input type=text  name=blockSizeTo class="form-control"  value =<%=toSize%> >
	</div>
</div>
