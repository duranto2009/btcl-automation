<% request.setAttribute("menu","crmMenu");
request.setAttribute("subMenu1","crmComplain");
request.setAttribute("subMenu2","addCrmComplain");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Add Admin Complain" /> 
	<jsp:param name="body" value="../crm/complain/addCrmComplainBody.jsp" />
</jsp:include> 