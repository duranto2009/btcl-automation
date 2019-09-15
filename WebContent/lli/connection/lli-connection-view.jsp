<%
  request.setAttribute("menu","lliMenu");

%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Connection" /> 
	<jsp:param name="body" value="../lli/connection/lli-connection-view-body.jsp" />
	<jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
</jsp:include>