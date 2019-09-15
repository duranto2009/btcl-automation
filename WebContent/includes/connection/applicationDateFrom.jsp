<%@page import="java.text.SimpleDateFormat"%>
<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
String requestTime = (String)session.getAttribute("applicationDateFrom");
/* if(orderTime == null)orderTime=format.format(System.currentTimeMillis()); */
if(requestTime == null)requestTime="";
System.out.println("request Time " + requestTime);
%>

<script src="${context}scripts/bootstrap-datepicker.js"></script>

<link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">

<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Application Date From</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
<div class="input-group date ">
                  <div class="input-group-addon">
                    <i class="fa fa-calendar"></i>
                  </div>
                  <input type="text"  name="applicationDateFrom" class="form-control pull-right" id="applicationDateFrom"  value =<%=requestTime %> >
                </div>
</div>
</div>


<script>
$(document).ready(function(){
	
	$('#applicationDateFrom').datepicker({
	    dateFormat: 'dd/mm/yy',
	    autoclose: true
	  });
});

</script>