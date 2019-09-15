<%@page import="mmlclient.mml.CommandConstants"%>
<%@page import="mmlclient.mml.packetizer.MMLConstants"%>
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*" %>
<%String chStatus = (String)session.getAttribute("chStatus");
if(chStatus == null)chStatus="-1";
%>
<tr >

<%-- <html:hidden property="chStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="chStatus">
 <option value="-1" <%=(chStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=CommandConstants.SUCCESSFUL%>" <%=(chStatus.equals(""+CommandConstants.SUCCESSFUL)?"selected=\"selected\"":"")%>>Successful</option> 
<option value="<%=CommandConstants.UNSUCCESSFUL%>" <%=(chStatus.equals(""+CommandConstants.UNSUCCESSFUL)?"selected=\"selected\"":"")%>>Unsuccessful</option> 
<option value="<%=CommandConstants.PENDING%>" <%=(chStatus.equals(""+CommandConstants.PENDING)?"selected=\"selected\"":"")%>>Pending</option> 

  
</select>
</td>
</tr>