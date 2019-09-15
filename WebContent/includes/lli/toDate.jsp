<%@page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	String toTime = (String)session.getAttribute("toDate");
	if(toTime == null)toTime="";
%>

<div class=form-group>
	<label class="control-label col-md-4 col-sm-4 col-xs-4">To Date</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
		<div class="input-group date">
			<div class=input-group-addon>
				<i class="fa fa-calendar"></i>
			</div>
			<input type=text  name="toDate" class="form-control datepicker"  value ="<%=toTime%>" autocomplete=off>
		</div>	
	</div>
</div>