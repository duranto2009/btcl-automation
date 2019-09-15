<%
  	request.setAttribute("menu","lli");
  	request.setAttribute("subMenu1","lli-connection");
  	request.setAttribute("subMenu2","lli-close-connection");
//   	request.setAttribute("subMenu3","lli-application-new-" + request.getParameter("id"));
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Application" /> 
	<jsp:param name="body" value="../lli/application/close-connection/lli-application-close-connection-process-body.jsp" />
</jsp:include>