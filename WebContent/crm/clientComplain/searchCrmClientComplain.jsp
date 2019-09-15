<% request.setAttribute("menu","crmMenu");
request.setAttribute("subMenu1","crmClientComplain");
request.setAttribute("subMenu2","crmClientComplainSearch");
%>

<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Search Complain" /> 
	<jsp:param name="body" value="../crm/clientComplain/searchCrmClientComplainBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 