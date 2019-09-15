<%
  request.setAttribute("menu","nixMenu");
  request.setAttribute("subMenu1","nix-connection");
  request.setAttribute("subMenu2","nix-application-new-connection");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="NIX Connection" />
	<jsp:param name="body" value="../nix/application/new-body.jsp" />
	<jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
</jsp:include>