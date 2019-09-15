<%
  request.setAttribute("menu","crmMenu");
  request.setAttribute("subMenu1","crmEmployee");
  request.setAttribute("subMenu2","searchCrmEmployee"); 
%>



<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Search Employee" /> 
	<jsp:param name="body" value="../crm/tree/crmEmployeeSearchBody.jsp" />
</jsp:include> 