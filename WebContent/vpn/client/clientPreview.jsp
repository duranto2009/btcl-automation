<%
request.setAttribute("menu","vpnMenu");
request.setAttribute("subMenu1","vpnClient");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="VPN Client Edit" /> 
	<jsp:param name="body" value="../vpn/client/clientPreviewBody.jsp" />
</jsp:include> 