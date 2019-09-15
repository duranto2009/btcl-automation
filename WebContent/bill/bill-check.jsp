<%--<%
    request.setAttribute("menu","lliMenu");
    request.setAttribute("subMenu1","lliBillAndPayment");

    request.setAttribute("subMenu2","monthly-bill");
    request.setAttribute("subMenu3","lli-monthly-bill-search");
%>--%>
<jsp:include page="../layout/layout2018.jsp" flush="true">
        <jsp:param name="title" value="LLI Bill Check" />
        <jsp:param name="body" value="../bill/bill-check-body.jsp" />
        <jsp:param name="helpers" value="/common/datePickerHelper.jsp" />
</jsp:include>
