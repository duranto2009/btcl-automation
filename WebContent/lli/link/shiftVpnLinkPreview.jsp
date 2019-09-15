<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliLink");
  request.setAttribute("subMenu2","updateBandwidth");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value=" Link Shifting Request Preview" /> 
	<jsp:param name="body" value="../lli/link/shiftLliLinkPreviewBody.jsp" />
</jsp:include> 
