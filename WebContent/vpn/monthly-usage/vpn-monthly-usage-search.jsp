<%@ include file="../../includes/checkLogin.jsp" %>
<%
    request.setAttribute("menu","vpnMenu");
    request.setAttribute("subMenu1","vpn-monthly-usage");
    request.setAttribute("subMenu2","vpn-monthly-usage-search");
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
    <jsp:param name="title" value="VPN monthly usage" />
    <jsp:param name="body" value="../vpn/monthly-usage/vpn-monthly-usage-search-body.jsp" />
	<jsp:param name="helpers" value="/common/datePickerHelper.jsp" />
</jsp:include>
