<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
  request.setAttribute("subMenu2","addVpnLink");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Link Edit/View Details" /> 
	<jsp:param name="body" value="../vpn/link/linkPreviewBody.jsp" />
	<jsp:param name="helpers" value="fancyBoxHelper.jsp" />
</jsp:include> 