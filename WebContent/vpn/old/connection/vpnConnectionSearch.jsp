<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnConnection");
  request.setAttribute("subMenu2","searchVpnConnection");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Vpn Connection Search" /> 
	<jsp:param name="body" value="../vpn/connection/vpnConnectionSearchBody.jsp" />
</jsp:include> 