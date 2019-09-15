<% request.setAttribute("menu","crmMenu");
request.setAttribute("subMenu1","crmComplain");
request.setAttribute("subMenu2","addCrmComplain");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="View Sub Complain" /> 
	<jsp:param name="body" value="../crm/complain/viewCrmComplainBody.jsp" />
	<jsp:param name="helpers" value="fancyBoxHelper.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
</jsp:include> 