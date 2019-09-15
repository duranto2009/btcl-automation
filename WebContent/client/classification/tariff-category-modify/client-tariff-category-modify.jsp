<%
    request.setAttribute("menu","client-classification-menu");
    request.setAttribute("subMenu1","modify-client-tariff-category");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="Client Tariff Category Modification" />
    <jsp:param name="body" value="/client/classification/tariff-category-modify/client-tariff-category-modify-body.jsp" />
</jsp:include>
