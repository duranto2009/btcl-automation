<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,messageformat.*,regiontype.*"%>
<%String msgFormatType = (String)session.getAttribute("msgFormatType");
if(msgFormatType == null)msgFormatType="-1";
%>
<tr >

<%-- <html:hidden property="msgFormatType" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Message Type</TD>
<td bgcolor="#deede6" align="left" height="25" style="padding-left: 14px"> 
<select size="1" name="msgFormatType">
 <option value="-1" <%=(msgFormatType.equals("-1")?"selected=\"selected\"":"")%>>All</option>
 				          
   
    
               <option value="<%=MessageConstants.MESSAGE_TYPE_MAIL%>" <%=(msgFormatType.equals(""+MessageConstants.MESSAGE_TYPE_MAIL)?"selected=\"selected\"":"")%>>Mail</option>
               <option value="<%=MessageConstants.MESSAGE_TYPE_SMS%>" <%=(msgFormatType.equals(""+MessageConstants.MESSAGE_TYPE_SMS)?"selected=\"selected\"":"")%>>SMS</option>
 
 
  
</select>
</td>
</tr>