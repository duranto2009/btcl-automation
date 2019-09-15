<% request.setAttribute("menu","crmMenu");
request.setAttribute("subMenu1","crmClientComplain");
request.setAttribute("subMenu2","crmClientComplainAdd");
%>

<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Add Client Complain" /> 
	<jsp:param name="body" value="../crm/clientComplain/addCrmClientComplainBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
</jsp:include> 