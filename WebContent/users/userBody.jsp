<%@page import="permission.PermissionRepository"%>
<%@ include file="../includes/checkLogin.jsp"%>
<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList, sessionmanager.SessionConstants, user.*"%>
<%-- <%@ page errorPage="failure.jsp"%> --%>



	<%
		String msg = null;
		if ((msg = (String) session.getAttribute(util.ConformationMessage.USER_EDIT)) != null) {
			session.removeAttribute(util.ConformationMessage.USER_EDIT);
		}

		String url = "SearchUser";
		String navigator = SessionConstants.NAV_USER;
		
		PermissionRepository permissionRepository = PermissionRepository.getInstance();
		
	%>
	<jsp:include page="../includes/nav.jsp" flush="true">
		<jsp:param name="url" value="<%=url%>" />
		<jsp:param name="navigator" value="<%=navigator%>" />
	</jsp:include>


<div class="portlet box">
	<div class="portlet-body">
		<html:form action="/DropUser" method="POST" styleId="tableForm">
			<div class="table-responsive">
					<table id="tableData" class="table table-bordered table-striped">
						<thead>
							<tr>
								<th>Username</th>
								<th>Status</th>
								<th>Name</th>
								<th>Role</th>
								<th>Phone</th>
								<th>Designation(s)</th>
								<th>Edit</th>
								<%
									int permission = loginDTO.getMenuPermission(login.PermissionConstants.USER_SEARCH);
										if (permission == 3) {
								%>
								<th><input type="submit" class="btn btn-xs btn-danger" value="Delete" /></th>
								<%
									}
								%>
							</tr>
						</thead>
						<tbody>
							<%
								ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_USER);

									if (data != null) {
										int size = data.size();
										for (int i = 0; i < size; i++) {
											UserDTO row = (UserDTO) data.get(i);
							%>
							<tr>
								<td><%=row.getUsername()%></td>
								<td><%= UserDTO.userStatusMap.get( row.getStatus())%></td>
								<td><%=row.getFullName()%></td>
								<td><%=permissionRepository.getRoleDTOByRoleID(row.getRoleID()).getRoleName() %></td>
								<td><%=row.getPhoneNo()%></td>
								<td><%=row.getDesignation()%></td>
								<td><a href="${context}GetUser.do?id=<%=row.getUserID()%>">Edit</a></td>
								<%
									if (permission == 3){
								%>
								<td><input type="checkbox" name="deleteIDs" value="<%=row.getUserID()%>" /></td>
								<%
									} else {
								%>
								<td><input type="checkbox" name="deleteIDs" value="<%=row.getUserID()%>" disabled="true" /></td>
								<%
									}
								%>
							</tr>

							<%
								}
							%>
						</tbody>
						<%
							}
						%>

					</table>
				</div>			
		</html:form>
	</div>
</div>
<script src="<%=context%>/assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	$('#tableForm').submit(function(e) {
	    var currentForm = this;
	    var selected=false;
	    e.preventDefault();
	    var set = $('#tableData').find('tbody > tr > td:last-child input[type="checkbox"]');
	    $(set).each(function() {
	    	if($(this).prop('checked')){
	    		selected=true;
	    	}
	    });
	    if(!selected){
	    	 bootbox.alert("Select user to delete!", function() { });
	    }else{
	    	 bootbox.confirm("Are you sure you want to delete the user (s)?", function(result) {
	             if (result) {
	                 currentForm.submit();
	             }
	         });
	    }
	});
})

</script>


