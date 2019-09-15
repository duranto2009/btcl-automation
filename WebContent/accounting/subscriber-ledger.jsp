<%
    request.setAttribute("menu","lliMenu");
    request.setAttribute("subMenu1","lliBillAndPayment");

    request.setAttribute("subMenu2","accounting-subscriber-ledger");

%>
<jsp:include page="../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="Ledger" />
    <jsp:param name="body" value="../accounting/subscriber-ledger-body.jsp" />
    <jsp:param name="helpers" value="../common/datePickerHelper.jsp"/>
</jsp:include>
