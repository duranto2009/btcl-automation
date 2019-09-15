<%
  request.setAttribute("menu","crmMenu");
  request.setAttribute("subMenu1","crmDepartmentSubmenu1");
  request.setAttribute("subMenu2","crmDepartmentSubmenu3"); 
%>



<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Search Department" /> 
	<jsp:param name="body" value="../crm/department/departmentSearchBody.jsp" />
</jsp:include> 