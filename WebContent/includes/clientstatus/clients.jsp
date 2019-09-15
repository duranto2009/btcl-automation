
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="role.RoleService"%>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,client.*" %>
<%String csUniqueID = (String)session.getAttribute("csUniqueID");
if(csUniqueID == null)csUniqueID="-1";
RoleService rservice = util.ServiceDAOFactory.getService(RoleService.class);
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Client ID</td>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="csUniqueID">
 <option value="-1" <%=(csUniqueID.equals("-1")?"selected=\"selected\"":"")%>>All</option> 
 <%					          
    ArrayList clientList = client.ClientRepository.getInstance().getClientList();
    for(int i=0;i<clientList.size();i++)
    {
      client.ClientDTO clientDTO = (client.ClientDTO)clientList.get(i);
      if(rservice.getRole(""+loginDTO.getRoleID()).getRestrictedtoOwn())
      {
    	  if(clientDTO.getAddedBy() == loginDTO.getUserID())
    	  {
      
 %>
               <option value="<%=clientDTO.getUniqueID()%>" <%=(csUniqueID.equals(""+clientDTO.getUniqueID())?"selected=\"selected\"":"")%>><%=clientDTO.getUserName()%></option>
        <%} %>
        
        <%}else{ %>
         <option value="<%=clientDTO.getUniqueID()%>" <%=(csUniqueID.equals(""+clientDTO.getUniqueID())?"selected=\"selected\"":"")%>><%=clientDTO.getUserName()%></option>
 <% }} %>
 
 
</select>
</td>
</tr>