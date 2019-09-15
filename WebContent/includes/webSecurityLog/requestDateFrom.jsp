<%@page import="java.text.SimpleDateFormat"%>
<%
String requestTime = (String)session.getAttribute("awslTimeFrom");
if(requestTime == null)requestTime="";
%>
<!-- <input type="hidden" name="mode" value="search"> -->
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Attempt Date From</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
        	<input type="text"  name="awslTimeFrom" class="form-control datepicker" value =<%=requestTime %> >
	</div>
</div>
