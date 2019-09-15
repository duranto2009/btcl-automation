<%
  	request.setAttribute("menu","lliMenu");
  	request.setAttribute("subMenu1","lli-application");
  	request.setAttribute("subMenu2","lli-application-new-connection");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
	<jsp:param name="title" value="LLI Application" /> 
	<jsp:param name="body" value="../lli/application/new-connection/lli-application-new-connection-body.jsp" />
	<jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
</jsp:include>