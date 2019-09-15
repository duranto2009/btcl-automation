<%
    request.setAttribute("menu","client-classification-menu");
    request.setAttribute("subMenu1","add-client-category");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="Client Category Addition" />
    <jsp:param name="body" value="/client/classification/category/client-category-add-body.jsp" />
</jsp:include>
