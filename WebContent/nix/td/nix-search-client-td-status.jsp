<%
  request.setAttribute("menu","lli");
  request.setAttribute("subMenu1","lli-connection");
  request.setAttribute("subMenu2","lli-application-existing-connection");
  request.setAttribute("subMenu3", "lli-td-reconnect-connection");
  request.setAttribute("subMenu4", "lli-td-client-search");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="NIX Connection" />
	<jsp:param name="body" value="../nix/td/nix-search-client-td-status-body.jsp" />
	<jsp:param name="helpers" value="../common/datePickerHelper.jsp" />
</jsp:include>