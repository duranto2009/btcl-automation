<%
  request.setAttribute("menu","lliMenu");
  request.setAttribute("subMenu1","lli-configuration");
  request.setAttribute("subMenu2","lli-configuration-fixed-cost");
 
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Connection" /> 
	<jsp:param name="body" value="/lli/configuration/lli-configuration-fixed-cost-body.jsp" />
</jsp:include>