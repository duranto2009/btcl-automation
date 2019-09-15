<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
  request.setAttribute("subMenu2","searchVpnLink");
%>
<!-- BEGIN PAGE LEVEL STYLES -->
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="VPN Link View" /> 
	<jsp:param name="body" value="../vpn/link/linkViewBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="commentBoxHelper.jsp" />
	<jsp:param name="helpers" value="fancyBoxHelper.jsp" />
</jsp:include> 

