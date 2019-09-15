<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpnLink");
%>  
  
<jsp:include page="../../../common/layout.jsp" flush="true">
<jsp:param name="title" value="End Point Details" /> 
	<jsp:param name="body" value="../vpn/link/endPointPreviewBody.jsp" />
</jsp:include> 