<%
    request.setAttribute("menu","lli");
    request.setAttribute("subMenu1","lli-application");
    request.setAttribute("subMenu2","lli-application-new");

%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="TD" />
    <jsp:param name="body" value="../lli/revise/td-create-view-body.jsp" />
</jsp:include>