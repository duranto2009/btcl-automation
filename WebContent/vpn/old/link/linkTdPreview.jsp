<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
  request.setAttribute("subMenu2","updateBandwidth");
%>
<jsp:include page="../../../common/layout.jsp" flush="true">
	<jsp:param name="title" value=" Link TD Request Preview" /> 
	<jsp:param name="body" value="../vpn/link/linkTdPreviewBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
</jsp:include> 
