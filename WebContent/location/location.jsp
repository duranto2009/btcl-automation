<%
//  request.setAttribute("menu", "WEB-INF/classes/location");
  request.setAttribute("subMenu1","lli-server");
  request.setAttribute("subMenu2","lli-server-new");
%>
<jsp:include page="../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Application" />
	<jsp:param name="body" value="../location/location-body.jsp" />
</jsp:include>