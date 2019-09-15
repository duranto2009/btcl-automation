<%@ include file="../checkLogin.jsp" %><%@ page language="java" %>
<%@ taglib uri="../../WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="../../WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="../../WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList,java.util.*,sessionmanager.SessionConstants,databasemanager.*,user.*,login.*,role.*" %>
<%
	String usRoleID = (String)session.getAttribute("usRoleID");
	if(usRoleID == null)usRoleID="-1";
	RoleService rservice = util.ServiceDAOFactory.getService(RoleService.class);
%>
<div class="form-group">
	<label class="control-label col-md-4 col-sm-4">Role</label>
	<div class="col-md-8 col-sm-8">
		<select class="form-control" name="usRoleID">
 			<option value="" <%=(usRoleID.equals("-1")?"selected=\"selected\"":"")%>>All</option> 
			<%					          
				ArrayList<RoleDTO> roleList = rservice.getPermittedRoleListForSpAdmin();
				for(int i=0;i<roleList.size();i++){
					RoleDTO roleDTO = (RoleDTO)roleList.get(i);
			%>
              		<option value="<%=roleDTO.getRoleID()%>" <%=(usRoleID.equals(""+roleDTO.getRoleID())?"selected=\"selected\"":"")%>><%=roleDTO.getRoleName()%></option>
 			<% } %>
		</select>
	</div>		
</div>
