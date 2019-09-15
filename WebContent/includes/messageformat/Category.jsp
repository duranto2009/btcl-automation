<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,messageformat.*,regiontype.*"%>
<%String msgFormatCategory = (String)session.getAttribute("msgFormatCategory");
if(msgFormatCategory == null)msgFormatCategory="-1";
%>
<tr >

<%-- <html:hidden property="msgFormatCategory" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Message Category</TD>
<td bgcolor="#deede6" align="left" height="25" style="padding-left: 14px"> 
<select size="1" name="msgFormatCategory">
 <option value="-1" <%=(msgFormatCategory.equals("-1")?"selected=\"selected\"":"")%>>All</option>
 <%					          
   
    for(int i=0;i<MessageConstants.MESSAGE_TYPE_NAME.length;i++)
    {
     
 %>
               <option value="<%=MessageConstants.MESSAGE_TYPE_VALUE[i]%>" <%=(msgFormatCategory.equals(""+MessageConstants.MESSAGE_TYPE_VALUE[i])?"selected=\"selected\"":"")%>><%=MessageConstants.MESSAGE_TYPE_NAME[i]%></option>
 <% } %>
 
  
</select>
</td>
</tr>