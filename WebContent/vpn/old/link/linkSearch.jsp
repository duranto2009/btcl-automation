
<%
	request.setAttribute("menu", "vpnMenu");
	request.setAttribute("subMenu1", "vpnLink");
	request.setAttribute("subMenu2", "searchVpnLink");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Vpn Link Search" />
	<jsp:param name="body" value="../vpn/link/linkSearchBody.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include>
