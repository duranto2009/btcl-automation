<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
  request.setAttribute("subMenu2","VpnEnable");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value=" Link Enable Request" /> 
	<jsp:param name="body" value="../vpn/link/linkEnableBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="datePickerHelper.jsp" />
</jsp:include> 
