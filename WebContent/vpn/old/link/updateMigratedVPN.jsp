<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnMigration");
  request.setAttribute("subMenu2","vpnLinkMigration");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Vpn Link Migration" /> 
	<jsp:param name="body" value="../vpn/link/updateMigratedVPNBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 
