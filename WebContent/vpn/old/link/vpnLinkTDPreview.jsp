<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
  request.setAttribute("subMenu2","VpnTdClose");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value=" Link TD Request" /> 
	<jsp:param name="body" value="../vpn/link/vpnLinkTDPreviewBody.jsp" />
</jsp:include> 
