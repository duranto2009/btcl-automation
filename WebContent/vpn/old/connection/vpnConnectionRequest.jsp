<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnConnection");
  request.setAttribute("subMenu2","addVpnConnection");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
<jsp:param name="title" value="VPN Conntection Request" /> 
	<jsp:param name="body" value="../vpn/connection/vpnConnectionRequestBody.jsp" />
</jsp:include> 
