<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lli-application");

%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Connection" /> 
	<jsp:param name="body" value="../lli/demand-note/lli-dn-revise-connection-body.jsp" />
</jsp:include>