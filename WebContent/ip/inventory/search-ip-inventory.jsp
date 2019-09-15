<%
    request.setAttribute("menu","ip-management");
    request.setAttribute("subMenu1","ip-management-inventory-search");
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="IP Inventory" />
    <jsp:param name="body" value="../ip/inventory/search-ip-inventory-body.jsp" />
</jsp:include>