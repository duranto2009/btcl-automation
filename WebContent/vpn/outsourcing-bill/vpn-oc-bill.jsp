<%
  request.setAttribute("menu","vpnMenu");
  request.setAttribute("subMenu1","vpn-monthly-outsource-Bill");
  request.setAttribute("subMenu2","vpn-monthly-outsource-Bill-search");
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
<jsp:param name="title" value="VPN Outsource Bill" />
	<jsp:param name="body" value="/vpn/outsourcing-bill/vpn-oc-bill-body.jsp" />
    <jsp:param name="helpers" value="/common/datePickerHelper.jsp" />
</jsp:include>