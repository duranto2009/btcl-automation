
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*" %>
<%String valueApplicationStatus = (String)session.getAttribute("clApplicationStatus");
if(valueApplicationStatus == null)valueApplicationStatus="-1";
%>
<tr >

<%-- <html:hidden property="clApplicationStatus" value ="1"/> --%>

<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Application Status</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="clApplicationStatus">
 <option value="-1" <%=(valueApplicationStatus.equals("-1")?"selected=\"selected\"":"")%>>All</option>
 <%					          
    
    for(int i=0;i<ClientConstants.APPLICATIONSTATUS_NAME.length;i++)
    {
     
 %>
               <option value="<%=ClientConstants.APPLICATIONSTATUS_VALUE[i]%>" <%=(valueApplicationStatus.equals(""+ClientConstants.APPLICATIONSTATUS_VALUE[i])?"selected=\"selected\"":"")%>><%=ClientConstants.APPLICATIONSTATUS_NAME[i]%></option>
 <% } %>
 
  
</select>
</td>
</tr>