<%
  	request.setAttribute("menu","lliMenu");
  	request.setAttribute("subMenu1","lli-application");
  	request.setAttribute("subMenu2","lli-close-connection");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Close Connection" /> 
	<jsp:param name="body" value="../lli/application/close-connection/lli-application-close-connection-body.jsp" />
	<jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
</jsp:include>