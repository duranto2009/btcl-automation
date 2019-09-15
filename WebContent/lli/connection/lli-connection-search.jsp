<%
    request.setAttribute("menu","lliMenu");
    request.setAttribute("subMenu1","lli-search");
    request.setAttribute("subMenu2","lli-connection-search");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Connection" /> 
	<jsp:param name="body" value="../lli/connection/lli-connection-search-body.jsp" />
</jsp:include>