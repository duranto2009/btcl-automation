<%
  request.setAttribute("menu","lli");
  request.setAttribute("subMenu1","lli-server");
  request.setAttribute("subMenu2","lli-server-new");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Connection" /> 
	<jsp:param name="body" value="../lli/application/lli-application-build-body.jsp" />
	<jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
</jsp:include>