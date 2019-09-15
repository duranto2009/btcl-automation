<%
    request.setAttribute("menu","ip-management");
    request.setAttribute("subMenu1","ip-management-sub-region");
    request.setAttribute("subMenu2","all-ip-sub-regions");
%>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="List of all IP Sub Regions" />
    <jsp:param name="body" value="/ip/sub-region/ip-all-sub-regions-body.jsp" />
</jsp:include>
