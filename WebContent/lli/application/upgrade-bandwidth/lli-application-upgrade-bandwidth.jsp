<%
  	request.setAttribute("menu","lliMenu");
  	request.setAttribute("subMenu1","lli-application");
  	request.setAttribute("subMenu2","lli-application-upgrade-connection");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Application" /> 
	<jsp:param name="body" value="../lli/application/upgrade-bandwidth/lli-application-upgrade-bandwidth-body.jsp" />
</jsp:include>