<%@page import="java.text.SimpleDateFormat"%>
<%
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
String requestTime = (String)session.getAttribute("arReqTimeFrom");
if(requestTime == null)requestTime="";
System.out.println("request Time " + requestTime);
%>
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Request Date From</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
        	<input type="text"  name="arReqTimeFrom" class="form-control datepicker" id="expireDateFrom"  value =<%=requestTime %> >
	</div>
</div>
