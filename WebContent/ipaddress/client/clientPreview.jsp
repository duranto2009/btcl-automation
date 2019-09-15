<%
request.setAttribute("menu","ipaddressMenu");
request.setAttribute("subMenu1","ipaddressClientMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="IP Address Client Summary" /> 
	<jsp:param name="body" value="../ipaddress/client/clientPreviewBody.jsp" />
</jsp:include> 