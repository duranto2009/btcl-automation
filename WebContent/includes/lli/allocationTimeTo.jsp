<%@page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	String toTime = (String)session.getAttribute("allocation-date-to");
	if(toTime == null)toTime="";

	
%>

<div class=form-group>
	<label class="control-label col-md-4 col-sm-4 col-xs-4">Allocation To</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
		<div class="input-group date">
			<div class=input-group-addon>
				<i class="fa fa-calendar"></i>
			</div>
			<input type=text  name="allocation-date-to" class="form-control datepicker"  value =<%=toTime%> >
		</div>
	</div>
</div>
