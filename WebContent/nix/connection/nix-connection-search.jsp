<%
    request.setAttribute("menu","nixMenu");
    request.setAttribute("subMenu1","nix-connection");
    request.setAttribute("subMenu2","nix-connection-search");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="NIX Connection" />
	<jsp:param name="body" value="../nix/connection/nix-connection-search-body.jsp" />
</jsp:include>