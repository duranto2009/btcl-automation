
<%
	request.setAttribute("menu","nixMenu");
	request.setAttribute("subMenu1","nix-connection");
	request.setAttribute("subMenu2","nix-application-existing-connection");
	request.setAttribute("subMenu3","nix-td-reconnect-connection");
	request.setAttribute("subMenu4","nix-td-list-search");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="NIX Connection" />
	<jsp:param name="body" value="../nix/revise/nix-requested-td-list-view-body.jsp" />
	<jsp:param name="helpers" value="../common/datePickerHelper.jsp" />
</jsp:include>