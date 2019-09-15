<%
  	request.setAttribute("menu", "lliMenu");
//	request.setAttribute("subMenu1","lli-application");
//	request.setAttribute("subMenu2","lli-application-new-connection");
//  	request.setAttribute("subMenu3","lli-application-new-" + request.getParameter("id"));
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Application" /> 
	<jsp:param name="body" value="../lli/application/new-connection/view/lli-application-new-connection-view-body.jsp" />
</jsp:include>