<%
    request.setAttribute("menu","ip-management");
    request.setAttribute("subMenu1","ip-management-sub-region");
    request.setAttribute("subMenu2","ip-sub-region-create");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="IP Sub Region create" />
    <jsp:param name="body" value="/ip/sub-region/ip-sub-region-create-body.jsp" />
</jsp:include>
