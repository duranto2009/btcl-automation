<%@ include file="../../includes/checkLogin.jsp" %>
<%
    request.setAttribute("menu","lliMenu");
    request.setAttribute("subMenu1","lliBillAndPayment");

    request.setAttribute("subMenu2","search-multiple-bill");
%>
<%
boolean isAdmin = false;
if( loginDTO.getUserID()>0 )
{ 	
        isAdmin=true;
}
%>

<script>var isAdmin= <%=isAdmin %></script>
<jsp:include page="../../layout/layout2018.jsp" flush="true">
        <jsp:param name="title" value="Multiple Month bill summary" />
        <jsp:param name="body" value="../client/bill/multiple-monthly-bill-summary-body.jsp" />
        <jsp:param name="helpers" value="/common/datePickerHelper.jsp" />
</jsp:include>
