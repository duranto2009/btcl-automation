<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Update VPN Link" /> 
	<jsp:param name="body" value="../vpn/link/updateLinkAfterActivationBody.jsp" />
</jsp:include> 
