<%
  request.setAttribute("menu","lli");
  request.setAttribute("subMenu1","lli-connection");
  request.setAttribute("subMenu2","lli-application-existing-connection");
  request.setAttribute("subMenu3","lli-long-term-connection");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Connection" /> 
	<jsp:param name="body" value="../lli/demand-note/lli-dn-break-long-term-view-body.jsp" />
</jsp:include>