<%@ include file="../includes/checkLogin.jsp"%>

<%@ page language="java"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.ArrayList,
 				 sessionmanager.SessionConstants,
				 role.*"%>

<%@ page errorPage="failure.jsp"%>
<!-- <script>
var pieces = window.location.href.split("/");
alert(pieces[pieces.length-1]);
</script> -->

			<%
				String url = "ViewRole";
				String navigator = SessionConstants.NAV_ROLE;
			%>
			<jsp:include page="../includes/nav.jsp" flush="true">
				<jsp:param name="url" value="<%=url%>" />
				<jsp:param name="navigator" value="<%=navigator%>" />
			</jsp:include>

		<div class="portlet box">
		<div class="portlet-body">
		<html:form action="/DropRole" method="POST" styleId="tableForm">
			<div class="table-responsive">
					<table id="tableData" class="table table-bordered table-striped">
						<thead>
							<tr>
							<th>Role Name</th>
							<th>Description</th>
							<th>Edit</th>
							<%  int permission = loginDTO.getMenuPermission(login.PermissionConstants.ROLE_SEARCH);%>
							<%
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
								ArrayList data = (ArrayList) session.getAttribute(SessionConstants.VIEW_ROLE);

									if (data != null) {
										int size = data.size();

										for (int i = 0; i < size; i++) {

											RoleDTO row = (RoleDTO) data.get(i);
							%>
							<tr>
								<td><%=row.getRoleName()%></td>
								<td><%=row.getRoleDescription()%></td>
								<td><a href="../GetRole.do?id=<%=row.getRoleID()%>"><!--Edit-->Edit</a></td>
								<td><input type="checkbox" name="deleteIDs" value="<%=row.getRoleID()%>" /></td>
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
	    	 bootbox.alert("Select role to delete!", function() { });
	    }else{
	    	 bootbox.confirm("Are you sure you want to delete the role (s)?", function(result) {
	             if (result) {
	                 currentForm.submit();
	             }
	         });
	    }
	});
})

</script>