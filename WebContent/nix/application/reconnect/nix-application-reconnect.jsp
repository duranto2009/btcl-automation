<%
	request.setAttribute("menu","nixMenu");
	request.setAttribute("subMenu1","nix-connection");
	request.setAttribute("subMenu2","nix-application-existing-connection");
	request.setAttribute("subMenu3","nix-td-reconnect-connection");
	request.setAttribute("subMenu4","nix-reconnect");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="Reconnect" /> 
	<jsp:param name="body" value="../nix/application/reconnect/nix-application-reconnect-body.jsp" />
</jsp:include>