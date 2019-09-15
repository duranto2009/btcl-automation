<% request.setAttribute("menu","crmMenu");
request.setAttribute("subMenu1","crmComplain");
request.setAttribute("subMenu2","searchCrmComplain");
%>

<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Search Complain" /> 
	<jsp:param name="body" value="../crm/complain/searchCrmComplainBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 