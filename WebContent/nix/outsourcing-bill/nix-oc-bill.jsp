<%
  request.setAttribute("menu","nixMenu");
  request.setAttribute("subMenu1","nix-monthly-outsource-Bill");
  request.setAttribute("subMenu2","nix-monthly-outsource-Bill-search");
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="NIX Monthly Outsourcing Bill" />
	<jsp:param name="body" value="/nix/outsourcing-bill/nix-oc-bill-body.jsp" />
    <jsp:param name="helpers" value="/common/datePickerHelper.jsp" />
</jsp:include>