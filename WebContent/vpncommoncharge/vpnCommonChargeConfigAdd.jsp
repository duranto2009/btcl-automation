<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%

  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnConfigurationSubmenu1");
  request.setAttribute("subMenu2","vpnCommonChargeMenu");
  
%>
<jsp:include page="../common/layout.jsp" flush="true">
<jsp:param name="title" value="Add Vpn Common Charge Config" /> 
	<jsp:param name="body" value="../vpncommoncharge/vpnCommonChargeConfigAddBody.jsp" />
	
	<jsp:param name="css" value="assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.css" />
	<jsp:param name="css" value="assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" />
	<jsp:param name="js" value="assets/global/plugins/moment.min.js" />
	<jsp:param name="js" value="assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" />
</jsp:include> 
