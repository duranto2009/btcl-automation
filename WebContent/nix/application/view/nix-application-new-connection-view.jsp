<%
  	request.setAttribute("menu", "lli");
  	request.setAttribute("subMenu1","lli-connection");
  	request.setAttribute("subMenu2","lli-application-new-connection");
//  	request.setAttribute("subMenu3","lli-application-new-" + request.getParameter("id"));
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Nix Application" />
	<jsp:param name="body" value="/nix/application/view/nix-application-new-connection-view-body.jsp" />
</jsp:include>