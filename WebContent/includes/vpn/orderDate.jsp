<%@page import="java.text.SimpleDateFormat"%>
<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
String orderTime = (String)session.getAttribute("ORTIME");
//if(orderTime == null)orderTime=format.format(System.currentTimeMillis());
if(orderTime == null)orderTime="";
System.out.println("orderTime " + orderTime);
%>


<link rel="stylesheet" type="text/css" href="${context }stylesheets/Calendar/calendar.css">


<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Order Date From</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
<div class='input-group'>
<input type = "text" name="ORTIME" value="<%=orderTime%>" class="form-control"/>
<span class="input-group-addon">
<script type="text/javascript">new tcal({
	'formname' : 'vpnForm',
	'controlname' : 'ORTIME'
});</script>
</span>
</div> 
</div>
</div>