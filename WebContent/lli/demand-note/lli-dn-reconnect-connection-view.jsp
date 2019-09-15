<%
  request.setAttribute("menu","lli");
  request.setAttribute("subMenu1","lli-connection");
  request.setAttribute("subMenu2","lli-td-reconnect-connection");
  request.setAttribute("subMenu3","reconnect");
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="LLI Connection" /> 
	<jsp:param name="body" value="/lli/demand-note/lli-dn-reconnect-connection-view-body.jsp" />
</jsp:include>