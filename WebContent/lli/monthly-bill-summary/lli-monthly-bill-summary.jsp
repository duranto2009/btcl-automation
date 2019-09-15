<%@ include file="../../includes/checkLogin.jsp" %>
<%
    request.setAttribute("menu","lliMenu");
    request.setAttribute("subMenu1","lliBillAndPayment");

    request.setAttribute("subMenu2","monthly-bill");
    request.setAttribute("subMenu3","lli-monthly-bill-summary-search");
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
        <jsp:param name="title" value="LLI monthly bill summary" />       
        <jsp:param name="body" value="../lli/monthly-bill-summary/lli-monthly-bill-summary-body.jsp" />
        <jsp:param name="helpers" value="/common/datePickerHelper.jsp" />
</jsp:include>
