<%
  	request.setAttribute("menu","lliMenu");
  	request.setAttribute("subMenu1","lli-application");
  	request.setAttribute("subMenu2","lli-application-downgrade-connection");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Application" /> 
	<jsp:param name="body" value="../lli/application/downgrade-bandwidth/lli-application-downgrade-bandwidth-body.jsp" />
</jsp:include>