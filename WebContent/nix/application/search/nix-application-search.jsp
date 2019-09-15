<%
  request.setAttribute("menu","nixMenu");
  request.setAttribute("subMenu1","nix-connection");
  request.setAttribute("subMenu2","nix-application-search");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="nix Application" />
	<jsp:param name="body" value="../nix/application/search/nix-application-search-body.jsp" />
    <jsp:param name="helpers" value="../common/datePickerHelper.jsp"/>
</jsp:include>