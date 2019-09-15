<%
	request.setAttribute("menu","nixMenu");
	request.setAttribute("subMenu1","nix-connection");
	request.setAttribute("subMenu2","nix-application-existing-connection");
	request.setAttribute("subMenu3","nix-application-revise-connection");
	request.setAttribute("subMenu4","nix-application-close");
%>

<jsp:include page="../../../layout/layout2018.jsp" flush="true">

    <jsp:param name="title" value="NIX Close" />
	<jsp:param name="body" value="../nix/application/close/nix-close-body.jsp" />
	<jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
</jsp:include>