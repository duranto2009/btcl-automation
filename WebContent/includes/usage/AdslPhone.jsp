

<%@page import="clientmodule.usagehistory.GetUsageHistoryDAO"%>
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*" %>
<%String clUniqueID = (String)session.getAttribute("cdrClientID");
if(clUniqueID == null)clUniqueID="";
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">ADSL Phone</td>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="cdrClientID">
 <option value="" <%=(clUniqueID.equals("")?"selected=\"selected\"":"")%>>All</option> 
 <%		
 	//GetUsageHistoryDAO usageHistory = new GetUsageHistoryDAO();
    ArrayList<ClientDTO> clientList = (ArrayList<ClientDTO>)ClientRepository.getInstance().getClientList();
    for(int i=0;i<clientList.size();i++)
    {
 %>
               <option value="<%=Long.toString(clientList.get(i).getPhoneNo())%>" <%=(clUniqueID.equals(""+Long.toString(clientList.get(i).getPhoneNo()))?"selected=\"selected\"":"")%>><%=Long.toString(clientList.get(i).getPhoneNo())%></option>
 <% } %>
 
 
</select>
</td>
</tr>