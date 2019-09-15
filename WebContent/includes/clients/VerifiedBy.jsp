<%@page import="login.PermissionConstants"%>
<%@page import="user.UserDTO"%>
<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,invoice.*,user.*,role.*" %>
<%String clVerifiedBy = (String)session.getAttribute("clVerifiedBy");
if(clVerifiedBy == null)clVerifiedBy="-1";
RoleService rservice = util.ServiceDAOFactory.getService(RoleService.class);
%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Verified By</td>
<td bgcolor="#deede6" align="left" height="25"> 





<select size="1" name="clVerifiedBy">
 <option value="-1" <%=(clVerifiedBy.equals("-1")?"selected=\"selected\"":"")%>>All</option> 
 <%		
 	ColumnPermissionDTO dto = new ColumnPermissionDTO(-1,23,"Verify");
    ArrayList userList = user.UserRepository.getInstance().getUserList();
    for(int i=0;i<userList.size();i++)
    {
      UserDTO userDTO = (UserDTO)userList.get(i);
      
      if( rservice.getRole(""+userDTO.getRoleID()).getColumnPermission(23)){
 %>
     
      
     
               <option value="<%=userDTO.getUserID()%>" <%=(clVerifiedBy.equals(""+userDTO.getUserID())?"selected=\"selected\"":"")%>><%=userDTO.getUsername()%></option>
 <%   
      } }
 %>
 
 
</select>

</td>
</tr>