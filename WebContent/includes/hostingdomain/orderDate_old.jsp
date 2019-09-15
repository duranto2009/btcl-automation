<%@page import="java.text.SimpleDateFormat"%>
<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%
SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
String orderTime = (String)session.getAttribute("ORTIME");
/* if(orderTime == null)orderTime=format.format(System.currentTimeMillis()); */
if(orderTime == null)orderTime="";
System.out.println("orderTime " + orderTime);
%>

<%

%>
<link rel="stylesheet" type="text/css" href="../${context}stylesheets/Calendar/calendar.css">
<script type="text/javascript" src="../${context}scripts/Calendar/calendar_eu.js"></script>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">
Order Date From
</td>
<td bgcolor="#deede6" align="left" height="25">
<input type = "text" name="ORTIME" value="<%=orderTime%>"/><script type="text/javascript">new tcal({
	'formname' : 'hostingdomainForm',
	'controlname' : 'ORTIME'
});</script>
</td>
</tr>