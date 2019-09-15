<%
        request.setAttribute("menu","lliMenu");
        request.setAttribute("subMenu1","lliBillAndPayment");

        request.setAttribute("subMenu2","search-request-letter");

%>

<jsp:include page="../../layout/layout2018.jsp" flush="true">
        <jsp:param name="title" value="Request Letter" />
        <jsp:param name="body" value="/client/request-letter/request-letter-body.jsp" />
        <jsp:param name="helpers" value="/common/datePickerHelper.jsp" />
</jsp:include>
