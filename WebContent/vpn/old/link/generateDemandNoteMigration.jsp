<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnMigration");
  request.setAttribute("subMenu2","vpnBillMigration");
%>
<!-- BEGIN PAGE LEVEL STYLES -->
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="VPN Bill Migration" /> 
	<jsp:param name="body" value="../vpn/link/generateDemandNoteMigrationBody.jsp" />
	<jsp:param name="css" value="assets/global/plugins/odometer/odometer.css" />
	<jsp:param name="css" value="assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" />
	<jsp:param name="js" value="assets/global/plugins/odometer/odometer.js" />
	<jsp:param name="js" value="assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" />
</jsp:include> 
