<%
    request.setAttribute("menu","client-classification-menu");
    request.setAttribute("subMenu1","modify-client-category");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="Client Category Modification" />
    <jsp:param name="body" value="/client/classification/category-modify/client-category-modify-body.jsp" />
</jsp:include>
