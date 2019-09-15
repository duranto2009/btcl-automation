<%@page import="java.text.SimpleDateFormat"%>
<%
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
String requestTime = (String)session.getAttribute("arReqTimeTo");

if(requestTime == null)requestTime="";
System.out.println("request Time " + requestTime);
%>

<!-- <input type="hidden" name="mode" value="search"> -->
<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4"> Request Date To</label>
	<div class="col-md-8 col-sm-8 col-xs-8">
			<input type="text" name="arReqTimeTo" class="form-control datepicker" id="expireDateTo" value=<%=requestTime %>>
	</div>
</div>
