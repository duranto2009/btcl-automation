<%
    request.setAttribute("menu","lliMenu");
    request.setAttribute("subMenu1","lli-application");
%>

<jsp:include page="../../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="LLI Application"/>
    <jsp:param name="body" value="../lli/application/new-connection/lli-application-new-connection-details-body.jsp"/>
</jsp:include>
