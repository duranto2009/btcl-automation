<%--<%
    request.setAttribute("menu","lli");
    request.setAttribute("subMenu1","lli-connection");
    request.setAttribute("subMenu2","lli-application-existing-connection");
    request.setAttribute("subMenu3","lli-application-revise-connection");
%>--%>

<jsp:include page="/layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="Owner Change Application Details"/>
    <jsp:param name="body" value="../lli/application/change-ownership/view/lli-owner-change-application-details-body.jsp" />
</jsp:include>
