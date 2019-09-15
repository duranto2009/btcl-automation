<%
  request.setAttribute("menu","crmMenu");
  request.setAttribute("subMenu1","crmComplain");
  request.setAttribute("subMenu2","searchCrmActivityLog"); 
%>



<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Search Activity" /> 
	<jsp:param name="body" value="../crm/activityLog/activityLogSearchBody.jsp" />
</jsp:include> 