<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
  request.setAttribute("subMenu2","searchVpnLink");
%>
<!-- BEGIN PAGE LEVEL STYLES -->
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="VPN Link View" /> 
	<jsp:param name="body" value="../vpn/link/generateDemandNoteBandwidthChangeBody.jsp" />
</jsp:include> 
