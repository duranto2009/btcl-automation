<% request.setAttribute("menu","crmMenu");
request.setAttribute("subMenu1","crmClientComplain");
request.setAttribute("subMenu2","crmClientComplainSearch");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="View Client Complain" /> 
	<jsp:param name="body" value="../crm/clientComplain/viewCrmClientComplainBody.jsp" />
	<jsp:param name="helpers" value="fancyBoxHelper.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
</jsp:include> 