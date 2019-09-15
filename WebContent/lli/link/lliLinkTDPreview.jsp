<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliLink");
  request.setAttribute("subMenu2","lliLinkTDLinkSubmenu2");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value=" Connection TD Request" /> 
	<jsp:param name="body" value="../lli/link/lliLinkTDPreviewBody.jsp" />
</jsp:include> 
