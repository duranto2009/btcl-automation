<%@page import="java.text.SimpleDateFormat"%>
<%
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
String requestTime = (String)session.getAttribute("dateFrom");
if(requestTime == null)requestTime="";
System.out.println("request Time " + requestTime);
%>

<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Date From</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
		<div class="input-group date">
             <div class="input-group-addon">
             	<i class="fa fa-calendar"></i>
             </div>
			<input type="text"  name="dateFrom" class="form-control datepicker" id="dateFrom"  value =<%=requestTime %> >
		</div>
	</div>
</div>
