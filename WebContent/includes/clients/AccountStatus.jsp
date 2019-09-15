
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*" %>
<%String valueAccountStatus = (String)session.getAttribute("clAccountStatus");
if(valueAccountStatus == null)valueAccountStatus="-1";
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Account Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="clAccountStatus">
 <option value="-1" <%=(valueAccountStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option> 
 <%					          
    
    for(int i=0;i<ClientConstants.CLIENTSTATUS_NAME.length;i++)
    {
     
 %>
               <option value="<%=ClientConstants.CLIENTSTATUS_VALUE[i]%>" <%=(valueAccountStatus.equals(""+ClientConstants.CLIENTSTATUS_VALUE[i])?"selected=\"selected\"":"")%>><%=ClientConstants.CLIENTSTATUS_NAME[i]%></option>
 <% } %>
 
 
</select>
</td>
</tr>