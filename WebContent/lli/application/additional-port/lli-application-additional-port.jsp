<%
  	request.setAttribute("menu","lliMenu");
  	request.setAttribute("subMenu1","lli-application");
  	request.setAttribute("subMenu2","lli-additional-port-local-loop");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Additional Port" /> 
	<jsp:param name="body" value="../lli/application/additional-port/lli-application-additional-port-body.jsp" />
	<jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
</jsp:include>