<%@page import="login.LoginDTO"%>
<%@page import="login.PermissionConstants"%>
<%@page import="user.UserDTO"%>
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,invoice.*,user.*,role.*" %>
<%String clApprovedBy = (String)session.getAttribute("clApprovedBy");
if(clApprovedBy == null)clApprovedBy="-1";
RoleService rservice = util.ServiceDAOFactory.getService(RoleService.class);
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3"  align="left" height="25">Approved By</td>
<td bgcolor="#deede6" align="left" height="25"> 





<select size="1" name="clApprovedBy">

 <option value="-1" <%=(clApprovedBy.equals("-1")?"selected=\"selected\"":"")%>>All</option> 
 <%			
 	ColumnPermissionDTO adto = new ColumnPermissionDTO(-1,22,"Approve");
    ArrayList userList = user.UserRepository.getInstance().getUserList();
   
    for(int i=0;i<userList.size();i++)
    {
      UserDTO userDTO = (UserDTO)userList.get(i);      
      if(rservice.getRole(""+userDTO.getRoleID()).getColumnPermission(22)){
 %>
     
      
     
               <option value="<%=userDTO.getUserID()%>" <%=(clApprovedBy.equals(""+userDTO.getUserID())?"selected=\"selected\"":"")%>><%=userDTO.getUsername()%></option>
 <%   
      } }
 %>
 
 
</select>

</td>
</tr>