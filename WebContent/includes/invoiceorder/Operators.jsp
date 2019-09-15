<%@page import="user.UserDTO"%>
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,invoice.*,user.*,role.*" %>
<%String invoiceOrderOperator = (String)session.getAttribute("invoiceOrderOperator");
if(invoiceOrderOperator == null)invoiceOrderOperator="-1";
RoleService rservice = util.ServiceDAOFactory.getService(RoleService.class);
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Operator</td>
<td bgcolor="#deede6" align="left" height="25"> 



<%if(!rservice.getRole(""+loginDTO.getRoleID()).getRestrictedtoOwn()){ %>

<select size="1" name="invoiceOrderOperator">
 <option value="-1" <%=(invoiceOrderOperator.equals("-1")?"selected=\"selected\"":"")%>>All</option> 
 <%					          
    ArrayList userList = user.UserRepository.getInstance().getUserList();
    for(int i=0;i<userList.size();i++)
    {
      UserDTO userDTO = (UserDTO)userList.get(i);
     
      if(rservice.getRole(""+userDTO.getRoleID()).getRestrictedtoOwn())
      {
 %>
               <option value="<%=userDTO.getUserID()%>" <%=(invoiceOrderOperator.equals(""+userDTO.getUserID())?"selected=\"selected\"":"")%>><%=userDTO.getUsername()%></option>
 <%   }
    }
 %>
 
 
</select>
<%}else{ %>

		<select size="1" name="invoiceOrderOperator">
		
		 
		  <option value="<%=loginDTO.getUserID()%>" <%=(invoiceOrderOperator.equals(""+loginDTO.getUserID())?"selected=\"selected\"":"")%>><%=loginDTO.getUsername()%></option>
		 
		</select>

<%} %>
</td>
</tr>