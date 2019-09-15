<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliLink");
  request.setAttribute("subMenu2","updateBandwidth");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value=" Link Upgrade/Downgrade Request" /> 
	<jsp:param name="body" value="../lli/link/bandwidthChangePreviewBody.jsp" />
</jsp:include> 
