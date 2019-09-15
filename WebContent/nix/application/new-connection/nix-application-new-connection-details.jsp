<%--<%
    request.setAttribute("menu","lli");
    request.setAttribute("subMenu1","lli-connection");
    request.setAttribute("subMenu2","lli-application-existing-connection");
    request.setAttribute("subMenu3","lli-application-revise-connection");
%>--%>

<jsp:include page="../../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="NIX Application"/>
    <jsp:param name="body" value="../nix/application/new-connection/nix-application-new-connection-details-body.jsp"/>
</jsp:include>
