<%@page import="hosting.HostingConstants"%>
<%-- <%@ include file="../checkLogin.jsp" %> --%>
<%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%String packageStatus = (String)session.getAttribute("ORSTATUS");
if(packageStatus == null)packageStatus="-1";
%>
<tr >

<%-- <html:hidden property="bonusStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="ORSTATUS">
 <option value="-1" <%=(packageStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=HostingConstants.STATUS_PENDING%>" <%=(packageStatus.equals(""+HostingConstants.STATUS_PENDING)?"selected=\"selected\"":"")%>>Pending</option> 
<option value="<%=HostingConstants.STATUS_APPROVED%>" <%=(packageStatus.equals(""+HostingConstants.STATUS_APPROVED)?"selected=\"selected\"":"")%>>Approved</option> 
<option value="<%=HostingConstants.STATUS_PAID%>" <%=(packageStatus.equals(""+HostingConstants.STATUS_PAID)?"selected=\"selected\"":"")%>>Paid</option>

</select>
</td>
</tr>