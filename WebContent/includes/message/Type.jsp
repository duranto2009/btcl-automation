<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,messageformat.*,regiontype.*"%>
<%String msgType = (String)session.getAttribute("msgType");
if(msgType == null)msgType="-1";
%>
<tr >

<%-- <html:hidden property="msgType" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Message Category</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="msgType">
 <option value="-1" <%=(msgType.equals("-1")?"selected=\"selected\"":"")%>>All</option>
 				          
   
    
               <option value="<%=MessageConstants.MESSAGE_TYPE_MAIL%>" <%=(msgType.equals(""+MessageConstants.MESSAGE_TYPE_MAIL)?"selected=\"selected\"":"")%>>Mail</option>
               <option value="<%=MessageConstants.MESSAGE_TYPE_SMS%>" <%=(msgType.equals(""+MessageConstants.MESSAGE_TYPE_SMS)?"selected=\"selected\"":"")%>>SMS</option>
 
 
  
</select>
</td>
</tr>