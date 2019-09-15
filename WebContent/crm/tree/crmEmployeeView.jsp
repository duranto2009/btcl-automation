<% request.setAttribute("menu","crmMenu");
request.setAttribute("subMenu1","crmEmployee");
request.setAttribute("subMenu2","viewCrmEmployee");
%>

<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Crm Employee View" /> 
	<jsp:param name="body" value="../crm/tree/crmEmployeeViewBody.jsp" />
	<jsp:param name="css" value="assets/global/plugins/jsTree/themes/default/style.min.css" />
	<jsp:param name="js" value="assets/global/plugins/jsTree/jstree.min.js" />
	<jsp:param name="js" value="assets/scripts/crm/TreeView/DesignationTree.js" />
	<jsp:param name="js" value="assets/scripts/crm/TreeView/EmployeeTree.js" />
	<jsp:param name="js" value="assets/scripts/crm/TreeView/TreeBuilder.js" />
</jsp:include> 