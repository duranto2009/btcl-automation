<%
  request.setAttribute("menu","lli");
  request.setAttribute("subMenu1","lli-connection");
  request.setAttribute("subMenu2","lli-application-new-connection");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Connection" /> 
	<jsp:param name="body" value="../lli/demand-note/lli-dn-new-connection-view-body.jsp" />
</jsp:include>