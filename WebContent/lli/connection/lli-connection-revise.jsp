<%
  request.setAttribute("menu","lli");
  request.setAttribute("subMenu1","connection");
  request.setAttribute("subMenu2","add");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Connection" /> 
	<jsp:param name="body" value="../lli/connection/lli-connection-revise-body.jsp" />
	<jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
</jsp:include>