<%
	String fromSize = (String)session.getAttribute("blockSizeFrom");
	if(fromSize == null ) fromSize="";	

%>
<div class=form-group>
	<label class="control-label col-md-4 col-sm-4 col-xs-4">Block Size From </label>
	<div class="col-md-8 col-sm-8 col-xs-8">
		<input type=text  name=blockSizeFrom class=form-control value =<%=fromSize%> >
	</div>
</div>

