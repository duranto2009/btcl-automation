<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
  request.setAttribute("subMenu2","updateBandwidth");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value=" Link Close Request Preview" /> 
	<jsp:param name="body" value="../vpn/link/linkClosePreviewBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
</jsp:include> 
