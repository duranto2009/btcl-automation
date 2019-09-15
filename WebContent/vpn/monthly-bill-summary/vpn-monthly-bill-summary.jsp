<%@ include file="../../includes/checkLogin.jsp" %>
<%
    request.setAttribute("menu", "vpnMenu");
    request.setAttribute("subMenu1", "vpn-monthly-Bill-summary");
    request.setAttribute("subMenu2", "vpn-monthly-bill-summary-search");
%>
<%
    boolean isAdmin = false;
    if (loginDTO.getUserID() > 0) {
        isAdmin = true;
    }
%>

<script>var isAdmin = <%=isAdmin %>
</script>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
    <jsp:param name="title" value="VPN monthly bill summary"/>
    <jsp:param name="body" value="../vpn/monthly-bill-summary/vpn-monthly-bill-summary-body.jsp"/>
    <jsp:param name="helpers" value="/common/datePickerHelper.jsp"/>
</jsp:include>
