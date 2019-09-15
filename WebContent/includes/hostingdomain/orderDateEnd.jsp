<%@page import="java.text.SimpleDateFormat"%>
<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
String orderTime = (String)session.getAttribute("ORTIMEEND");
if(orderTime == null)orderTime=format.format(System.currentTimeMillis());
System.out.println("orderTimEnd " + orderTime);
%>

<%

%>
<%-- <link rel="stylesheet" type="text/css" href="../${context}stylesheets/Calendar/calendar.css">
<script type="text/javascript" src="../${context}scripts/Calendar/calendar_eu.js"></script>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">
Order Date To
</td>
<td bgcolor="#deede6" align="left" height="25">
<input type = "text" name="ORTIMEEND" value="<%=orderTime%>"/><script type="text/javascript">new tcal({
	'formname' : 'hostingdomainForm',
	'controlname' : 'ORTIMEEND'
});</script>
</td>
</tr> --%>

<link rel="stylesheet" type="text/css" href="${context}stylesheets/Calendar/calendar.css">


<div class="form-group">
	<label for="" class="control-label col-md-4 col-sm-4 col-xs-4">Order Date To</label>
	<div class="col-md-8 col-sm-8 col-xs-8"> 
<div class='input-group'>
<input type = "text" name="ORTIMEEND" value="<%=orderTime%>" class="form-control"/>
<span class="input-group-addon">
<script type="text/javascript">new tcal({
	'formname' : 'hostingdomainForm',
	'controlname' : 'ORTIMEEND'
});</script>
</span>
</div> 
</div>
</div>