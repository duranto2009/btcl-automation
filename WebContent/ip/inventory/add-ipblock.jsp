<%
    request.setAttribute("menu","ip-management");
    request.setAttribute("subMenu1","ip-management-inventory");
    request.setAttribute("subMenu2","inventory-add");
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="IP Management" />
    <jsp:param name="body" value="/ip/inventory/add-ipblock-body.jsp" />
</jsp:include>
