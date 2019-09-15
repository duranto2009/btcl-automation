<%
        request.setAttribute("menu","lliMenu");
        request.setAttribute("subMenu1","lliBillAndPayment");
        request.setAttribute("subMenu2","search-clearance-certificate");
%>

<jsp:include page="../../layout/layout2018.jsp" flush="true">
        <jsp:param name="title" value="Clearance Certificate" />
        <jsp:param name="body" value="/client/clearance-certificate/clearance-certificate-body.jsp" />
        <jsp:param name="helpers" value="/common/datePickerHelper.jsp" />
</jsp:include>
