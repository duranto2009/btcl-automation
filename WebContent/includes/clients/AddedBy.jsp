<%@page import="user.UserDTO"%>
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,invoice.*,user.*,role.*" %>
<%String clAddedBy = (String)session.getAttribute("clAddedBy");
if(clAddedBy == null)clAddedBy="-1";
RoleService rservice = util.ServiceDAOFactory.getService(RoleService.class);
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Operator</td>
<td bgcolor="#deede6" align="left" height="25"> 





<select size="1" name="clAddedBy">
 <option value="-1" <%=(clAddedBy.equals("-1")?"selected=\"selected\"":"")%>>All</option> 
 <%					          
    List userList = user.UserRepository.getInstance().getUserList();
    for(int i=0;i<userList.size();i++)
    {
      UserDTO userDTO = (UserDTO)userList.get(i);
      if(userDTO.getRoleID() == UserDTO.ROLE_DATA_ENTRY || userDTO.getRoleID()==UserDTO.ROLE_OPERATOR||userDTO.getRoleID()== UserDTO.ROLE_ADMIN)
      {
    	  if(UserRepository.getInstance().getUserDTOByUserID(loginDTO.getUserID()).getRoleID() == UserDTO.ROLE_DATA_ENTRY && loginDTO.getUserID()!= UserDTO.BTCL)
    	  {
    		  
    	  	if(loginDTO.getUserID() == userDTO.getUserID())
    	  	{
 %>
     
     
      
     
               <option value="<%=userDTO.getUserID()%>" <%=(clAddedBy.equals(""+userDTO.getUserID())?"selected=\"selected\"":"")%>><%=userDTO.getUsername()%></option>
 <%  
    	  	}}else{%>
    	  	
    	  	<option value="<%=userDTO.getUserID()%>" <%=(clAddedBy.equals(""+userDTO.getUserID())?"selected=\"selected\"":"")%>><%=userDTO.getUsername()%></option>
    	  		
    	  	<%}}else{%>
    	  	}
    	  	
    	  	 
   <% }}
 %>
 
 
</select>

</td>
</tr>