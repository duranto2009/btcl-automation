<%
  request.setAttribute("menu","nix");
  request.setAttribute("subMenu1","connection");
  request.setAttribute("subMenu2","add");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="NIX Connection" />
	<jsp:param name="body" value="../nix/connection/nix-connection-view-body.jsp" />
	<jsp:param name="helpers" value="../common/fileUploadHelper.jsp" />
</jsp:include>