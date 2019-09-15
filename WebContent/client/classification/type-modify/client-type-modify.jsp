<%
    request.setAttribute("menu","client-classification-menu");
    request.setAttribute("subMenu1","modify-client-type");
%>
<jsp:include page="../../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="Client Type Modification" />
    <jsp:param name="body" value="/client/classification/type-modify/client-type-modify-body.jsp" />
</jsp:include>
