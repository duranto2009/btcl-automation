<%
  request.setAttribute("menu","lli");
  request.setAttribute("subMenu1","lli-server");
  request.setAttribute("subMenu2","lli-server-new");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Long Term" /> 
	<jsp:param name="body" value="../lli/longterm/lli-longterm-view-body.jsp" />
</jsp:include>