<%@ include file="../checkLogin.jsp" %><%@ page language="java" %><%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %><%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,user.*,login.*,role.*" %>
<%String operatorID = (String)session.getAttribute("operatorID");
if(operatorID == null)operatorID="-1";
RoleService rservice = util.ServiceDAOFactory.getService(RoleService.class);

%>
<tr>
<td bgcolor="#deede6" style="font-family: Verdana; font-size: 8pt; font-weight: bold; padding-left: 3" align="left" height="25" >Operator</td>
<td bgcolor="#deede6" align="left" height="25"> 
<select size="1" name="operatorID">
 <option value="-1" <%=(operatorID.equals("-1")?"selected=\"selected\"":"")%>>All</option> 
 <%					          
    ArrayList userList = user.UserRepository.getInstance().getUserList();
    for(int i=0;i<userList.size();i++)
    {
      UserDTO userDTO = (UserDTO)userList.get(i);
      if(rservice.getRole(""+userDTO.getRoleID()).getRestrictedtoOwn())
      {
 %>
               <option value="<%=userDTO.getUserID()%>" <%=(operatorID.equals(""+userDTO.getUserID())?"selected=\"selected\"":"")%>><%=userDTO.getUsername()%></option>
 <% }} %>
 
 
</select>
</td>
</tr>