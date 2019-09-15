<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lli-application");
  request.setAttribute("subMenu2","lli-change-ownership");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Connection" /> 
	<jsp:param name="body" value="../lli/demand-note/lli-dn-lli.Application.ownership-change-body.jsp" />
</jsp:include>