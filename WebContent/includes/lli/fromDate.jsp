<%@page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	String fromTime = (String)session.getAttribute("fromDate");
	if(fromTime == null ) fromTime="";
%>
<div class=form-group>
	<label class="control-label col-md-4 col-sm-4 col-xs-4">From Date</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
		<div class="input-group date">
			<div class=input-group-addon>
				<i class="fa fa-calendar"></i>
			</div>
			<input type=text  name=fromDate class="form-control datepicker" value ="<%=fromTime%>" autocomplete=off>	
		</div>
	</div>
</div>


