<%
request.setAttribute("menu","dnshostingMenu");
request.setAttribute("subMenu1","dnshostingClientMenu");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="DNS Hosting Client Summary" /> 
	<jsp:param name="body" value="../dnshosting/client/clientPreviewBody.jsp" />
</jsp:include> 