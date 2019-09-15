<%
    request.setAttribute("menu","ip-management");
    request.setAttribute("subMenu1","ip-management-usage-search");
%>
<jsp:include page="/layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="IP Usage" />
    <jsp:param name="body" value="../ip/usage/search-ip-usage-body.jsp" />
    <jsp:param name="helpers" value="../common/datePickerHelper.jsp" />
</jsp:include>