<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
  request.setAttribute("subMenu2","searchVpnLink");
%>
<!-- BEGIN PAGE LEVEL STYLES -->
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="VPN Link Shift Demand Note" /> 
	<jsp:param name="body" value="../vpn/link/generateDemandNoteLinkShiftBody.jsp" />
	<jsp:param name="css" value="assets/global/plugins/odometer/odometer.css" />
	<jsp:param name="js" value="assets/global/plugins/odometer/odometer.js" />
</jsp:include> 
