<%
    request.setAttribute("menu","client-classification-menu");
    request.setAttribute("subMenu1","add-client-type");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="Client Type Addition" />
    <jsp:param name="body" value="/client/classification/type/client-type-add-body.jsp" />
</jsp:include>
