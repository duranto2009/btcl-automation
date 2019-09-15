<%@page import="java.text.SimpleDateFormat"%>
<%
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
String requestTime = (String)session.getAttribute("activationDateFrom");
if(requestTime == null)requestTime="";
System.out.println("request Time " + requestTime);
%>

<input type="hidden" name="mode" value="search">
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Activation Date From</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
		<input type="text"  name="activationDateFrom" class="form-control datepicker" id="activationDateFrom"  value =<%=requestTime %>>
	</div>
</div>

