<%@ include file="../includes/checkLogin.jsp" %>
<%
    request.setAttribute("menu","lliMenu");
    request.setAttribute("subMenu1","lliBillAndPayment");

    request.setAttribute("subMenu2","search-final-bill");
%>
<%
boolean isAdmin = false;
if( loginDTO.getUserID()>0 )
{
        isAdmin=true;
}
%>

<jsp:include page="/layout/layout2018.jsp" flush="true">
        <jsp:param name="title" value="Data Entry" />
        <jsp:param name="body" value="../dataEntry/connection-entry-body.jsp" />
        <jsp:param name="helpers" value="/common/datePickerHelper.jsp" />
</jsp:include>
