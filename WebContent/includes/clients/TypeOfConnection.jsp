<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*" %>
<%String valueTypeOfConnection = (String)session.getAttribute("clTypeOfConnection");
if(valueTypeOfConnection == null)valueTypeOfConnection="-1";
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Type of Connection</TD>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="clTypeOfConnection">
  <option value="-1" <%=(valueTypeOfConnection.equals("-1")?"selected=\"selected\"":"")%>>All</option>
 <%					          
    
    for(int i=0;i<ClientConstants.TYPEOFCONNECTION_NAME.length;i++)
    {
      
 %>
     <option value="<%=ClientConstants.TYPEOFCONNECTION_VALUE[i]%>" <%=(valueTypeOfConnection.equals(""+ClientConstants.TYPEOFCONNECTION_VALUE[i])?"selected=\"selected\"":"")%>><%=ClientConstants.TYPEOFCONNECTION_NAME[i]%></option>
 <% } %>
 
 
</select>
</td>
</tr>
