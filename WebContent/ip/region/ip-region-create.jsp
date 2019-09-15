<%
    request.setAttribute("menu","ip-management");
    request.setAttribute("subMenu1","ip-management-region");
    request.setAttribute("subMenu2","ip-region-create");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="IP Region create" />
    <jsp:param name="body" value="/ip/region/ip-region-create-body.jsp" />
</jsp:include>
