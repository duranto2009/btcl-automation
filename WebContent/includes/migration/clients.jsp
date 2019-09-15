
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*" %>
<%String clientID = (String)session.getAttribute("clientID");
if(clientID == null)clientID="-1";
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Client</td>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="clientID">
 <option value="-1" <%=(clientID.equals("-1")?"selected=\"selected\"":"")%>>All</option> 
 <%					          
    ArrayList clientList = client.ClientRepository.getInstance().getClientList();
    for(int i=0;i<clientList.size();i++)
    {
      client.ClientDTO clientDTO = (client.ClientDTO)clientList.get(i);
 %>
               <option value="<%=clientDTO.getUniqueID()%>" <%=(clientID.equals(""+clientDTO.getUniqueID())?"selected=\"selected\"":"")%>><%=clientDTO.getUserName()%></option>
 <% } %>
 
 
</select>
</td>
</tr>