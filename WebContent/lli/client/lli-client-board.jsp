<%
  request.setAttribute("menu","lli");
  request.setAttribute("subMenu1","lli-server");
  request.setAttribute("subMenu2","lli-server-new");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Client" /> 
	<jsp:param name="body" value="../lli/client/lli-client-board-body.jsp" />
</jsp:include>