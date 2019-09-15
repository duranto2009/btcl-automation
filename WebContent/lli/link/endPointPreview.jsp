<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliLink");
%>  
  
<jsp:include page="../../common/layout.jsp" flush="true">
<jsp:param name="title" value="End Point Details" /> 
	<jsp:param name="body" value="../lli/link/endPointPreviewBody.jsp" />
</jsp:include> 