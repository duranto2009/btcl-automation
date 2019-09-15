<%
	String title = request.getParameter("title");

	String additionalTitle = "";
	String inputName = title;
	if(title.contains("<")){
		int limit1 = title.indexOf("<");
		int limit2 = title.indexOf(">");
		additionalTitle += " ( " + title.substring(limit1+1, limit2) + " ) ";
		inputName = title.substring(0, limit1);
	}
	String toTime = (String)session.getAttribute(inputName);
	if(toTime == null ) toTime="";

%>
<div class=form-group>
	<label class="control-label col-md-4 col-sm-4 col-xs-4">To Date<%=additionalTitle %></label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
		<div class="input-group date">
			<div class=input-group-addon>
				<i class="fa fa-calendar"></i>
			</div>
			<input type=text  name="<%=inputName %>" class="form-control datepicker" value ="<%=toTime%>" autocomplete=off>
		</div>
	</div>
</div>


