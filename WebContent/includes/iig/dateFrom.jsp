<%@page import="java.text.SimpleDateFormat"%>
<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
String serviceStartTime = (String)session.getAttribute("IGSERVICESTARTTIME");
if(serviceStartTime == null)serviceStartTime="";//format.format(System.currentTimeMillis());
System.out.println("serviceStartTime " + serviceStartTime);
%>

<%

%>
<%-- <link rel="stylesheet" type="text/css" href="../${context}stylesheets/Calendar/calendar.css">
<script type="text/javascript" src="../${context}scripts/Calendar/calendar_eu.js"></script>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">
Service Start Date
</td>
<td bgcolor="#deede6" align="left" height="25">
<input type = "text" name="IGSERVICESTARTTIME" value="<%=serviceStartTime%>"/><script type="text/javascript">new tcal({
	'formname' : 'iigForm',
	'controlname' : 'IGSERVICESTARTTIME'
});</script>
</td>
</tr> --%>

<link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">


<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Service Start Date From</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
<div class='input-group'>
<input type = "text" name="IGSERVICESTARTTIME" value="<%=serviceStartTime%>" class="form-control"/>
<span class="input-group-addon">
<script type="text/javascript">new tcal({
	'formname' : 'iigForm',
	'controlname' : 'IGSERVICESTARTTIME'
});</script>
</span>
</div> 
</div>
</div>