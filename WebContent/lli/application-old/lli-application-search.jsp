<%
  request.setAttribute("menu","lli");
  request.setAttribute("subMenu1","lli-application");
  request.setAttribute("subMenu2","lli-server-search");
  request.setAttribute("subMenu3","lli-application-new-" + request.getParameter("applicationTypeID"));
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Application Search" /> 
	<jsp:param name="body" value="../lli/application/lli-application-search-body.jsp" />
</jsp:include>