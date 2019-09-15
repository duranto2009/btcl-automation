<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliLink");
  request.setAttribute("subMenu2","addLliLink");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
<jsp:param name="title" value="Link Edit Details" /> 
	<jsp:param name="body" value="../lli/link/linkEditBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
</jsp:include> 