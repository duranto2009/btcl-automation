<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,message.*" %>
<%String msgStatus = (String)session.getAttribute("msgStatus");
if(msgStatus == null)msgStatus="-1";
%>
<tr >

<%-- <html:hidden property="msgStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">MESSAGE Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="msgStatus">
 <option value="-1" <%=(msgStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>

<option value="<%=MsgConstants.MESSAGE_USED%>" <%=(msgStatus.equals(""+MsgConstants.MESSAGE_USED)?"selected=\"selected\"":"")%>>Sent</option> 
<option value="<%=MsgConstants.MESSAGE_UNUSED%>" <%=(msgStatus.equals(""+MsgConstants.MESSAGE_UNUSED)?"selected=\"selected\"":"")%>>Not Sent</option> 

  
</select>
</td>
</tr>