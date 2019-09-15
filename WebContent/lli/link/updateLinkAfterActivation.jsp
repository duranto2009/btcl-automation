<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lliLink");
%>
<jsp:include page="../../common/layout.jsp" flush="true">
	<jsp:param name="title" value="Update LLI Connection" /> 
	<jsp:param name="body" value="../lli/link/updateLinkAfterActivationBody.jsp" />
	<jsp:param name="helpers" value="fileUploadHelper.jsp" />
	<jsp:param name="helpers" value="fancyBoxHelper.jsp" />
</jsp:include> 