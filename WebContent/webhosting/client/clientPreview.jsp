<%
request.setAttribute("menu","webHostingMenu");
request.setAttribute("subMenu1","webHostingClientMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Web Hosting Client Summary" /> 
	<jsp:param name="body" value="../webhosting/client/clientPreviewBody.jsp" />
</jsp:include> 