<%
request.setAttribute("menu","colocationMenu");
request.setAttribute("subMenu1","colocationClientMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Co Location Client Summary" /> 
	<jsp:param name="body" value="../coLocation/client/clientPreviewBody.jsp" />
</jsp:include> 